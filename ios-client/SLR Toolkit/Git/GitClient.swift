import Foundation
import SwiftGit2

protocol GitClient {
    func clone(from remoteURL: URL, to localURL: URL) -> Error?
}

struct HttpsGitClient: GitClient {
    private let username, token: String
    
    init(username: String, token: String) {
        self.username = username
        self.token = token
    }
    
    func clone(from remoteURL: URL, to localURL: URL) -> Error? {
        let credentials = Credentials.plaintext(username: username, password: token)
        let result = Repository.clone(from: remoteURL, to: localURL, credentials: credentials)
        switch result {
        case .success:
            return nil
        case .failure(let error):
            return error
        }
    }
}
