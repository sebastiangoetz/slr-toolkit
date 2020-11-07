import Foundation

struct TaxonomyNode: Identifiable {
    let id = UUID()
    let name: String
    let children: [TaxonomyNode]?
    
    init(name: String, children: [TaxonomyNode]? = nil) {
        self.name = name
        self.children = children
    }
}
