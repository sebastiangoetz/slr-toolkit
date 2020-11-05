import Foundation
@testable import SLR_Toolkit

struct MockGitClient: GitClient {
    func clone(from remoteURL: URL, to localURL: URL) -> Error? {
        let gitFolder = localURL.appendingPathComponent(".git", isDirectory: true)
        do {
            try FileManager.default.createDirectory(at: gitFolder, withIntermediateDirectories: false)
        } catch {
            return error
        }
        return nil
    }
}
