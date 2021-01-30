import SwiftUI

protocol ProjectViewInteractor {
    /// Fetches updates from origin. Uses bindings to report back whether it's loading, how many commits the local revision is behind origin, and what alert to show.
    func fetch(project: Project, isLoading: Binding<Bool>, commitsBehindOrigin: Binding<Int>, alertContent: Binding<AlertContent?>)

    /// Fetches updates from origin and merges them into the main branch. Uses binding to report back whether it's loading, how many commits the local revision is behind origin, and what alert to show.
    func pull(project: Project, isLoading: Binding<Bool>, commitsBehindOrigin: Binding<Int>, alertContent: Binding<AlertContent?>)

    /// Commits all local changes. Uses bindings to report back whether it's loading and what alert to show.
    func commit(project: Project, isLoading: Binding<Bool>, alertContent: Binding<AlertContent?>)
}

struct ProjectViewInteractorImplementation: ProjectViewInteractor {
    func fetch(project: Project, isLoading: Binding<Bool>, commitsBehindOrigin: Binding<Int>, alertContent: Binding<AlertContent?>) {
        isLoading.wrappedValue = true
        GitManager.default.fetch(project: project) { error in
            isLoading.wrappedValue = false
            if let error = error {
                alertContent.wrappedValue = AlertContent(title: "Error fetching repository", message: error.localizedDescription)
            } else {
                commitsBehindOrigin.wrappedValue = GitManager.default.commitsAheadAndBehindOrigin(project: project).behind
            }
        }
    }

    func pull(project: Project, isLoading: Binding<Bool>, commitsBehindOrigin: Binding<Int>, alertContent: Binding<AlertContent?>) {
        isLoading.wrappedValue = true
        let fileModificationDates = project.fileModificationDates
        GitManager.default.pull(project: project) { error in
            if let error = error {
                alertContent.wrappedValue = AlertContent(title: "Error pulling repository", message: error.localizedDescription)
            } else {
                commitsBehindOrigin.wrappedValue = 0
                ProjectManager.updateProject(project, fileModificationDates: fileModificationDates) {
                    isLoading.wrappedValue = false
                }
            }
        }
    }

    func commit(project: Project, isLoading: Binding<Bool>, alertContent: Binding<AlertContent?>) {
        guard !project.discardedEntries.isEmpty || !project.classifiedEntries.isEmpty else {
            alertContent.wrappedValue = AlertContent(title: "Nothing to commit.", message: nil)
            return
        }

        guard project.commitName?.isEmpty == false && project.commitName?.isEmpty == false else {
            alertContent.wrappedValue = AlertContent(title: "No commit identity", message: "Please enter a name and email address for your commits in project settings.")  // TODO Add button that links to ProjectSettingsView
            return
        }

        isLoading.wrappedValue = true
        DispatchQueue.global(qos: .userInitiated).async {
            do {
                let changes = try ProjectManager.commitChanges(project: project)
                if let error = GitManager.default.commit(project: project, changes: changes) {
                    alertContent.wrappedValue = AlertContent(title: "Error commiting changes", message: error.localizedDescription)
                    isLoading.wrappedValue = false
                    return
                }
                GitManager.default.push(project: project) { error in
                    if let error = error {
                        alertContent.wrappedValue = AlertContent(title: "Error pushing changes", message: error.localizedDescription)
                    } else {
                        isLoading.wrappedValue = false
                    }
                }
            } catch {
                isLoading.wrappedValue = false
                alertContent.wrappedValue = AlertContent(title: "Error committing changes", message: error.localizedDescription)
            }
        }
    }
}

struct ProjectViewInteractorKey: EnvironmentKey {
    static let defaultValue: ProjectViewInteractor = ProjectViewInteractorImplementation()
}

extension EnvironmentValues {
    var projectViewInteractor: ProjectViewInteractor {
        get { self[ProjectViewInteractorKey.self] }
        set { self[ProjectViewInteractorKey.self] = newValue }
    }
}
