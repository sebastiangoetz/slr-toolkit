import Foundation

struct Directory: Hashable, Identifiable {
    let url: URL
    
    var id: URL { url }
    
    var name: String {
        return url.pathComponents.last!
    }
    
    var directories: [Directory]? {
        do {
            let contents = try FileManager.default.contentsOfDirectory(at: url, includingPropertiesForKeys: [.isDirectoryKey])
            return contents.filter { url in
                do {
                    let resourceValues = try url.resourceValues(forKeys: [.isDirectoryKey])
                    return resourceValues.isDirectory!
                } catch {
                    print("Error fetching resource values for \(url): \(error)")
                    return false
                }
            }.map { Directory(url: $0) }
        } catch {
            print("Error listing contents of \(url): \(error)")
            return nil
        }
    }
    
    var isValidProjectDirectory: Bool {
        var bibFiles = 0
        var taxonomyFiles = 0
        do {
            let contents = try FileManager.default.contentsOfDirectory(at: url, includingPropertiesForKeys: [.isDirectoryKey])
            for url in contents {
                do {
                    let resourceValues = try url.resourceValues(forKeys: [.isDirectoryKey])
                    if !resourceValues.isDirectory! {
                        if url.pathComponents.last?.hasSuffix(".bib") == true {
                            bibFiles += 1
                        } else if url.pathComponents.last?.hasSuffix(".taxonomy") == true {
                            taxonomyFiles += 1
                        }
                    }
                } catch {
                    print("Error fetching resource values for \(url): \(error)")
                }
            }
        } catch {
            print("Error listing contents of \(url): \(error)")
        }
        return bibFiles == 1 && taxonomyFiles == 1
    }
}
