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
        //remove the extensions from the file path so that we
        //can use the same name with a .xml extension for the new file
        String filePath = airport.getFilePath().split("\\.")[0];
        filePath += ".xml";

        xml_string += "<airport>\n" +
            "<location>" + airport.getLocation() + "</location>\n" +
            "<name>" + airport.getName() + "</name>\n" +
            "<variation>" + airport.getVariation() + "</variation>\n";
        sortPaths(airport);
        xml_string += "</airport>";
        try { 
            File file = new File(filePath);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(xml_string);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;        
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
                runwayToXml(currPath);
            } else if(currPath instanceof Taxiway) {
                taxiwayToXml(currPath);
            } else {
                //do nothing
                //in the future there may be a need for a
                //different extension of Path
            }
        xml_string += "</path>\n";
        }  
    }
    
    private void runwayToXml(Path runway)
    {
        xml_string += "<path_name>" + runway.getName() + "</path_name>\n";
        xml_string += "<path_type>" + "runway" + "</path_type>\n";
        
    }
    
    private void taxiwayToXml(Path taxiway)
    {
        xml_string += "<path_name>" + taxiway.getName() + "</path_name>\n";
        xml_string += "<path_type>" + "taxiway" + "</path_type>\n";
    }
    
}
