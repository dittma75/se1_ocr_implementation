/**
 * this class is purely a test to make sure an Airport object is converted to 
 * XML format properly and the new .xml file is created and saved properly with
 * the correct file path.  This particular test will make sure it handles all 
 * of the possible combinations of Taxiways regarding the number of 
 * intersections a Taxiway can have (ie. none, 1, 2, many). This will also test 
 * the possibility that any given Runway will have an intersection and a
 * displacement threshold, an intersection an no displacement threshold, and a 
 * displacement threshold with no intersections.
 */
package faa_ocr.testing;
import faa_ocr.ADTs.*;
import faa_ocr.xml_parser.*;

/**
 * create a new Airport object, fill it with runways, taxiways and all of the 
 * other required information, and convert it to XML format.
 * @author g_ric_000
 */
public class XMLCreationTest1 {
    private static final AirportToXML xmlConverter = new AirportToXML();
    
    public static void main(String args[])
    {
        Airport airport = new Airport("ATL_Airport.pdf");
        //give values to all data fields
        airport.setName("Atlanta Airport");
        airport.setLocation("Atlanta, Georgia");
        airport.setAbbreviation("ATL");
        airport.setVariation((float).07);
        
        //create a runway and fill it with completely made up numbers and names
        //this is strictly a visual test so the values are arbitrary
        
        // runway = new Runway(elevation, heading, name);
        Runway runway1 = new Runway(100, (float)2346.5, "R1");
        // node = new Node(longitude, latitude);
        Node startNode1 = new Node((float)113.74, (float)36.86);
        Node endNode1 = new Node((float)114.02, (float)37.05);
        //add the nodes to the runway object
        runway1.addPathNode(startNode1);
        runway1.addPathNode(endNode1);
        //make a threshold node and a intersection node
        Node thresholdNode1 = new Node((float)113.87, (float)36.97);
        Node intNode1 = new Node((float)113.99, (float)36.01);
        //add the displacement threshold and an intersection to the runway object
        runway1.setThreshold(thresholdNode1);
        runway1.addIntNode(intNode1);
        //add the runway to the airport
        airport.addRunway(runway1);
        
        //make another Runway object whose end is the first runway's start and 
        //whose start is the first runway's end.
        // runway = new Runway(elevation, heading, name);
        Runway runway2 = new Runway(100, (float)2346.5, "R2");
        // node = new Node(longitude, latitude);
        Node startNode2 = new Node((float)114.02, (float)37.05);
        Node endNode2 = new Node((float)113.74, (float)36.86);
        //add the nodes to the runway object
        runway2.addPathNode(startNode1);
        runway2.addPathNode(endNode1);
        //make a intersection node that is the same as the first because the 
        //first two runways are virtually the same runway, just going in
        //different directions
        Node intNode2 = new Node((float)113.99, (float)36.01);
        //add the intersection to the runway object
        runway2.addIntNode(intNode2);
        //add the runway to the airport
        airport.addRunway(runway2);
        
        //make another Runway object to add to the airport
        //this runway will not have a displacement threshold and will intersect
        //the first two runways.
        Runway runway3 = new Runway(100, (float)3678.5, "R3");
        // node = new Node(longitude, latitude);
        Node startNode3 = new Node((float)126.47, (float)63.45);
        Node endNode3 = new Node((float)150.23, (float)47.63);
        //add the nodes to the runway object
        runway3.addPathNode(startNode3);
        runway3.addPathNode(endNode3);
        //make an intersection node that is the same as the first runway
        //because this runway intersects the other 2
        Node intNode3 = new Node((float)113.99, (float)36.01);
        //add the intersection to the runway object
        runway3.addIntNode(intNode3);
        //add the runway to the airport
        airport.addRunway(runway3);
        
        //make another Runway object to add to the airport
        //this runway will not have a displacement threshold nor any intersections
        Runway runway4 = new Runway(54, (float)5062.5, "R4");
        // node = new Node(longitude, latitude);
        Node startNode4 = new Node((float)102.47, (float)27.45);
        Node endNode4 = new Node((float)109.32, (float)31.36);
        //add the nodes to the runway object
        runway4.addPathNode(startNode4);
        runway4.addPathNode(endNode4);
        //add the runway to the airport
        airport.addRunway(runway4);
        
        //make a taxiway object to add to the airport
        //this taxiway will not have any intersections 
        Taxiway taxiway1 = new Taxiway("T1");
        // node = new Node(longitude, latitude);
        Node node1 = new Node((float)93.74, (float)41.86);
        Node node2 = new Node((float)95.02, (float)47.46);
        Node node3 = new Node((float)98.34, (float)55.71);
        Node node4 = new Node((float)101.55, (float)59.08);
        //add the nodes to the Taxiway object 
        taxiway1.addPathNode(node1);
        taxiway1.addPathNode(node2);
        taxiway1.addPathNode(node3);
        taxiway1.addPathNode(node4);
        //add the taxiway to the airport
        airport.addTaxiway(taxiway1);
        
        //make another taxiway object to add to the airport
        //this taxiway will have 1 intersection 
        Taxiway taxiway2 = new Taxiway("T2");
        // node = new Node(longitude, latitude);
        Node node5 = new Node((float)40.74, (float)41.86);
        Node node6 = new Node((float)45.02, (float)47.46);
        Node node7 = new Node((float)49.34, (float)55.71);
        Node node8 = new Node((float)52.55, (float)59.08);
        Node node9 = new Node((float)56.15, (float)63.22);
        //add the nodes to the Taxiway object 
        taxiway2.addPathNode(node5);
        taxiway2.addPathNode(node6);
        taxiway2.addPathNode(node7);
        taxiway2.addPathNode(node8);
        taxiway2.addPathNode(node9);
        //create a single intersection node
        Node intNode4 = new Node((float)42.51, (float)42.51);
        //add the intersection to the taxiway
        taxiway2.addIntNode(intNode4);
        //add the taxiway to the airport
        airport.addTaxiway(taxiway2);
        
        //make another taxiway object to add to the airport
        //this taxiway will have 2 intersections 
        Taxiway taxiway3 = new Taxiway("T3");
        // node = new Node(longitude, latitude);
        Node node10 = new Node((float)70.74, (float)41.86);
        Node node11 = new Node((float)74.02, (float)47.46);
        Node node12 = new Node((float)82.34, (float)55.71);
        //add the nodes to the Taxiway object 
        taxiway3.addPathNode(node10);
        taxiway3.addPathNode(node11);
        taxiway3.addPathNode(node12);
        //create a single intersection node
        Node intNode5 = new Node((float)77.51, (float)45.51);
        Node intNode6 = new Node((float)80.13, (float)48.94);
        //add the intersection to the taxiway
        taxiway3.addIntNode(intNode5);
        taxiway3.addIntNode(intNode6);
        //add the taxiway to the airport
        airport.addTaxiway(taxiway3);
        
        //make another taxiway object to add to the airport
        //this taxiway will have 2 intersections 
        Taxiway taxiway4 = new Taxiway("T4");
        // node = new Node(longitude, latitude);
        Node node13 = new Node((float)41.01, (float)48.86);
        Node node14 = new Node((float)63.02, (float)47.46);
        Node node15 = new Node((float)75.34, (float)55.71);
        Node node16 = new Node((float)86.77, (float)55.92);
        //add the nodes to the Taxiway object 
        taxiway4.addPathNode(node13);
        taxiway4.addPathNode(node14);
        taxiway4.addPathNode(node15);
        taxiway4.addPathNode(node16);
        //create a single intersection node
        Node intNode7 = new Node((float)42.51, (float)42.51);
        Node intNode8 = new Node((float)77.51, (float)45.51);
        Node intNode9 = new Node((float)80.13, (float)48.94);
        //add the intersection to the taxiway
        taxiway4.addIntNode(intNode7);
        taxiway4.addIntNode(intNode8);
        taxiway4.addIntNode(intNode9);
        //add the taxiway to the airport
        airport.addTaxiway(taxiway4);
        
        //now convert the airport to XML and create the .xml file
        xmlConverter.convertToXml(airport);
        System.out.println("The .xml file was generated!");
        
    }
}
