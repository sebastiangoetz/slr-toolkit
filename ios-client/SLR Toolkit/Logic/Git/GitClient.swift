import Foundation

/// Interface for the app's git client
protocol GitClient {
    typealias Credentials = (username: String, token: String)
    typealias Author = (name: String, email: String)

    /// Clones a repository from a remote url.
    func clone(from remoteURL: URL, to localURL: URL, credentials: Credentials, progress: ((Float) -> Void)?, completion: @escaping (Error?) -> Void)

    /// Fetches changes from a remote repository.
    func fetch(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void)

    /// Fetches changes from a remote repository and merges them into the local main branch.
    func pull(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void)

    /// Commits all local changes.
    func commitAll(repositoryURL: URL, message: String, author: Author) -> Error?

    /// Pushes local commits to a remote repository.
    func push(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void)

    /// Reports how many commits the local repository is ahead or behind its remote counterpart.
    func commitsAheadAndBehindOrigin(repositoryURL: URL) -> Result<(ahead: Int, behind: Int), Error>
}
