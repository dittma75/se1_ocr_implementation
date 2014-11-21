/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package faa_ocr.xml_parser;
import faa_ocr.ADTs.*;
import java.io.*;

/**
 *
 * @author g_ric_000
 */
public class AirportToXML {
    private String xml_string;
    
    public AirportToXML()
    {
        xml_string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    }
    
    /**
     * @param airport
     * @return the path of the newly created .xml file
     */
    public String convertToXml(Airport airport)
    {
        xml_string += "<airport>\n" +
            "<location>" + airport.getLocation() + "</location>\n" +
            "<name>" + airport.getName() + "</name>\n" +
            "<variation>" + airport.getVariation() + "</variation>\n";
        
        sortPaths(airport); // take the loop from sortPaths and put it here to send each Path to be redirected individually
        xml_string += "</airport>";
        return writeToFile(airport.getFilePath());
    }
    
    /*
     * take all of the Path objects and separate them by if they are an instance
     * of Runway or Taxiway and send them to their respective XML conversion
     */
    private void sortPaths(Airport airport)
    {
        for(int i = 0; i < airport.numPaths(); i++) {
            Path currPath = airport.getPath(i);
            xml_string += "<path>" + "\n";
            if(currPath instanceof Runway) {
                runwayToXml((Runway)currPath);
                //we know that currPath is an instance of Runway so cast it 
                //as one when we pass it to runwayToXml(Runway runway)
            } else if(currPath instanceof Taxiway) {
                taxiwayToXml((Taxiway)currPath);
                //we know that currPath is an instance of Taxiway so cast it
                //as one when we pass it to taxiwayToXml(Taxiway taxiway)
            } else {
                //do nothing
                //in the future there may be a need for a
                //different extension of Path
            }
        xml_string += "</path>\n";
        }  
    }
    
    /*
     *
     * @param
     */
    private void runwayToXml(Runway runway)
    {
        xml_string += "<path_name>" + runway.getName() + "</path_name>\n";
        xml_string += "<path_type>" + "runway" + "</path_type>\n";
        xml_string += "<heading>" + runway.getHeading() + "</heading>\n";
        xml_string += "<elevation>" + runway.getElevation() + "</elevation>\n";
        xml_string += "<coordinates>\n";
        //there will always be two nodes in a Runway, the start and the end
        xml_string += "<start>\n";
        Node startNode = runway.getPathNode(0);
        xml_string += "<longitude>" + startNode.getLong() + "</longitude>\n";
        xml_string += "<latitude>" + startNode.getLat() + "</latitude>\n";
        xml_string += "</start>\n";
        xml_string += "<end>\n";
        Node endNode = runway.getPathNode(1);
        xml_string += "<longitude>" + endNode.getLong() + "</longitude>\n";
        xml_string += "<latitude>" + endNode.getLat() + "</latitude>\n";
        xml_string += "</end>\n";
        if(runway.hasThreshold()) {
            Node threshold = runway.getThreshold();
            xml_string += "<threshold>\n";
            xml_string += "<longitude>" + threshold.getLong() + "</longitude>\n";
            xml_string += "<latitude>" + threshold.getLat() + "</latitude>\n";
            xml_string += "</threshold>\n";
        }
        if(runway.getNumIntNodes() != 0) {
            for(int i = 0; i < runway.getNumIntNodes(); i++) {
                //any given runway may have 0 or more intersection nodes
                Node intersection = runway.getIntNode(i);
                xml_string += "<intersection>\n";
                xml_string += "<longitude>" + intersection.getLong() + "</longitude>\n";
                xml_string += "<latitude>" + intersection.getLat() + "</latitude>\n";
                xml_string += "</intersection>\n";
            }
        }
        xml_string += "</coordinates>\n";
    }
    
    /*
     *
     * @param
     */
    private void taxiwayToXml(Taxiway taxiway)
    {
        xml_string += "<path_name>" + taxiway.getName() + "</path_name>\n";
        xml_string += "<path_type>" + "taxiway" + "</path_type>\n";
        xml_string += "<coordinates>\n";
        for(int i = 0; i < taxiway.getNumPathNodes(); i++) {
            Node currNode = taxiway.getPathNode(i);
            xml_string += "<node>\n";
            xml_string += "<longitude>" + currNode.getLong() + "</longitude>\n";
            xml_string += "<latitude>" + currNode.getLat() + "</latitude>\n";
            xml_string += "</node>\n";
        }
        for(int i = 0; i < taxiway.getNumIntNodes(); i++) {
            Node intersection = taxiway.getIntNode(i);
            xml_string += "<intersection>\n";
            xml_string += "<longitude>" + intersection.getLong() + "</longitude>\n";
            xml_string += "<latitude>" + intersection.getLat() + "</latitude>\n";
            xml_string += "</intersection>\n";
        }
        xml_string += "</coordinates>\n";
    }
    
    private String writeToFile(String filePath)
    {
        String xmlPath = filePath.split("\\.")[0];
        xmlPath += ".xml";
        //remove the extensions from the file path so that we
        //can use the same name with a .xml extension for the new file
        
        try {
            //create the new .xml file and write the formatted String to it
            File file = new File(xmlPath);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(xml_string);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlPath;        
    }
    
}
