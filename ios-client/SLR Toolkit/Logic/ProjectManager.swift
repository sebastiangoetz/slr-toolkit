import Foundation
import SwiftyBibtex

enum ProjectManager {
    static func createProject(name: String, username: String, token: String, repositoryURL: String, pathInGitDirectory: String, pathInRepository: String, completion: @escaping (Project) -> Void) {
        DispatchQueue.global(qos: .userInitiated).async {
            let managedObjectContext = PersistenceController.shared.container.viewContext
            let project = Project.newEntity(name: name, username: username, token: token, repositoryURL: repositoryURL, pathInGitDirectory: pathInGitDirectory, pathInRepository: pathInRepository, in: managedObjectContext)

            do {
                try managedObjectContext.save() // Workaround, otherwise a Core Data exception is thrown
            } catch {
                print("Error saving managed object context: \(error)")
            }


            let nodes = parseTaxonomy(project: project) ?? []
            let classes = createTaxonomyClasses(project: project, nodes: nodes)
            let publications = parsePublications(project: project)
            let entries = createEntries(project: project, publications: publications)
            assign(entries: entries, to: classes)

            do {
                try managedObjectContext.save()
                UserDefaults.standard.set(project.objectID.uriRepresentation(), forKey: .activeProject)
            } catch {
                print("Error saving managed object context: \(error)")
            }

            completion(project)
        }
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

    private static func createTaxonomyClasses(project: Project, nodes: [TaxonomyParserNode]) -> [String: TaxonomyClass] {
        let managedObjectContext = PersistenceController.shared.container.viewContext
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

    private static func createEntries(project: Project, publications: [Publication]) -> [(Entry, Set<String>)] {
        let managedObjectContext = PersistenceController.shared.container.viewContext
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
