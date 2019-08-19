package com.company.dom;

import com.company.saxExample.SaxExample;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dom {
    private static List<Employee> employeeList = new ArrayList<>();

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("src\\com\\company\\dom\\employees.xml");
//            Document document = builder.parse("E:\\NEW\\Java\\Дататех\\LABs\\Lab6\\__FILES_FOR_LABS\\demo.svg");

            NodeList employeeElements = document.getDocumentElement().getElementsByTagName("employee");

            for (int i = 0; i < employeeElements.getLength(); i++) {
                Node employee = employeeElements.item(i);
                NamedNodeMap attributes = employee.getAttributes();
                employeeList.add(new Employee(attributes.getNamedItem("name").getNodeValue(), attributes.getNamedItem("job").getNodeValue()));
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println(e);
        }

        for (Employee i: employeeList) {
            System.out.println(i);
        }
    }
}
