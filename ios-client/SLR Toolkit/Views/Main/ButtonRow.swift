import SwiftUI

struct ButtonRow: View {
    typealias ButtonConfiguration = (title: String, subtitle: String, enabled: Bool, loading: Bool, action: () -> Void)

    var buttons: [ButtonConfiguration]

    var body: some View {
        HStack(spacing: 20) {
            ForEach(buttons, id: \.0) { configuration in
                if configuration.loading {
                    ExpandingButton(action: {}) {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: .white))
                            .padding(.vertical, 16)
                    }
                } else {
                    SubtitleButton(title: configuration.title, subtitle: configuration.subtitle, action: configuration.action)
                        .buttonStyle(PlainButtonStyle())
                        .disabled(!configuration.enabled)
                }
            }
        }
        .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
        .listRowBackground(Color(.systemGroupedBackground))
    }
}

struct ButtonRow_Previews: PreviewProvider {
    static var previews: some View {
        ButtonRow(buttons: [
            ("Title1", "Subtitle1", true, false, {}),
            ("Title2", "Subtitle2", false, false, {}),
            ("Title2", "Subtitle2", true, true, {})
        ])
    }
}
