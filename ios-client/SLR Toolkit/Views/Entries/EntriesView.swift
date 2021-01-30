import CoreData
import SwiftUI

/// View for showing a list of entries with sort controls.
struct EntriesView: View {

    @Environment(\.managedObjectContext) private var managedObjectContext

    @State private var fetchRequest: NSFetchRequest<Entry>

    @FetchRequest private var entries: FetchedResults<Entry>

    private enum SortMode: String {
        case title
        case date
    }

    @State private var sortMode: SortMode
    @State private var sortAscending: Bool

    private var project: Project
    private var taxonomyClass: TaxonomyClass?

    init(project: Project, taxonomyClass: TaxonomyClass?) {
        self.project = project
        self.taxonomyClass = taxonomyClass

        let sortMode = SortMode(rawValue: UserDefaults.standard.string(forKey: .sortMode) ?? "") ?? .title
        let sortAscending = UserDefaults.standard.bool(forKey: .sortAscending)
        let fetchRequest = Entry.fetchRequest
        if let taxonomyClass = taxonomyClass {
            fetchRequest.predicate = NSPredicate(format: "project == %@ && (ANY classes == %@) && decisionRaw != 2", project, taxonomyClass)
        } else {
            fetchRequest.predicate = NSPredicate(format: "project == %@ && decisionRaw != 2", project)
        }
        fetchRequest.sortDescriptors = (sortMode == .title ? [] : [NSSortDescriptor(key: "year", ascending: sortAscending), NSSortDescriptor(key: "month", ascending: sortAscending)]) + [NSSortDescriptor(key: "title", ascending: sortMode == .date || sortAscending)]
        _fetchRequest = State(initialValue: fetchRequest)
        _entries = FetchRequest(fetchRequest: fetchRequest)

        _sortMode = State(initialValue: sortMode)
        _sortAscending = State(initialValue: sortAscending)
    }

    var body: some View {
        Group {
            if entries.isEmpty {
                VStack(spacing: 32) {
                    Image(systemName: "tray")
                        .font(.system(size: 50, weight: .light))
                    Text("No Entries")
                        .font(.title)
                }
                .foregroundColor(.secondary)
            } else {
                EntriesList(fetchRequest: fetchRequest)
                    .toolbar {
                        ToolbarItem(placement: .primaryAction) {
                            Menu {
                                sortModeButton(newSortMode: .title)
                                sortModeButton(newSortMode: .date)
                            } label : {
                                Label("Sort", systemImage: "arrow.up.arrow.down")
                            }
                        }
                    }
            }
        }
        .navigationBarTitle(taxonomyClass?.name ?? "All Entries", displayMode: .inline)
    }

    private func sortModeButton(newSortMode: SortMode) -> some View {
        let title = "Sort by " + (newSortMode == .title ? "title" : "date")
        return Button {
            if sortMode == newSortMode {
                sortAscending.toggle()
            } else {
                sortMode = newSortMode
                sortAscending = true
                UserDefaults.standard.set(sortMode.rawValue, forKey: .sortMode)
                UserDefaults.standard.set(sortAscending, forKey: .sortAscending)
            }
            fetchRequest.sortDescriptors = (sortMode == .title ? [] : [NSSortDescriptor(key: "year", ascending: sortAscending), NSSortDescriptor(key: "month", ascending: sortAscending)]) + [NSSortDescriptor(key: "title", ascending: sortMode == .date || sortAscending)]
        } label: {
            if sortMode == newSortMode {
                Label(title, systemImage: sortAscending ? "chevron.up" : "chevron.down")
            } else {
                Text(title)
            }
        }
    }
}
