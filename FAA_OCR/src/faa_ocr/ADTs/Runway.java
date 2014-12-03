/*
 * Purpose: Software Engineering I: FAA OCR Project 
 * Status: Complete and thoroughly tested
 * Last update: 12/02/14
 * Submitted:
 * Comment: Runway ADT
 * @author: Greg Richards
 * @version: 2014.11.20
 * 
 */

package faa_ocr.ADTs;

/**
 *
 */
public class Runway extends Path 
{
    private final int elevation;
    private final float heading;
    private Node threshold;
    
    /**
     * 
     * @param elevation
     * @param heading
     * @param name 
     */
    public Runway(int elevation, float heading, String name)
    {
        super(name);
        this.elevation = elevation;
        this.heading = heading;
    }
    
    /**
     * 
     * @return 
     */
    public int getElevation()
    {
        return elevation;
    }
    
    /**
     * 
     * @return 
     */
    public float getHeading()
    {
        return heading;
    }
    
    /**
     * 
     * @return 
     */
    public Node getThreshold()
    {
        return threshold;
    }
    
    /**
     * 
     * @param threshold 
     */
    public void setThreshold(Node threshold)
    {
        this.threshold = threshold;
    }
    
    /**
     * 
     * @return 
     */
    public boolean hasThreshold()
    {
        return threshold != null;
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        String runwayString = "Path Type: Runway\n";
        return runwayString;
    }
    
}
