
package faa_ocr.ADTs;

/**
 *
 * @author g_ric_000
 */
public class Runway extends Path 
{
    private int elevation;
    private float heading;
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
    
    public String toString()
    {
        return "" + heading;
    }
    
}
