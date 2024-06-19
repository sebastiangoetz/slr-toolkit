package de.slrtoolkit.util;

import androidx.annotation.NonNull;

import com.amrdeveloper.treeview.TreeNode;

import java.util.concurrent.atomic.AtomicInteger;

public class TaxonomyTreeNode {
    private int id;
    private String name;
    private boolean showNumberOfEntries;
    private int numberOfEntries;
    private TreeNode encapsulatingTreeNode;

    public TaxonomyTreeNode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTreeNode(TreeNode treeNode) {
        encapsulatingTreeNode = treeNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShowNumberOfEntries(boolean showNumberOfEntries) {
        this.showNumberOfEntries = showNumberOfEntries;
    }

    public void setNumberOfEntries(int numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }

    public int getNumberOfEntries() {
        if(encapsulatingTreeNode.getChildren().isEmpty()) {
            return numberOfEntries;
        } else {
            AtomicInteger total = new AtomicInteger();
            encapsulatingTreeNode.getChildren().forEach(treeNode -> total.addAndGet(((TaxonomyTreeNode) treeNode.getValue()).getNumberOfEntries()));
            return total.intValue();
        }

    }

    @NonNull
    @Override
    public String toString() {
        return name + (showNumberOfEntries ? " ("+getNumberOfEntries()+")" : "");
    }
}
