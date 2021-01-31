import XCTest

class SLRToolkitUITests: XCTestCase {
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

    func testChangeProject() {
        let app = UITestHelper.launchApp(arguments: [.testProject, .testProject2])
        XCTAssert(app.navigationBars[TestConstants.testProjectName].exists)
        app.navigationBars.buttons.firstMatch.tap()  // Labels on toolbar items aren't supported in SwiftUI yet
        app.buttons["Change Project"].tap()
        XCTAssert(app.navigationBars["Projects"].exists)
        XCTAssert(app.staticTexts[TestConstants.testProjectName].exists)
        XCTAssert(app.staticTexts[TestConstants.testProjectName + "2"].exists)
        app.staticTexts[TestConstants.testProjectName + "2"].tap()
        XCTAssert(app.navigationBars[TestConstants.testProjectName + "2"].exists)
    }

    func testProjectSettings() {
        let app = UITestHelper.launchApp(argument: .testProject)
        app.navigationBars.buttons.firstMatch.tap()  // Labels on toolbar items aren't supported in SwiftUI yet
        app.buttons["Project Settings"].tap()
        XCTAssert(app.navigationBars["Project Settings"].exists)
        XCTAssert(app.staticTexts[TestConstants.testProjectName].exists)
        XCTAssert(app.staticTexts[TestConstants.testGitURL].exists)
        app.navigationBars.buttons["Done"].tap()
        XCTAssert(!app.navigationBars["Project Settings"].exists)
    }

    func testAppSettings() {
        let app = UITestHelper.launchApp(argument: .testProject)
        app.navigationBars.buttons.firstMatch.tap()  // Labels on toolbar items aren't supported in SwiftUI yet
        app.buttons["App Settings"].tap()
        XCTAssert(app.navigationBars["Settings"].exists)
        XCTAssert(app.staticTexts.matching(NSPredicate(format: "label BEGINSWITH 'SLR Toolkit App'")).firstMatch.exists)
        app.navigationBars.buttons["Done"].tap()
        XCTAssert(!app.navigationBars["Settings"].exists)
    }
}
