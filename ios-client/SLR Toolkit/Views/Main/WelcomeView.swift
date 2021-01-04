import SwiftUI

struct WelcomeView: View {
    @Binding var project: Project?

    @Environment(\.managedObjectContext) private var managedObjectContext

    @State private var addProjectIsPresented = false

    var body: some View {
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
}

struct WelcomeView_Previews: PreviewProvider {
    static var previews: some View {
        WelcomeView(project: .constant(nil))
    }
}
