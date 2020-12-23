import SwiftyBibtex

struct TaxonomyNode: Identifiable {
    let name: String
    let path: String
    let entries: [Publication]
    let children: [TaxonomyNode]?

    var id: String { path }

    init(name: String, path: String, allEntries: [Publication], children: [TaxonomyNode]?) {
        self.name = name
        self.path = path
        self.entries = allEntries.filter { $0.classes.contains(path) }
        self.children = children
    }
}
