import SwiftUI

struct RoundedButton: View {
    private var title: String
    private var action: () -> Void
    
    init(_ title: String, action: @escaping () -> Void) {
        self.title = title
        self.action = action
    }
    
    var body: some View {
        Button(action: action) {
            Text(title)
                .padding(EdgeInsets(top: 12, leading: 20, bottom: 12, trailing: 20))
                .foregroundColor(.white)
                .background(Color.accentColor)
                .cornerRadius(12)
        }
    }
}

struct RoundedRectButton_Previews: PreviewProvider {
    static var previews: some View {
        RoundedButton("OK", action: {})
    }
}
