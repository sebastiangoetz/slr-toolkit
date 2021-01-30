import SwiftUI

struct ClassifyEntryView: View {
    private var entry: Entry

    @Environment(\.presentationMode) private var presentationMode
    @Environment(\.managedObjectContext) private var managedObjectContext

    @State private var classes: Set<TaxonomyClass>

    init(entry: Entry) {
        self.entry = entry
        _classes = State(initialValue: entry.classes)
    }

    var body: some View {
        NavigationView {
            List {
                OutlineGroup(entry.project.sortedRootClasses, children: \.sortedChildren) { taxonomyClass in
                    Button {
                        didSelectTaxonomyClass(taxonomyClass)
                    } label: {
                        HStack {
                            Text(taxonomyClass.name)
                                .foregroundColor(.primary)
                            Spacer()
                            if classes.contains(taxonomyClass) {
                                Image(systemName: "checkmark")
                            }
                        }
                    }
                }
            }
            .listStyle(InsetGroupedListStyle())
            .navigationBarTitle("Classify " + entry.citationKey, displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") {
                        presentationMode.wrappedValue.dismiss()
                    }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Done", action: done)
                }
            }
        }
    }

    private func didSelectTaxonomyClass(_ taxonomyClass: TaxonomyClass) {
        if classes.contains(taxonomyClass) {
            classes.remove(taxonomyClass)
        } else {
            classes.insert(taxonomyClass)
        }
    }

    private func done() {
        entry.classesChanged = true
        entry.classes = classes
        managedObjectContext.saveAndLogError()
        presentationMode.wrappedValue.dismiss()
    }
}
