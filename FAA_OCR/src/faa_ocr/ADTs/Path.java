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
 * Path is an abstract super class that encapsulates all similarities amongst 
 * Runways and Taxiways and gives the subclasses three data fields and several
 * methods that all instances of Path should have regardless of the type. It 
 * is important to encapsulate this information in a super class because it is
 * good possibility that in the future there will be new extensions of Path that
 * will need the same fields and methods.
 */
public abstract class Path {
    String name;
    private final ArrayList<Node> nodes;
    ArrayList<Node> intersections;
    
    /**
     * constructor for the Path class
     * 
     * @param name String name of the class that we have pulled from the diagram
     */
    public Path(String name)
    {
        this.name = name;
        nodes = new ArrayList<>();
        intersections = new ArrayList<>();
    }
    
    /**
     * get the name of the 
     * 
     * @return String name of the Path
     */
    public String getName()
    {
        return name;   
    }
    
    /**
     * add a Node to the list defining the coordinates of the Path
     * 
     * @param newNode Node we are adding to the nodes collection
     */
    public void addPathNode(Node newNode)
    {
        nodes.add(newNode);
    }
    
    /**
     * get the number of Node objects in the Path
     * 
     * @return int size of the collection
     */
    public int getNumPathNodes()
    {
        return nodes.size();
    }
    
    /**
     * get the Node at the specified index in the ArrayList
     * 
     * @param index int value of the desired position in the nodes collection 
     * @return the Node at the desired spot in the collection
     */
    public Node getPathNode(int index)
    {
        return nodes.get(index);
    }
    
    /**
     * add a new Node to the collection of intersections
     * 
     * @param newNode intersection Node we are adding to the collection
     */
    public void addIntNode(Node newNode)
    {
        intersections.add(newNode);
    }
    
    /**
     * get the number of intersections in an instance of Path
     * 
     * @return int size of the intersections collection 
     */
    public int getNumIntNodes()
    {
        return intersections.size();
    }
    
    /**
     * get the intersection Node at the specified index in the ArrayList
     * 
     * @param index int value of the desired position in the intersections collection 
     * @return the Node at the desired spot in the collection
     */
    public Node getIntNode(int index)
    {
        return intersections.get(index);
    }
    
    /**
     * Get the String representation of an Path object, including all of the
     * current values of the object's data fields in an organized manner.
     * 
     * @return String representation of a Path object 
     */
    @Override
    public String toString()
    {
        String pathString = "Name: " + name + "\n";
        for(Node node : nodes) {
            pathString += node.toString();
        }
        pathString += "Intersections: ";
        if(intersections.isEmpty()){
            pathString += "This path does not have any intersections.\n";
        } else {
            for(Node node : intersections) {
            System.out.println(node.toString());
            }
        }
        return pathString;
    }
} //end Path
