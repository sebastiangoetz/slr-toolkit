import Foundation

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

    static let `default` = GitManager(gitClient: HttpsGitClient())
    
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
                completion(.success(repositoryDirectory))
            }
        }
    }

    func fetch(project: Project, completion: @escaping (Error?) -> Void) {
        gitClient.fetch(repositoryURL: Self.gitDirectory.appendingPathComponent(project.pathInGitDirectory), credentials: (project.username, project.token), completion: completion)
    }
}
