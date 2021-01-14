import CoreData
import SwiftyBibtex

final class TaxonomyClass: NSManagedObject, Identifiable {
    var id: String { name }

    @NSManaged var name: String

    @NSManaged var project: Project
    @NSManaged var entries: Set<Entry>
    @NSManaged var parent: TaxonomyClass?
    @NSManaged var children: Set<TaxonomyClass>

    var sortedChildren: [TaxonomyClass]? {
        return children.isEmpty ? nil : children.sorted { $0.name < $1.name }
    }

    var classesString: String {
        return children.isEmpty ? name : "\(name) {\(children.map(\.classesString).joined(separator: ", "))}"
    }

    @discardableResult static func newEntity(name: String, project: Project, parent: TaxonomyClass?, in managedObjectContext: NSManagedObjectContext) -> TaxonomyClass {
        let taxonomyClass = NSEntityDescription.insertNewObject(forEntityName: String(describing: self), into: managedObjectContext) as! TaxonomyClass
        taxonomyClass.name = name
        taxonomyClass.project = project
        taxonomyClass.parent = parent
        return taxonomyClass
    }

    static var fetchRequest: NSFetchRequest<TaxonomyClass> {
        return NSFetchRequest(entityName: String(describing: self))
    }
}
