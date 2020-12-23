import Foundation

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
    
    var directories: [Directory]? {
        return FileManager.default.contentsOfDirectory(at: url) { isDirectory, fileName in
            return isDirectory && (!isRoot || fileName != ".git")
        }.map { Directory(url: $0) }
    }
    
    var isValidProjectDirectory: Bool {
        let bibFiles = FileManager.default.contentsOfDirectory(at: url) { $1.hasSuffix(".bib") }.count
        if bibFiles != 1 {
            return false
        }
        return FileManager.default.contentsOfDirectory(at: url) { $1.hasSuffix(".taxonomy") }.count == 1
    }
}
