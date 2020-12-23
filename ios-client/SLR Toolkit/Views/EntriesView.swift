import SwiftUI
import SwiftyBibtex

struct EntriesView: View {
    var project: Project
    var taxonomyClass: String?

    @FetchRequest private var entries: FetchedResults<Entry>

    init(project: Project, taxonomyClass: String?) {
        self.project = project
        self.taxonomyClass = taxonomyClass
        self._entries = FetchRequest(sortDescriptors: [NSSortDescriptor(key: "title", ascending: true)], predicate: NSPredicate(format: "project == %@ && isRemoved == NO", project))
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
//                // TODO show sorted
                List(entries, id: \.citationKey) { entry in
                    NavigationLink(destination: EntryDetailsView(entry: entry)) {
                        VStack(alignment: .leading, spacing: 4) {
                            Text(entry.title ?? "No Title")
                                .bold()
                                .lineLimit(1)
                            if let author = entry.author {
                                Text(author)
                                    .font(.footnote)
                                    .lineLimit(1)
                            }
                            Text(entry.abstract ?? "No Abstract")
                                .font(.footnote)
                                .foregroundColor(.secondary)
                                .lineLimit(2)
                        }
                        .padding(.vertical, 2)
                    }
                }
            }
        }
        .navigationBarTitle(taxonomyClass?.components(separatedBy: "###").last ?? "All Entries", displayMode: .inline)
    }
}

//struct EntriesView_Previews: PreviewProvider {
//    static var previews: some View {
//        Group {
//            NavigationView {
//                EntriesView(title: "All Entries", entries: [])
//            }
//            NavigationView {
//                EntriesView(title: "All Entries", entries: [])
//            }
//            .colorScheme(.dark)
//        }
//    }
//}
