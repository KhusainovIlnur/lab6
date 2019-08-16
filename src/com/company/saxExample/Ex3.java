package com.company.saxExample;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class Ex3 {
    private static boolean isFound;
    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            parser.parse("src/com/company/SaxExample/xmlForEx3.xml", new XMLHandler3("root"));
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println(e);
        }
    }

    private static class XMLHandler3 extends DefaultHandler {
        private String searchElem;
        private boolean isEntered;

        public XMLHandler3(String searchElem) {
            this.searchElem = searchElem;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals(searchElem)) {
                isEntered = true;
                isFound = true;
            }

            if (isEntered) {
                System.out.println(qName);
                for (int i = 0; i < attributes.getLength(); i++) {
                    System.out.printf("    %s: %s", attributes.getQName(i), attributes.getValue(i));
                    if (i == attributes.getLength()-1) System.out.println();
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals(searchElem)) {
                isEntered = false;
            }
        }
    }
}

