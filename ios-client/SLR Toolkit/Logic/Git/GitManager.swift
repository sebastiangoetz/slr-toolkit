import SwiftUI

/// Struct that wraps a git client. Transforms git errors into user-readable messages.
struct GitManager {
    enum CloneError: Error {
        case unsupportedScheme
        case invalidURL
        case fileManagerError(Error)
        case gitError(Error)
        
        var description: String {
            switch self {
            case .unsupportedScheme:
                return "The URL has an unsupported scheme."
            case .invalidURL:
                return "The URL is invalid."
            case .fileManagerError(let error):
                return "\(error)"
            case .gitError(let error):
                return "\(error)"
            }
        }
    }

    /// Directory on the device containing this app's git repositories.
    static let gitDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0].appendingPathComponent("git", isDirectory: true)
    
    static func deleteGitDirectory() {
        if FileManager.default.fileExists(atPath: gitDirectory.path) {
            do {
                try FileManager.default.removeItem(at: gitDirectory)
            } catch {
                print(error)
            }
        }
    }
    
    private let gitClient: GitClient
    
    init(gitClient: GitClient) {
        self.gitClient = gitClient
    }

    /// Clones a repository from a remote url.
    func cloneRepository(at url: URL, credentials: GitClient.Credentials, progress: ((Float) -> Void)? = nil, completion: @escaping (Result<URL, CloneError>) -> Void) {
        guard url.scheme == "https" else {
            completion(.failure(.unsupportedScheme))
            return
        }
        
        guard let host = url.host else {
            completion(.failure(.invalidURL))
            return
        }
        
        var repositoryDirectory = Self.gitDirectory.appendingPathComponent(host, isDirectory: true)
        for component in url.pathComponents[1...] {
            if component.hasSuffix(".git") {
                let repositoryName = component.prefix(upTo: component.index(component.endIndex, offsetBy: -4))
                repositoryDirectory.appendPathComponent(String(repositoryName))
            } else {
                repositoryDirectory.appendPathComponent(component)
            }
        }
        
        if FileManager.default.fileExists(atPath: repositoryDirectory.path) {
            completion(.success(repositoryDirectory))
            return
        }
        
        do {
            try FileManager.default.createDirectory(at: repositoryDirectory, withIntermediateDirectories: true)
        } catch {
            completion(.failure(.fileManagerError(error)))
            return
        }
        
        gitClient.clone(from: url, to: repositoryDirectory, credentials: credentials, progress: progress) { error in
            if let error = error {
                return completion(.failure(.gitError(error)))
            } else {
                print("Cloning to \(repositoryDirectory)")
                completion(.success(repositoryDirectory))
            }
        }
    }

    /// Fetches changes for a project's git repository from a remote repository.
    func fetch(project: Project, completion: @escaping (Error?) -> Void) {
        gitClient.fetch(repositoryURL: Self.gitDirectory.appendingPathComponent(project.pathInGitDirectory), credentials: (project.username, project.token), completion: completion)
    }

    /// Fetches changes for a project's git repository from a remote repository and merges them into the local main branch.
    func pull(project: Project, completion: @escaping (Error?) -> Void) {
        gitClient.pull(repositoryURL: Self.gitDirectory.appendingPathComponent(project.pathInGitDirectory), credentials: (project.username, project.token), completion: completion)
    }

    /// Commits all local changes for a project.
    func commit(project: Project, changes: (Int, Int)) -> Error? {
        guard let commitName = project.commitName, let commitEmail = project.commitEmail, !commitName.isEmpty && !commitEmail.isEmpty else { return nil }  // TODO return proper error

        let (discarded, classified) = changes

        let message: String
        if discarded > 0 && classified > 0 {
            message = "discard \(discarded) \(discarded == 1 ? "entry" : "entries") and classify \(classified) \(classified == 1 ? "entry" : "entries")"
        } else if discarded > 0 {
            message = "discard \(discarded) \(discarded == 1 ? "entry" : "entries")"
        } else if classified > 0 {
            message = "classify \(classified) \(classified == 1 ? "entry" : "entries")"
        } else {
            message = "nothing changed"
        }

        return gitClient.commitAll(repositoryURL: Self.gitDirectory.appendingPathComponent(project.pathInGitDirectory), message: "SLR Toolkit App: " + message, author: (commitName, commitEmail))
    }

    /// Pushes a project's repository's local commits to a remote repository.
    func push(project: Project, completion: @escaping (Error?) -> Void) {
        gitClient.push(repositoryURL: Self.gitDirectory.appendingPathComponent(project.pathInGitDirectory), credentials: (project.username, project.token), completion: completion)
    }

    /// Reports how many commits the project's repository is ahead or behind its remote counterpart.
    func commitsAheadAndBehindOrigin(project: Project) -> (ahead: Int, behind: Int) {
        switch gitClient.commitsAheadAndBehindOrigin(repositoryURL: Self.gitDirectory.appendingPathComponent(project.pathInGitDirectory)) {
        case .success(let tuple):
            return tuple
        case .failure:
            return (0, 0)
        }
    }
}

struct GitManagerKey: EnvironmentKey {
    static let defaultValue: GitManager = GitManager(gitClient: HttpsGitClient())
}

extension EnvironmentValues {
    var gitManager: GitManager {
        get { self[GitManagerKey.self] }
        set { self[GitManagerKey.self] = newValue }
    }
}
