import CoreData
import SwiftUI

struct MainView: View {
    @Environment(\.managedObjectContext) private var managedObjectContext
    
    @State var project: Project?
    
    @State private var addProjectIsPresented = false
    @State private var projectsViewIsPresented = false
    
    var body: some View {
        if let project = project {
            List {
                Section {
                    NavigationLink(destination: EntriesView(title: "All Entries", entries: project.entries ?? [])) {
                        DetailRow(text: "All Entries", detail: "\(project.entries?.count ?? 0)")
                    }
                }
                Section(header: HStack {
                    Text("Taxonomy")
                    Spacer()
                    Button("Edit", action: {})
                        .foregroundColor(.accentColor)
                }) {
                    OutlineGroup(project.taxonomy, children: \.children) { node in
                        DetailRow(text: node.name, detail: "\(node.entries.count)")
                        NavigationLink(destination: EntriesView(title: node.name, entries: node.entries)) {
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
