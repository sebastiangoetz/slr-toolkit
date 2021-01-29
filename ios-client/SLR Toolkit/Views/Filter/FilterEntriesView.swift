import SwiftUI

struct FilterEntriesView: View {
    let entries: [Entry]

    @Environment(\.managedObjectContext) private var managedObjectContext
    @Environment(\.presentationMode) private var presentationMode

    @State private var dragAmount: CGFloat = 0
    @State private var verticalOffset: CGFloat = 0

    init(entries: [Entry]) {
        self.entries = entries
    }

    var body: some View {
        NavigationView {
            VStack {
                ZStack {
                    if let entry = entries.first {  // to prevent crash after last unfiltered entry is removed
                        EntryCard(entry: entry)
                    }
                    Color.red.opacity(Double(-dragAmount) / 2500)
                    Color.green.opacity(Double(dragAmount) / 2500)
                }
                .cornerRadius(20)
                .zIndex(1)
                .offset(x: 1.5 * dragAmount, y: verticalOffset)
                .rotationEffect(Angle(degrees: Double(dragAmount / 40)))
                .gesture(
                    DragGesture()
                        .onChanged { dragAmount = $0.translation.width }
                        .onEnded { _ in dragGestureEnded() }
                )
                HStack {
                    Text("← Discard")
                    Spacer()
                    Text("\(entries.count) to go")
                    Spacer()
                    Text("Keep →")
                }
                .padding(EdgeInsets(top: 8, leading: 12, bottom: 0, trailing: 12))
                .font(.footnote)
                .foregroundColor(.secondary)
            }
            .padding([.top, .leading, .trailing], 24)
            .background(Color(.systemGroupedBackground).ignoresSafeArea())
            .navigationBarTitle(entries.first?.citationKey ?? "", displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button("Done") {
                        presentationMode.wrappedValue.dismiss()
                    }
                }
            }
        }
    }

    private func dragGestureEnded() {
        let screenSize = UIScreen.main.bounds
        if abs(dragAmount) < screenSize.width / 3 {
            withAnimation {
                dragAmount = 0
            }
        } else {
            // Modify entry
            if dragAmount > 0 {
                entries.first?.decision = .keep
            } else {
                entries.first?.decision = .discard
            }
            managedObjectContext.saveAndLogError()

            // Animate card out of the screen
            withAnimation {
                dragAmount = (dragAmount > 0 ? 1 : -1) * screenSize.width
            }

            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                if entries.count <= 1 {
                    presentationMode.wrappedValue.dismiss()
                } else {
                    // Prepare next card
                    dragAmount = 0
                    verticalOffset = screenSize.height
                    withAnimation(.spring()) {
                        verticalOffset = 0
                    }
                }
            }
        }
    }
}
