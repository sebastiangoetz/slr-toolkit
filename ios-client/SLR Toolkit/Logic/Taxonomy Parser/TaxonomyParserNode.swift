import Foundation
import SwiftyBibtex

/// Node in the taxonomy tree.
final class TaxonomyParserNode: CustomStringConvertible, Equatable {
    let name: String
    let parent: TaxonomyParserNode?
    var children: [TaxonomyParserNode]

    var path: String {
        if let parent = parent {
            return parent.path + "###" + name
        }
        return name
    }
    
    init(name: String, parent: TaxonomyParserNode? = nil, children: [TaxonomyParserNode] = []) {
        self.name = name
        self.parent = parent
        self.children = children
    }

    var description: String {
        return description(level: 0)
    }

    func description(level: Int) -> String {
        let inset = String(repeating: " ", count: level * 4)
        return inset + name + (children.isEmpty ? "" : " {\n\(children.map { $0.description(level: level + 1) }.joined(separator: "\n"))\n\(inset)}")
    }

    static func == (lhs: TaxonomyParserNode, rhs: TaxonomyParserNode) -> Bool {
        return lhs.name == rhs.name && lhs.children == rhs.children
    }
}
