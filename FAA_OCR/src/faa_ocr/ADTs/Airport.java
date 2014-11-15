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
    
    public String getPath()
    {
        return pdf_file_path;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getLocation()
    {
        return location;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    public String getAbbreviation()
    {
        return abbreviation;
    }
    
    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }
    
    public float getVariation()
    {
        return variation;
    }
    
    public void setVariation(float variation)
    {
        this.variation = variation;
    }
    
    public String toString()
    {
        return name;
    }
}
