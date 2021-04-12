import SwiftUI

/// UICollectionViewCell showing the details of an entry. Wraps the SwiftUI view EntryCard.
final class EntryCell: UICollectionViewCell {
    private var hostingController: UIHostingController<EntryCard>?

    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }

    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = .clear
    }

    func display(_ entry: Entry) {
        if let hostingController = hostingController {
            hostingController.rootView = EntryCard(entry: entry)
        } else {
            hostingController = UIHostingController(rootView: EntryCard(entry: entry))
            hostingController!.view.translatesAutoresizingMaskIntoConstraints = false
            hostingController!.view.backgroundColor = .clear
            contentView.addSubview(hostingController!.view)

            NSLayoutConstraint.activate([
                hostingController!.view.topAnchor.constraint(equalTo: contentView.topAnchor),
                hostingController!.view.leftAnchor.constraint(equalTo: contentView.leftAnchor),
                hostingController!.view.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
                hostingController!.view.rightAnchor.constraint(equalTo: contentView.rightAnchor)
            ])
        }
    }
}
