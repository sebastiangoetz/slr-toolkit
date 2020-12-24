import SwiftUI

struct CardValue<Content>: View where Content: View {
    let title: String
    let content: Content

    init(title: String, @ViewBuilder content: () -> Content) {
        self.title = title
        self.content = content()
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(title)
                .textCase(.uppercase)
                .font(.caption)
                .foregroundColor(.secondary)
            content
        }
    }
}
