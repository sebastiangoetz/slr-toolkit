import SwiftUI

struct ProjectSettingsView: View {
    private let project: Project

    @Environment(\.presentationMode) private var presentationMode
    @Environment(\.managedObjectContext) private var managedObjectContext

    @State private var commitName: String
    @State private var commitEmail: String

    init(project: Project) {
        self.project = project

        _commitName = State(initialValue: project.commitName ?? "")
        _commitEmail = State(initialValue: project.commitEmail ?? "")
    }

    var body: some View {
        NavigationView {
            List {
                Section(header: Text("Project Name")) {
                    Text(project.name)  // TODO make editable
                }
                Section(header: Text("Commit Identity")) {
                    TextField("Name", text: $commitName)
                    TextField("E-Mail", text: $commitEmail)
                        .autocapitalization(.none)
                        .disableAutocorrection(true)
                        .textContentType(.emailAddress)
                }
            }
            .listStyle(InsetGroupedListStyle())
            .navigationBarTitle("Project Settings", displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button("Done", action: done)
                }
            }
        }
    }

    private func done() {
        let trimmedCommitName = commitName.trimmingCharacters(in: .whitespaces)
        project.commitName = trimmedCommitName.isEmpty ? nil : trimmedCommitName

        let trimmedCommitEmail = commitEmail.trimmingCharacters(in: .whitespaces)
        project.commitEmail = trimmedCommitEmail.isEmpty ? nil : trimmedCommitEmail

        managedObjectContext.saveAndLogError()

        presentationMode.wrappedValue.dismiss()
    }
}
