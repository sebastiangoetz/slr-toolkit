import SwiftUI

/// Button with a minimum width showing a simple string.
struct TextButton: View {
    private var title: String
    private var minWidth: CGFloat?
    private var action: () -> Void

    init(_ title: String, minWidth: CGFloat? = nil, action: @escaping () -> Void) {
        self.title = title
        self.minWidth = minWidth
        self.action = action
    }

    var body: some View {
        RoundedButton(action: action) {
            Text(title)
                .frame(minWidth: minWidth ?? 0)
                .padding(EdgeInsets(top: 12, leading: 20, bottom: 12, trailing: 20))
        }
    }
}

struct TextButton_Previews: PreviewProvider {
    static var previews: some View {
        TextButton("OK") {}
        TextButton("OK") {}
            .colorScheme(.dark)
    }
}
