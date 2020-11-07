import SwiftUI

struct RoundedButton: View {
    var title: String
    var action: () -> Void
    
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
        RoundedButton(title: "OK", action: {})
    }
}
