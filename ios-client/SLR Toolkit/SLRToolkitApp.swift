import SwiftUI

@main
struct SLRToolkitApp: App {
    let persistenceController = PersistenceController.shared

    var body: some Scene {
        WindowGroup {
            NavigationView {
                MainView(project: nil)
                EntryDetailView()
            }
            .environment(\.persistenceController, persistenceController)
            .environment(\.managedObjectContext, persistenceController.container.viewContext)
        }
    }
}

struct PersistenceControllerKey: EnvironmentKey {
    static let defaultValue: PersistenceController = PersistenceController(inMemory: true)
}

extension EnvironmentValues {
    var persistenceController: PersistenceController {
        get {
            return self[PersistenceControllerKey.self]
        }
        set {
            self[PersistenceControllerKey.self] = newValue
        }
    }
}
