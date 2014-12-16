/*
 * Purpose: Software Engineering I: FAA OCR Project 
 * Status: Complete and thoroughly tested
 * Last update: 12/02/14
 * Submitted: 
 * Comment: Node ADT
 * @author: Greg Richards
 * @version: 2014.11.20
 */

package faa_ocr.ADTs;

/**
 * Class Node represents a single point in a single Path for which we have
 * calculated its latitude and longitude coordinates based on the location of 
 * its respective pixel on the airport diagram .pdf file that was used as input
 * for the program.
 */
public class Node {
    private final float longitude;
    private final float latitude;
    
    /**
     * Constructor for the Node class
     * 
     * @param longi: is a float value that represents the node's degree of longitude
     * @param lat: is a float value that represents the node's degree of latitude 
     */
    public Node(float longi, float lat)
    {
        longitude = longi; //-180 degrees to 180 degrees
        latitude = lat; //-90 degrees to 90 degrees
    }
    
    
    /**
     * get the node's degree of longitude
     * 
     * @return float value that represents the node's degree of longitude 
     */
    public float getLong()
    {
            return longitude;
    }
    
    /**
     * get the node's degree of latitude 
     * 
     * @return float value that represents the node's degree of latitude
     */
    public float getLat()
    {
        return latitude;
    }
    
    /**
     * get a String representation of all the current values of the data fields
     * within the instance of Node.
     * 
     * @return String representation of the instance
     */
    @Override
    public String toString()
    {
        return ("Longitude: " + longitude + " Latitude: " + latitude);
    }
} //end Node 
