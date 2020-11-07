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
            .environment(\.managedObjectContext, persistenceController.container.viewContext)
        }
    }
}
