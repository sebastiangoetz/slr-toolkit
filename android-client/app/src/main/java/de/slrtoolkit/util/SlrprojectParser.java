package de.slrtoolkit.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SlrprojectParser {
    public void parseSlr(String localpath, String newTitle, String newAbstract) {
        try {
            //TODO: intergate parser for slr to the fragment. check if changes are there. try to commit them
            // add also other tags to be changed
            File inputFile = new File(localpath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            Element root = doc.getDocumentElement();

            NodeList titleList = root.getElementsByTagName("title");
            if (titleList.getLength() > 0) {
                Element titleElement = (Element) titleList.item(0);
                titleElement.setTextContent(newTitle);
            }
            NodeList abstractList = root.getElementsByTagName("projectAbstract");
            if(abstractList.getLength()>0){
                Element abstractElement = (Element) abstractList.item(0);
                abstractElement.setTextContent(newAbstract);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public void addAuthorList(String localpath, String name, String email, String organisation) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        File inputFile = new File(localpath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        Element root = doc.getDocumentElement();

        Element newAuthorsList = doc.createElement("authorsList");
        Element emailElement = doc.createElement("email");
        emailElement.setTextContent(email);
        newAuthorsList.appendChild(emailElement);

        Element nameElement = doc.createElement("name");
        nameElement.setTextContent(name);
        newAuthorsList.appendChild(nameElement);

        Element organisationElement = doc.createElement("organisation");
        organisationElement.setTextContent(organisation);
        newAuthorsList.appendChild(organisationElement);

        root.appendChild(newAuthorsList);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(inputFile);
        transformer.transform(source, result);
    }

    public void deleteAuthorList(String localpath, String email) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        File inputFile = new File(localpath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        Element root = doc.getDocumentElement();

        NodeList authorsListNodes = root.getElementsByTagName("authorsList");
        for (int i = 0; i < authorsListNodes.getLength(); i++) {
            Element authorsListElement = (Element) authorsListNodes.item(i);

            Element emailElement = (Element) authorsListElement.getElementsByTagName("email").item(0);
            String emailTemp = emailElement.getTextContent();

            if (emailTemp.equals(email)) {
                root.removeChild(authorsListElement);
                break;
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(inputFile);
        transformer.transform(source, result);
    }

    public void editKeywords(String localpath){
        try {
            // Load the XML file
            File inputFile = new File(localpath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            // Get the root element
            Element root = doc.getDocumentElement();

            // Find the keywords tag
            NodeList keywordsList = root.getElementsByTagName("keywords");
            if (keywordsList.getLength() > 0) {
                Element keywordsElement = (Element) keywordsList.item(0);

                // Get the current content of keywords
                String keywordsContent = keywordsElement.getTextContent();

                // Modify the keywords content as needed
                // Example: Split the content into individual keywords
                String[] keywords = keywordsContent.split(",");

                // Example: Add a new keyword
                String newKeyword = "new_keyword";

                // Example: Remove a keyword
                String keywordToRemove = "keyword_to_remove";

                // Example: Modify a keyword
                String keywordToModify = "keyword_to_modify";
                String modifiedKeyword = "modified_keyword";

                // Rebuild the keywords content
                StringBuilder updatedKeywords = new StringBuilder();
                for (String keyword : keywords) {
                    // Remove leading and trailing whitespaces
                    keyword = keyword.trim();

                    // Skip the keyword to be removed
                    if (keyword.equals(keywordToRemove)) {
                        continue;
                    }

                    // Modify the keyword if needed
                    if (keyword.equals(keywordToModify)) {
                        keyword = modifiedKeyword;
                    }

                    // Add the keyword to the updated content
                    updatedKeywords.append(keyword).append(",");
                }

                // Remove the trailing comma if any
                if (updatedKeywords.length() > 0) {
                    updatedKeywords.deleteCharAt(updatedKeywords.length() - 1);
                }

                // Set the updated keywords content
                keywordsElement.setTextContent(updatedKeywords.toString());
            }

            // Write the updated XML content to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

//    public void editAuthorList(String localpath, String name, String email, String organisation) throws ParserConfigurationException, IOException, SAXException, TransformerException {
//        File inputFile = new File(localpath);
//        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(inputFile);
//        Element root = doc.getDocumentElement();
//
//        NodeList authorsListNodes = root.getElementsByTagName("authorsList");
//        for (int i = 0; i < authorsListNodes.getLength(); i++) {
//            Element authorsListElement = (Element) authorsListNodes.item(i);
//
//            // Edit specific child elements of the <authorsList> tag
//            Element emailElement = (Element) authorsListElement.getElementsByTagName("email").item(0);
//            emailElement.setTextContent("edited_email@example.com");
//
//            Element nameElement = (Element) authorsListElement.getElementsByTagName("name").item(0);
//            nameElement.setTextContent("Edited Author");
//
//            Element organisationElement = (Element) authorsListElement.getElementsByTagName("organisation").item(0);
//            organisationElement.setTextContent("Edited Organisation");
//        }
//
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        DOMSource source = new DOMSource(doc);
//        StreamResult result = new StreamResult(inputFile);
//        transformer.transform(source, result);
//    }
}