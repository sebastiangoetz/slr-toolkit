import CoreData
import SwiftUI

struct EntryDetailsView: View {
    @Environment(\.presentationMode) private var presentationMode
    @Environment(\.managedObjectContext) private var managedObjectContext

    @State private var currentEntry: Entry
    @State private var classifyEntryViewIsPresented = false

    private var fetchRequest: NSFetchRequest<Entry>

    init(fetchRequest: NSFetchRequest<Entry>, entry: Entry) {
        self.fetchRequest = fetchRequest
        _currentEntry = State(initialValue: entry)
    }

    var body: some View {
        let currentEntryBinding = Binding<Entry?>(
            get: { currentEntry },
            set: {
                if let newValue = $0 {
                    currentEntry = newValue
                } else {
                    presentationMode.wrappedValue.dismiss()
                }
            }
        )
        return EntryDetailsUIView(fetchRequest: fetchRequest, currentEntry: currentEntryBinding)
            .background(Color(.systemGroupedBackground).ignoresSafeArea())
            .navigationBarTitle(currentEntry.citationKey, displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("") {}  // Workaround
                }
                ToolbarItemGroup(placement: .navigationBarTrailing) {
                    Menu {
                        Button(action: discard) {
                            Label("Discard entry", systemImage: "trash")
                        }
                    } label: {
                        Label("Discard entry", systemImage: "trash")
                    }
                    Button {
                        classifyEntryViewIsPresented = true
                    } label: {
                        Label("Classify", systemImage: "tag")
                    }
                }
            }
            .sheet(isPresented: $classifyEntryViewIsPresented) {
                ClassifyEntryView(entry: currentEntry)
                    .environment(\.managedObjectContext, managedObjectContext)
            }
    }

    private func discard() {
        currentEntry.decision = .discard
        managedObjectContext.saveAndLogError()
    }
}

struct EntryDetailsUIView: UIViewControllerRepresentable {
    var fetchRequest: NSFetchRequest<Entry>
    var currentEntry: Binding<Entry?>

    func makeUIViewController(context: Context) -> EntryDetailsViewController {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let viewController = EntryDetailsViewController(collectionViewLayout: layout)
        viewController.fetchRequest = fetchRequest
        viewController.currentEntry = currentEntry
        return viewController
    }

    func updateUIViewController(_ uiViewController: EntryDetailsViewController, context: Context) {}
}
