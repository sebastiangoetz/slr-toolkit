import CoreData
import SwiftUI

struct EntryDetailsView: View {
    var entry: Entry

    @Environment(\.managedObjectContext) private var managedObjectContext
    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        EntryCard(entry: entry)
            .padding(20)
            .background(Color(.systemGroupedBackground).ignoresSafeArea())
            .navigationBarTitle(entry.citationKey, displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Menu {
                        Button(action: removeEntry) {
                            Label("Remove entry", systemImage: "trash")
                        }
                    } label: {
                        Label("Remove", systemImage: "trash")
                    }
                }
            }
    }

    private func removeEntry() {
        entry.decision = .discard
        do {
            try managedObjectContext.save()
        } catch {
            print("Error saving context: \(error)")
        }
        presentationMode.wrappedValue.dismiss()
    }
}
