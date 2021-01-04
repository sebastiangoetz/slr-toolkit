import Foundation
import ObjectiveGit

protocol GitClient {
    typealias Credentials = (username: String, token: String)

    func clone(from remoteURL: URL, to localURL: URL, credentials: Credentials, progress: ((Float) -> Void)?, completion: @escaping (Error?) -> Void)
    func fetch(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void)
}

struct HttpsGitClient: GitClient {
    func clone(from remoteURL: URL, to localURL: URL, credentials: GitClient.Credentials, progress: ((Float) -> Void)?, completion: @escaping (Error?) -> Void) {
        DispatchQueue.global(qos: .userInitiated).async {
            do {
                let credential = try GTCredential(userName: credentials.username, password: credentials.token)
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

    func fetch(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void) {
        DispatchQueue.global(qos: .userInitiated).async {
            do {
                let repository = try GTRepository(url: repositoryURL)
                let remote = try GTRemote(name: "origin", in: repository)
                let credential = try GTCredential(userName: credentials.username, password: credentials.token)
                let auth = GTCredentialProvider { _, _, _ in credential }
                try repository.fetch(remote, withOptions: [GTRepositoryRemoteOptionsCredentialProvider: auth])
                completion(nil)
            } catch {
                completion(error)
            }
        }
    }
}
