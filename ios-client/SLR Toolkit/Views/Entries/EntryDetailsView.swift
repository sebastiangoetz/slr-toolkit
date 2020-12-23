import SwiftUI
import SwiftyBibtex

struct EntryDetailsView: View {
    var entry: Entry

    var body: some View {
        List {
            Section(header: Text("Title").font(.caption)) {
                Text(entry.title ?? "No Title")
                    .font(.headline)
            }
            if let author = entry.author {
                Section(header: Text("Author").font(.caption)) {
                    Text(author)
                }
            }
            if let dateString = entry.dateString {
                Section(header: Text("Date").font(.caption)) {
                    Text(dateString)
                }
            }
            Section(header: Text("Abstract").font(.caption)) {
                Text(entry.abstract ?? "No Abstract")
                    .font(.callout)
            }
            if let urlString = entry.fields["url"] {
                Section(header: Text("URL").font(.caption)) {
                    if let url = URL(string: urlString) {
                        Button {
                            UIApplication.shared.open(url, options: [:])
                        } label: {
                            Text(urlString)
                                .lineLimit(1)
                        }
                    } else {
                        Text(urlString)
                            .lineLimit(1)
                    }
                }
            }
        }
        .listStyle(GroupedListStyle())
        .navigationBarTitle(entry.citationKey, displayMode: .inline)
    }
}

//struct EntryDetailView_Previews: PreviewProvider {
//    static var previews: some View {
//        EntryDetailsView(entry: Antp)
//    }
//}
