import SwiftUI

struct RoundedButton<Label: View>: View {
    private var action: () -> Void
    private var label: Label
    
    init(action: @escaping () -> Void, @ViewBuilder label: () -> Label) {
        self.action = action
        self.label = label()
    }
    
    var body: some View {
        Button(action: action) {
            label
                .foregroundColor(.white)
                .background(Color.accentColor)
                .cornerRadius(10)
        }
    }
}

struct RoundedButton_Previews: PreviewProvider {
    static var previews: some View {
        RoundedButton(action: {}) {
            Text("OK")
        }
    }
}
