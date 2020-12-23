import CoreData
import SwiftUI

struct EntriesView: View {
    var project: Project
    var taxonomyClass: String?

    @Environment(\.managedObjectContext) private var managedObjectContext

    @State private var fetchRequest: NSFetchRequest<Entry>

    @FetchRequest private var entries: FetchedResults<Entry>

    private enum SortMode: String {
        case title
        case date
    }

    @State private var sortMode: SortMode

    init(project: Project, taxonomyClass: String?) {
        self.project = project
        self.taxonomyClass = taxonomyClass

        let sortMode = SortMode(rawValue: UserDefaults.standard.string(forKey: .sortMode) ?? "") ?? .title
        let fetchRequest = Entry.fetchRequest
        fetchRequest.predicate = NSPredicate(format: "project == %@ && isRemoved == NO", project)
        fetchRequest.sortDescriptors = (sortMode == .title ? [] : [NSSortDescriptor(key: "year", ascending: true), NSSortDescriptor(key: "month", ascending: true)]) + [NSSortDescriptor(key: "title", ascending: true)]
        _fetchRequest = State(initialValue: fetchRequest)
        _entries = FetchRequest(fetchRequest: fetchRequest)

        _sortMode = State(initialValue: sortMode)
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
        .navigationBarTitle(taxonomyClass?.components(separatedBy: "###").last ?? "All Entries", displayMode: .inline)
    }

    private func sortModeButton(newSortMode: SortMode) -> some View {
        let title = "Sort by " + (sortMode == .title ? "title" : "date")
        return Button {
            if sortMode != newSortMode {
                sortMode = newSortMode
                UserDefaults.standard.set(sortMode.rawValue, forKey: .sortMode)
                fetchRequest.sortDescriptors = (sortMode == .title ? [] : [NSSortDescriptor(key: "year", ascending: true), NSSortDescriptor(key: "month", ascending: true)]) + [NSSortDescriptor(key: "title", ascending: true)]
            }
        } label : {
            if sortMode == newSortMode {
                Label(title, systemImage: "checkmark")
            } else {
                Text(title)
            }
        }
    }
}
