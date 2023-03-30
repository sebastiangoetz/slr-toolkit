package de.davidtiede.slrtoolkit.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TaxonomyParserNode {
    private String name;
    private String path;
    private TaxonomyParserNode parent;
    private final List<TaxonomyParserNode> children;

    public TaxonomyParserNode() {
        children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public TaxonomyParserNode getParent() {
        return parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
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
