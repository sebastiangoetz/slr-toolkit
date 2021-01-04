import SwiftUI

struct ButtonRow: View {
    typealias ButtonConfiguration = (title: String, subtitle: String, enabled: Bool)

    var buttons: [ButtonConfiguration]

    var body: some View {
        HStack(spacing: 20) {
            ForEach(buttons, id: \.0) { configuration in
                SubtitleButton(title: configuration.title, subtitle: configuration.subtitle)
                    .disabled(!configuration.enabled)
            }
        }
        .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
        .listRowBackground(Color(.systemGroupedBackground))
    }
}

struct ButtonRow_Previews: PreviewProvider {
    static var previews: some View {
        ButtonRow(buttons: [("Title1", "Subtitle1", true), ("Title2", "Subtitle2", false)])
    }
}
