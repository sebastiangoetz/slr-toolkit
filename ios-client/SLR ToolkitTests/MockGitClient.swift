import Foundation
@testable import SLR_Toolkit

struct MockGitClient: GitClient {
    func clone(from remoteURL: URL, to localURL: URL, credentials: Credentials, progress: ((Float) -> Void)?, completion: @escaping (Error?) -> Void) {
        let gitFolder = localURL.appendingPathComponent(".git", isDirectory: true)
        do {
            try FileManager.default.createDirectory(at: gitFolder, withIntermediateDirectories: false)
            completion(nil)
        } catch {
            completion(error)
        }
    }

    func fetch(repositoryURL: URL, credentials: Credentials, completion: @escaping (Error?) -> Void) {
        completion(nil)
    }
}
