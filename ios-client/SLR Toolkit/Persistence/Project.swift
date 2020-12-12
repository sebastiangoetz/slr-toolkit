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
        guard let bibFileURL = bibFileURL else { return nil }
        do {
            let fileContents = try String(contentsOf: bibFileURL)
            return try SwiftyBibtex.parse(fileContents).publications
        } catch {
            print("Error reading or parsing bib file: \(error)")
            return []
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

    private lazy var bibFileURL: URL? = {
        do {
            let contents = try FileManager.default.contentsOfDirectory(at: projectURL, includingPropertiesForKeys: [.isDirectoryKey])
            for url in contents {
                do {
                    let resourceValues = try url.resourceValues(forKeys: [.isDirectoryKey])
                    if !resourceValues.isDirectory! && url.pathComponents.last?.hasSuffix(".bib") == true {
                        return url
                    }
                } catch {
                    print("Error fetching resource values for \(url): \(error)")
                }
            }
        } catch {
            print("Error listing contents of \(projectURL): \(error)")
        }
        return nil
    }()
}
