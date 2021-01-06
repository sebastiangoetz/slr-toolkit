import SwiftUI

struct SettingsView: View {
    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        NavigationView {
            List {
                Section(footer: Text("SLR Toolkit App " + (Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "Unknown"))) {
                    NavigationLink("Acknowledgements", destination: AcknowledgementsView())
                }
            }
            .listStyle(InsetGroupedListStyle())
            .navigationBarTitle("Settings", displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button("Done") {
                        presentationMode.wrappedValue.dismiss()
                    }
                }
            }
        }
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
    }
}
