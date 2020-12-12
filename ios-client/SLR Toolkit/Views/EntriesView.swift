import SwiftUI
import SwiftyBibtex

struct EntriesView: View {
    var title: String
    var entries: [Publication]

    var body: some View {
        return Group {
            if entries.isEmpty {
                VStack(spacing: 32) {
                    Image(systemName: "tray")
                        .font(.system(size: 50, weight: .light))
                    Text("No Entries")
                        .font(.title)
                }
                .foregroundColor(.secondary)
                .navigationBarTitle(title, displayMode: .inline)
            } else {
                // TODO show sorted
                List(entries, id: \.citationKey) { entry in
                    NavigationLink(destination: EntryDetailsView(entry: entry)) {
                        VStack(alignment: .leading, spacing: 4) {
                            Text(entry.title)
                                .bold()
                                .lineLimit(1)
                            if let author = entry.author {
                                Text(author)
                                    .font(.footnote)
                                    .lineLimit(1)
                            }
                            Text(entry.abstract)
                                .font(.footnote)
                                .foregroundColor(.secondary)
                                .lineLimit(2)
                        }
                        .padding(.vertical, 2)
                    }
                }
                .navigationBarTitle(title, displayMode: .inline)
            }
        }
        .navigationBarTitle(title, displayMode: .inline)
    }
}

struct EntriesView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationView {
                EntriesView(title: "All Entries", entries: [])
            }
            NavigationView {
                EntriesView(title: "All Entries", entries: [])
            }
            .colorScheme(.dark)
        }
    }
}
