import Foundation

/// Directory in a file tree.
struct Directory: Hashable, Identifiable {
    let url: URL
    let isRoot: Bool
    
    var id: URL { url }
    
    init(url: URL, isRoot: Bool = false) {
        self.url = url
        self.isRoot = isRoot
    }
    
    var name: String {
        return url.pathComponents.last!
    }

    /// Child directories.
    var directories: [Directory]? {
        return FileManager.default.contentsOfDirectory(at: url) { isDirectory, fileName in
            return isDirectory && (!isRoot || fileName != ".git")
        }.map { Directory(url: $0) }
    }

    /// Checks whether a directory is a valid project directory (having ≥ 1 .bib files and a .taxonomy file).
    var isValidProjectDirectory: Bool {
        let bibFiles = FileManager.default.contentsOfDirectory(at: url) { $1.hasSuffix(".bib") }.count
        return bibFiles == 1 && FileManager.default.contentsOfDirectory(at: url) { $1.hasSuffix(".taxonomy") }.count == 1
    }
}
