import SwiftUI

protocol ProjectViewInteractor {
    func fetch(project: Project, isLoading: Binding<Bool>, commitsBehindOrigin: Binding<Int>, alertError: Binding<AlertError?>)
    func pull(project: Project, isLoading: Binding<Bool>, commitsBehindOrigin: Binding<Int>, alertError: Binding<AlertError?>)
}

struct ProjectViewInteractorImplementation: ProjectViewInteractor {
    func fetch(project: Project, isLoading: Binding<Bool>, commitsBehindOrigin: Binding<Int>, alertError: Binding<AlertError?>) {
        isLoading.wrappedValue = true
        GitManager.default.fetch(project: project) { error in
            isLoading.wrappedValue = false
            if let error = error {
                alertError.wrappedValue = AlertError(title: "Error fetching repository", message: error.localizedDescription)
            } else {
                commitsBehindOrigin.wrappedValue = GitManager.default.commitsAheadAndBehindOrigin(project: project).behind
            }
        }
    }

    func pull(project: Project, isLoading: Binding<Bool>, commitsBehindOrigin: Binding<Int>, alertError: Binding<AlertError?>) {
        isLoading.wrappedValue = true
        GitManager.default.pull(project: project) { error in
            isLoading.wrappedValue = false
            if let error = error {
                alertError.wrappedValue = AlertError(title: "Error pulling repository", message: error.localizedDescription)
            } else {
                commitsBehindOrigin.wrappedValue = 0
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
