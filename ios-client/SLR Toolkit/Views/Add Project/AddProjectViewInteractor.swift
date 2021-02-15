import SwiftUI

protocol AddProjectViewInteractor {
    /// Clones a project from a remote git repository.
    func cloneProject(username: String, token: String, rememberCredentials: Bool, repositoryURL: String, isLoading: Binding<Bool>, progress: Binding<Float?>, completion: @escaping (Result<URL, Error>) -> Void)
}

struct AddProjectViewInteractorImplementation: AddProjectViewInteractor {
    func cloneProject(username: String, token: String, rememberCredentials: Bool, repositoryURL: String, isLoading: Binding<Bool>, progress: Binding<Float?>, completion: @escaping (Result<URL, Error>) -> Void) {
        isLoading.wrappedValue = true

        if rememberCredentials {
            // TODO store securely in keychain, e.g. using https://github.com/kishikawakatsumi/KeychainAccess
            UserDefaults.standard.set(username, forKey: .username)
        }

        guard let url = URL(string: repositoryURL) else { return }
        GitManager.default.cloneRepository(at: url, credentials: (username, token)) { prgrss in
            progress.wrappedValue = prgrss
        } completion: { result in
            switch result {
            case .success(let repositoryDirectory):
                isLoading.wrappedValue = false
                if rememberCredentials {
                    UserDefaults.standard.set(token, forKey: .token)
                }
                completion(.success(repositoryDirectory))
            case .failure(let error):
                completion(.failure(error))
            }
        }
    }
}

struct AddProjectViewInteractorMock: AddProjectViewInteractor {
    func cloneProject(username: String, token: String, rememberCredentials: Bool, repositoryURL: String, isLoading: Binding<Bool>, progress: Binding<Float?>, completion: @escaping (Result<URL, Error>) -> Void) {
        guard let url = URL(string: repositoryURL) else { return }
        GitManager(gitClient: MockGitClient()).cloneRepository(at: url, credentials: (username, token)) { result in
            switch result {
            case .success(let url):
                completion(.success(url))
            case .failure(let error):
                completion(.failure(error))
            }
        }
    }
}

struct AddProjectViewInteractorKey: EnvironmentKey {
    static let defaultValue: AddProjectViewInteractor = AddProjectViewInteractorImplementation()
}

extension EnvironmentValues {
    var addProjectViewInteractor: AddProjectViewInteractor {
        get { self[AddProjectViewInteractorKey.self] }
        set { self[AddProjectViewInteractorKey.self] = newValue }
    }
}

