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
    
    func cloneRepository(at url: URL, progress: ((Float) -> Void)? = nil) -> Result<URL, CloneError> {
        if url.scheme != "https" {
            return .failure(.unsupportedScheme)
        }
        
        guard let host = url.host else { return .failure(.invalidURL) }
        
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
            return .success(repositoryDirectory)
        }
        
        do {
            try FileManager.default.createDirectory(at: repositoryDirectory, withIntermediateDirectories: true)
        } catch {
            return .failure(.fileManagerError(error))
        }
        
        let error = gitClient.clone(from: url, to: repositoryDirectory, progress: progress)
        if let error = error {
            return .failure(.gitError(error))
        }
        return .success(repositoryDirectory)
    }
}
