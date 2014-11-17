/*
 * The Airport class is an abstrat data type that represents the entire Airport
 * diagram pdf.  It contains all of the textual identification information that
 * makes each airport diagram unique, as well as all of the nodes that are to be
 * mapped onto the xml file.
 */
package faa_ocr.ADTs;

/**
 *
 * @author g_ric_000
 * @author Kevin Dittmar (documentation)
 */
public class Airport {
    private final String pdf_file_path;
    private String name;
    private String location;
    private String abbreviation;
    private float variation;
    
    public Airport (String pdf_file_path)
    {
        this.pdf_file_path = pdf_file_path;
    }
    
    /**
     * Get the file path of the airport diagram PDF file.
     * @return the airport diagram file path as a String.
     */
    public String getPath()
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
