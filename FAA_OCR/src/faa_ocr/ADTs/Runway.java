/**
 * Purpose: Software Engineering I: FAA OCR Project 
 * Status: Complete and thoroughly tested
 * Last update: 12/02/14
 * Submitted:
 * Comment: Runway ADT
 * @author: Greg Richards
 * @version: 2014.11.20
 */

package faa_ocr.ADTs;

/**
 * The Runway class extends the abstract Path class and inherits its fields and
 * methods as well as adding additional functionality that includes the elevation
 * of the runway, the heading of the runway, and the displacement threshold. A 
 * Runway is created when one is discovered through the image parsing module, and
 * the rest of the information is gathered through the text parsing module.
 */
public class Runway extends Path 
{
    private final int elevation;
    private final float heading;
    private Node threshold;
    
    /**
     * constructor for the Runway class
     * 
     * @param elevation int elevation of the runway 
     * @param heading float heading of the runway
     * @param name String name of the runway
     */
    public Runway(int elevation, float heading, String name)
    {
        super(name);
        this.elevation = elevation;
        this.heading = heading;
    }
    
    /**
     * get the elevation of the runway
     * 
     * @return int value equal to the elevation of the runway
     */
    public int getElevation()
    {
        return elevation;
    }
    
    /**
     * get the heading of the runway
     * 
     * @return float value equal to the heading of the runway
     */
    public float getHeading()
    {
        return heading;
    }
    
    /**
     * get the threshold Node of the runway if a particular instance has one 
     * 
     * @return threshold Node if one exists
     */
    public Node getThreshold()
    {
        return threshold;
    }
    
    /**
     * set a particular Node to be the displacement threshold
     * 
     * @param threshold 
     */
    public void setThreshold(Node threshold)
    {
        this.threshold = threshold;
    }
    
    /**
     * determine if an instance of Runway has a displacement threshold or not
     * 
     * @return true if a threshold node exists, false otherwise
     */
    public boolean hasThreshold()
    {
        return threshold != null;
    }
    
    /**
     * Get the String representation of an Runway object, including all of the
     * current values of the object's data fields in an organized manner.
     * 
     * @return String representation of a Runway object
     */
    @Override
    public String toString()
    {
        String runwayString = "Path Type: Runway\n";
        runwayString += super.toString();
        runwayString += "Elevation: " + elevation + "\n"
                        + "Heading: " + heading + "\n";
        if(this.hasThreshold()) {
            runwayString += this.getThreshold().toString();
        } else {
            //do nothing 
        }
        return runwayString;
    }
    
} //end Runway
