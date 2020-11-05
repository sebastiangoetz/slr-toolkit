import XCTest
@testable import SLR_Toolkit

class GitManagerTests: XCTestCase {
    let gitManager = GitManager(gitClient: MockGitClient())
    
    override func setUp() {
        GitManager.deleteGitDirectory()
    }
    
    func testInvalidScheme() {
        let url = URL(string: "ssh://git@github.com:MaxHaertwig/slr-example.git")!
        let result = gitManager.cloneRepository(at: url)
        guard case .failure(let error) = result, case .invalidScheme = error else { XCTFail(); return }
    }
    
    func testInvalidURL() {
        let url = URL(string: "https:///MaxHaertwig/slr-example.git")!
        let result = gitManager.cloneRepository(at: url)
        guard case .failure(let error) = result, case .invalidURL = error else { XCTFail(); return }
    }
    
    func testCorrectPath() {
        let url = URL(string: "https://github.com/MaxHaertwig/slr-example.git")!
        let result = gitManager.cloneRepository(at: url)
        switch result {
        case .success(let url):
            let expectedURL = GitManager.gitDirectory
                .appendingPathComponent("github.com", isDirectory: true)
                .appendingPathComponent("MaxHaertwig", isDirectory: true)
                .appendingPathComponent("slr-example", isDirectory: true)
            XCTAssertEqual(url.standardizedFileURL, expectedURL.standardizedFileURL)
            XCTAssert(FileManager.default.fileExists(atPath: expectedURL.path))
        case .failure:
            XCTFail()
        }
    }
    
    func testRepositoryAlreadyCloned() {
        let url = URL(string: "https://github.com/MaxHaertwig/slr-example.git")!
        let result = gitManager.cloneRepository(at: url)
        print(result)
        guard case .success = result else { XCTFail(); return }
        let result2 = gitManager.cloneRepository(at: url)
        guard case .failure(let error) = result2, case .repositoryAlreadyCloned = error else { XCTFail(); return }
    }
}
