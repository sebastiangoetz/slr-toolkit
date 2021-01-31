import Foundation

enum TestHelper {
    static func handleLaunchArguments(_ arguments: [String]) {
        let userDefaults = UserDefaults.standard
        let launchArguments = arguments.compactMap { LaunchArgument(rawValue: $0) }

        if launchArguments.contains(.reset) {
            (BoolUserDefaultsKey.allCases.map(\.rawValue) + StringUserDefaultsKey.allCases.map(\.rawValue) + URLUserDefaultsKey.allCases.map(\.rawValue)).forEach { userDefaults.removeObject(forKey: $0) }
            PersistenceController.shared.deleteAllData()
            let gitDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0].appendingPathComponent("git", isDirectory: true)
            try? FileManager.default.removeItem(at: gitDirectory)
        }

        if launchArguments.contains(.testProject) {
            let managedObjectContext = PersistenceController.shared.container.viewContext
            try? MockGitClient.createTestFiles(at: GitManager.gitDirectory.appendingPathComponent("slr"))
            ProjectManager.createProject(name: TestConstants.testProjectName, username: TestConstants.testUsername, token: TestConstants.testToken, repositoryURL: TestConstants.testGitURL, pathInGitDirectory: "slr", pathInRepository: "", managedObjectContext: managedObjectContext) { _ in }
        }

        if launchArguments.contains(.testProject2) {
            let managedObjectContext = PersistenceController.shared.container.viewContext
            try? MockGitClient.createTestFiles(at: GitManager.gitDirectory.appendingPathComponent("slr2"))
            ProjectManager.createProject(name: TestConstants.testProjectName + "2", username: TestConstants.testUsername, token: TestConstants.testToken, repositoryURL: TestConstants.testGitURL, pathInGitDirectory: "slr2", pathInRepository: "", managedObjectContext: managedObjectContext) { _ in }
        }
    }
}
