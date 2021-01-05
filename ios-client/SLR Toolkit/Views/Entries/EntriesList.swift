import CoreData
import SwiftUI

struct EntriesList: View {
    @Environment(\.managedObjectContext) private var managedObjectContext

    @FetchRequest private var entries: FetchedResults<Entry>

    init(fetchRequest: NSFetchRequest<Entry>) {
        _entries = FetchRequest(fetchRequest: fetchRequest)
    }

    var body: some View {
        List {
            ForEach(entries) { entry in
                NavigationLink(destination: EntryDetailsView(entry: entry)) {
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
