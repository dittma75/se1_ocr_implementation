/**
 * Purpose: Software Engineering I: FAA OCR Project 
 * Status: Complete and thoroughly tested
 * Last update: 12/02/14
 * Submitted: 
 * Comment: Path ADT
 * @author: Greg Richards
 * @version: 2014.11.20
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
    
    /**
     * 
     * @param name 
     */
    public Path(String name)
    {
        this.name = name;
        nodes = new ArrayList<Node>();
        intersections = new ArrayList<Node>();
    }
    
    /**
     * 
     * @return 
     */
    public String getName()
    {
        return name;   
    }
    
    /**
     * 
     * @param newNode 
     */
    public void addPathNode(Node newNode)
    {
        nodes.add(newNode);
    }
    
    /**
     * 
     * @return 
     */
    public int getNumPathNodes()
    {
        return nodes.size();
    }
    
    /**
     * 
     * @param index
     * @return 
     */
    public Node getPathNode(int index)
    {
        return nodes.get(index);
    }
    
    /**
     * 
     * @param newNode 
     */
    public void addIntNode(Node newNode)
    {
        intersections.add(newNode);
    }
    
    /**
     * 
     * @return 
     */
    public int getNumIntNodes()
    {
        return intersections.size();
    }
    
    /**
     * 
     * @param index
     * @return 
     */
    public Node getIntNode(int index)
    {
        return intersections.get(index);
    }
    
    /**
     * 
     * @return 
     */
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
