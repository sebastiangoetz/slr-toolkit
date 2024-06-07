package de.slrtoolkit.util;

import androidx.annotation.NonNull;

public class TaxonomyTreeNode {
    private int id;
    private String name;
    private boolean showNumberOfEntries;
    private int numberOfEntries;

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
        return numberOfEntries;
    }

    @NonNull
    @Override
    public String toString() {
        return name + (showNumberOfEntries ? " ("+numberOfEntries+")" : "");
    }
}
