import Foundation

enum TestConstants {
    static let testGitURL = "https://tu-dresden.de/Max/slr.git"
    static let testUsername = "Max"
    static let testToken = "abc"
    static let testProjectName = "TestProject"
}

enum LaunchArgument: String {
    case testing
    case reset         // delete all app data
    case testProject   // sets up a test project
    case testProject2  // sets up a second test project
}
