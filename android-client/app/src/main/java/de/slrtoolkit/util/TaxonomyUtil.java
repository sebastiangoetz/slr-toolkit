package de.slrtoolkit.util;

import android.util.Log;

import com.amrdeveloper.treeview.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.viewmodels.ProjectViewModel;

public class TaxonomyUtil {

    private boolean withNumberOfEntries;
    private ProjectViewModel viewModel;

    public List<TreeNode> taxonomiesToTreeNodes(List<Taxonomy> taxonomies, boolean withNumberOfEntries, ProjectViewModel viewModel) {
        this.withNumberOfEntries = withNumberOfEntries;
        this.viewModel = viewModel;
        return taxonomiesToTreeNodes(taxonomies);
    }
    public List<TreeNode> taxonomiesToTreeNodes(List<Taxonomy> taxonomies) {
        List<TreeNode> rootTaxonomies = new ArrayList<>();
        for(Taxonomy root : taxonomies) {
            if(root.getParentId() == 0) {
                TaxonomyTreeNode n = new TaxonomyTreeNode(root.getTaxonomyId(), root.getName());
                if(withNumberOfEntries) {
                    n.setShowNumberOfEntries(true);
                    n.setNumberOfEntries(getNumberOfEntries(root));
                }
                TreeNode rootNode = new TreeNode(n, R.layout.item_taxonomy_entry);
                n.setTreeNode(rootNode);
                addChildrenToRoot(rootNode, root.getTaxonomyId(), taxonomies);
                rootTaxonomies.add(rootNode);
            }
        }

        return rootTaxonomies;
    }

    private int getNumberOfEntries(Taxonomy taxonomy) {
        try {
            return viewModel.getBibEntryAmountForTaxonomy(taxonomy.getTaxonomyId());
        } catch (ExecutionException | InterruptedException e) {
            Log.e(this.getClass().getName(), "Could not load number of entries for this taxonomy.", e);
            return 0;
        }
    }

    /**Adds all taxonomy entries from the third parameter to the root node, if this node is their parent
     *
     * @param root the TreeNode to which children shall be added
     * @param rootId the id of the root
     * @param taxonomies list of taxonomy entries which potentially are children of root
     */
    private void addChildrenToRoot(TreeNode root, int rootId, List<Taxonomy> taxonomies) {
        for (Taxonomy tax : taxonomies) {
            if (tax.getParentId() == rootId) {
                TaxonomyTreeNode n = new TaxonomyTreeNode(tax.getTaxonomyId(), tax.getName());
                if(withNumberOfEntries) {
                    n.setShowNumberOfEntries(true);
                    n.setNumberOfEntries(getNumberOfEntries(tax));
                }
                TreeNode child = new TreeNode(n, R.layout.item_taxonomy_entry);
                n.setTreeNode(child);
                addChildrenToRoot(child, tax.getTaxonomyId(), taxonomies);
                root.addChild(child);
            }
        }
    }
}
