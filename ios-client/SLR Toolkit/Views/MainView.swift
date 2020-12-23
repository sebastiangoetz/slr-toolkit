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
                let fetchRequest = Entry.fetchRequest.withPredicate(NSPredicate(format: "project == %@ && isRemoved == NO", project))
                let entriesCount = (try? managedObjectContext.count(for: fetchRequest)) ?? 0
                List {
                    Section {
                        NavigationLink(destination: EntriesView(project: project, taxonomyClass: nil)) {
                            // TODO
                            DetailRow(text: "All Entries", detail: "\(entriesCount)")
                        }
                    }
                    Section(header: Text("Taxonomy")) {
                        OutlineGroup(project.taxonomy, children: \.children) { node in
                            // TODO
                            DetailRow(text: node.name, detail: "0")
                            NavigationLink(destination: EntriesView(project: project, taxonomyClass: node.path)) {
                                EmptyView()
                            }
                            .frame(width: 0)
                            .opacity(0)
                        }
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
                    RoundedButton("Add Project") {
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
