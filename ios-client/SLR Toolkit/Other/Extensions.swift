import CoreData
import Foundation
import SwiftyBibtex

extension Array {
    func interspersing(_ element: Element) -> Array {
      flatMap { [$0, element] }.dropLast()
    }
}

extension String {
    private static let latexReplacements = [
        ("a", "'", "á"),
        ("e", "'", "é"),
        ("i", "'", "í"),
        ("o", "'", "ó"),
        ("u", "'", "ú"),
        ("a", "\"", "ä"),
        ("e", "\"", "ë"),
        ("o", "\"", "ö"),
        ("u", "\"", "ü"),
        ("a", "^", "â"),
        ("e", "^", "ê"),
        ("i", "^", "î"),
        ("n", "~", "ñ")
    ].flatMap { replacements(for: $0.0, with: $0.1, replacement: $0.2) } + [
        ("{-}", "-"),
        ("{\\o}", "ø"),
        ("{\\O}", "Ø"),
        ("{\\c{c}}", "ç"),
        ("{\\C{c}}", "Ç"),
        ("{\\v{s}}", "š"),
        ("{\\ss}", "ß"),
        ("\\ss", "ß")
    ]

    private static func replacements(for letter: String, with diacritic: String, replacement: String) -> [(String, String)] {
        return [
            ("{\\\(diacritic){\\\(letter)}}", replacement),
            ("{\\\(diacritic){\(letter)}}", replacement),
            ("{\\\(diacritic)\(letter)}", replacement),
            ("{\\\(diacritic)\\\(letter)}", replacement),
            ("\\\(diacritic){\\\(letter)}", replacement),
            ("\\\(diacritic){\(letter)}", replacement),
            ("\\\(diacritic)\(letter)", replacement)
        ] + (letter == letter.uppercased() ? [] : replacements(for: letter.uppercased(), with: diacritic, replacement: replacement.uppercased()))
    }

    /// Replaces latex macros with their unicode counterparts.
    var withLatexMacrosReplaced: String {
        var string = self
        for (s, r) in Self.latexReplacements {
            string = string.replacingOccurrences(of: s, with: r)
        }
        return string
    }
}

extension String: Identifiable {
    public var id: String { self }
}

// Foundation

extension FileManager {
    /// Returns the urls for files/folders in a directory matching a predicate. The predicate's parameters are a bool telling whether it's a directory and a string with the file/folder's name
    func contentsOfDirectory(at url: URL, matching predicate: (Bool, String) -> Bool) -> [URL] {
        do {
            let contents = try contentsOfDirectory(at: url, includingPropertiesForKeys: [.isDirectoryKey])
            return contents.filter { url in
                do {
                    let resourceValues = try url.resourceValues(forKeys: [.isDirectoryKey])
                    return predicate(resourceValues.isDirectory!, url.pathComponents.last!)
                } catch {
                    print("Error fetching resource values for \(url): \(error)")
                    return false
                }
            }
        } catch {
            print("Error listing contents of \(url): \(error)")
            return []
        }
    }
}

// Core Data

extension NSFetchRequest {
    @objc func withPredicate(_ predicate: NSPredicate) -> NSFetchRequest {
        self.predicate = predicate
        return self
    }

    @objc func withSortDescriptors(_ sortDescriptors: [NSSortDescriptor]) -> NSFetchRequest {
        self.sortDescriptors = sortDescriptors
        return self
    }
}

extension NSManagedObjectContext {
    func saveAndLogError() {
        do {
            try save()
        } catch {
            print("Error saving managed object context: \(error)")
        }
    }
}

// SwiftyBibtex

extension Publication {
    /// Parses a publication's "classes" field and returns it as a set. Each node in the taxonomy tree is represented by a string denoting its path starting from root (joined with "###").
    var classes: Set<String> {
        guard let classesString = fields["classes"], let taxonomy = TaxonomyParser.parse(classesString) else { return [] }
        var classes = Set<String>()
        var nodes = taxonomy
        while let node = nodes.last {
            nodes.removeLast()
            if node.children.isEmpty {
                classes.insert(node.path)
            } else {
                nodes.append(contentsOf: node.children)
            }
        }
        return classes
    }
}

extension RangeInFile: Comparable {
    // Assuming that publications don't overlap
    public static func < (lhs: RangeInFile, rhs: RangeInFile) -> Bool {
        if lhs.start.line != rhs.start.line {
            return lhs.start.line < rhs.start.line
        }
        return lhs.start.positionInLine < rhs.start.positionInLine
    }
}
