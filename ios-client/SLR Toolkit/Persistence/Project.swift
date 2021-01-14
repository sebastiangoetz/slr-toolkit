import CoreData

final class Project: NSManagedObject {
    @NSManaged var name: String
    
    @NSManaged var username: String
    @NSManaged var token: String
    
    @NSManaged var repositoryURL: String
    @NSManaged var pathInGitDirectory: String
    @NSManaged var pathInRepository: String

    @NSManaged var commitName: String?
    @NSManaged var commitEmail: String?

    @NSManaged var entries: Set<Entry>
    @NSManaged var classes: Set<TaxonomyClass>

    var discardedEntries: Set<Entry> {
        return entries.filter { $0.decision == .discard }
    }

    var classifiedEntries: Set<Entry> {
        return entries.filter { !$0.hadClasses && !$0.classes.isEmpty }
    }

    var sortedRootClasses: [TaxonomyClass] {
        return classes.filter { $0.parent == nil }.sorted { $0.name < $1.name }
    }

    var url: URL {
        return GitManager.gitDirectory
            .appendingPathComponent(pathInGitDirectory)
            .appendingPathComponent(pathInRepository, isDirectory: true)
    }

    var fileModificationDates: [String: Date] {
        do {
            let contents = try FileManager.default.contentsOfDirectory(at: url, includingPropertiesForKeys: [.isDirectoryKey, .contentModificationDateKey])
            return contents.reduce(into: [:]) { acc, url in
                do {
                    let fileName = url.pathComponents.last!
                    let resourceValues = try url.resourceValues(forKeys: [.isDirectoryKey, .contentModificationDateKey])
                    if let isDirectory = resourceValues.isDirectory, !isDirectory, let contentModificationDate = resourceValues.contentModificationDate, fileName.hasSuffix(".bib") || fileName.hasSuffix(".taxonomy") {
                        acc[fileName] = contentModificationDate
                    }
                } catch {
                    print("Error fetching resource values for \(url): \(error)")
                }
            }
        } catch {
            print("Error listing contents of \(url): \(error)")
            return [:]
        }
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
