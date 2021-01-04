import XCTest
@testable import SLR_Toolkit

class GitManagerTests: XCTestCase {
    let gitManager = GitManager(gitClient: MockGitClient())
    
    override func setUp() {
        GitManager.deleteGitDirectory()
    }
    
    func testInvalidScheme() {
        let expectation = XCTestExpectation(description: "")
        let url = URL(string: "ssh://git@github.com:MaxHaertwig/slr-example.git")!
        gitManager.cloneRepository(at: url, credentials: ("", "")) { result in
            guard case .failure(let error) = result, case .unsupportedScheme = error else { XCTFail(); return }
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 2)
    }
    
    func testInvalidURL() {
        let expectation = XCTestExpectation(description: "")
        let url = URL(string: "https:///MaxHaertwig/slr-example.git")!
        gitManager.cloneRepository(at: url, credentials: ("", "")) { result in
            guard case .failure(let error) = result, case .invalidURL = error else { XCTFail(); return }
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 2)
    }
    
    func testCorrectPath() {
        let expectation = XCTestExpectation(description: "")
        let url = URL(string: "https://github.com/MaxHaertwig/slr-example.git")!
        gitManager.cloneRepository(at: url, credentials: ("", "")) { result in
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
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 2)
    }
}
