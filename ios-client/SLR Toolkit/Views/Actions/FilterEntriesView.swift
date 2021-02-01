import SwiftUI

/// View to let users filter all entries for which they haven't made a decision yet.
struct FilterEntriesView: View {
    /// The list of entries with an outstanding decision.
    private let entries: [Entry]

    @Environment(\.managedObjectContext) private var managedObjectContext
    @Environment(\.presentationMode) private var presentationMode

    /// Horizontal drag amount.
    @State private var dragAmount: CGFloat = 0

    /// Vertical card offset. Used to move next card from the bottom into view.
    @State private var verticalOffset: CGFloat = 0

    /// The decided entries of this session. Used for undoing.
    @State private var undoStack = [Entry]()

    init(entries: [Entry]) {
        self.entries = entries
    }

    var body: some View {
        NavigationView {
            HStack {
                Spacer()
                VStack {
                    ZStack {
                        if let entry = entries.first {  // to prevent crash after last unfiltered entry is removed
                            EntryCard(entry: entry)  // TODO make sure ScrollView scrolls
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
                        Text(entries.first?.citationKey ?? "")
                        Spacer()
                        Text("Keep →")
                    }
                    .padding(EdgeInsets(top: 8, leading: 12, bottom: 20, trailing: 12))
                    .font(.footnote)
                    .foregroundColor(.secondary)
                }
                .frame(maxWidth: 512)
                .padding([.top, .leading, .trailing], 24)
                Spacer()
            }
            .background(Color(.systemGroupedBackground).ignoresSafeArea())
            .navigationBarTitle("Filter (\(entries.count) to go)", displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    if !undoStack.isEmpty {
                        Button("Undo") {
                            undoStack.popLast()?.decision = .outstanding
                        }
                    } else {
                        EmptyView()  // Workaround: if statements not allowed in ToolbarContentBuilder
                    }
                }
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
            guard let entry = entries.first else { return }

            // Modify entry
            entry.decision = dragAmount > 0 ? .keep : .discard
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

            undoStack.append(entry)
        }
    }
}
