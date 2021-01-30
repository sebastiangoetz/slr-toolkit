import SwiftUI

/// View that is shown when no projects have been created,
struct WelcomeView: View {
    /// The WelcomeView is replaces by ProjectView as soon as this is set to a non-nil value.
    @Binding var project: Project?

    @Environment(\.managedObjectContext) private var managedObjectContext

    @State private var addProjectIsPresented = false

    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Text("Welcome to SLR Toolkit!\nAdd a project to get started.")
                    .multilineTextAlignment(.center)
                TextButton("Add Project", minWidth: 120) {
                    addProjectIsPresented = true
                }
            }
            .navigationBarTitle("SLR Toolkit")
            .sheet(isPresented: $addProjectIsPresented) {
                AddProjectView(project: $project, isPresented: $addProjectIsPresented)
                    .environment(\.managedObjectContext, managedObjectContext)
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())  // to disable split on iPad
    }
}

struct WelcomeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            WelcomeView(project: .constant(nil))
            WelcomeView(project: .constant(nil))
                .colorScheme(.dark)
        }
    }
}
