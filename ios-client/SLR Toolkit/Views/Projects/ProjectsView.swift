import CoreData
import SwiftUI

struct ProjectsView: View {
    @Binding var activeProject: Project?
    
    @Environment(\.managedObjectContext) private var managedObjectContext
    @Environment(\.presentationMode) private var presentationMode
    
    @FetchRequest(sortDescriptors: [NSSortDescriptor(key: "name", ascending: true)]) private var projects: FetchedResults<Project>
    
    @State private var addProjectViewIsPresented = false
    
    var body: some View {
        let optionalProjectBinding = Binding<Project?>(
            get: { activeProject },
            set: { newProject in
                if let newProject = newProject {
                    activeProject = newProject
                }
            }
        )
        return NavigationView {
            List {
                ForEach(projects, id: \.objectID) { project in
                    ProjectRow(project: project, isActiveProject: project == activeProject)
                        .tag(project.objectID)
                        .onTapGesture {
                            activeProject = project
                            presentationMode.wrappedValue.dismiss()
                        }
                }
                .onDelete(perform: deleteProjects)
            }
            .listStyle(InsetGroupedListStyle())
            .navigationBarTitle("Projects", displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button {
                        addProjectViewIsPresented = true
                    } label: {
                        Label("Add Project", systemImage: "plus")
                    }
                    .keyboardShortcut("n", modifiers: .command)
                }
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") {
                        presentationMode.wrappedValue.dismiss()
                    }
                }
            }
            .sheet(isPresented: $addProjectViewIsPresented) {
                AddProjectView(project: optionalProjectBinding, isPresented: $addProjectViewIsPresented)
                    .environment(\.managedObjectContext, managedObjectContext)
            }
        }
    }

    private func deleteProjects(indexSet: IndexSet) {
        for index in indexSet {
            let project = projects[index]
            if project == activeProject {
                if projects.count <= 1 {
                    activeProject = nil
                    UserDefaults.standard.removeURL(forKey: .activeProject)
                    presentationMode.wrappedValue.dismiss()
                } else {
                    activeProject = projects.first { $0 != project }
                    UserDefaults.standard.set(activeProject!.objectID.uriRepresentation(), forKey: .activeProject)
                }
            }
            managedObjectContext.delete(project)
        }
        try? managedObjectContext.save()
    }
}

struct ProjectRow: View {
    var project: Project
    var isActiveProject: Bool
    
    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(project.name)
                Text(project.repositoryURL)
                    .font(.caption)
                    .foregroundColor(.secondary)
                    .lineLimit(1)
            }
            if isActiveProject {
                Spacer()
                Image(systemName: "checkmark")
            }
        }
    }
}

//struct ProjectsView_Previews: PreviewProvider {
//    static var previews: some View {
//        let persistenceController = PersistenceController(inMemory: true)
//        for i in 1...5 {
//            Project.newProject(name: "Project \(i)", username: "", token: "", repository: URL(fileURLWithPath: ""), path: "", in: persistenceController.container.viewContext)
//        }
//        try? persistenceController.container.viewContext.save()
//        return ProjectsView()
//            .environment(\.managedObjectContext, persistenceController.container.viewContext)
//    }
//}
