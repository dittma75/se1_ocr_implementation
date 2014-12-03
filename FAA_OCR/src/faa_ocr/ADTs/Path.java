/**
 * Purpose: Software Engineering I: FAA OCR Project 
 * Status: Complete and thoroughly tested
 * Last update: 12/02/14
 * Submitted: 
 * Comment: Path ADT
 * @author: Greg Richards
 * @version: 2014.11.20
 * 
 */

package faa_ocr.ADTs;
import java.util.*;

/**
 *
 */
public abstract class Path {
    String name;
    private ArrayList<Node> nodes;
    ArrayList<Node> intersections;
    
    public Path(String name)
    {
        this.name = name;
        nodes = new ArrayList<Node>();
        intersections = new ArrayList<Node>();
    }
    
    public String getName()
    {
        return name;   
    }
    
    public void addPathNode(Node newNode)
    {
        nodes.add(newNode);
    }
    
    public int getNumPathNodes()
    {
        return nodes.size();
    }
    
    public Node getPathNode(int index)
    {
        return nodes.get(index);
    }
    
    public void addIntNode(Node newNode)
    {
        intersections.add(newNode);
    }
    
    public int getNumIntNodes()
    {
        return intersections.size();
    }
    
    public Node getIntNode(int index)
    {
        return intersections.get(index);
    }
    
    @Override
    public String toString()
    {
        String pathString = (name + "/n" + "Path: ");
        for(Node node : nodes) {
            pathString += node.toString();
        }
        pathString += "Intersections: ";
        if(intersections.isEmpty()){
            pathString += "This path does not have any intersections.";
        } else {
            for(Node node : intersections) {
            System.out.println(node.toString());
            }
        }
        return pathString;
    }
}
