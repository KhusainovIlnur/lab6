package com.company;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
import java.util.TreeMap;


public class StaxDemo {

    List<String> bus_stop = new ArrayList<>();
    List<String> streets = new ArrayList<>();

    Map<String, StreetData> streetsMap = new TreeMap();

    // readStaxEvent высокоуровненвое API
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

    // readStaxStream низкоуровневое API
    void readStaxStream(String filename) {
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

            XMLStreamReader sReader = xmlInputFactory.createXMLStreamReader(new FileReader(filename));

            boolean flag1 = false, flag2 = false;
            boolean way = false, highway = false, name = false;

            String streetName, roadType = "";

            // XMLEventReader создаёт временные объекты XMLEvent по ходу чтения
            while (sReader.hasNext()) {
                //eReader.peek() - подсмотреть следущее событие без перехода на него

                int code = sReader.next();
                // следующее событие при чтении XML

                switch(code) {
                    case XMLEvent.START_ELEMENT:

                        if (sReader.getLocalName().equals("tag")) {
                            if (sReader.getAttributeLocalName(0).equals("k") && sReader.getAttributeValue(0).equals("highway") &&
                                    sReader.getAttributeLocalName(1).equals("v") && sReader.getAttributeValue(1).equals("bus_stop")) {
//                                System.out.print(sReader.getLocalName() + " " + sReader.getAttributeValue(1)+ " ");
                                flag1 = true;
                            }
                        }
                        if (flag1) {
                            if (sReader.getAttributeLocalName(0).equals("k") && sReader.getAttributeValue(0).equals("name")) {
//                                System.out.println(sReader.getAttributeValue(1));
                                bus_stop.add(sReader.getAttributeValue(1));
                                flag2 = true;
                            }
                        }
                        if (sReader.getLocalName().equals("way")) {way = true;}
                        if (way && sReader.getAttributeLocalName(0).equals("k") && sReader.getAttributeValue(0).equals("highway") && sReader.getAttributeLocalName(1).equals("v")) {
                            highway = true;
                            roadType = sReader.getAttributeValue(1);
                        }
                        if (highway && sReader.getAttributeLocalName(0).equals("k") && sReader.getAttributeValue(0).equals("name")) {
                            streetName = sReader.getAttributeValue(1);
                            if (streetsMap.containsKey(streetName)) {
                                streetsMap.get(streetName).countIncrement();
                                streetsMap.get(streetName).addRoadType(roadType);
                            }
                            else {
                                StreetData streetData = new StreetData(streetName, roadType);
                                streetsMap.put(streetName, streetData);
                            }
                            System.out.println(streetName);
                            streets.add(streetName);
                            name = true;
                        }



                        /*int ac = sReader.getAttributeCount();
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
                        }*/
                        break;
                    case XMLEvent.END_ELEMENT:
                        if (flag1 && flag2) {
                            flag1 = false;
                            flag2 = false;
                        }

                        if (way && highway && name) {
                            way = false;
                            highway = false;
                            name = false;
                        }

                        if (sReader.getLocalName().equals("way")) {
                            way = false;
                            highway = false;
                            name = false;
                        }

                        /*System.out.printf("</%s> end \n",
                                sReader.getLocalName());*/
                        break;
                }
                if (sReader.isCharacters()) {
                    /*if (!sReader.isWhiteSpace()) {
                        System.out.println("["+sReader.getText()+"]");
                    }*/
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

    public void tablePrint() {
        for (Map.Entry<String, StreetData> entry: streetsMap.entrySet()) {
            System.out.println(entry.getValue());
        }
    }
}
