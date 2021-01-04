import Foundation
import SwiftGit2

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
            let credentials = Credentials.plaintext(username: username, password: token)
            let result: Result<Repository, NSError>
            if let progress = progress {
                result = Repository.clone(from: remoteURL, to: localURL, credentials: credentials) { _, completed, total in
                    progress(Float(completed) / Float(total))
                }
            } else {
                result = Repository.clone(from: remoteURL, to: localURL, credentials: credentials)
            }
            switch result {
            case .success:
                completion(nil)
            case .failure(let error):
                completion(error)
            }
        }
    }
}
