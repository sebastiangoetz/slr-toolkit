import CoreData
import SwiftUI

struct ProjectView: View {
    @Environment(\.managedObjectContext) private var managedObjectContext

    @Binding var project: Project!

    @State private var projectsViewIsPresented = false

    var body: some View {
        List {
            ButtonRow(buttons: [
                ("Filter", "84 entries", true),
                ("Classify", "12 entries", true)
            ])
            Section(header: Text("Entries")) {
                NavigationLink(destination: EntriesView(project: project, taxonomyClass: nil)) {
                    DetailRow(text: "All Entries", detail: "\(project.entries.count)")
                }
            }
            Section(header: Text("Taxonomy")) {
                OutlineGroup(project.sortedRootClasses, children: \.sortedChildren) { taxonomyClass in
                    DetailRow(text: taxonomyClass.name, detail: "\(taxonomyClass.entries.count)")
                    NavigationLink(destination: EntriesView(project: project, taxonomyClass: taxonomyClass)) {
                        EmptyView()
                    }
                    .frame(width: 0)
                    .opacity(0)
                }
            }
            Section(header: Text("Source Control")) {
                ButtonRow(buttons: [
                    ("Pull", "4 commits behind", true),
                    ("Commit", "37 changes", true)
                ])
            }
        }
        .listStyle(InsetGroupedListStyle())
        .navigationBarTitle(project.name)
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                Menu {
                    Button {
                        projectsViewIsPresented = true
                    } label: {
                        Label("Change Project", systemImage: "folder")
                    }
                    Button(action: {}) {
                        Label("Project Settings", systemImage: "folder.badge.gear")
                    }
                } label: {
                    Image(systemName: "ellipsis.circle")
                        .imageScale(.large)
                }
            }
        }
        .sheet(isPresented: $projectsViewIsPresented) {
            ProjectsView(activeProject: $project)
                .environment(\.managedObjectContext, managedObjectContext)
        }
    }
}

struct ProjectView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationView {
                MainView(project: nil)
            }
        }
    }
}
