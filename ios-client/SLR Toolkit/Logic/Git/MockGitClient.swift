import Foundation

struct MockGitClient: GitClient {
    static func createTestFiles(at url: URL) throws {
        let gitDirectory = url.appendingPathComponent(".git", isDirectory: true)
        try FileManager.default.createDirectory(at: gitDirectory, withIntermediateDirectories: false)
        try FileManager.default.createDirectory(at: url, withIntermediateDirectories: true)
        let bib = "@article{key,author={max},title={TestArticle}}"
        try bib.write(to: url.appendingPathComponent("test.bib", isDirectory: false), atomically: false, encoding: .utf8)
        let taxonomy = "A { B C } D"
        try taxonomy.write(to: url.appendingPathComponent("test.taxonomy", isDirectory: false), atomically: false, encoding: .utf8)
    }

    func clone(from remoteURL: URL, to localURL: URL, credentials: Credentials, progress: ((Float) -> Void)?, completion: @escaping (Error?) -> Void) {
        do {
            try Self.createTestFiles(at: localURL)
            completion(nil)
        } catch {
            completion(error)
        }
    }

    func fetch(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void) {
        completion(nil)
    }

    func pull(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void) {
        completion(nil)
    }
    func commitAll(repositoryURL: URL, message: String, author: Author) -> Error? {
        return nil
    }

    func push(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void) {
        completion(nil)
    }

    func commitsAheadAndBehindOrigin(repositoryURL: URL) -> Result<(ahead: Int, behind: Int), Error> {
        return .success((0, 0))
    }
}
