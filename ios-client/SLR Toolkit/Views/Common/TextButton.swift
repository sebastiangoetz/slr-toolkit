import SwiftUI

struct TextButton: View {
    private var title: String
    private var action: () -> Void

    init(_ title: String, action: @escaping () -> Void) {
        self.title = title
        self.action = action
    }

    var body: some View {
        RoundedButton(action: action) {
            Text(title)
                .padding(EdgeInsets(top: 12, leading: 20, bottom: 12, trailing: 20))
        }
    }
}

struct TextButton_Previews: PreviewProvider {
    static var previews: some View {
        TextButton("OK") {}
    }
}
