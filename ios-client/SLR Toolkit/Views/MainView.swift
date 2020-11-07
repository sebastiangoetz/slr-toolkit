import SwiftGit2
import SwiftUI

struct MainView: View {
    @State var project: Project?
    
    var body: some View {
        if let project = project {
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
                            Label("Switch Project", systemImage: "folder")
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
        } else {
            VStack(spacing: 20) {
                Text("Welcome to SLR Toolkit!\nClone your first project to get started.")
                    .multilineTextAlignment(.center)
                RoundedButton(title: "Clone Project", action: {})
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
                MainView(project: Project(name: "My Project", path: URL(fileURLWithPath: ""), taxonomy: [
                    TaxonomyNode(name: "Types of Research", children: [
                        TaxonomyNode(name: "Applied"),
                        TaxonomyNode(name: "Fundamental")
                    ]),
                    TaxonomyNode(name: "Application Domains", children: [
                        TaxonomyNode(name: "none"),
                        TaxonomyNode(name: "enterprise"),
                        TaxonomyNode(name: "cloud")
                    ])
                ]))
            }
        }
    }
}
