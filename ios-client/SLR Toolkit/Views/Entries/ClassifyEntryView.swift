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
                        entry.classesChanged = true
                        if classes.contains(taxonomyClass) {
                            classes.remove(taxonomyClass)
                            entry.classes.remove(taxonomyClass)
                        } else {
                            classes.insert(taxonomyClass)
                            entry.classes.insert(taxonomyClass)
                        }
                        managedObjectContext.saveAndLogError()
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
                ToolbarItem(placement: .confirmationAction) {
                    Button("Done") {
                        presentationMode.wrappedValue.dismiss()
                    }
                }
            }
        }
    }
}
