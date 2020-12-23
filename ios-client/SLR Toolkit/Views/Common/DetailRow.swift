import SwiftUI

struct DetailRow: View {
    let text: String
    let detail: String

    var body: some View {
        HStack {
            Text(text)
            Spacer()
            Text(detail)
                .foregroundColor(.secondary)
        }
    }
}

struct DetailRow_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            DetailRow(text: "Text", detail: "Detail")
            DetailRow(text: "Text", detail: "Detail")
                .colorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
    }
}
