package faa_ocr.kml_parser;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This class is part of a Software Engineering course project,
 * assigned by Adrian Rusu of the Computer Science department at
 * Rowan University, and introduced to him by the Federal Aviation Administration.
 * Our team consists of Kevin Dittmar, Joseph Kvedaras, Jeffrey Koellner,
 * James O'Donnell, Gregory Richards, and Scott Ritchie.
 * 
 * The XMLtoKML class uses the XML parser DocumentBuilderFactory to
 * go through an XML representation of an airport diagram in order to
 * convert its contents to KML format.
 * @author Scott Ritchie
 * @author James O'Donnell
 */
public class XMLtoKML
{
    public XMLtoKML()
    {
            //constructor
    }

    /**
     * This method takes the XML representation of an airport, creates
     * a KML file of the same name, and converts all XML file
     * information to KML format.
     * @param xml_file Airport diagram in XML format
     * @return String path of KML file
     */
    public String writeKML(File xml_file)
    {
        String kml_string = "";
        String kml_path = "";
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                            .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xml_doc = dBuilder.parse(xml_file);
            // Normalize the document
            xml_doc.getDocumentElement().normalize();
            //Add header to main string
            kml_string += getHeader(xml_doc);
            //Add line styles to main string
            kml_string += getLineStyle();
            //List of pathways
            NodeList path_list = xml_doc.getElementsByTagName("path");
            //Add body to main string
            kml_string += getBody(path_list, xml_doc);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            // Define KML file path String
            kml_path = xml_file.getPath().replace(".xml", ".kml");
            File kml_file = new File(kml_path);

            //Create and write kml_string to file
            kml_file.createNewFile();
            
            BufferedWriter output = new BufferedWriter(
                new FileWriter(kml_file)
            );
            
            output.write(kml_string);
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return kml_path;
    }

    /**
     * Adds airport information including name, location,
     *  and variation to the string.
     * @param xml_doc to extract XML tag info
     * @return String representation of the KML file's header info
     */
    private String getHeader(Document xml_doc)
    {
            String header_string ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            header_string += "\n<kml xmlns=\"http://www.opengis.net/kml/2.2\">"
            + "\n  <Document>"
            + "\n\t<name>"        //Airport name
            + xml_doc.getElementsByTagName("name").item(0).getTextContent()
            + "</name>"
            + "\n\t<description>" //Other info
            + "\n\t Location: "
            + xml_doc.getElementsByTagName("location").item(0).getTextContent()
            + "\n\t Variation: "
            + xml_doc.getElementsByTagName("variation").item(0).getTextContent() 
            + "\n\t</description>";
            return header_string;
    }

    /**
     * Stores all line style information to a string
     * @return String representation of the KML file's line styles
     */
    private String getLineStyle()
    {
            //Local LineStyle string to be added to kml_string
            String line_style = "";
            line_style += "\n\t<Style id=\"runway\">" //Style of Runway path
            + "\n\t  <LineStyle>"             
            + "\n\t\t<color>501400FF</color>" //Red Line
            + "\n\t\t<width>10</width>"
            + "\n\t  </LineStyle>"
            + "\n\t</Style>"
            + "\n\t<Style id=\"taxiway\">"    //Style of Taxiway Path
            + "\n\t  <LineStyle>"           
            + "\n\t\t<color>50000000</color>" //Black line
            + "\n\t\t<width>8</width>"
            + "\n\t  </LineStyle>"
            + "\n\t</Style>";
            return line_style;
    }

    /**
     * Stores all airport path information to a string
     * @param path_list to extract XML path info
 * @param xml_doc 
     * @return String representation of the KML file's body
     */
    private String getBody(NodeList path_list, Document xml_doc)
    {
            //Local Body string to be added to kml_string
            String body_string = "";

            //Loop through each path and add all it's information to the string
            for (int path = 0; path < path_list.getLength(); path++)
            {
                    Node node = path_list.item(path);

                    //Check if Node is Element
                    if (node.getNodeType() == Node.ELEMENT_NODE)
                    {
                            //Convert Node to Element
                            Element element = (Element) node;
                            //Store path type for LineStyle distinction
                            String type = element.getElementsByTagName("path_type").item(0)
                                            .getTextContent();

                            //Begin adding path to string
                            body_string += "\n\t<Placemark>" // Runway name, heading,
                                                                                            // elevation
                                            + "\n\t  <name>"
                                            + element.getElementsByTagName("path_name").item(0)
                                                            .getTextContent()
                                            + "</name>"
                                            + "\n\t  <description>"
                                            + "\n\t   Path type: "
                                            + type;

                            //Determine whether the path has heading/elevation info
                            // and add the correct LineStyle Url
                            if (type.equals("runway"))
                            {
                                    body_string += "\n\t   Heading: "
                                            + element.getElementsByTagName("heading").item(0)
                                                            .getTextContent()
                                            + "\n\t   Elevation: "
                                            + element.getElementsByTagName("elevation").item(0)
                                                            .getTextContent()
                                            + "\n\t  </description>"
                                            + "\n\t  <styleUrl>#runway</styleUrl>";
                            }
                            else
                            {
                                    body_string += "\n\t  </description>"
                                            + "\n\t  <styleUrl>#taxiway</styleUrl>";
                            }

                            //Add coordinates of path to a LingString
                            body_string += "\n\t  <LineString>"
                                            + "\n\t\t<tessellate>1</tessellate>"
                                            + "\n\t\t<coordinates>"
                                            //Obtain all coordinates
                    + getCoords(element)
                                    + "\n\t\t</coordinates>" + "\n\t  </LineString>"
                                            + "\n\t</Placemark>";
                    }
            }
            //Finish KML file String
            body_string += "\n  </Document>" + "\n</kml>";
            return body_string;
    }

