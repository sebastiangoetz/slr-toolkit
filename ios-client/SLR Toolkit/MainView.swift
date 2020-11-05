import SwiftGit2
import SwiftUI

struct MainView: View {
    @State private var repositoryURL: URL?
    @State private var cloneError: GitManager.CloneError?
    
    var body: some View {
        VStack {
            HStack {
                Spacer()
                Button("Clone", action: clone)
                Spacer()
                Button("Delete", action: delete)
                Spacer()
            }
            if let cloneError = cloneError {
                Text("Error: \(cloneError.description)")
            } else if let repositoryURL = repositoryURL {
                Text("Repository cloned at \(repositoryURL.path)")
            }
        }
    }
    
    private func clone() {
        let environment = ProcessInfo.processInfo.environment
        guard let username = environment["GITHUB_USERNAME"], let token = environment["GITHUB_TOKEN"] else {
            print("Please set the environment variables GITHUB_USERNAME and GITHUB_TOKEN.")
            return
        }        
        
        let gitManager = GitManager(gitClient: HttpsGitClient(username: username, token: token))
        let remoteURL = URL(string: "https://github.com/MaxHaertwig/slr-example.git")!
        let result = gitManager.cloneRepository(at: remoteURL)
        switch result {
        case .success(let url):
            repositoryURL = url
        case .failure(let error):
            cloneError = error
        }
    }
    
    private func delete() {
        GitManager.deleteGitDirectory()
        repositoryURL = nil
        cloneError = nil
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
    }
}
