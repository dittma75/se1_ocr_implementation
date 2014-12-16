/**
 * Purpose: Software Engineering I: FAA OCR Project 
 * Status: Complete and thoroughly tested
 * Last update: 12/02/14
 * Submitted:
 * Comment: AirportToXML
 * @author: Greg Richards
 * @version: 2014.11.20
 */
package faa_ocr.xml_parser;
import faa_ocr.ADTs.*;
import java.io.*;

/**
 * The AirportToXML class takes an Airport object and takes all of the
 * information inside it to create an XML formatted representation of that
 * object.  That representation is then written to a .xml file with the same
 * header as the .pdf file that was used as the initial input for the program.
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
    	xml_string = "";
        xml_string += "<airport>\n" +
            "\t" + "<location>" + airport.getLocation() + "</location>\n" +
            "\t" + "<name>" + airport.getName() + "</name>\n" +
            "\t" + "<variation>" + airport.getVariation() + "</variation>\n";
        
        for(int i = 0; i < airport.numRunways(); i++) {
            xml_string += "\t" + "<path>\n";
            runwayToXml(airport.getRunway(i));
            xml_string += "\t" + "</path>\n";
        }
        for(int i = 0; i < airport.numTaxiways(); i++) {
            xml_string += "\t" + "<path>\n";
            taxiwayToXml(airport.getTaxiway(i));
            xml_string += "\t" + "</path>\n";
        }
        xml_string += "</airport>";
        return writeToFile(airport.getFilePath());
    }
    
    /*
     * Take all of the information stored in an instance of Runway that we pass
     * as a parameter and add it to the String we will be writing to the .xml
     * file we are to create.  runwayToXml(Runway runway) will be called only by 
     * sortPaths(Airport airport) and will organize all of the information into
     * organized and labeled XML formatted lines.
     *
     * @param runway is the instance of Runway that we are currently converting
     * to XML format.
     */
    private void runwayToXml(Runway runway)
    {
        xml_string += "\t\t" + "<path_type>runway</path_type>\n";
        xml_string += "\t\t" + "<path_name>" + runway.getName() + "</path_name>\n";
        xml_string += "\t\t" + "<heading>" + runway.getHeading() + "</heading>\n";
        xml_string += "\t\t" + "<elevation>" + runway.getElevation() + "</elevation>\n";
        xml_string += "\t\t" + "<coordinates>\n";
        //there will always be two nodes in a Runway, the start and the end
        for(int i = 0; i < runway.getNumPathNodes(); i++) {
            Node currNode = runway.getPathNode(i);
            xml_string += "\t\t\t" + "<node>\n";
            xml_string += "\t\t\t\t" + "<longitude>" + currNode.getLong() + "</longitude>\n";
            xml_string += "\t\t\t\t" + "<latitude>" + currNode.getLat() + "</latitude>\n";
            xml_string += "\t\t\t" + "</node>\n";
        }
        if(runway.hasThreshold()) {
            Node threshold = runway.getThreshold();
            xml_string += "\t\t\t" + "<threshold>\n";
            xml_string += "\t\t\t\t" + "<longitude>" + threshold.getLong() + "</longitude>\n";
            xml_string += "\t\t\t\t" + "<latitude>" + threshold.getLat() + "</latitude>\n";
            xml_string += "\t\t\t" + "</threshold>\n";
        }
        if(runway.getNumIntNodes() != 0) {
            for(int i = 0; i < runway.getNumIntNodes(); i++) {
                //any given runway may have 0 or more intersection nodes
                Node intersection = runway.getIntNode(i);
                xml_string += "\t\t\t" + "<intersection>\n";
                xml_string += "\t\t\t\t" + "<longitude>" + intersection.getLong() + "</longitude>\n";
                xml_string += "\t\t\t\t" + "<latitude>" + intersection.getLat() + "</latitude>\n";
                xml_string += "\t\t\t" + "</intersection>\n";
            }
        }
        xml_string += "\t\t" + "</coordinates>\n";
    }
    
    /**
     * Take all of the information stored in an instance of Taxiway that we pass
     * as a parameter and add it to the String we will be writing to the .xml
     * file we are to create.  taxiwayToXml(Taxiway taxiway) will be called only by 
     * sortPaths(Airport airport) and will organize all of the information into
     * organized and labeled XML formatted lines.
     * 
     * @param taxiway instance of Taxiway that we are currently converting to
     * .xml format
     */
    private void taxiwayToXml(Taxiway taxiway)
    {
        xml_string += "\t\t" + "<path_type>taxiway</path_type>\n";
        xml_string += "\t\t" + "<path_name>" + taxiway.getName() + "</path_name>\n";
        xml_string += "\t\t" + "<coordinates>\n";
        for(int i = 0; i < taxiway.getNumPathNodes(); i++) {
            Node currNode = taxiway.getPathNode(i);
            xml_string += "\t\t\t" + "<node>\n";
            xml_string += "\t\t\t\t" + "<longitude>" + currNode.getLong() + "</longitude>\n";
            xml_string += "\t\t\t\t" + "<latitude>" + currNode.getLat() + "</latitude>\n";
            xml_string += "\t\t\t" + "</node>\n";
        }
        for(int i = 0; i < taxiway.getNumIntNodes(); i++) {
            Node intersection = taxiway.getIntNode(i);
            xml_string += "\t\t\t" + "<intersection>\n";
            xml_string += "\t\t\t\t" + "<longitude>" + intersection.getLong() + "</longitude>\n";
            xml_string += "\t\t\t\t" + "<latitude>" + intersection.getLat() + "</latitude>\n";
            xml_string += "\t\t\t" + "</intersection>\n";
        }
        xml_string += "\t\t" + "</coordinates>\n";
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
} //end AirportToXML
