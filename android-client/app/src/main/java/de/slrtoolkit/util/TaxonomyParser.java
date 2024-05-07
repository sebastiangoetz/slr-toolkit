package de.slrtoolkit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class TaxonomyParser {
    public boolean isEmpty(String string) {
        return string != null && !string.trim().isEmpty();
    }

    public List<TaxonomyParserNode> parse(String taxonomy) {
        char openingBracet = '{';
        char closingBracet = '}';
        char comma = ',';
        String node = "";
        Stack<TaxonomyParserNode> parentNodes = new Stack<>();
        List<TaxonomyParserNode> taxonomyNodes = new ArrayList<>();

        for (int i = 0; i < taxonomy.length(); i++) {
            if (taxonomy.charAt(i) == openingBracet) {
                if (isEmpty(node)) {
                    String trimmedNode = node.trim();
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if (!parentNodes.isEmpty()) {
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
                if (isEmpty(node)) {
                    String trimmedNode = node.trim();
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if (!parentNodes.isEmpty()) {
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
                    if (!parentNodes.isEmpty()) {
                        TaxonomyParserNode currentParent = parentNodes.pop();
                        taxonomyNodes.add(currentParent);
                    }
                }
            } else if (taxonomy.charAt(i) == comma) {
                if (isEmpty(node)) {
                    addNode(node, parentNodes, taxonomyNodes);
                    node = "";
                }
            } else {
                if(!String.valueOf(taxonomy.charAt(i)).equals(System.lineSeparator())) {
                    node += taxonomy.charAt(i);
                }
            }
        }
        if(isEmpty(node)) {
            addNode(node, parentNodes, taxonomyNodes);
        }
        return taxonomyNodes;
    }

    private void addNode(String node, Stack<TaxonomyParserNode> parentNodes, List<TaxonomyParserNode> taxonomyNodes) {
        String trimmedNode = node.trim();
        TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
        taxonomyNode.setName(trimmedNode);
        if (!parentNodes.isEmpty()) {
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
