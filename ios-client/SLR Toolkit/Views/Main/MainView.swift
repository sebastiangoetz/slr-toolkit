import CoreData
import SwiftUI

struct MainView: View {
    @Environment(\.managedObjectContext) private var managedObjectContext
    
    @State var project: Project?
    
    @State private var addProjectIsPresented = false
    @State private var projectsViewIsPresented = false
    
    var body: some View {
        Group {
            if let project = project {
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
            } else {
                VStack(spacing: 20) {
                    Text("Welcome to SLR Toolkit!\nAdd a project to get started.")
                        .multilineTextAlignment(.center)
                    TextButton("Add Project") {
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
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationView {
                MainView(project: nil)
            }
        }
    }
}
