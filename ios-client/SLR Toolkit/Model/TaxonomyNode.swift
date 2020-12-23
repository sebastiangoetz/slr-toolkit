import SwiftyBibtex

struct TaxonomyNode: Identifiable {
    let name: String
    let path: String
    let children: [TaxonomyNode]?

    var id: String { path }

    init(name: String, path: String, children: [TaxonomyNode]?) {
        self.name = name
        self.path = path
        self.children = children
    }
}
