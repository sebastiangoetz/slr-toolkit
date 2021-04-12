import Foundation
import SwiftyBibtex

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
