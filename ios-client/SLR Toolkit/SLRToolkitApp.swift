import CoreData
import SwiftUI

@main
struct SLRToolkitApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) private var applicationDelegateAdaptor

    private let persistenceController = PersistenceController.shared
    private let testing: Bool

    init() {
        testing = ProcessInfo.processInfo.arguments.contains(LaunchArgument.testing.rawValue)
    }

    var body: some Scene {
        WindowGroup {
            Group {
                if testing {
                    MainView(project: loadActiveProject())
                        .environment(\.addProjectViewInteractor, AddProjectViewInteractorMock())
                        .environment(\.projectViewInteractor, ProjectViewInteractorMock())
                        .environment(\.gitManager, GitManager(gitClient: MockGitClient()))
                } else {
                    MainView(project: loadActiveProject())
                }
            }
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
                if let uri = UserDefaults.standard.url(forKey: .activeProject),
                   let managedObjectID = container.persistentStoreCoordinator.managedObjectID(forURIRepresentation: uri),
                   let activeProject = container.viewContext.object(with: managedObjectID) as? Project {
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
        if let entries = try? managedObjectContext.fetch(Entry.fetchRequest) {
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

final class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        TestHelper.handleLaunchArguments(ProcessInfo.processInfo.arguments)
        return true
    }
}
