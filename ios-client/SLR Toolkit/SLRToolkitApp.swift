import CoreData
import SwiftUI

@main
struct SLRToolkitApp: App {
    private let persistenceController = PersistenceController.shared

    var body: some Scene {
        WindowGroup {
            NavigationView {
                MainView(project: loadActiveProject())
                    .environment(\.managedObjectContext, persistenceController.container.viewContext)
                EntryDetailView()
            }
        }
    }
    
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
}
