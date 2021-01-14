import CoreData
import SwiftUI

struct ProjectView: View {
    enum Sheet: Int, Identifiable {
        case projectsView, projectSettingsView, settingsView

        var id: Int { rawValue }
    }

    @Environment(\.managedObjectContext) private var managedObjectContext
    @Environment(\.projectViewInteractor) private var interactor

    @Binding var project: Project!

    @FetchRequest private var unfilteredEntries: FetchedResults<Entry>
    @FetchRequest private var unclassifiedEntries: FetchedResults<Entry>
    @FetchRequest private var changedEntries: FetchedResults<Entry>

    @State private var commitsBehindOrigin: Int
    @State private var isFetchingOrPulling = false
    @State private var alertError: AlertError?
    @State private var presentedSheet: Sheet?

    init(project: Binding<Project?>) {
        _project = project

        _commitsBehindOrigin = State(initialValue: GitManager.default.commitsAheadAndBehindOrigin(project: project.wrappedValue!).behind)

        _unfilteredEntries = FetchRequest(fetchRequest: Entry.fetchRequest.withPredicate(NSPredicate(format: "project == %@ && decisionRaw == 0", project.wrappedValue!)).withSortDescriptors([]))
        _unclassifiedEntries = FetchRequest(fetchRequest: Entry.fetchRequest.withPredicate(NSPredicate(format: "project == %@ && classes.@count == 0", project.wrappedValue!)).withSortDescriptors([]))
        _changedEntries = FetchRequest(fetchRequest: Entry.fetchRequest.withPredicate(NSPredicate(format: "project == %@ && (decisionRaw == 2 || (hadClasses == NO && classes.@count > 0))", project.wrappedValue!)).withSortDescriptors([]))
    }

    var body: some View {
        List {
            ButtonRow(buttons: [
                ("Filter", unfilteredEntries.isEmpty ? "All done!" : "\(unfilteredEntries.count) entries", !unfilteredEntries.isEmpty, false, {}),
                ("Classify", unclassifiedEntries.isEmpty ? "All done!" : "\(unclassifiedEntries.count) entries", !unclassifiedEntries.isEmpty, false, {})
            ])
            Section(header: Text("Source Control")) {
                ButtonRow(buttons: [
                    ("Pull", commitsBehindOrigin == 0 ? "Up to date" : commitsBehindOrigin == 1 ? "1 commit behind" : "\(commitsBehindOrigin) commits behind", true, isFetchingOrPulling, { interactor.pull(project: project, isLoading: $isFetchingOrPulling, commitsBehindOrigin: $commitsBehindOrigin, alertError: $alertError) }),
                    ("Commit", changedEntries.isEmpty ? "No changes" : changedEntries.count == 1 ? "1 change" : "\(changedEntries.count) changes", !isFetchingOrPulling && !changedEntries.isEmpty, isCommitting, {})
                ])
            }
            Section(header: Text("Entries")) {
                NavigationLink(destination: EntriesView(project: project, taxonomyClass: nil)) {
                    DetailRow(text: "All Entries", detail: "\(project.entries.filter { $0.decision != .discard }.count)")
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
        .sheet(item: $presentedSheet) { sheet in
            switch sheet {
            case .projectsView:
                ProjectsView(activeProject: $project)
                    .environment(\.managedObjectContext, managedObjectContext)
            case .projectSettingsView:
                ProjectSettingsView(project: project)
            case .settingsView:
                SettingsView()
            }
        }
    }

    private func menu() -> some View {
        Menu {
            Button {
                interactor.fetch(project: project, isLoading: $isFetchingOrPulling, commitsBehindOrigin: $commitsBehindOrigin, alertError: $alertError)
            } label: {
                Label("Fetch", systemImage: "arrow.down")
            }
            Divider()
            Button {
                presentedSheet = .projectsView
            } label: {
                Label("Change Project", systemImage: "folder")
            }
            Button {
                presentedSheet = .projectSettingsView
            } label: {
                Label("Project Settings", systemImage: "folder.badge.gear")
            }
            Divider()
            Button {
                presentedSheet = .settingsView
            } label: {
                Label("App Settings", systemImage: "gearshape")
            }
        } label: {
            Image(systemName: "ellipsis.circle")
                .imageScale(.large)
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
