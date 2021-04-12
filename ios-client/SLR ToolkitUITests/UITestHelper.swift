import XCTest

enum UITestHelper {
    @discardableResult static func launchApp(argument: LaunchArgument) -> XCUIApplication {
        launchApp(arguments: [argument])
    }

    @discardableResult static func launchApp(arguments: [LaunchArgument]) -> XCUIApplication {
        let app = XCUIApplication()
        app.launchArguments = ([.testing, .reset] + arguments).map(\.rawValue)
        app.launch()
        return app
    }
}
