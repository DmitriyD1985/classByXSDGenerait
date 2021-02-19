package task3;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HamletXMLParser {
    final static String filePath = "G:\\tasksFromDaria\\src\\main\\java\\task3\\FileseForReading\\hamlet.xml";

    private HamletXMLParser() {
    }

    public static void parserSAX() {
        try {
            File input = new File(filePath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    FileMain.writeResult(new String(ch, start, length));
                }
            };
            saxParser.parse(input, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parserDOM() {
        try {
            File input = new File(filePath);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(input);
            Node root = document.getDocumentElement();
            NodeList tags = root.getChildNodes();
            for (int i = 0; i < tags.getLength(); i++) {
                Node tag = tags.item(i);
                FileMain.writeResult(tag.getTextContent());
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
    This method don't work.
    in package "PlayMapping" i tryed to build tree of classes and put annotations
    but it still didn't work
    */
    public static void parserObject() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            String readContent = new String(Files.readAllBytes(Paths.get(filePath)));
            Speech deserializedData = xmlMapper.readValue(readContent, Speech.class);

            System.out.println(deserializedData.getSpeaker());
//            Set<String> uniqueWords =
//                    Arrays
//                    .stream(deserializedData.getSpeeshes())
//                    .filter(a->a.getSpeaker()
//                            .equalsIgnoreCase("HAMLET"))
//                    .map(Speech::getSpeeshes)
//                    .forEach(a->System.out.println(a.toString()));
////            System.out.println(uniqueWords.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void countAmountUniqueWords() {

        try {
            String findIt = "PLAY/ACT/SCENE/SPEECH";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            NodeList list = (NodeList) xPath.evaluate(findIt, document, XPathConstants.NODESET);
            Set<String> textOfHamlet = new HashSet<>();
            for (int i = 0; i < list.getLength(); i++) {
                if (xPath.compile("SPEAKER").evaluate(list.item(i)).equalsIgnoreCase("HAMLET")) {
                    textOfHamlet.addAll(Arrays.asList(xPath.compile("LINE").evaluate(list.item(i)).split(" ")));
                }
            }
            System.out.println("Number of unique words in Hamlet’s replies -" + textOfHamlet.size());
        } catch (ParserConfigurationException | IOException | XPathExpressionException | SAXException e) {
            e.printStackTrace();
        }
    }


    public static void countTheXMLTagsDistribution() {

        File input = new File(filePath);
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = builder.parse(input);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        NodeList list = doc.getElementsByTagName("*");
        Set<String> setElement = new HashSet<>();
        List<String> listElement = new ArrayList<>();
        for (int i = 0; i < list.getLength(); i++) {
            setElement.add(list.item(i).getNodeName());
            listElement.add(list.item(i).getNodeName());
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("word");
        stringBuilder.append(',');
        stringBuilder.append("amount");
        stringBuilder.append('\n');
        for (String el : setElement) {
            stringBuilder.append(el);
            stringBuilder.append(',');
            stringBuilder.append("" + Collections.frequency(listElement, el));
            stringBuilder.append('\n');
        }
        toCSVWriter(stringBuilder.toString());
    }

    public static void toCSVWriter(String inpoutInfo) {
        try (PrintWriter writer = new PrintWriter(new File("src/main/java/task3/test.csv"))) {
            writer.write(inpoutInfo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