    /**
     * Gets all the coordinates in a given path.
     * Treats Nodes, Intersections, and Displaced Thresholds as normal points.
     * @param element
     * @return String of coordinates
     */
    private String getCoords(Element element)
    {
        //Create list of <coordinates> tags
        NodeList coord_list = element.getElementsByTagName("coordinates");
        //String to append and add to kml_string
        String coord_string = "";

        //Loop through every <coordinates> tag.
        //Each one corresponds to a different <path> tag.
        for (int coord = 0; coord < coord_list.getLength(); coord++)
        {
            //Make the first <coordinates> tag a Node
            Node coord_node = coord_list.item(0);

            //Check if the Node is an element
            if (coord_node.getNodeType() == Node.ELEMENT_NODE) 
            {
                //Convert your Node to an Element
                Element coord_element = (Element) coord_node;
                /* Create NodeLists for all types of coordinate nodes
                 * that can be present in a given <path>
                 */
                NodeList node_list = coord_element.getElementsByTagName("node");
                NodeList intersections = coord_element.getElementsByTagName("intersection");
                NodeList thresholds = coord_element.getElementsByTagName("threshold");

                /* Now, for all three types of path nodes, loop through and
                 * add every node's coordinates to the local coord_string
                 */

                Node node = node_list.item(0);
                    //Check that the Node is an Element
                    if (node.getNodeType() == Node.ELEMENT_NODE) 
                    {
                        //Convert to Element
                        Element node_element = (Element) node;
                        //Add coordinates to String
                        coord_string += "\n\t\t " +
                            node_element.getElementsByTagName("longitude")
                                .item(0)
                                .getTextContent() + "," +
                            node_element.getElementsByTagName("latitude")
                                .item(0)
                                .getTextContent() + ",0"; // Height 0
                    }
                
                    //Check if intersections exist in path
                for(int node_index = 0; node_index < intersections.getLength(); node_index++)
                {
                    //Obtain an "intersection" Node from its NodeList
                    Node inter = intersections.item(node_index);

                    //Check that the Node is an Element
                    if (inter.getNodeType() == Node.ELEMENT_NODE)
                    {
                        //Convert to Element
                        Element node_element = (Element) inter;

                        //Add coordinates to String
                        coord_string += "\n\t\t " + 
                            node_element.getElementsByTagName("longitude")
                                        .item(0)
                                        .getTextContent() + "," +
                            node_element.getElementsByTagName("latitude")
                                        .item(0)
                                        .getTextContent() + ",0"; // Height 0
                    }
                }
                    
                    
                    node = node_list.item(1);
                    //Check that the Node is an Element
                    if (node.getNodeType() == Node.ELEMENT_NODE) 
                    {
                        //Convert to Element
                        Element node_element = (Element) node;
                        //Add coordinates to String
                        coord_string += "\n\t\t " +
                            node_element.getElementsByTagName("longitude")
                                .item(0)
                                .getTextContent() + "," +
                            node_element.getElementsByTagName("latitude")
                                .item(0)
                                .getTextContent() + ",0"; // Height 0
                    }
                
                //Check if displaced thresholds exist in path
                for(int node_index = 0; node_index < thresholds.getLength(); node_index++)
                {
                    //Obtain a "threshold" Node from its NodeList
                    Node thresh = thresholds.item(node_index);

                    //Check that the Node is an Element
                    if (thresh.getNodeType() == Node.ELEMENT_NODE)
                    {
                        //Convert to Element
                        Element node_element = (Element) thresh;
                        //Add coordinates to String
                        coord_string += "\n\t\t " +
                            node_element.getElementsByTagName("longitude")
                                        .item(0)
                                        .getTextContent() + "," +
                            node_element.getElementsByTagName("latitude")
                                        .item(0)
                                        .getTextContent() + ",0"; // Height 0
                    }
                }
            }
        }
        return coord_string;
    }
}