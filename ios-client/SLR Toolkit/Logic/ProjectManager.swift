import CoreData
import Foundation
import SwiftyBibtex

/// Utility functions for projects.
enum ProjectManager {
    /// Reads a project's name from a .project file in its directory.
    static func projectName(forProjectAt url: URL) -> String? {
        guard let xmlFileURL = FileManager.default.contentsOfDirectory(at: url, matching: { $1 == ".project" }).first else { return nil }
        do {
            // TODO properly parse xml
            let content = try String(contentsOf: xmlFileURL)
            let regex = try NSRegularExpression(pattern: "<name>(.*)</name>", options: [])
            let match = regex.firstMatch(in: content, options: [], range: NSRange(location: 0, length: content.utf16.count))
            guard let nsRange = match?.range(at: 1) else { return nil }
            guard let swiftRange = Range(nsRange, in: content) else { return nil }
            return String(content[swiftRange])
        } catch {
            return nil
        }
    }

    /// Writes a new name into a project's .project file.
    static func setName(_ newName: String, for project: Project) -> Bool {
        guard let xmlFileURL = FileManager.default.contentsOfDirectory(at: project.url, matching: { $1 == ".project" }).first else { return false }
        do {
            var content = try String(contentsOf: xmlFileURL)
            let regex = try NSRegularExpression(pattern: "<name>(.*)</name>", options: [])
            let match = regex.firstMatch(in: content, options: [], range: NSRange(location: 0, length: content.utf16.count))
            guard let nsRange = match?.range(at: 1) else { return false }
            guard let swiftRange = Range(nsRange, in: content) else { return false }
            content.replaceSubrange(swiftRange, with: newName)
            try content.write(to: xmlFileURL, atomically: true, encoding: .utf8)
            // TODO show as change in UI (on commit button)
            return true
        } catch {
            return false
        }
    }

    /// Creates a new project and its contents.
    @discardableResult static func createProjectSync(name: String, username: String, token: String, repositoryURL: String, pathInGitDirectory: String, pathInRepository: String, managedObjectContext: NSManagedObjectContext) -> Project {
        let project = Project.newEntity(name: name, username: username, token: token, repositoryURL: repositoryURL, pathInGitDirectory: pathInGitDirectory, pathInRepository: pathInRepository, in: managedObjectContext)
        managedObjectContext.saveAndLogError()  // Workaround, otherwise a Core Data exception is thrown
        createContents(for: project, managedObjectContext: managedObjectContext)
        return project
    }

    /// Creates a new project and its contents.
    static func createProjectAsync(name: String, username: String, token: String, repositoryURL: String, pathInGitDirectory: String, pathInRepository: String, managedObjectContext: NSManagedObjectContext, completion: @escaping (Project) -> Void) {
        DispatchQueue.global(qos: .userInitiated).async {
            completion(createProjectSync(name: name, username: username, token: token, repositoryURL: repositoryURL, pathInGitDirectory: pathInGitDirectory, pathInRepository: pathInRepository, managedObjectContext: managedObjectContext))
        }
    }

    /// Updates a project (after new changes have been pulled) by re-creating its contents.
    static func updateProject(_ project: Project, fileModificationDates: [String: Date], completion: @escaping () -> Void) {
        guard fileModificationDates != project.fileModificationDates else {
            completion()
            return
        }
        DispatchQueue.global(qos: .userInitiated).async {
            let managedObjectContext = PersistenceController.shared.container.viewContext

            // Remember non-outstanding decisions
            let decisions = project.entries.reduce(into: [String: Entry.Decision]()) { acc, entry in
                if entry.decision != .outstanding {
                    acc[entry.citationKey] = entry.decision
                }
            }

            // Delete all classes and entries
            project.classes.filter { $0.parent == nil }.forEach { managedObjectContext.delete($0) } // Delete rule is "cascade"
            project.entries.forEach { managedObjectContext.delete($0) }

            let entries = createContents(for: project, managedObjectContext: managedObjectContext)
            for entry in entries {
                if let decision = decisions[entry.citationKey] {
                    entry.decision = decision
                }
            }
            completion()
        }
    }

