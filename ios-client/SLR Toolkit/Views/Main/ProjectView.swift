import CoreData
import SwiftUI

/// Main view for showing a project.
struct ProjectView: View {
    /// Used to denote the sheet that should be presented.
    enum Sheet: Int, Identifiable {
        case projectsView, projectSettingsView, settingsView, filterEntriesView
        var id: Int { rawValue }
    }

    @Environment(\.projectViewInteractor) private var interactor
    @Environment(\.gitManager) private var gitManager
    @Environment(\.managedObjectContext) private var managedObjectContext

    @Binding var project: Project!

    @FetchRequest private var unfilteredEntries: FetchedResults<Entry>
    @FetchRequest private var unclassifiedEntries: FetchedResults<Entry>
    @FetchRequest private var changedEntries: FetchedResults<Entry>

    @State private var commitsBehindOrigin = 0
    @State private var isFetchingOrPulling = false
    @State private var isCommitting = false
    @State private var isShowingUnclassifiedEntries = false
    @State private var alertContent: AlertContent?
    @State private var presentedSheet: Sheet?

    init(project: Binding<Project?>) {
        _project = project

        _unfilteredEntries = FetchRequest(fetchRequest: Entry.fetchRequest.withPredicate(NSPredicate(format: "project == %@ && decisionRaw == 0", project.wrappedValue!)).withSortDescriptors([]))
        _unclassifiedEntries = FetchRequest(fetchRequest: Entry.fetchRequest.withPredicate(NSPredicate(format: "project == %@ && decisionRaw != 2 && classes.@count == 0", project.wrappedValue!)).withSortDescriptors([]))
        _changedEntries = FetchRequest(fetchRequest: Entry.fetchRequest.withPredicate(NSPredicate(format: "project == %@ && (decisionRaw == 2 || classesChanged == YES)", project.wrappedValue!)).withSortDescriptors([]))
    }

    var body: some View {
        List {
            ButtonRow(buttons: [
                ("Filter", unfilteredEntries.isEmpty ? "All done!" : unfilteredEntries.count == 1 ? "1 entry" : "\(unfilteredEntries.count) entries", !unfilteredEntries.isEmpty, false, {
                    presentedSheet = .filterEntriesView
                }),
                ("Classify", unclassifiedEntries.isEmpty ? "All done!" : unclassifiedEntries.count == 1 ? "1 entry" : "\(unclassifiedEntries.count) entries", !unclassifiedEntries.isEmpty, false, {
                    isShowingUnclassifiedEntries = true
                })
            ])
            Section(header: Text("Source Control")) {
                ButtonRow(buttons: [
                    ("Pull", commitsBehindOrigin == 0 ? "Up to date" : commitsBehindOrigin == 1 ? "1 commit behind" : "\(commitsBehindOrigin) commits behind", true, isFetchingOrPulling, {
                        interactor.pull(project: project, isLoading: $isFetchingOrPulling, commitsBehindOrigin: $commitsBehindOrigin, alertContent: $alertContent)
                    }),
                    ("Commit", changedEntries.isEmpty ? "No changes" : changedEntries.count == 1 ? "1 change" : "\(changedEntries.count) changes", !isFetchingOrPulling && !changedEntries.isEmpty, isCommitting, {
                        interactor.commit(project: project, isLoading: $isCommitting, alertContent: $alertContent)
                    })
                ])
            }
            Section(header: Text("Entries")) {
                NavigationLink(destination: EntriesView(project: project, taxonomyClass: nil)) {
                    DetailRow(text: "All Entries", detail: "\(project.entries.filter { $0.decision != .discard }.count)")
                }
            }
            Section(header: Text("Taxonomy")) {
                OutlineGroup(project.sortedRootClasses, children: \.sortedChildren) { taxonomyClass in
                    DetailRow(text: taxonomyClass.name, detail: "\(taxonomyClass.entries.filter { $0.decision != .discard }.count)")
                    NavigationLink(destination: EntriesView(project: project, taxonomyClass: taxonomyClass)) {
                        EmptyView()
                    }
                    .frame(width: 0)
                    .opacity(0)
                }
            }
        }
        .listStyle(InsetGroupedListStyle())
        .overlay(NavigationLink(destination: EntryDetailsView(fetchRequest: Entry.fetchRequest.withPredicate(NSPredicate(format: "project == %@ && decisionRaw != 2 && classes.@count == 0", project)).withSortDescriptors([NSSortDescriptor(key: "citationKey", ascending: true)])), isActive: $isShowingUnclassifiedEntries) { Text("") })  // gets triggered by "Classify" button
        .navigationBarTitle(project.name)
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                menu()
            }
        }
        .alert(item: $alertContent) { alertContent in
            Alert(title: Text(alertContent.title), message: alertContent.message == nil ? nil : Text(alertContent.message!), dismissButton: .cancel(Text("OK")))
        }
        .sheet(item: $presentedSheet) { sheet in
            switch sheet {
            case .projectsView:
                ProjectsView(activeProject: $project)
                    .environment(\.managedObjectContext, managedObjectContext)
            case .projectSettingsView:
                ProjectSettingsView(project: project)
                    .environment(\.managedObjectContext, managedObjectContext)
            case .settingsView:
                SettingsView()
            case .filterEntriesView:
                FilterEntriesView(entries: unfilteredEntries.map { $0 })
            }
        }
        .onAppear {
            commitsBehindOrigin = gitManager.commitsAheadAndBehindOrigin(project: project).behind
        }
    }

    private func menu() -> some View {
        Menu {
            Button {
                interactor.fetch(project: project, isLoading: $isFetchingOrPulling, commitsBehindOrigin: $commitsBehindOrigin, alertContent: $alertContent)
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
            .keyboardShortcut(",", modifiers: .command)
        } label: {
            Label("Options", systemImage: "ellipsis.circle")
        }
    }
}
