import SwiftGit2
import SwiftUI

struct MainView: View {
    var project: Project?
    
    var body: some View {
        if let project = project {
            ProjectView(project: project)
        } else {
            VStack(spacing: 20) {
                Text("Welcome to SLR Toolkit!\nClone your first project to get started.")
                    .multilineTextAlignment(.center)
                RoundedButton("Clone Project") {}
            }
            .navigationBarTitle("SLR Toolkit")
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
        let _ = gitManager.cloneRepository(at: remoteURL)
    }
    
    private func delete() {
        GitManager.deleteGitDirectory()
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationView {
                MainView(project: nil)
            }
            NavigationView {
                MainView(project: Constants.exampleProject)
            }
        }
    }
}
