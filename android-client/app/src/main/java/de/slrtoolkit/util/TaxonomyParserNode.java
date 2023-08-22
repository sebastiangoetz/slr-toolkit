package de.slrtoolkit.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TaxonomyParserNode {
    private final List<TaxonomyParserNode> children;
    private String name;
    private String path;
    private TaxonomyParserNode parent;

    public TaxonomyParserNode() {
        children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TaxonomyParserNode getParent() {
        return parent;
    }

    public void setParent(TaxonomyParserNode parent) {
        this.parent = parent;
    }

    public List<TaxonomyParserNode> getChildren() {
        return children;
    }

    public void addChild(TaxonomyParserNode child) {
        children.add(child);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder children = new StringBuilder();
        for (TaxonomyParserNode child : getChildren()) {
            children.append(child.getName());
        }
        String string = "Name: " + getName() + ", Children: " + children;
        if (parent != null) {
            string += "Parent: " + parent.getName();
        }
        return string;
    }
}
