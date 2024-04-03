package de.slrtoolkit.views;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amrdeveloper.treeview.TreeNode;
import com.amrdeveloper.treeview.TreeViewHolder;

import de.slrtoolkit.R;

public class TaxonomyTreeViewHolder extends TreeViewHolder {
    TextView textName;
    ImageButton btn;
    public TaxonomyTreeViewHolder(@NonNull View itemView) {
        super(itemView);
        textName = itemView.findViewById(R.id.taxonomy_entry_name);
        btn = itemView.findViewById(R.id.taxonomy_entry_button);
    }

    @Override
    public void bindTreeNode(TreeNode node) {
        super.bindTreeNode(node);
        textName.setText(node.getValue().toString());
        if(node.getChildren().size() == 0) {
            btn.setVisibility(View.INVISIBLE);
        } else {
            btn.setVisibility(View.VISIBLE);
            if(node.isExpanded()) {
                btn.setRotation(90);
            } else {
                btn.setRotation(0);
            }
        }
    }
}
