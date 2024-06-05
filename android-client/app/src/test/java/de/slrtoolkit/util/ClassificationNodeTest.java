package de.slrtoolkit.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class ClassificationNodeTest {

    @Test
    public void parseStringFlat() {
        String classification = "A, B, Test";
        List<ClassificationNode> result = ClassificationNode.parseString(classification);
        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("B", result.get(1).getName());
        assertEquals("Test", result.get(2).getName());
    }

    @Test
    public void parseStringLevel1() {
        String classification = "X { A, B, Test }";
        List<ClassificationNode> result = ClassificationNode.parseString(classification);
        assertEquals(1, result.size());
        ClassificationNode first = result.get(0);
        assertEquals(3, first.getChildren().size());
    }

    @Test
    public void parseStringDeep() {
        String classification = "Venue { ICSE }, VenueType { Conference }, Evaluation { Case Study, Arguments, Empirical }";
        List<ClassificationNode> result = ClassificationNode.parseString(classification);
        assertEquals(3, result.size());
        ClassificationNode first = result.get(0);
        assertEquals(1, first.getChildren().size());
        ClassificationNode second = result.get(1);
        assertEquals("Conference", second.getChildren().get(0).getName());
        ClassificationNode third = result.get(2);
        assertEquals(3, third.getChildren().size());
    }

    @Test
    public void parseStringDeeper() {
        String classification = "Venue { ICSE }, VenueType { Conference { A+ } }, Evaluation { Case Study { Industrial, Simulation }, Arguments, Empirical }";
        List<ClassificationNode> result = ClassificationNode.parseString(classification);
        assertEquals(3, result.size());
        ClassificationNode first = result.get(0);
        assertEquals(1, first.getChildren().size());
        ClassificationNode second = result.get(1);
        assertEquals("Conference", second.getChildren().get(0).getName());
        ClassificationNode third = result.get(2);
        assertEquals(3, third.getChildren().size());
    }

    @Test
    public void testGetPath() {
        String classification = "Venue { ICSE }, VenueType { Conference { A+ } }, Evaluation { Case Study { Industrial, Simulation }, Arguments, Empirical }";
        List<ClassificationNode> result = ClassificationNode.parseString(classification);
        ClassificationNode aplus = result.get(1).getChildren().get(0).getChildren().get(0);
        assertEquals("VenueType/Conference/A+", aplus.getPath());
    }

    @Test
    public void testAddPath() {
        String classification = "Venue { ICSE }, VenueType { Conference { A+ } }, Evaluation { Case Study { Industrial, Simulation }, Arguments, Empirical }";
        String newClassification = BibUtil.addClassToClassification(classification, "VenueType/Conference/B");
        assertTrue(ClassificationNode.parseString(newClassification).get(1).getChildren().get(0).getChildren().stream().anyMatch(c -> c.getName().equals("B")));
    }

    @Test
    public void testAddPath2() {
        String classification = "Venue { ICSE }, VenueType { Conference { A+ } }, Evaluation { Case Study { Industrial, Simulation }, Arguments, Empirical }";
        String newClassification = BibUtil.addClassToClassification(classification, "Test/Subtest/Subsubtest");
        List<ClassificationNode> parsed = ClassificationNode.parseString(newClassification);
        assertEquals("Test", parsed.get(3).getName());
        assertEquals("Subtest", parsed.get(3).getChildren().get(0).getName());
        assertEquals("Subsubtest", parsed.get(3).getChildren().get(0).getChildren().get(0).getName());
    }

    @Test
    public void testAddPath3() {
        String classification = "Venue { ICSE }, VenueType { Conference { A+ } }, Evaluation { Case Study { Industrial, Simulation }, Arguments, Empirical }";
        String newClassification = BibUtil.addClassToClassification(classification, "Evaluation/None");
        List<ClassificationNode> parsed = ClassificationNode.parseString(newClassification);
        assertEquals("Evaluation", parsed.get(2).getName());
        assertEquals("None", parsed.get(2).getChildren().get(3).getName());
    }

    @Test
    public void testRemovePath() {
        String classification = "Venue { ICSE }, VenueType { Conference { A+ } }, Evaluation { Case Study { Industrial, Simulation }, Arguments, Empirical }";
        String newClassification = BibUtil.removeClassFromClassification(classification, "Evaluation/Case Study/Simulation");
        List<ClassificationNode> parsed = ClassificationNode.parseString(newClassification);
        assertEquals(1, parsed.get(2).getChildren().get(0).getChildren().size());

    }
}