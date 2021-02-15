/// Struct containing the contents of an alert.
struct AlertContent: Identifiable {
    var id: String { title }

    let title: String
    let message: String?
}
