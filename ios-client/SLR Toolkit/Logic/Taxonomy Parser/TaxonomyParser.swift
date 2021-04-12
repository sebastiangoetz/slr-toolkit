import Foundation

/// Parser for SLR Toolkit taxonomies.
enum TaxonomyParser {
    static func parse(_ taxonomy: String) -> [TaxonomyParserNode]? {
        guard taxonomy.filter({ $0 == "{" }).count == taxonomy.filter({ $0 == "}" }).count else { return nil }

        var nodes = [TaxonomyParserNode]()
        var parent: TaxonomyParserNode?

        var string = ""
        var lastControlChar = ""
        for char in taxonomy {
            if char == "{" {
                let trimmedString = string.trimmingCharacters(in: .whitespacesAndNewlines)
                if trimmedString.isEmpty {
                    return nil
                } else {
                    let newNode = TaxonomyParserNode(name: trimmedString, parent: parent)
                    if let parent = parent {
                        parent.children.append(newNode)
                    } else {
                        nodes.append(newNode)
                    }
                    parent = newNode
                    string = ""
                }
                lastControlChar = "{"
            } else if char == "}" {
                let trimmedString = string.trimmingCharacters(in: .whitespacesAndNewlines)
                if trimmedString.isEmpty {
                    if lastControlChar == "{" || lastControlChar == "," {
                        return nil
                    } else if lastControlChar == "}" {
                        parent = parent?.parent
                    }
                } else {
                    if let parent = parent {
                        parent.children.append(TaxonomyParserNode(name: trimmedString, parent: parent))
                    } else {
                        return nil
                    }
                    string = ""
                }
                lastControlChar = "}"
            } else if char == "," {
                let trimmedString = string.trimmingCharacters(in: .whitespacesAndNewlines)
                if trimmedString.isEmpty {
                    if lastControlChar == "{" || lastControlChar == "," {
                        return nil
                    } else if lastControlChar == "}" {
                        parent = parent?.parent
                    }
                } else {
                    if let parent = parent {
                        parent.children.append(TaxonomyParserNode(name: trimmedString, parent: parent))
                    } else {
                        nodes.append(TaxonomyParserNode(name: trimmedString))
                    }
                    string = ""
                }
                lastControlChar = ","
            } else {
                string += String(char)
            }
        }

        let trimmedString = string.trimmingCharacters(in: .whitespacesAndNewlines)
        if !trimmedString.isEmpty {
            nodes.append(TaxonomyParserNode(name: trimmedString))
        }

        return nodes
    }
}
