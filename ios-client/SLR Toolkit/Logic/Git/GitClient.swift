import Foundation
import ObjectiveGit

protocol GitClient {
    typealias Credentials = (username: String, token: String)

    func clone(from remoteURL: URL, to localURL: URL, credentials: Credentials, progress: ((Float) -> Void)?, completion: @escaping (Error?) -> Void)
    func fetch(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void)
    func pull(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void)
    func commitsAheadAndBehindOrigin(repositoryURL: URL) -> Result<(ahead: Int, behind: Int), Error>
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
                let origin = try GTRemote(name: "origin", in: repository)
                let credential = try GTCredential(userName: credentials.username, password: credentials.token)
                let auth = GTCredentialProvider { _, _, _ in credential }
                try repository.fetch(origin, withOptions: [GTRepositoryRemoteOptionsCredentialProvider: auth])
                completion(nil)
            } catch {
                completion(error)
            }
        }
    }

    func pull(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void) {
        DispatchQueue.global(qos: .userInitiated).async {
            do {
                let repository = try GTRepository(url: repositoryURL)
                let currentBranch = try repository.currentBranch()
                let origin = try GTRemote(name: "origin", in: repository)
                let credential = try GTCredential(userName: credentials.username, password: credentials.token)
                let auth = GTCredentialProvider { _, _, _ in credential }
                try repository.pull(currentBranch, from: origin, withOptions: [GTRepositoryRemoteOptionsCredentialProvider: auth])
                completion(nil)
            } catch {
                completion(error)
            }
        }
    }

    func commitsAheadAndBehindOrigin(repositoryURL: URL) -> Result<(ahead: Int, behind: Int), Error> {
        do {
            let repository = try GTRepository(url: repositoryURL)
            let currentBranch = try repository.currentBranch()
            let remoteBranches = try repository.remoteBranches()
            // TODO error handling
            let remoteBranch = remoteBranches.first { $0.shortName == currentBranch.shortName }!

            var ahead = 0
            var behind = 0
            try currentBranch.calculateAhead(&ahead, behind: &behind, relativeTo: remoteBranch)
            return .success((ahead, behind))
        } catch {
            return .failure(error)
        }
    }
}
