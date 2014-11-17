
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package faa_ocr.ADTs;
import java.util.*;

/**
 *
 * @author g_ric_000
 */
public abstract class Path {
    String name;
    private ArrayList<Node> paths;
    ArrayList<Node> intersections;
    
    public Path(String name)
    {
        this.name = name;
        paths = new ArrayList<Node>();
        intersections = new ArrayList<Node>();
    }
    
    public String getName()
    {
        return name;   
    }
    
    public void addPathNode(Node newNode)
    {
        paths.add(newNode);
    }
    
    public void addIntNode(Node newNode)
    {
        intersections.add(newNode);
    }
    
    @Override
    public String toString()
    {
        String pathString = (name + "/n" + "Path: ");
        for(Node node : paths) {
            pathString += node.toString();
        }
        pathString += "Intersections: ";
        if( intersections.size() == 0){
            pathString += "This path does not have any intersections.";
        } else {
            for(Node node : intersections) {
            System.out.println(node.toString());
            }
        }
        return pathString;
    }
}
