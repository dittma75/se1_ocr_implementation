/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package faa_ocr.ADTs;

/**
 *
 * @author g_ric_000
 */
public class Node {
    private final float longitude;
    private final float latitude;
    
    public Node(float longi, float lat)
    {
        longitude = longi;
        latitude = lat;
    }
    
    public float getLong()
    {
            return longitude;
    }
    
    public float getLat()
    {
        return latitude;
    }
    
    public String toString()
    {
        return ("Longitude: " + longitude + " Latitude: " + latitude);
    }
}
