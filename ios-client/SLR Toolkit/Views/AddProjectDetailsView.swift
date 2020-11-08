import CoreData
import SwiftUI

struct AddProjectDetailsView: View {
    @Environment(\.managedObjectContext) private var managedObjectContext
    @Environment(\.presentationMode) private var presentationMode
    
    @Binding var project: Project?
    
    @State private var projectName = ""
    @State private var selection: Directory
    @State private var isDirectoryValid = false
    
    private let username, token: String
    private let directories: [Directory]
    
    init(username: String, token: String, repositoryDirectory: URL, project: Binding<Project?>) {
        self.username = username
        self.token = token
        directories = [Directory(url: repositoryDirectory)]
        _selection = State(initialValue: directories[0])
        _isDirectoryValid = State(initialValue: directories[0].isValidProjectDirectory)
        _project = project
    }
    
    var body: some View {
        List {
            Section(header: Text("Name")) {
                TextField("Project name", text: $projectName)
            }
            Section(header: Text("Path"), footer: Text(isDirectoryValid ? "This is a valid directory ✓" : "Select a directory that contains a single .bib file and a single .taxonomy file.")) {
                OutlineGroup(self.directories, children: \Directory.directories) { directory in
                    Button(action: { selectDirectory(directory) }) {
                        HStack {
                            if let selection = selection, selection == directory {
                                Text(directory.name)
                                    .foregroundColor(.accentColor)
                            } else {
                                Text(directory.name)
                            }
                            Spacer()
                        }
                    }
                    .foregroundColor(.primary)
                }
            }
        }
        .listStyle(InsetGroupedListStyle())
        .navigationBarTitle("Add Project", displayMode: .inline)
        .toolbar {
            ToolbarItem(placement: .confirmationAction) {
                Button("Done", action: done)
                    .disabled(projectName.trimmingCharacters(in: .whitespaces).isEmpty || !isDirectoryValid)
            }
        }
    }
    
    private func selectDirectory(_ directory: Directory) {
        selection = directory
        isDirectoryValid = directory.isValidProjectDirectory
    }
    
    private func done() {
        let repositoryURL = directories[0].url
        let path = selection.url.pathComponents[repositoryURL.pathComponents.count...].joined(separator: "/")
        let newProject = Project.newProject(name: projectName.trimmingCharacters(in: .whitespaces), username: username, token: token, repository: repositoryURL, path: path, in: managedObjectContext)
        project = newProject
        do {
            try managedObjectContext.save()
            UserDefaults.standard.set(newProject.objectID.uriRepresentation(), forKey: .activeProject)
        } catch {
            print("Error saving managed object context: \(error)")
        }
        presentationMode.wrappedValue.dismiss()
    }
}

struct AddProjectDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            AddProjectDetailsView(username: "", token: "", repositoryDirectory: URL(fileURLWithPath: ""), project: .constant(nil))
        }
    }
}
