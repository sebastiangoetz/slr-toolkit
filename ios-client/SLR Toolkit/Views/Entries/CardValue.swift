import SwiftUI

/// Shows title and value of an entry's field.
struct CardValue<Content>: View where Content: View {
    private let title: String
    private let content: Content

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
