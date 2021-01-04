import Foundation
@testable import SLR_Toolkit

struct MockGitClient: GitClient {
    func clone(from remoteURL: URL, to localURL: URL, progress: ((Float) -> Void)?, completion: @escaping (Error?) -> Void) {
        let gitFolder = localURL.appendingPathComponent(".git", isDirectory: true)
        do {
            try FileManager.default.createDirectory(at: gitFolder, withIntermediateDirectories: false)
            completion(nil)
        } catch {
            completion(error)
        }
    }
}
