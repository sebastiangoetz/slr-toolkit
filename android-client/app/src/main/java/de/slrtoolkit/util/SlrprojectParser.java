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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SlrprojectParser {
    public void parseSlr(String localpath, String newTitle, String newAbstract) {
        try {
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

        Element emailElement = doc.createElement("email");
        emailElement.appendChild(doc.createTextNode(email));
        Element nameElement = doc.createElement("name");
        nameElement.appendChild(doc.createTextNode(name));
        Element organisationElement = doc.createElement("organisation");
        organisationElement.appendChild(doc.createTextNode(organisation));

        Element authorsListElement = doc.createElement("authorsList");
        authorsListElement.appendChild(emailElement);
        authorsListElement.appendChild(nameElement);
        authorsListElement.appendChild(organisationElement);

        root.appendChild(authorsListElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
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

    public void deleteAuthorList(String localpath, String email) {
        File inputFile = new File(localpath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(inputFile);
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
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
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(inputFile);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
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
}