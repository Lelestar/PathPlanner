package com.ap4b.pathplanner.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The RoadNetwork class manages the roads and points from an XML file.
 */
public class RoadNetwork {
    private HashMap<String, Road> routes = new HashMap<>();
    private HashMap<Integer, Point> points = new HashMap<>();
    private String xmlFileName;
    private String imageFileName; // Image filename read from the XML
    private int connectionCount; // Number of possible connections between two points

    /**
     * Parses the XML file and initializes the road network.
     * @param xmlFile the XML file path
     */
    public void parseXml(String xmlFile) {
        this.xmlFileName = xmlFile;
        routes.clear();
        points.clear();
        connectionCount = 0;

        try {
            // Load the XML file
            URL resourceUrl = getClass().getResource(xmlFile);
            if (resourceUrl == null) {
                throw new IOException("Resource URL not found for: " + xmlFile);
            }
            URI ressourceURI = resourceUrl.toURI();

            // Parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(ressourceURI));

            Element root = document.getDocumentElement();
            imageFileName = root.getAttribute("src");

            // Process points
            NodeList pointElements = root.getElementsByTagName("points");
            for (int i = 0; i < pointElements.getLength(); i++) {
                NodeList pointList = pointElements.item(i).getChildNodes();
                for (int j = 0; j < pointList.getLength(); j++) {
                    if ("point".equals(pointList.item(j).getNodeName())) {
                        int num = Integer.parseInt(pointList.item(j).getAttributes().getNamedItem("num").getNodeValue());
                        int x = (int) Double.parseDouble(pointList.item(j).getAttributes().getNamedItem("x").getNodeValue());
                        int y = (int) Double.parseDouble(pointList.item(j).getAttributes().getNamedItem("y").getNodeValue());
                        points.put(num, new Point(x, y));
                    }
                }
            }

            // Process roads
            NodeList roadElements = root.getElementsByTagName("rues");
            for (int i = 0; i < roadElements.getLength(); i++) {
                NodeList roadList = roadElements.item(i).getChildNodes();
                for (int j = 0; j < roadList.getLength(); j++) {
                    if ("rue".equals(roadList.item(j).getNodeName())) {
                        String roadName = roadList.item(j).getAttributes().getNamedItem("nom").getNodeValue();
                        roadName = Character.toUpperCase(roadName.charAt(0)) + roadName.substring(1);
                        int direction = Integer.parseInt(roadList.item(j).getAttributes().getNamedItem("sens").getNodeValue());
                        Road road = new Road(direction);

                        NodeList pointNodes = roadList.item(j).getChildNodes();
                        boolean firstPoint = true;
                        for (int k = 0; k < pointNodes.getLength(); k++) {
                            if ("pt".equals(pointNodes.item(k).getNodeName())) {
                                road.addPoint(Integer.parseInt(pointNodes.item(k).getAttributes().getNamedItem("num").getNodeValue()));
                                if (!firstPoint) {
                                    connectionCount++;
                                }
                                firstPoint = false;
                            }
                        }
                        routes.put(roadName, road);
                    }
                }
            }

        } catch (ParserConfigurationException pce) {
            System.err.println("Parser configuration error");
        } catch (SAXException se) {
            System.err.println("Parsing error");
        } catch (IOException ie) {
            System.err.println("IO error");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Point getPoint(Integer num) {
        return points.get(num);
    }

    public Set<Integer> getPointKeys() {
        return points.keySet();
    }

    public int getPointCount() {
        return points.size();
    }

    public Set getPointsList() {
        return points.keySet();
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public Road getRoad(String name) {
        return routes.get(name);
    }

    public Set<String> getRoadNames() {
        return routes.keySet();
    }

    public int getRoadCount() {
        return routes.size();
    }

    public Set getRoadsList() {
        return routes.keySet();
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }
}