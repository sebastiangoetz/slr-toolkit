import Foundation

enum Constants {
    static let exampleProject = Project(name: "My Project", path: URL(fileURLWithPath: ""), taxonomy: [
        TaxonomyNode(name: "Types of Research", children: [
            TaxonomyNode(name: "Applied", children: []),
            TaxonomyNode(name: "Fundamental")
        ]),
        TaxonomyNode(name: "Application Domains", children: [
            TaxonomyNode(name: "none"),
            TaxonomyNode(name: "enterprise"),
            TaxonomyNode(name: "cloud")
        ])
    ])
}
