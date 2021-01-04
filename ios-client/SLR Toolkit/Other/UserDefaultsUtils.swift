import Foundation

enum BoolUserDefaultsKey: String {
    case sortAscending
}

enum StringUserDefaultsKey: String {
    case username, token, sortMode
}

enum URLUserDefaultsKey: String {
    case activeProject
}

extension UserDefaults {
    func set(_ value: Bool, forKey key: BoolUserDefaultsKey) {
        set(value, forKey: key.rawValue)
    }

    func bool(forKey key: BoolUserDefaultsKey) -> Bool {
        return bool(forKey: key.rawValue)
    }

    func removeBool(forKey key: BoolUserDefaultsKey) {
        removeObject(forKey: key.rawValue)
    }

    func set(_ value: String, forKey key: StringUserDefaultsKey) {
        set(value, forKey: key.rawValue)
    }

    func string(forKey key: StringUserDefaultsKey) -> String? {
        return string(forKey: key.rawValue)
    }

    func removeString(forKey key: StringUserDefaultsKey) {
        removeObject(forKey: key.rawValue)
    }
    
    func set(_ value: URL, forKey key: URLUserDefaultsKey) {
        set(value, forKey: key.rawValue)
    }
    
    func url(forKey key: URLUserDefaultsKey) -> URL? {
        return url(forKey: key.rawValue)
    }
    
    func removeURL(forKey key: URLUserDefaultsKey) {
        removeObject(forKey: key.rawValue)
    }
}
