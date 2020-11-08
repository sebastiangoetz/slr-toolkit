import SwiftUI

struct ProjectView: View {
    var project: Project
    
    var body: some View {
        List {
            Section {
                Text("All Entries")
            }
            Section(header: HStack {
                Text("Taxonomy")
                Spacer()
                Button("Edit", action: {})
                    .foregroundColor(.accentColor)
            }) {
                OutlineGroup(project.taxonomy, children: \.children) { node in
                    Text(node.name)
                }
            }
        }
        .listStyle(InsetGroupedListStyle())
        .navigationBarTitle(project.name)
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                Menu {
                    Button(action: {}) {
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
    }
}

//struct ProjectView_Previews: PreviewProvider {
//    static var previews: some View {
//        NavigationView {
//            ProjectView(project: Constants.exampleProject)
//        }
//    }
//}
