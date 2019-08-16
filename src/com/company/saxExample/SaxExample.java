package com.company.saxExample;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaxExample {
    private static List<Employee> employeeList = new ArrayList<>();

    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            parser.parse("src/com/company/SaxExample/employees2.xml", new XMLHandler2());
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println(e);
        }

        for (Employee i: employeeList) {
            System.out.println(i);
        }
    }

    private static class XMLHandler extends DefaultHandler{
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("employee")) {
                String name = attributes.getValue("name");
                String job = attributes.getValue("job");
                employeeList.add(new Employee(name, job));
            }
        }
    }
    private static class XMLHandler2 extends DefaultHandler{
        private String lastElementName, name, job;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            lastElementName = qName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String charactersString = String.valueOf(ch, start, length);
            charactersString = charactersString.replace("\n", "").trim();

            if (charactersString.isEmpty()) return;
            if (lastElementName.equals("name")) name = charactersString;
            if (lastElementName.equals("job")) job = charactersString;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ((name != null && !name.isEmpty()) && (job != null && !job.isEmpty())) {
                employeeList.add(new Employee(name, job));
                name = null;
                job = null;
            }
        }
    }


}
