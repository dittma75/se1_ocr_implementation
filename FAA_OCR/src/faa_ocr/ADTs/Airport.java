/*
 * Purpose: Software Engineering I: FAA OCR Project 
 * Status: Complete and thoroughly tested
 * Last update: 12/02/14
 * Submitted: 
 * Comment: Airport ADT
 * @author: Greg Richards, Kevin Dittmar
 * @version: 2014.11.20
 * 
 */

package faa_ocr.ADTs;
import java.util.*;

/*
 * The Airport class is an abstrat data type that represents the entire Airport
 * diagram pdf.  It contains all of the textual identification information that
 * makes each airport diagram unique, as well as all of the nodes that are to be
 * mapped onto the xml file.
 */
public class Airport {
    private final String pdf_file_path;
    private String name;
    private String location;
    private String abbreviation;
    private float variation;
    private ArrayList<Path> paths;
    private final int PIXELS_PER_DEGREE_LAT;
    private final int PIXELS_PER_DEGREE_LONG;
    
    public Airport (String pdf_file_path)
    {
        this.pdf_file_path = pdf_file_path;
        PIXELS_PER_DEGREE_LAT = PDFToImage.findLatitudeScale(pdf_file_path);
        PIXELS_PER_DEGREE_LONG = PDFToImage.findLongitudeScale(pdf_file_path);
    }
    
    /**
     * Get the file path of the airport diagram PDF file.
     * @return the airport diagram file path as a String.
     */
    public String getFilePath()
    {
        return pdf_file_path;
    }
    
    /**
     * Get the name of the airport.
     * @return the name of the airport as a String.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Set the name of the airport to be the given String.
     * @param name is the String representation of the Airport's name.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Get the name of the location of the airport.
     * @return the location of the airport as a String.
     */
    public String getLocation()
    {
        return location;
    }
    
    /**
     * Set the location of the airport based on the given location.
     * @param location is the String representation of the airport location.
     */
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    /**
     * Get the abbreviation for the airport.
     * @return the abbreviation of the airport as a String.
     */
    public String getAbbreviation()
    {
        return abbreviation;
    }
    
    /**
     * Set the abbreviation of the airport.
     * @param abbreviation is the airport abbreviation as a String.
     */
    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }
    
    /**
     * Get the value that represents that variation from compass north and
     * true north.
     * @return the variation from true north.
     */
    public float getVariation()
    {
        return variation;
    }
    
    /**
     * Set the variation from true north.
     * @param variation is the variation from true north represented as a
     * float.
     */
    public void setVariation(float variation)
    {
        this.variation = variation;
    }
    /*
     * Get a Path object located at a specified index number
     * @param the int index value of the Path you wish to retrieve
     * @return the Path object at index i
     */
    public Path getPath(int i)
    {
        return paths.get(i);
    }
    
    /*
     * get the number of Path objects in this Airport
     * @return the int value of how many Paths are in the collection paths 
     */
    public int numPaths()
    {
        return paths.size();
    }
    
    /*
     * Add a Path object to the collection paths in the instance of Airport
     * @param the Path object to be added to the collection
     */
    public void addPath(Path path)
    {
        paths.add(path);
    }
    
    /**
     * Get the String representation of an Airport object, which is the name
     * of the airport.
     * @return the String representation of the airport.
     */
    public String toString()
    {
        return name;
    }
}
