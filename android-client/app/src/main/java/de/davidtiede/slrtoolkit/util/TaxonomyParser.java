package de.davidtiede.slrtoolkit.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import de.davidtiede.slrtoolkit.database.Taxonomy;

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
                    System.out.println(trimmedNode);
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    if(parentNodes.size() > 0) {
                        taxonomyNode.setParent(parentNodes.peek());
                    }
                    parentNodes.push(taxonomyNode);
                    taxonomyNodes.add(taxonomyNode);
                    node = "";
                }
            } else if(Character.compare(taxonomy.charAt(i), closingBracet) == 0) {
                if(!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    System.out.println(trimmedNode);
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if(parentNodes.size() > 0) {
                        taxonomyNode.setParent(parentNodes.peek());
                    }
                    parentNodes.pop();
                    taxonomyNodes.add(taxonomyNode);
                    node = "";
                }
            } else if(Character.compare(taxonomy.charAt(i), comma) == 0) {
                if(!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    System.out.println(trimmedNode);
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if(parentNodes.size() > 0) {
                        taxonomyNode.setParent(parentNodes.peek());
                    }
                    taxonomyNodes.add(taxonomyNode);
                    node = "";
                }
            } else {
                node += taxonomy.charAt(i);
            }
        }
        System.out.println(taxonomyNodes.size());
        return taxonomyNodes;
    }
}
