package de.slrtoolkit.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ClassificationNode {
    private final String name;
    private final List<ClassificationNode> children;
    private ClassificationNode parent;

    public ClassificationNode(String name) {
        this.name = name;
        children = new ArrayList<>();
    }

    public void setParent(ClassificationNode parent) {
        this.parent = parent;
    }

    public void addChild(ClassificationNode child) {
        child.setParent(this);
        children.add(child);
    }

    public List<ClassificationNode> getChildren() {
        return children;
    }

    public ClassificationNode getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        StringBuilder b = new StringBuilder();
        if(parent != null) {
            b.append(parent.getPath()).append("/");
        }
        b.append(name);
        return b.toString();
    }

    public void addPath(String path) {
        if(path.contains("/")) {
            ClassificationNode child = getChildren().stream().filter(c -> c.getName().equals(path.substring(0,path.indexOf("/")))).findAny().orElse(null);
            if(child != null) {
                child.addPath(path.substring(path.indexOf("/")+1));
            } else {
                ClassificationNode newChild = new ClassificationNode(path.substring(0, path.indexOf("/")));
                newChild.addPath(path.substring(path.indexOf("/")+1));
                addChild(newChild);
            }
        } else {
            ClassificationNode newChild = new ClassificationNode(path);
            addChild(newChild);
        }
    }

    public void removePath(String path) {
        String rootName;
        if(path.contains("/")) {
            rootName = path.substring(0,path.indexOf("/"));
        } else {
            rootName = path;
        }
        ClassificationNode child = getChildren().stream().filter(c -> c.getName().equals(rootName)).findAny().orElse(null);
        if(child == null) {
            System.err.println("Can't find next element. Cannot remove.");
        } else {
            if(path.contains("/")) {
                child.removePath(path.substring(path.indexOf("/")+1));
                if(child.getChildren().isEmpty()) getChildren().remove(child);
            } else {
                getChildren().remove(child);
            }
        }
    }

    public static List<ClassificationNode> parseString(String classification) {
        List<ClassificationNode> ret = new ArrayList<>();
        if(classification.trim().isEmpty()) return ret;
        if(!classification.contains("{")) {
            if(!classification.contains(",")) {
                ret.add(new ClassificationNode(classification.trim()));
            } else {
                for(String n : classification.split(",")) {
                    ret.addAll(parseString(n));
                }
            }
        } else {
            String name = classification.substring(0, classification.indexOf("{"));
            String children = classification.substring(classification.indexOf("{")+1);
            String remainder;
            int idxClosing = children.indexOf("}");
            if(children.indexOf("{") < idxClosing) {
                idxClosing = findClosingIndex(children, idxClosing, children.indexOf("{"));
            }
            remainder = children.substring(idxClosing+1);
            children = children.substring(0, idxClosing);
            ClassificationNode parent = new ClassificationNode(name.trim());
            for(ClassificationNode child : parseString(children)) {
                parent.addChild(child);
            }
            ret.add(parent);
            if(remainder.trim().startsWith(",")) {
                ret.addAll(parseString(remainder.substring(remainder.indexOf(",")+1)));
            }
        }
        return ret;
    }

    private static int findClosingIndex(String children, int idxClosing, int idxOpening) {
        if(idxOpening == -1) return idxClosing;
        //search for close after idxClosing
        int nextCloseIdx = children.indexOf("}", idxClosing+1);
        //search for start before idxOpening
        int nextOpenIdx = children.substring(0, idxOpening).lastIndexOf("{");
        if(nextOpenIdx == -1) return nextCloseIdx;
        else return findClosingIndex(children, nextCloseIdx, nextOpenIdx);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if(!getChildren().isEmpty()) {
            sb.append(" { ");
            for(ClassificationNode n : getChildren()) {
                sb.append(n.toString());
                sb.append(", ");
            }
            sb.delete(sb.length()-2, sb.length());
            sb.append(" } ");
        }
        return sb.toString();
    }
}
