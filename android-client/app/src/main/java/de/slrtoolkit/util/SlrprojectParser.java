package de.slrtoolkit.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
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

        NodeList authorsList = root.getElementsByTagName("authorsList");
        if (authorsList.getLength() > 0) {
            Element authorsListElement = (Element) authorsList.item(0);
            if (isEmptyAuthorsList(authorsListElement)) {
                root.removeChild(authorsListElement);
            }
        }

//        Element newAuthorsList = doc.createElement("authorsList");
//        Element emailElement = doc.createElement("email");
//        emailElement.setTextContent(email);
//        newAuthorsList.appendChild(emailElement);
//
//        Element nameElement = doc.createElement("name");
//        nameElement.setTextContent(name);
//        newAuthorsList.appendChild(nameElement);
//
//        Element organisationElement = doc.createElement("organisation");
//        organisationElement.setTextContent(organisation);
//        newAuthorsList.appendChild(organisationElement);
//
//        root.appendChild(newAuthorsList);
        Element emailElement = doc.createElement("email");
        emailElement.appendChild(doc.createTextNode(email));
        Element nameElement = doc.createElement("name");
        nameElement.appendChild(doc.createTextNode(name));
        Element organisationElement = doc.createElement("organisation");
        organisationElement.appendChild(doc.createTextNode(organisation));

        // Create the <authorsList> element and append the child elements
        Element authorsListElement = doc.createElement("authorsList");
        authorsListElement.appendChild(emailElement);
        authorsListElement.appendChild(nameElement);
        authorsListElement.appendChild(organisationElement);

        // Append the <authorsList> element to the root element
        root.appendChild(authorsListElement);
//
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        DOMSource source = new DOMSource(doc);
//        StreamResult result = new StreamResult(inputFile);
//        transformer.transform(source, result);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Number of spaces for indentation
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream(inputFile));
        transformer.transform(source, result);
    }

    private static boolean isEmptyAuthorsList(Element authorsListElement) {
        NodeList childNodes = authorsListElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element childElement = (Element) childNodes.item(i);
                String textContent = childElement.getTextContent().trim();
                if (!textContent.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
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

    public void editKeywords(String localpath, String keyword, Boolean toAdd){
        try {
            File inputFile = new File(localpath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            Element root = doc.getDocumentElement();

            NodeList keywordsList = root.getElementsByTagName("keywords");
            if (keywordsList.getLength() > 0) {
                Element keywordsElement = (Element) keywordsList.item(0);

                String keywordsContent = keywordsElement.getTextContent().trim();

                if (keywordsContent.isEmpty()) {
                    keywordsElement.setTextContent(keyword);
                } else {
                    keywordsElement.setTextContent(keywordsContent + ", " + keyword);
                }
            }


//            NodeList keywordsList = root.getElementsByTagName("keywords");
//            if (keywordsList.getLength() > 0) {
//                Element keywordsElement = (Element) keywordsList.item(0);
//
//                String keywordsContent = keywordsElement.getTextContent();
//
//                String[] keywords = keywordsContent.split(",");
//
//                String newKeyword = "new_keyword";
//
//                String keywordToRemove = "keyword_to_remove";
//
//                String keywordToModify = "keyword_to_modify";
//                String modifiedKeyword = "modified_keyword";
//
//                StringBuilder updatedKeywords = new StringBuilder();
//                for (String keyword : keywords) {
//                    keyword = keyword.trim();
//
//                    if (keyword.equals(keywordToRemove)) {
//                        continue;
//                    }
//
//                    if (keyword.equals(keywordToModify)) {
//                        keyword = modifiedKeyword;
//                    }
//
//                    updatedKeywords.append(keyword).append(",");
//                }
//
//                if (updatedKeywords.length() > 0) {
//                    updatedKeywords.deleteCharAt(updatedKeywords.length() - 1);
//                }
//
//                keywordsElement.setTextContent(updatedKeywords.toString());
//            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }


    public void deleteKeyword(String localPath, String keywordToDelete){
        try {
            File inputFile = new File(localPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            Element root = doc.getDocumentElement();

            NodeList keywordsList = root.getElementsByTagName("keywords");
            if (keywordsList.getLength() > 0) {
                Element keywordsElement = (Element) keywordsList.item(0);

                String keywordsContent = keywordsElement.getTextContent().trim();

                keywordsContent = keywordsContent.replaceAll("\\b" + keywordToDelete + "\\b", "").replaceAll(",\\s*,", ",").replaceAll("^,|,$", "").trim();
                keywordsElement.setTextContent(keywordsContent);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);


        } catch (ParserConfigurationException | org.xml.sax.SAXException | javax.xml.transform.TransformerException | java.io.IOException e) {
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