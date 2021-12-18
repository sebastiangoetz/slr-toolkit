package de.davidtiede.slrtoolkit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class TaxonomyParser {
    public boolean isEmpty(String string) {
        if(string != null && !string.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public List<TaxonomyParserNode> parse(String taxonomy) {
        char openingBracet = "{".charAt(0);
        char closingBracet = "}".charAt(0);
        char comma = ",".charAt(0);
        String node = "";
        Stack<TaxonomyParserNode> parentNodes = new Stack<>();
        List<TaxonomyParserNode> taxonomyNodes = new ArrayList<>();

        for(int i = 0; i < taxonomy.length(); i++) {
            if(Character.compare(taxonomy.charAt(i), openingBracet) == 0) {
                if(!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if(parentNodes.size() > 0) {
                        TaxonomyParserNode currentParent = parentNodes.peek();

                        //set child and parent on respective nodes
                        taxonomyNode.setParent(currentParent);
                        currentParent.addChild(taxonomyNode);
                        int parentIndex = parentNodes.indexOf(currentParent);
                        parentNodes.set(parentIndex, currentParent);
                    }
                    String path = getTaxonomyPath(parentNodes, trimmedNode);
                    taxonomyNode.setPath(path);
                    parentNodes.push(taxonomyNode);
                    node = "";
                }
            } else if(Character.compare(taxonomy.charAt(i), closingBracet) == 0) {
                if(!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if(parentNodes.size() > 0) {
                        TaxonomyParserNode currentParent = parentNodes.pop();
                        taxonomyNode.setParent(currentParent);
                        currentParent.addChild(taxonomyNode);
                        taxonomyNodes.add(currentParent);
                    }
                    String path = getTaxonomyPath(parentNodes, trimmedNode);
                    taxonomyNode.setPath(path);
                    taxonomyNodes.add(taxonomyNode);
                    node = "";
                } else {
                    if(parentNodes.size() > 0) {
                        TaxonomyParserNode currentParent = parentNodes.pop();
                        taxonomyNodes.add(currentParent);
                    }
                }
            } else if(Character.compare(taxonomy.charAt(i), comma) == 0) {
                if(!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if(parentNodes.size() > 0) {
                        TaxonomyParserNode currentParent = parentNodes.peek();

                        //set child and parent on respective nodes
                        taxonomyNode.setParent(currentParent);
                        currentParent.addChild(taxonomyNode);
                        int parentIndex = parentNodes.indexOf(currentParent);
                        parentNodes.set(parentIndex, currentParent);
                    }
                    String path = getTaxonomyPath(parentNodes, trimmedNode);
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

    public String getTaxonomyPath(Stack<TaxonomyParserNode> stack, String taxonomyName) {
        String path = "";
        for(int i = 0; i < stack.size(); i++) {
            TaxonomyParserNode node = stack.get(i);
            path += node.getName() + "#";
        }
        path+= taxonomyName;
        return path;
    }
}
