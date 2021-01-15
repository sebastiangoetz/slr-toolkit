import CoreData
import Foundation
import SwiftyBibtex

enum ProjectManager {
    static func createProject(name: String, username: String, token: String, repositoryURL: String, pathInGitDirectory: String, pathInRepository: String, managedObjectContext: NSManagedObjectContext, completion: @escaping (Project) -> Void) {
        DispatchQueue.global(qos: .userInitiated).async {
            let managedObjectContext = PersistenceController.shared.container.viewContext
            let project = Project.newEntity(name: name, username: username, token: token, repositoryURL: repositoryURL, pathInGitDirectory: pathInGitDirectory, pathInRepository: pathInRepository, in: managedObjectContext)
            managedObjectContext.saveAndLogError()  // Workaround, otherwise a Core Data exception is thrown

            createContents(for: project, managedObjectContext: managedObjectContext)
            completion(project)
        }
    }

    static func updateProject(_ project: Project, fileModificationDates: [String: Date], completion: @escaping () -> Void) {
        guard fileModificationDates != project.fileModificationDates else {
            completion()
            return
        }
        DispatchQueue.global(qos: .userInitiated).async {
            let managedObjectContext = PersistenceController.shared.container.viewContext
            let decisions = project.entries.reduce(into: [String: Entry.Decision]()) { acc, entry in
                if entry.decision != .outstanding {
                    acc[entry.citationKey] = entry.decision
                }
            }
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

    static func commitChanges(project: Project) throws -> (Int, Int) {
        // TODO handle multiple bib files, each entry needs to know to which file it belongs; new CD entity?
        let bibFileURL = FileManager.default.contentsOfDirectory(at: project.url) { $1.hasSuffix(".bib") }.first!
        let bibText = try String(contentsOf: bibFileURL)
        var lines = bibText.split(omittingEmptySubsequences: false) { $0 == "\n" || $0 == "\r\n" }  // TODO Switch to approach on character level

        let classifiedEntries = project.classifiedEntries.count
        for entry in project.classifiedEntries {
            let entryLines = lines[(entry.rangeInFile.start.line - 1)...(entry.rangeInFile.end.line - 1)]

            let indentRegex = try! NSRegularExpression(pattern: "^\\s+", options: [])
            let indent: String
            let secondLine = String(Array(entryLines)[1])
            if let match = indentRegex.firstMatch(in: secondLine, options: [], range: NSRange(location: 0, length: secondLine.utf16.count)) {
                indent = String(secondLine[Range(match.range(at: 0), in: secondLine)!])
            } else {
                indent = ""
            }

            let classesString = indent + "classes = {\(entry.classesString)}"
            if let index = entryLines.firstIndex(where: { $0.trimmingCharacters(in: .whitespaces).lowercased().hasPrefix("classes") }) {
                lines[index] = classesString + (entryLines[index].hasSuffix(",") ? "," : "")
            } else {
                let index = entryLines.count - 2
                lines[entry.rangeInFile.start.line - 1 + index] += (entryLines[index].hasSuffix(",") ? "" : ",") + "\n" + classesString
            }
            entry.classesChanged = false
        }

        let managedObjectContext = PersistenceController.shared.container.viewContext

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

    private static func createEntries(project: Project, publications: [Publication], managedObjectContext: NSManagedObjectContext) -> [(Entry, Set<String>)] {
        return publications.map {
            return (Entry.newEntity(publication: $0, decision: $0.classes.isEmpty ? .outstanding : .keep, project: project, in: managedObjectContext), $0.classes)
        }
    }

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
