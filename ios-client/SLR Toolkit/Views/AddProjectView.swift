import SwiftUI

struct AddProjectView: View {
    static private let httpsPrefix = "https://"
    
    @Binding var project: Project?
    @Binding var isPresented: Bool
    
    @Environment(\.presentationMode) var presentationMode
    
    @State private var repositoryURL = ""
    @State private var username = ""
    @State private var token = ""
    @State private var rememberCredentials = true
    
    @State private var isLoading = false
    @State private var progress: Float?
    @State private var cloneErrorAlertIsPresented = false
    @State private var cloneError: String?
    
    @State private var navigateToNextScreen = false
    @State private var repositoryDirectory = URL(fileURLWithPath: "")
    
    init(project: Binding<Project?>, isPresented: Binding<Bool>) {
        if let username = UserDefaults.standard.string(forKey: .username), !username.isEmpty {
            _username = State(initialValue: username)
        }
        if let token = UserDefaults.standard.string(forKey: .token), !token.isEmpty {
            _token = State(initialValue: token)
        }
        _project = project
        _isPresented = isPresented
    }
    
    var body: some View {
        NavigationView {
            ZStack {
                List {
                    if repositoryURL.isEmpty || repositoryURL.hasPrefix(Self.httpsPrefix) {
                        Section(header: Text("Repository URL")) {
                            TextField(Self.httpsPrefix, text: $repositoryURL)
                        }
                    } else {
                        Section(header: Text("Repository URL"), footer: Text("Only https URLs are supported at the moment.")) {
                            TextField(Self.httpsPrefix, text: $repositoryURL)
                        }
                    }
                    Section(header: Text("Credentials"), footer: Text("GitHub: Navigate to Settings, Developer settings, Personal access tokens, and click Generate new token. Make sure to select the repo scope.\n\nGitLab: Navigate to Settings, Access Tokens. Make sure to select the read_repository and write_repository scopes, and click Create personal access token.")) {
                        TextField("Username", text: $username)
                        TextField("Token", text: $token)
                        Toggle("Remember credentials", isOn: $rememberCredentials)
                            .toggleStyle(SwitchToggleStyle(tint: .accentColor))
                    }
                }
                NavigationLink(destination: AddProjectDetailsView(username: username.trimmingCharacters(in: .whitespaces), token: token.trimmingCharacters(in: .whitespaces), repositoryURL: repositoryURL.trimmingCharacters(in: .whitespaces), repositoryDirectory: repositoryDirectory, project: $project, isPresented: $isPresented), isActive: $navigateToNextScreen) {
                    EmptyView()
                }
                .hidden()
            }
            .listStyle(InsetGroupedListStyle())
            .navigationBarTitle("Add Project", displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    if isLoading {
                        if let progress = progress {
                            ProgressView(value: progress, total: 1)
                                .progressViewStyle(CircularProgressViewStyle())
                        } else {
                            ProgressView()
                                .progressViewStyle(CircularProgressViewStyle())
                        }
                    } else {
                        Button("Next", action: next)
                            .disabled(!repositoryURL.hasPrefix(Self.httpsPrefix) || username.trimmingCharacters(in: .whitespaces).isEmpty || token.trimmingCharacters(in: .whitespaces).isEmpty)
                    }
                }
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") {
                        presentationMode.wrappedValue.dismiss()
                    }
                }
            }
            .alert(isPresented: $cloneErrorAlertIsPresented) {
                Alert(title: Text("Clone error"), message: Text(cloneError!), dismissButton: .default(Text("OK")))
            }
        }
    }
    
    private func next() {
        isLoading = true
        
        let trimmedUsername = username.trimmingCharacters(in: .whitespaces)
        let trimmedToken = token.trimmingCharacters(in: .whitespaces)
        
        if rememberCredentials {
            UserDefaults.standard.set(trimmedUsername, forKey: .username)
        }
        
        let gitManager = GitManager(gitClient: HttpsGitClient(username: trimmedUsername, token: trimmedToken))
        guard let url = URL(string: repositoryURL) else { return }
        let result = gitManager.cloneRepository(at: url) { progress in
            self.progress = progress
        }
        
        switch result {
        case .success(let repositoryDirectory):
            isLoading = false
            if rememberCredentials {
                UserDefaults.standard.set(trimmedToken, forKey: .token)
            }
            self.repositoryDirectory = repositoryDirectory
            navigateToNextScreen = true
        case .failure(let error):
            cloneErrorAlertIsPresented = true
            cloneError = error.description
        }
    }
}

struct AddProjectView_Previews: PreviewProvider {
    static var previews: some View {
        AddProjectView(project: .constant(nil), isPresented: .constant(true))
    }
}
