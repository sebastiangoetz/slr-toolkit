package de.slrtoolkit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class TaxonomyParser {
    public boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    public List<TaxonomyParserNode> parse(String taxonomy) {
        char openingBracet = "{".charAt(0);
        char closingBracet = "}".charAt(0);
        char comma = ",".charAt(0);
        String node = "";
        Stack<TaxonomyParserNode> parentNodes = new Stack<>();
        List<TaxonomyParserNode> taxonomyNodes = new ArrayList<>();

        for (int i = 0; i < taxonomy.length(); i++) {
            if (taxonomy.charAt(i) == openingBracet) {
                if (!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if (parentNodes.size() > 0) {
                        TaxonomyParserNode currentParent = parentNodes.peek();

                        //set child and parent on respective nodes
                        taxonomyNode.setParent(currentParent);
                        currentParent.addChild(taxonomyNode);
                        int parentIndex = parentNodes.indexOf(currentParent);
                        parentNodes.set(parentIndex, currentParent);
                    }
                    String path = getTaxonomyPath(taxonomyNode);
                    taxonomyNode.setPath(path);
                    parentNodes.push(taxonomyNode);
                    node = "";
                }
            } else if (taxonomy.charAt(i) == closingBracet) {
                if (!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if (parentNodes.size() > 0) {
                        TaxonomyParserNode currentParent = parentNodes.pop();
                        taxonomyNode.setParent(currentParent);
                        currentParent.addChild(taxonomyNode);
                        taxonomyNodes.add(currentParent);
                    }
                    String path = getTaxonomyPath(taxonomyNode);
                    taxonomyNode.setPath(path);
                    taxonomyNodes.add(taxonomyNode);
                    node = "";
                } else {
                    if (parentNodes.size() > 0) {
                        TaxonomyParserNode currentParent = parentNodes.pop();
                        taxonomyNodes.add(currentParent);
                    }
                }
            } else if (taxonomy.charAt(i) == comma) {
                if (!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if (parentNodes.size() > 0) {
                        TaxonomyParserNode currentParent = parentNodes.peek();

                        //set child and parent on respective nodes
                        taxonomyNode.setParent(currentParent);
                        currentParent.addChild(taxonomyNode);
                        int parentIndex = parentNodes.indexOf(currentParent);
                        parentNodes.set(parentIndex, currentParent);
                    }
                    String path = getTaxonomyPath(taxonomyNode);
                    taxonomyNode.setPath(path);
                    taxonomyNodes.add(taxonomyNode);
                    node = "";
                }
            } else {
                node += taxonomy.charAt(i);
            }
        }
        return taxonomyNodes;
    }

    public String getTaxonomyPath(TaxonomyParserNode taxonomyNode) {
        String path = "";
        if (taxonomyNode.getParent() != null) {
            path += taxonomyNode.getParent().getPath();
        }
        path += "#" + taxonomyNode.getName();
        return path;
    }
}
