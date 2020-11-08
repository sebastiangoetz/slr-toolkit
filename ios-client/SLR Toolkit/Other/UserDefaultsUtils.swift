import Foundation

enum StringUserDefaultsKey: String {
    case username, token
}

enum URLUserDefaultsKey: String {
    case activeProject
}

extension UserDefaults {
    func set(_ value: String, forKey key: StringUserDefaultsKey) {
        set(value, forKey: key.rawValue)
    }
    
    func string(forKey key: StringUserDefaultsKey) -> String? {
        return string(forKey: key.rawValue)
    }
    
    func set(_ value: URL, forKey key: URLUserDefaultsKey) {
        set(value, forKey: key.rawValue)
    }
    
    func url(forKey key: URLUserDefaultsKey) -> URL? {
        return url(forKey: key.rawValue)
    }
}
