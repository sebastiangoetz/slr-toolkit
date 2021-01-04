import CoreData

final class Project: NSManagedObject {
    @NSManaged var name: String
    
    @NSManaged var username: String
    @NSManaged var token: String
    
    @NSManaged var repositoryURL: String
    @NSManaged var pathInGitDirectory: String
    @NSManaged var pathInRepository: String

    @NSManaged var entries: Set<Entry>
    @NSManaged var classes: Set<TaxonomyClass>

    var sortedRootClasses: [TaxonomyClass] {
        return classes.filter { $0.parent == nil }.sorted { $0.name < $1.name }
    }

    var url: URL {
        return GitManager.gitDirectory
            .appendingPathComponent(pathInGitDirectory)
            .appendingPathComponent(pathInRepository, isDirectory: true)
    }
    
    @discardableResult static func newEntity(name: String, username: String, token: String, repositoryURL: String, pathInGitDirectory: String, pathInRepository: String, in managedObjectContext: NSManagedObjectContext) -> Project {
        let project = NSEntityDescription.insertNewObject(forEntityName: String(describing: self), into: managedObjectContext) as! Project
        project.name = name
        project.username = username
        project.token = token
        project.repositoryURL = repositoryURL
        project.pathInGitDirectory = pathInGitDirectory
        project.pathInRepository = pathInRepository
        return project
    }
}
