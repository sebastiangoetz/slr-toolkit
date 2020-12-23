import CoreData
import SwiftyBibtex

class Project: NSManagedObject {
    @NSManaged var name: String
    
    @NSManaged var username: String
    @NSManaged var token: String
    
    @NSManaged var repositoryURL: String
    @NSManaged var pathInGitDirectory: String
    @NSManaged var pathInRepository: String
    
    @discardableResult static func newProject(name: String, username: String, token: String, repositoryURL: String, pathInGitDirectory: String, pathInRepository: String, in managedObjectContext: NSManagedObjectContext) -> Project {
        let project = NSEntityDescription.insertNewObject(forEntityName: "Project", into: managedObjectContext) as! Project
        project.name = name
        project.username = username
        project.token = token
        project.repositoryURL = repositoryURL
        project.pathInGitDirectory = pathInGitDirectory
        project.pathInRepository = pathInRepository
        return project
    }

    lazy var entries: [Publication]? = {
        let urls = FileManager.default.contentsOfDirectory(at: projectURL) { $1.hasSuffix(".bib") }
        return urls.reduce([Publication]()) {
            do {
                let fileContents = try String(contentsOf: $1)
                return $0 + (try SwiftyBibtex.parse(fileContents).publications)
            } catch {
                print("Error reading or parsing bib file: \(error)")
                return $0
            }
        }
    }()
    
    var taxonomy: [TaxonomyNode] {
        return [
            TaxonomyNode(name: "Types of Research", children: [
                TaxonomyNode(name: "Applied", children: []),
                TaxonomyNode(name: "Fundamental")
            ]),
            TaxonomyNode(name: "Application Domains", children: [
                TaxonomyNode(name: "none"),
                TaxonomyNode(name: "enterprise"),
                TaxonomyNode(name: "cloud")
            ])
        ]
    }

    private var projectURL: URL {
        return GitManager.gitDirectory
            .appendingPathComponent(pathInGitDirectory)
            .appendingPathComponent(pathInRepository, isDirectory: true)
    }
}
