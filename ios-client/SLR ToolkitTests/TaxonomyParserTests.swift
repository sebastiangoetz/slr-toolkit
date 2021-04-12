import XCTest
@testable import SLR_Toolkit

class TaxonomyParserTests: XCTestCase {
    func testEmptyTaxonomy() {
        XCTAssertEqual(TaxonomyParser.parse(""), [])
    }

    func testSimpleTaxonomy() {
        XCTAssertEqual(TaxonomyParser.parse("a b,\nc d"), [
            TaxonomyParserNode(name: "a b"),
            TaxonomyParserNode(name: "c d")
        ])
    }

    func testNestedTaxonomy() {
        XCTAssertEqual(TaxonomyParser.parse("a { b {c}, d {e}, f }, g"), [
            TaxonomyParserNode(name: "a", children: [
                TaxonomyParserNode(name: "b", children: [
                    TaxonomyParserNode(name: "c")
                ]),
                TaxonomyParserNode(name: "d", children: [
                    TaxonomyParserNode(name: "e")
                ]),
                TaxonomyParserNode(name: "f")
            ]),
            TaxonomyParserNode(name: "g")
        ])
    }

    func testInvalidTaxonomies() {
        // Comma first
        XCTAssertNil(TaxonomyParser.parse("a {,}"))

        // Empty class
        XCTAssertNil(TaxonomyParser.parse("a {}"))

        // Unbalanced braces
        XCTAssertNil(TaxonomyParser.parse("a { { b }"))

        // Unnecessary nesting
        XCTAssertNil(TaxonomyParser.parse("a { { b } }"))

        // Trailing comma
        XCTAssertNil(TaxonomyParser.parse("a { b, }"))
    }
}
