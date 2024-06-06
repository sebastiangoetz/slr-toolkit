package de.slrtoolkit.util;

import com.amrdeveloper.treeview.TreeNode;

import java.util.ArrayList;
import java.util.List;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Taxonomy;

public class TaxonomyUtil {

    public List<TreeNode> taxonomiesToTreeNodes(List<Taxonomy> taxonomies) {
        List<TreeNode> rootTaxonomies = new ArrayList<>();
        for(Taxonomy root : taxonomies) {
            if(root.getParentId() == 0) {
                TaxonomyTreeNode n = new TaxonomyTreeNode(root.getTaxonomyId(), root.getName());
                TreeNode rootNode = new TreeNode(n, R.layout.item_taxonomy_entry);
                addChildrenToRoot(rootNode, root.getTaxonomyId(), taxonomies);
                rootTaxonomies.add(rootNode);
            }
        }

        return rootTaxonomies;
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
                TreeNode child = new TreeNode(n, R.layout.item_taxonomy_entry);
                addChildrenToRoot(child, tax.getTaxonomyId(), taxonomies);
                root.addChild(child);
            }
        }
    }
}
