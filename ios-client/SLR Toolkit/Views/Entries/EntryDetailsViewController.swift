import CoreData
import SwiftUI

final class EntryDetailsViewController: UICollectionViewController, NSFetchedResultsControllerDelegate, UICollectionViewDelegateFlowLayout {
    var fetchRequest: NSFetchRequest<Entry>!
    var currentEntry: Binding<Entry?>!

    private var currentIndexPath = IndexPath(item: 0, section: 0) {
        didSet {
            currentEntry.wrappedValue = fetchedResultsController.object(at: currentIndexPath)
        }
    }

    private var blocks: [() -> Void]!

    private lazy var fetchedResultsController: NSFetchedResultsController<Entry> = {
        let fetchedResultsController = NSFetchedResultsController(fetchRequest: fetchRequest, managedObjectContext: PersistenceController.shared.container.viewContext, sectionNameKeyPath: nil, cacheName: nil)
        fetchedResultsController.delegate = self
        try? fetchedResultsController.performFetch()
        return fetchedResultsController
    }()

    // MARK: - UIViewController

    override func viewDidLoad() {
        super.viewDidLoad()

        collectionView.delegate = self
        collectionView.isPagingEnabled = true
        collectionView.bounces = true
        collectionView.backgroundColor = .systemGroupedBackground

        collectionView.register(EntryCell.self, forCellWithReuseIdentifier: "EntryCell")
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if let indexPath = fetchedResultsController.indexPath(forObject: currentEntry.wrappedValue!) {
            collectionView.scrollToItem(at: indexPath, at: .centeredHorizontally, animated: false)
        }
    }

    // MARK: - UICollectionViewDelegate

    override func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        if let indexPath = collectionView?.indexPathsForVisibleItems.first, currentIndexPath != indexPath {
            currentIndexPath = indexPath
        }
    }

    // MARK: - UICollectionViewDataSource

    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return fetchedResultsController.fetchedObjects?.count ?? 0
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "EntryCell", for: indexPath) as! EntryCell
        cell.display(fetchedResultsController.object(at: indexPath))
        return cell
    }

    // MARK: - NSFetchedResultsControllerDelegate

    func controllerWillChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        blocks = []
    }

    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange anObject: Any, at indexPath: IndexPath?, for type: NSFetchedResultsChangeType, newIndexPath: IndexPath?) {
        switch type {
        case .insert:
            if let newIndexPath = newIndexPath {
                blocks.append { self.collectionView?.insertItems(at: [newIndexPath]) }
            }
        case .delete:
            if let indexPath = indexPath {
                blocks.append { self.collectionView?.deleteItems(at: [indexPath]) }
            }
        case .update:
            if let indexPath = indexPath, let cell = collectionView?.cellForItem(at: indexPath) as? EntryCell {
                cell.display(fetchedResultsController.object(at: indexPath))
            }
        case .move:
            if let indexPath = indexPath, let newIndexPath = newIndexPath {
                blocks.append {
                    self.collectionView?.deleteItems(at: [indexPath])
                    self.collectionView?.insertItems(at: [newIndexPath])
                }
            }
        @unknown default:
            break
        }
    }

    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange sectionInfo: NSFetchedResultsSectionInfo, atSectionIndex sectionIndex: Int, for type: NSFetchedResultsChangeType) {
        switch type {
        case .insert:
            blocks.append { self.collectionView?.insertSections(IndexSet(integer: sectionIndex)) }
        case .delete:
            blocks.append { self.collectionView?.deleteSections(IndexSet(integer: sectionIndex)) }
        default:
            break
        }
    }

    func controllerDidChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        if fetchedResultsController.fetchedObjects?.isEmpty == false {
            collectionView?.performBatchUpdates {
                self.blocks.forEach { $0() }
            } completion: { _ in
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {  // workaround
                    if let indexPath = self.collectionView?.indexPathsForVisibleItems.first {
                        self.currentIndexPath = indexPath
                    }
                }
            }
        } else {
            currentEntry.wrappedValue = nil
        }
    }

    // MARK: - UICollectionViewDelegateFlowLayout

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return cardSize
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        let size = cardSize
        let topInset = size.height * 2 < collectionView.frame.size.height ? (collectionView.frame.size.height - size.height) / 2 : 0
        let leftInset = (collectionView.frame.size.width - size.width) / 2
        return UIEdgeInsets(top: topInset, left: leftInset, bottom: topInset, right: leftInset)
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return collectionView.frame.size.width - cardSize.width
    }

    private var cardSize: CGSize {
        let inset: CGFloat = 24
        return CGSize(width: collectionView.frame.size.width - 2 * inset, height: collectionView.frame.size.height - 2 * inset)
    }
}