    /// Commits a project's changes.
    static func commitChanges(project: Project) throws -> (Int, Int) {
        _ = setName(project.name, for: project)

        // TODO handle multiple bib files, each entry needs to know to which file it belongs. Perhaps with a new Core Data entity that Entries can reference.
        let bibFileURL = FileManager.default.contentsOfDirectory(at: project.url) { $1.hasSuffix(".bib") }.first!
        let bibText = try String(contentsOf: bibFileURL)
        var lines = bibText.split(omittingEmptySubsequences: false) { $0 == "\n" || $0 == "\r\n" }  // TODO Switch to approach on character level

        // Insert or modify "classes" lines
        let classifiedEntries = project.classifiedEntries.count
        for entry in project.classifiedEntries {
            let entryLines = lines[(entry.rangeInFile.start.line - 1)...(entry.rangeInFile.end.line - 1)]

            // Find out original indent
            let indentRegex = try! NSRegularExpression(pattern: "^\\s+", options: [])
            let indent: String
            let secondLine = String(Array(entryLines)[1])
            if let match = indentRegex.firstMatch(in: secondLine, options: [], range: NSRange(location: 0, length: secondLine.utf16.count)) {
                indent = String(secondLine[Range(match.range(at: 0), in: secondLine)!])
            } else {
                indent = ""
            }

            // Generate and insert "classes" line
            let classesString = indent + "classes = {\(entry.classesString)}"
            if let entryLinesIndex = entryLines.firstIndex(where: { $0.trimmingCharacters(in: .whitespaces).lowercased().hasPrefix("classes") }) {
                let index = entry.rangeInFile.start.line - 1 + entryLinesIndex
                lines[index] = classesString + (entryLines[index].hasSuffix(",") ? "," : "")
            } else {
                let index = entry.rangeInFile.start.line - 1 + entryLines.count - 2
                lines[index] += (entryLines[index].hasSuffix(",") ? "" : ",") + "\n" + classesString
            }
            entry.classesChanged = false
        }

        let managedObjectContext = PersistenceController.shared.container.viewContext

        // Delete discarded entries
        let discardedEntries = project.discardedEntries.count
        project.discardedEntries.map(\.rangeInFile).sorted { $0 > $1 }.forEach { lines.remove(atOffsets: IndexSet(integersIn: ($0.start.line - 1)...($0.end.line - 1))) }
        project.discardedEntries.forEach { managedObjectContext.delete($0) }

        // Write changes back to file
        try lines.joined(separator: "\n").write(to: bibFileURL, atomically: true, encoding: .utf8)

        // Update ranges
        let entries = project.entries.reduce(into: [String: Entry]()) { $0[$1.citationKey] = $1 }
        for publication in parsePublications(project: project) {
            if let entry = entries[publication.citationKey] {
                entry.rangeInFile = publication.rangeInFile
            }
        }
        managedObjectContext.saveAndLogError()

        // Report number of changes
        return (discardedEntries, classifiedEntries)
    }

    /// Creates a project's contents: entries and classes.
    @discardableResult private static func createContents(for project: Project, managedObjectContext: NSManagedObjectContext) -> [Entry] {
        let nodes = parseTaxonomy(project: project) ?? []
        let classes = createTaxonomyClasses(project: project, nodes: nodes, managedObjectContext: managedObjectContext)

        let publications = parsePublications(project: project)
        let entries = createEntries(project: project, publications: publications, managedObjectContext: managedObjectContext)

        assign(entries: entries, to: classes)

        managedObjectContext.saveAndLogError()
        UserDefaults.standard.set(project.objectID.uriRepresentation(), forKey: .activeProject)

        return entries.map(\.0)
    }

    /// Parse the project's .taxonomy file.
    private static func parseTaxonomy(project: Project) -> [TaxonomyParserNode]? {
        guard let url = FileManager.default.contentsOfDirectory(at: project.url, matching: { $1.hasSuffix(".taxonomy") }).first else { return nil }
        do {
            let fileContents = try String(contentsOf: url)
            return TaxonomyParser.parse(fileContents)
        } catch {
            print("Error reading or parsing bib file: \(error)")
            return nil
        }
    }

    /// Creates entities from parsed taxonomy class nodes.
    private static func createTaxonomyClasses(project: Project, nodes: [TaxonomyParserNode], managedObjectContext: NSManagedObjectContext) -> [String: TaxonomyClass] {
        var classes = [String: TaxonomyClass]()

        func createTaxonomyClasses(parent: TaxonomyClass?, children: [TaxonomyParserNode]) {
            for node in children {
                let taxonomyClass = TaxonomyClass.newEntity(name: node.name, project: project, parent: parent, in: managedObjectContext)
                classes[node.path] = taxonomyClass
                createTaxonomyClasses(parent: taxonomyClass, children: node.children)
            }
        }

        createTaxonomyClasses(parent: nil, children: nodes)

        return classes
    }

    /// Parse the project's .bib file.
    private static func parsePublications(project: Project) -> [Publication] {
        let urls = FileManager.default.contentsOfDirectory(at: project.url) { $1.hasSuffix(".bib") }
        return urls.reduce([Publication]()) {
            do {
                let fileContents = try String(contentsOf: $1).replacingOccurrences(of: "\r\n", with: "\n")
                return $0 + (try SwiftyBibtex.parse(fileContents).publications)
            } catch {
                print("Error reading or parsing bib file: \(error)")
                return $0
            }
        }
    }

    /// Transforms SwiftyBibtex Publiations into Entry entities.
    private static func createEntries(project: Project, publications: [Publication], managedObjectContext: NSManagedObjectContext) -> [(Entry, Set<String>)] {
        return publications.map {
            return (Entry.newEntity(publication: $0, decision: $0.classes.isEmpty ? .outstanding : .keep, project: project, in: managedObjectContext), $0.classes)
        }
    }

    /// Assigns entries to their respective classes.
    private static func assign(entries: [(Entry, Set<String>)], to classes: [String: TaxonomyClass]) {
        for (entry, assignedClasses) in entries {
            for assignedClass in assignedClasses {
                if let taxonomyClass = classes[assignedClass] {
                    entry.classes.insert(taxonomyClass)
                }
            }
        }
    }
}
