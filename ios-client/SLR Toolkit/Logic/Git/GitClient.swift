import Foundation
import ObjectiveGit

protocol GitClient {
    func clone(from remoteURL: URL, to localURL: URL, progress: ((Float) -> Void)?, completion: @escaping (Error?) -> Void)
}

struct HttpsGitClient: GitClient {
    private let username, token: String
    
    init(username: String, token: String) {
        self.username = username
        self.token = token
    }

    func clone(from remoteURL: URL, to localURL: URL, progress: ((Float) -> Void)?, completion: @escaping (Error?) -> Void) {
        DispatchQueue.global(qos: .userInitiated).async {
            do {
                let credential = try GTCredential(userName: username, password: token)
                let auth = GTCredentialProvider { _, _, _ in credential }
                try GTRepository.clone(from: remoteURL, toWorkingDirectory: localURL, options: [GTRepositoryCloneOptionsCredentialProvider: auth]) { progressPointer, _ in
                    progress?(Float(progressPointer.pointee.received_objects) / Float(progressPointer.pointee.total_objects))
                }
                completion(nil)
            } catch {
                completion(error)
            }
        }
    }
}
