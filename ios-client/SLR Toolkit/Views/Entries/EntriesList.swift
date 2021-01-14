import CoreData
import SwiftUI

struct EntriesList: View {
    @Environment(\.managedObjectContext) private var managedObjectContext

    private var fetchRequest: NSFetchRequest<Entry>
    @FetchRequest private var entries: FetchedResults<Entry>

    init(fetchRequest: NSFetchRequest<Entry>) {
        self.fetchRequest = fetchRequest
        _entries = FetchRequest(fetchRequest: fetchRequest)
    }

    var body: some View {
        List {
            ForEach(entries) { entry in
                NavigationLink(destination: EntryDetailsView(fetchRequest: fetchRequest, entry: entry)) {
                    EntryRow(entry: entry)
                }
            }
            .onDelete { indexSet in
                indexSet.forEach { entries[$0].decision = .discard }
                managedObjectContext.saveAndLogError()
            }
        }
    }
}
