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

        let managedObjectContext = PersistenceController.shared.container.viewContext

        if launchArguments.contains(.testProject) {
            try? MockGitClient.createTestFiles(at: GitManager.gitDirectory.appendingPathComponent("slr"))
            ProjectManager.createProjectSync(name: TestConstants.testProjectName, username: TestConstants.testUsername, token: TestConstants.testToken, repositoryURL: TestConstants.testGitURL, pathInGitDirectory: "slr", pathInRepository: "", managedObjectContext: managedObjectContext)
        }

        if launchArguments.contains(.testProject2) {
            try? MockGitClient.createTestFiles(at: GitManager.gitDirectory.appendingPathComponent("slr2"))
            ProjectManager.createProjectSync(name: TestConstants.testProjectName + "2", username: TestConstants.testUsername, token: TestConstants.testToken, repositoryURL: TestConstants.testGitURL, pathInGitDirectory: "slr2", pathInRepository: "", managedObjectContext: managedObjectContext)
        }
    }
}
