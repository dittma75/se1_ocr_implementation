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
 * @author g_ric_000
 */
public class Runway extends Path 
{
    private final int elevation;
    private final float heading;
    private Node threshold;
    
    public Runway(int elevation, float heading, String name)
    {
        super(name);
        this.elevation = elevation;
        this.heading = heading;
    }
    
    public int getElevation()
    {
        return elevation;
    }
    
    public float getHeading()
    {
        return heading;
    }
    
    public Node getThreshold()
    {
        return threshold;
    }
    
    public void setThreshold(Node threshold)
    {
        this.threshold = threshold;
    }
    
    public boolean hasThreshold()
    {
        return threshold != null;
    }
    
    @Override
    public String toString()
    {
        return "" + heading;
    }
    
}
