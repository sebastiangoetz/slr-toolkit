import SwiftGit2
import SwiftUI

struct MainView: View {
    @Environment(\.managedObjectContext) var managedObjectContext
    @Environment(\.persistenceController) var persistenceController
    
    @State var project: Project?
    
    @State private var addProjectIsPresented = false
    
    init(project: Project?) {
        if let project = project {
            _project = State(initialValue: project)
        } else if let activeProject = loadActiveProject() {
            _project = State(initialValue: activeProject)
        }
    }
    
    var body: some View {
        if let project = project {
            ProjectView(project: project)
        } else {
            VStack(spacing: 20) {
                Text("Welcome to SLR Toolkit!\nAdd a project to get started.")
                    .multilineTextAlignment(.center)
                RoundedButton("Add Project") {
                    addProjectIsPresented = true
                }
            }
            .navigationBarTitle("SLR Toolkit")
            .sheet(isPresented: $addProjectIsPresented) {
                AddProjectView(project: $project)
            }
        }
    }
    
    private func loadActiveProject() -> Project? {
        if let uri = UserDefaults.standard.url(forKey: .activeProject), let managedObjectID = persistenceController.container.persistentStoreCoordinator.managedObjectID(forURIRepresentation: uri), let activeProject = managedObjectContext.object(with: managedObjectID) as? Project {
            return activeProject
        }
        return nil
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationView {
                MainView(project: nil)
            }
//            NavigationView {
//                MainView(project: Constants.exampleProject)
//            }
        }
    }
}
