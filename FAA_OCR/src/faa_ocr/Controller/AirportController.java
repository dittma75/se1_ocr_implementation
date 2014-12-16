package faa_ocr.Controller;


import java.io.File;
import java.util.ArrayList;

import faa_ocr.ADTs.Airport;
import faa_ocr.ADTs.DiagramRunway;
import faa_ocr.ADTs.LineSegment;
import faa_ocr.ADTs.Node;
import faa_ocr.ADTs.Point;
import faa_ocr.ADTs.Runway;
import faa_ocr.image_parser.PDFToImage;
import faa_ocr.text_parser.PDFToText;
import faa_ocr.xml_parser.AirportToXML;
import faa_ocr.kml_parser.XMLtoKML;
import java.lang.Math;
import java.awt.geom.Line2D.Double;

/**
 * Class to control all airport behaviors
 * @author Joe Kvedaras
 *
 */
public class AirportController 
{	
	/* For unit testing Argument Parser */
	public static void main(String[] args)
	{		
		AirportController airport_controller = new AirportController();
		
		//Accept list of list of PDFS
		for(String arg : args){
			if(ArgumentParser.parseArgument(arg))
			{
				//are we going to pass airports around 1 at a time as we see that the path is valid?
				// or are we going to check everything, then call all the airports?
				airport_controller.getInformationFromPDF(arg);
			}
			else
			{
				System.out.println(arg + " is not a valid PDF");
			}
		}
		//System.out.println(ArgumentParser.parseArgument("./res/ACY/00669AD.PDF"));
		airport_controller.getInformationFromPDF("/Users/jokvedaras/Documents/workspace/faa_implementation/FAA_OCR/res/ATL/00026AD.PDF");
	}
	
	
	private PDFToText pdf_to_text;
	private PDFToImage pdf_to_image;
	private AirportToXML xml_parser;
	private XMLtoKML kml_parser;
	
	/**
	 * Control all behaviors of an airport
	 */
	public AirportController()
	{
		pdf_to_text = new PDFToText();
		pdf_to_image = new PDFToImage();
		xml_parser = new AirportToXML();
		kml_parser = new XMLtoKML();
	}
	
	/**
	 * Create an airport and gather all information from the PDF
	 * @param path
	 */
	private void getInformationFromPDF(String path)
	{
		ArrayList <DiagramRunway> runways;
		Airport airport;
		
		
		//create a new airport with Sting path to PDF
		airport = new Airport(path, true);
		
		//get textual data from PDF
		pdf_to_text.parseTextData(airport);
		
		//get visual data from PDF
		runways = pdf_to_image.parseVisualData(airport);
		
		//All all runways to airport and convert x/y to lat/long
		addToAirport(airport, runways);
		
		//turn Airport into an XML and save path to XML
		String path_to_xml = xml_parser.convertToXml(airport);
		
		//turn xml file into a kml file.
		String path_to_kml = kml_parser.writeKML(new File(path_to_xml));
		
		
		//print out results of transformations
		printResults(airport, path_to_xml,path_to_kml);
		
	}
	
	/**
	 * Print resulting files from XML and KML
	 * @param airport
	 * @param path_to_xml
	 * @param path_to_kml
	 */
	private void printResults(Airport airport, String path_to_xml, String path_to_kml)
	{
		System.out.println();
		System.out.println(airport.getName());
		System.out.println("Path to XML: " + path_to_xml);
		System.out.println("Path to KML: " + path_to_kml);
		System.out.println();
	}
	
	/**
	 * Find all intersections between runways and runways, runways and taxiways,
	 * taxiways and taxiways.
	 * @param airport
	 */
	private void findIntersections(ArrayList<DiagramRunway> runways)
	{
		int num_runways = runways.size();
		
		/*
		 * Loop through every combination of runways and runways seeing if there is
		 * an intersection. If we find an intersection, we will add that intersection
		 * to the pair of runways.
		 */
		for (int ii = 0; ii < num_runways; ii = ii + 1)
		{
			DiagramRunway alpha = runways.get(ii);
			
			for (int kk = ii + 2; kk < num_runways; kk = kk + 1)
			{
				DiagramRunway beta = runways.get(kk);
				
				//Check to see if two runways are intersecting
				Point intersecting_point;
				intersecting_point = findIntersections(alpha, beta);
				if(intersecting_point != null)
				{
					//add intersecting point to both runways
					alpha.addIntersection(intersecting_point);
					beta.addIntersection(intersecting_point);
				}
				else
				{
					//intersecting_point was not found
				}
			}
		}
		
	}
	
