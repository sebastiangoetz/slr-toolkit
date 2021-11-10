package de.davidtiede.slrtoolkit.util;

import java.util.ArrayList;
import java.util.List;

public class TaxonomyParserNode {
    private String name;
    private TaxonomyParserNode parent;

    public String getName() {
        return name;
    }

    public TaxonomyParserNode getParent() {
        return parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(TaxonomyParserNode parent) {
        this.parent = parent;
    }
}
