package com.company;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;


public class StaxDemo {


    void readStaxEvent(String filename) {
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

            XMLEventReader eReader = xmlInputFactory.createXMLEventReader(new FileReader(filename));

            // XMLEventReader создаёт временные объекты XMLEvent по ходу чтения
            while (eReader.hasNext()) {
                //eReader.peek() - подсмотреть следущее событие без перехода на него
                XMLEvent e =
                        eReader.nextEvent();
                //eReader.nextTag();

                // следующее событие при чтении XML

                switch(e.getEventType()) {
                    case XMLEvent.START_ELEMENT:
                        StartElement se =
                                e.asStartElement();
                        System.out.printf(
                                "<%s> started \n",
                                se.getName().getLocalPart());
                        break;
                    case XMLEvent.END_ELEMENT:
                        EndElement ee =
                                e.asEndElement();
                        System.out.printf(
                                "</%s> end \n",
                                ee.getName().getLocalPart());

                        break;
                }

                if (e.isCharacters()) {
                    if (e.asCharacters().isCData()) {
                        System.out.println("(CDATA)");
                    }
                    e.asCharacters().getData();
                    if (!e.asCharacters().isIgnorableWhiteSpace() ) {
                        System.out.println(
                                "["+e.asCharacters().getData()+"]"
                        );
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        }
    }

    void readStaxStream(String filename) {
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

            XMLStreamReader sReader = xmlInputFactory.createXMLStreamReader(new FileReader(filename));

            // XMLEventReader создаёт временные объекты XMLEvent по ходу чтения
            while (sReader.hasNext()) {
                //eReader.peek() - подсмотреть следущее событие без перехода на него

                int code = sReader.next();
                // следующее событие при чтении XML

                switch(code) {
                    case XMLEvent.START_ELEMENT:

                        System.out.printf(
                                "<%s> started ",
                                sReader.getLocalName());

                        int ac = sReader.getAttributeCount();
                        Map<String,String> attrMap
                                = new LinkedHashMap<String, String>();
                        for (int i=0;i<ac;i++) {
                            attrMap.put(
                                    sReader.getAttributeLocalName(i)+"/"+
                                            sReader.getAttributeType(i)+"/",
                                    sReader.getAttributeValue(i));
                        }

                        System.out.println(attrMap);

                        if (sReader.getLocalName().equals("item")) {
                            System.out.println(
                                    "→ → → → → item with price="
                                            +sReader.getAttributeValue("", "price"));
                        }

                        if (sReader.getLocalName()
                                .equals("material")) {
                            System.out.println("inner text="+
                                    sReader.getElementText()
                            );
                            // считывает до конца тега и берет текст
                        }
                        break;
                    case XMLEvent.END_ELEMENT:
                        System.out.printf("</%s> end \n",
                                sReader.getLocalName());
                        break;
                }
                if (sReader.isCharacters()) {
                    if (!sReader.isWhiteSpace()) {
                        System.out.println("["+sReader.getText()+"]");
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        }
    }


    // простая проверка схемы
    void schemaCheckerTest() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("E:\\NEW\\Java\\Дататех\\Lecture\\Day12\\SaxSample\\osm.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File("E:\\NEW\\Java\\Дататех\\Lecture\\Day12\\SaxSample\\UfaCenterSmall.xml")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
/*
    void writeStaxExample() {
        XMLOutputFactory xof
                = XMLOutputFactory.newFactory();

        try {
            XMLStreamWriter sw2
                    = xof.createXMLStreamWriter(System.err);
            XMLStreamWriter sw=new IndentingXMLStreamWriter(sw2);

            sw.writeStartDocument();
            sw.writeStartElement("hello");
            sw.writeAttribute("a", "2");
            sw.writeAttribute("b", "3&<>&");
            sw.writeCharacters("\nsome text \"\"<b> ");
            sw.writeEmptyElement("emptyElement");
            sw.writeEndElement();
            sw.writeEndDocument();

            sw.close();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        }



    }*/
}