	private Point findIntersections(DiagramRunway one, DiagramRunway two)
	{
		
		Point returnPoint = null;
//		//See if two runways intersect
//		LineSegment alpha = new LineSegment(one.getStartPoint(), one.getEndPoint());
//		LineSegment beta = new LineSegment(two.getStartPoint(), two.getEndPoint());
//
//		if(LineSegment.doBoundingBoxesIntersect(alpha.getBoundingBox(), beta.getBoundingBox()))
//		{
//			
//		}
//		else
//		{
//			return returnPoint;
//		}
		
		double x1 = one.getStartPoint().getX();
		double y1 = one.getStartPoint().getY();
		double x2 = one.getEndPoint().getX();
		double y2 = one.getEndPoint().getY();
		
		double x3 = two.getStartPoint().getX();
		double y3 = two.getStartPoint().getY();
		double x4 = two.getEndPoint().getX();
		double y4 = two.getEndPoint().getY();
		
		
		Double line1 = new Double(x1,y1,x2,y2);
        Double line2 = new Double(x3,y3,x4,y4);
        //See if the two paths intercept
        boolean inter = line1.intersectsLine(line2);
        /*Check that there is an intercept and they aren't the same path.
          They may not be the exact same points, and so this if will 
          probably need to be changed. */
        if(inter == true && x1 != x3 && x1 != x4){
            /*Get the distance from the end point of the "first" line
              to where it intercepts the other line */
            double dist = Double.ptLineDist(x3,y3,x4,y4, x1,y1);
            System.out.println(inter);
            System.out.println(dist);

            /*Find intersection X and Y values by subtracting the
              dist from the length of the "first" line. */
            double xlen = x1 - x2;   // length of x of right triangle (2.31)
            if(xlen < 0) //Make pos if neg
                xlen = xlen * -1;
            double ylen = y1 - y2;   // length of y of right triangle (3.86)
            if(ylen < 0) //Make pos if neg
                ylen = ylen * -1;
            double hpt = Math.hypot(xlen,ylen); // length of hypot 4.498
            double x = (xlen/hpt)*dist;
            double y = (ylen/hpt)*dist;
            
            x = x + x1; //Intersection X val
            y = y + y1; //Intersection Y val
            
            return returnPoint = new Point(x,y);
            
            
            
//            System.out.println(xlen);
//            System.out.println(ylen);
//            System.out.println(hpt);
//            System.out.println(x + ", " + y);
        }
        //Check if there is an intercept (overlap) ????
        else
        {
        	return returnPoint;
        }
    
	}
	
	
	/**
	 * Traverse the runway at the rate of the slope and add those points to the airport
	 * @param midpoint of the current runway
	 * @param slope of the current runway
	 */
	private boolean addToAirport(Airport airport, ArrayList<DiagramRunway> runways)
	{             	
		
		int runways_left = airport.numRunways();
		findIntersections(runways);
		
		for(int kk = 0; kk < runways.size(); kk++)
		{
			Point midpoint = runways.get(kk).getStartPoint();
			Point end_point = runways.get(kk).getEndPoint();
			ArrayList<Point> intersections = runways.get(kk).returnIntersections();
			
			 //Translate the midpoint and end_point from x/y to lat/long
            float mid_long = airport.longitudeConversion(midpoint);
            float mid_lat = airport.latitudeConversion(midpoint);
            Node startNode = new Node(mid_long, mid_lat);

            float end_long = airport.longitudeConversion(end_point);
            float end_lat = airport.latitudeConversion(end_point);
            Node endNode = new Node(end_long, end_lat);

            /* Add points to existing Runway instance in airport
             * object.  Each physical runway is two runways, and they
             * are stored consecutively in pairs.  Hence, we add
             * the start and end nodes to the next two runways, since
             * they represent the same physical runway.
             */
            for (int i = 0; i < 2; i++)
            {
                Runway runway = airport.getRunway(
                        airport.numRunways() - runways_left
                );
                runway.addPathNode(startNode);
                runway.addPathNode(endNode);
                
                
                //Add all intersections to runway
                for(Point point: intersections)
                {
                	float point_long = airport.longitudeConversion(point);
                	float point_lat = airport.latitudeConversion(point);
                	Node pointNode = new Node(point_long, point_lat);
                	runway.addIntNode(pointNode);
                }
                
                runways_left --;
                
            }
		}   
		return true;
            
	}
	
}
