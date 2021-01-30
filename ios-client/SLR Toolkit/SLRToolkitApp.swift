import CoreData
import SwiftUI

@main
struct SLRToolkitApp: App {
    private let persistenceController = PersistenceController.shared

    var body: some Scene {
        WindowGroup {
            MainView(project: loadActiveProject())
                .environment(\.managedObjectContext, persistenceController.container.viewContext)
        }
    }

    /// Loads the last known active project. If it can't be found, it loads the first one instead.
    private func loadActiveProject() -> Project? {
        let container = PersistenceController.shared.container
        let fetchRequest = Project.fetchRequest()
        fetchRequest.sortDescriptors = [NSSortDescriptor(key: "name", ascending: true)]
        do {
            if let projects = try container.viewContext.fetch(fetchRequest) as? [Project], let firstProject = projects.first {
                if let uri = UserDefaults.standard.url(forKey: .activeProject), let managedObjectID = container.persistentStoreCoordinator.managedObjectID(forURIRepresentation: uri), let activeProject = container.viewContext.object(with: managedObjectID) as? Project {
                    return activeProject
                }
                return firstProject
            }
        } catch {
            print("Error fetching projects: \(error)")
        }
        return nil
    }

    // Utility functions (useful for testing/debugging)

    /// Marks two entries as having an outstanding decision. This is useful for enabling the "Filter" button.
    private func markTwoEntriesAsOutstanding() {
        let managedObjectContext = PersistenceController.shared.container.viewContext
        let fetchRequest = Entry.fetchRequest.withPredicate(NSPredicate(format: "decisionRaw != 2"))
        if let entries = try? managedObjectContext.fetch(fetchRequest) {
            entries.first?.decision = .outstanding
            entries.last?.decision = .outstanding
        }
        try? managedObjectContext.save()
    }

    /// Removed all classes from two entries. This is useful for enabling the "Classify" button.
    private func markTwoEntriesAsUnclassified() {
        let managedObjectContext = PersistenceController.shared.container.viewContext
        let fetchRequest = Entry.fetchRequest.withPredicate(NSPredicate(format: "decisionRaw != 2"))
        if let entries = try? managedObjectContext.fetch(fetchRequest) {
            entries.first?.classes.removeAll()
            entries.last?.classes.removeAll()
        }
        try? managedObjectContext.save()
    }
}
