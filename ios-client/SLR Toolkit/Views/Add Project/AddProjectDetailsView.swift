import CoreData
import SwiftUI

struct AddProjectDetailsView: View {
    @Binding var project: Project?
    @Binding var isPresented: Bool
    
    @Environment(\.managedObjectContext) private var managedObjectContext
    
    @State private var projectName = ""
    @State private var selection: Directory
    @State private var isDirectoryValid = false

    @State private var isLoading = false
    @State private var projectCreationPhase: String?
    
    private let username, token, repositoryURL: String
    private let directories: [Directory]
    
    init(username: String, token: String, repositoryURL: String, repositoryDirectory: URL, project: Binding<Project?>, isPresented: Binding<Bool>) {
        self.username = username
        self.token = token
        self.repositoryURL = repositoryURL
        
        directories = [Directory(url: repositoryDirectory, isRoot: true)]
        _selection = State(initialValue: directories[0])
        _isDirectoryValid = State(initialValue: directories[0].isValidProjectDirectory)
        _project = project
        _isPresented = isPresented
    }
    
    var body: some View {
        List {
            Section(header: Text("Name")) {
                TextField("Project name", text: $projectName)
            }
            Section(header: Text("Path"), footer: Text(isDirectoryValid ? "This is a valid directory ✓" : "Select a directory that contains one or more .bib files and a single .taxonomy file.")) {
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
                if isLoading {
                    if let phase = projectCreationPhase {
                        VStack {
                            ProgressView()
                            Text(phase)
                                .font(.caption2)
                                .foregroundColor(.secondary)
                        }
                    } else {
                        ProgressView()
                    }
                } else {
                    Button("Done", action: done)
                        .disabled(projectName.trimmingCharacters(in: .whitespaces).isEmpty || !isDirectoryValid)
                }
            }
        }
    }
    
    private func selectDirectory(_ directory: Directory) {
        selection = directory
        isDirectoryValid = directory.isValidProjectDirectory
    }
    
    private func done() {
        isLoading = true
        let repositoryDirectory = directories[0].url
        let pathInGitDirectory = repositoryDirectory.pathComponents[GitManager.gitDirectory.pathComponents.count...].joined(separator: "/")
        let pathInRepository = selection.url.pathComponents[repositoryDirectory.pathComponents.count...].joined(separator: "/")
        ProjectManager.createProject(name: projectName.trimmingCharacters(in: .whitespaces), username: username, token: token, repositoryURL: repositoryURL, pathInGitDirectory: pathInGitDirectory, pathInRepository: pathInRepository) { newProject in
            project = newProject
            isLoading = false
            isPresented = false
        }
    }
}

struct AddProjectDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            AddProjectDetailsView(username: "", token: "", repositoryURL: "", repositoryDirectory: URL(fileURLWithPath: ""), project: .constant(nil), isPresented: .constant(true))
        }
    }
}
