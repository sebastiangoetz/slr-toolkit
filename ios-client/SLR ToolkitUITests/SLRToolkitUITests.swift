import XCTest

final class SLRToolkitUITests: XCTestCase {
    override func setUp() {
        continueAfterFailure = false
    }

    func testAddProject() {
        let app = UITestHelper.launchApp(argument: .reset)

        app.buttons["Add Project"].tap()

        XCTAssert(!app.navigationBars.buttons["Next"].isEnabled)

        app.textFields["URLTextField"].tap()
        app.typeText(TestConstants.testGitURL)
        app.textFields["Username"].tap()
        app.typeText(TestConstants.testUsername)
        app.textFields["Token"].tap()
        app.typeText(TestConstants.testToken)
        app.navigationBars.buttons["Next"].tap()

        XCTAssert(!app.navigationBars.buttons["Done"].isEnabled)

        app.textFields["Project name"].tap()
        app.typeText(TestConstants.testProjectName)
        app.navigationBars.buttons["Done"].tap()

        XCTAssert(app.navigationBars[TestConstants.testProjectName].exists)
    }

    func testAllEntries() {
        let app = UITestHelper.launchApp(argument: .testProject)
        app.tables.buttons.matching(NSPredicate(format: "label BEGINSWITH 'All Entries'")).firstMatch.tap()
        XCTAssert(app.navigationBars["All Entries"].exists)

        // Sort
        app.buttons["Sort"].tap()
        app.buttons["Sort by date"].tap()
        app.buttons["Sort"].tap()
        app.buttons["Sort by title"].tap()
        // TODO assert order of entries
    }

    func testEntryDetails() {
        let app = UITestHelper.launchApp(argument: .testProject)
        app.tables.buttons.matching(NSPredicate(format: "label BEGINSWITH 'All Entries'")).firstMatch.tap()
        XCTAssert(app.navigationBars["All Entries"].exists)
        app.tables.buttons.matching(NSPredicate(format: "label BEGINSWITH 'TestArticle'")).firstMatch.tap()
        XCTAssert(app.navigationBars["keyA"].exists)
        XCTAssert(app.staticTexts["TestArticle"].exists)
        XCTAssert(app.staticTexts["Max"].exists)
    }

    func testChangeProject() {
        let app = UITestHelper.launchApp(arguments: [.testProject, .testProject2])

        // Ensure first project is active
        app.buttons["Options"].tap()
        app.buttons["Change Project"].tap()
        app.staticTexts[TestConstants.testProjectName].tap()

        XCTAssert(app.navigationBars[TestConstants.testProjectName].exists)
        app.buttons["Options"].tap()
        app.buttons["Change Project"].tap()
        XCTAssert(app.navigationBars["Projects"].exists)
        XCTAssert(app.staticTexts[TestConstants.testProjectName].exists)
        XCTAssert(app.staticTexts[TestConstants.testProjectName + "2"].exists)
        app.staticTexts[TestConstants.testProjectName + "2"].tap()
        XCTAssert(app.navigationBars[TestConstants.testProjectName + "2"].exists)
    }

    func testProjectSettings() {
        let app = UITestHelper.launchApp(argument: .testProject)
        app.buttons["Options"].tap()
        app.buttons["Project Settings"].tap()
        XCTAssert(app.navigationBars["Project Settings"].exists)
        XCTAssert(app.staticTexts[TestConstants.testProjectName].exists)
        XCTAssert(app.staticTexts[TestConstants.testGitURL].exists)
        app.navigationBars.buttons["Done"].tap()
        sleep(1)
        XCTAssert(!app.navigationBars["Project Settings"].exists)
    }

    func testAppSettings() {
        let app = UITestHelper.launchApp(argument: .testProject)
        app.buttons["Options"].tap()
        app.buttons["App Settings"].tap()
        XCTAssert(app.navigationBars["Settings"].exists)
        XCTAssert(app.staticTexts.matching(NSPredicate(format: "label BEGINSWITH 'SLR Toolkit App'")).firstMatch.exists)
        app.buttons["Acknowledgements"].tap()
        XCTAssert(app.navigationBars["Acknowledgements"].exists)
        app.navigationBars.buttons["Settings"].tap()
        app.navigationBars.buttons["Done"].tap()
        sleep(1)
        XCTAssert(!app.navigationBars["Settings"].exists)
    }
}
