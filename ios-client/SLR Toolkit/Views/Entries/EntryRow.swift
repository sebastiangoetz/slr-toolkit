import SwiftUI

struct EntryRow: View {
    var entry: Entry

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(entry.title ?? "No Title")
                .bold()
                .lineLimit(1)
            if entry.dateString != nil || entry.author != nil {
                Text(entry.dateString != nil && entry.author != nil ? entry.dateString! + ", " + entry.author! : entry.dateString != nil ? entry.dateString! : entry.author!)
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
