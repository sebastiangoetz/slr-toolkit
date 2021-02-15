import SwiftUI

/// Button that expands to take as much horizontal space as it can.
struct ExpandingButton<Label: View>: View {
    private var action: () -> Void
    private var label: Label

    init(action: @escaping () -> Void, @ViewBuilder label: () -> Label) {
        self.action = action
        self.label = label()
    }

    var body: some View {
        RoundedButton(action: action) {
            HStack {
                Spacer()
                label
                Spacer()
            }
        }
        .frame(maxWidth: .infinity)
    }
}

struct ExpandingButton_Previews: PreviewProvider {
    static var previews: some View {
        ExpandingButton(action: {}) {
            Text("OK")
        }
        ExpandingButton(action: {}) {
            Text("OK")
        }
        .colorScheme(.dark)
    }
}
