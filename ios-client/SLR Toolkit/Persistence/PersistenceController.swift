import CoreData

/// Sets up the app's persistent container (for Core Data).
struct PersistenceController {
    static let shared = PersistenceController()

    let container: NSPersistentContainer

    init(inMemory: Bool = false) {
        container = NSPersistentContainer(name: "SLR_Toolkit")
        if inMemory {
            container.persistentStoreDescriptions.first!.url = URL(fileURLWithPath: "/dev/null")
        }
        container.loadPersistentStores { storeDescription, error in
            if let error = error as NSError? {
                fatalError("Unresolved error \(error), \(error.userInfo)")
            }
        }
    }

    func deleteAllData() {
        guard let url = container.persistentStoreDescriptions.first?.url else { return }
        do {
            try container.persistentStoreCoordinator.destroyPersistentStore(at: url, ofType: NSSQLiteStoreType, options: nil)
            try container.persistentStoreCoordinator.addPersistentStore(ofType: NSSQLiteStoreType, configurationName: nil, at: url, options: nil)
//            try container.viewContext.fetch(Project.fetchRequest).forEach { container.viewContext.delete($0) } // cascade delete rule ensures that entries and classes are deleted as well
        } catch {
            print("Error deleting persisted data: \(error)")
        }
    }
}
