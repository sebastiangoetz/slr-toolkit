import SwiftUI

/// View for third-party libraries.
struct AcknowledgementsView: View {
    var body: some View {
        List {
            Section(footer: Text("SLR Toolkit App makes use of the following third party libraries:")) {}
                .padding(.bottom, 8)
            ForEach(Acknowledgements.acknowledgements.sorted { $0 < $1 }, id: \.key) { tuple in
                Section(header: Text(tuple.key)) {
                    Text(tuple.value)
                        .font(.callout)
                }
            }
        }
        .listStyle(GroupedListStyle())
        .navigationBarTitle("Acknowledgements", displayMode: .inline)
    }
}

struct AcknowledgementsView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            AcknowledgementsView()
        }
        NavigationView {
            AcknowledgementsView()
        }
        .colorScheme(.dark)
    }
}
