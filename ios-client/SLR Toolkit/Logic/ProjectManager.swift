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
            project.entries.forEach { managedObjectContext.delete($0) }
            project.classes.forEach { managedObjectContext.delete($0) }
            let entries = createContents(for: project, managedObjectContext: managedObjectContext)
            for entry in entries {
                if let decision = decisions[entry.citationKey] {
                    entry.decision = decision
                }
            }
            completion()
        }
    }

    @discardableResult static func createContents(for project: Project, managedObjectContext: NSManagedObjectContext) -> [Entry] {
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
                let fileContents = try String(contentsOf: $1)
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
