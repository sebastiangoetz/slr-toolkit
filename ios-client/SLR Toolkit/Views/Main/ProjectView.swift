import CoreData
import SwiftUI

struct ProjectView: View {
    @Environment(\.managedObjectContext) private var managedObjectContext

    @Binding var project: Project!

    @FetchRequest private var unfilteredEntries: FetchedResults<Entry>
    @FetchRequest private var unclassifiedEntries: FetchedResults<Entry>

    @State private var commitsBehindOrigin: Int
    @State private var isFetchingOrPulling = false
    @State private var alertError: AlertError?
    @State private var projectsViewIsPresented = false

    init(project: Binding<Project?>) {
        _project = project

        _commitsBehindOrigin = State(initialValue: GitManager.default.commitsAheadAndBehindOrigin(project: project.wrappedValue!).behind)

        let fetchRequest1 = Entry.fetchRequest
        fetchRequest1.predicate = NSPredicate(format: "project == %@ && decisionRaw == 0", project.wrappedValue!)
        fetchRequest1.sortDescriptors = []
        _unfilteredEntries = FetchRequest(fetchRequest: fetchRequest1)

        let fetchRequest2 = Entry.fetchRequest
        fetchRequest2.predicate = NSPredicate(format: "project == %@ && classes.@count == 0", project.wrappedValue!)
        fetchRequest2.sortDescriptors = []
        _unclassifiedEntries = FetchRequest(fetchRequest: fetchRequest2)
    }

    var body: some View {
        List {
            ButtonRow(buttons: [
                ("Filter", unfilteredEntries.isEmpty ? "All done!" : "\(unfilteredEntries.count) entries", !unfilteredEntries.isEmpty, false, {}),
                ("Classify", unclassifiedEntries.isEmpty ? "All done!" : "\(unclassifiedEntries.count) entries", !unclassifiedEntries.isEmpty, false, {})
            ])
            Section(header: Text("Source Control")) {
                ButtonRow(buttons: [
                    ("Pull", commitsBehindOrigin == 0 ? "Up to date" : commitsBehindOrigin == 1 ? "1 commit behind" : "\(commitsBehindOrigin) commits behind", true, isFetchingOrPulling, pull),
                    ("Commit", "37 changes", !isFetchingOrPulling, false, {})
                ])
            }
            Section(header: Text("Entries")) {
                NavigationLink(destination: EntriesView(project: project, taxonomyClass: nil)) {
                    DetailRow(text: "All Entries", detail: "\(project.entries.count)")
                }
            }
            Section(header: Text("Taxonomy")) {
                OutlineGroup(project.sortedRootClasses, children: \.sortedChildren) { taxonomyClass in
                    DetailRow(text: taxonomyClass.name, detail: "\(taxonomyClass.entries.count)")
                    NavigationLink(destination: EntriesView(project: project, taxonomyClass: taxonomyClass)) {
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
                menu()
            }
        }
        .alert(item: $alertError, content: { alertError in
            Alert(title: Text(alertError.title), message: Text(alertError.message), dismissButton: .cancel(Text("OK")))
        })
        .sheet(isPresented: $projectsViewIsPresented) {
            ProjectsView(activeProject: $project)
                .environment(\.managedObjectContext, managedObjectContext)
        }
    }

    private func menu() -> some View {
        Menu {
            Button(action: fetch) {
                Label("Fetch", systemImage: "arrow.down")
            }
            Divider()
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

    private func fetch() {
        isFetchingOrPulling = true
        GitManager.default.fetch(project: project) { error in
            isFetchingOrPulling = false
            if let error = error {
                alertError = AlertError(title: "Error fetching repository", message: error.localizedDescription)
            } else {
                commitsBehindOrigin = GitManager.default.commitsAheadAndBehindOrigin(project: project).behind
            }
        }
    }

    private func pull() {
        isFetchingOrPulling = true
        GitManager.default.pull(project: project) { error in
            isFetchingOrPulling = false
            if let error = error {
                alertError = AlertError(title: "Error pulling repository", message: error.localizedDescription)
            } else {
                commitsBehindOrigin = 0
            }
        }
    }
}

struct ProjectView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationView {
                MainView(project: nil)
            }
        }
    }
}
