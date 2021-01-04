import SwiftUI

struct SubtitleButton: View {
    var title: String
    var subtitle: String

    var body: some View {
        ExpandingButton(action: {}) {
            VStack(spacing: 2) {
                Text(title)
                Text(subtitle)
                    .font(.caption)
                    .opacity(0.75)
            }
            .padding(.vertical, 8)
        }
    }
}

struct SubtitleButton_Previews: PreviewProvider {
    static var previews: some View {
        SubtitleButton(title: "Title", subtitle: "Subtitle")
    }
}
