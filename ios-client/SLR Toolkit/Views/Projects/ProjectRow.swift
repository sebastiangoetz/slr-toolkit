import SwiftUI

/// A row in the list of projects.
struct ProjectRow: View {
    var project: Project
    var isActiveProject: Bool

    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(project.name)
                Text(project.repositoryURL)
                    .font(.caption)
                    .foregroundColor(.secondary)
                    .lineLimit(1)
            }
            if isActiveProject {
                Spacer()
                Image(systemName: "checkmark")
            }
        }
    }
}
