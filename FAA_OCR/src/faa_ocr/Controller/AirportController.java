package faa_ocr.Controller;


import java.io.File;
import java.util.ArrayList;

import faa_ocr.ADTs.Airport;
import faa_ocr.ADTs.Node;
import faa_ocr.ADTs.Point;
import faa_ocr.ADTs.Runway;
import faa_ocr.ADTs.Slope;
import faa_ocr.image_parser.MyRunway;
import faa_ocr.image_parser.PDFToImage;
import faa_ocr.text_parser.PDFToText;
import faa_ocr.xml_parser.AirportToXML;
import faa_ocr.kml_parser.XMLtoKML;


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
				//TODO: do we want to do anything if the path is not a pdf??
				System.out.println(arg + " is not a valid PDF");
			}
		}
		//System.out.println(ArgumentParser.parseArgument("./res/ACY/00669AD.PDF"));
	}
	
	
	private PDFToText pdf_to_text;
	private PDFToImage pdf_to_image;
	private AirportToXML xml_parser;
	private XMLtoKML kml_parser;
	private ArrayList <MyRunway> runways;
	private Airport airport;
	
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
		//create a new airport with Sting path to PDF
		airport = new Airport(path, false);
		
		//get textual data from PDF
		pdf_to_text.parseTextData(airport);
		
		//get visual data from PDF
		runways = pdf_to_image.parseVisualData(airport);
		
		//Check runways and taxiways for intersections
		findIntersections(airport);
		
		//turn Airport into an XML and save path to XML
		String path_to_xml = xml_parser.convertToXml(airport);
		
		//turn xml file into a kml file.
		String path_to_kml = kml_parser.writeKML(new File(path_to_xml));
		
		//Add all runways to the airport!!!
		addToAirport();
		
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
	private void findIntersections(Airport airport)
	{
		
		int num_runways = airport.numRunways();
		int num_taxiways = airport.numTaxiways();
		
		
		/*
		 * Loop through every combination of runways and runways seeing if there is
		 * an intersection. We increase both loops by 2 because runways are
		 * stored in pairs. If we find an intersection, we will add that intersection
		 * to the matching pair.
		 */
		for (int ii = 0; ii < num_runways; ii = ii + 2)
		{
			for (int kk = ii + 2; kk < num_runways; kk = kk + 2)
			{
				//Check to see if two runways are intersecting
				findIntersections(airport.getRunway(ii), airport.getRunway(kk));
			}
		}
		
		
		/*
		 * Loop through every combination of runway and taxiway seeing if there is
		 * an intersection. We increase the runway look by 2 because runways are stored
		 * in pairs. If we find an intersection, we will add that intersection to the
		 * matching pair
		 */
		for (int jj = 0; jj < num_runways; jj = jj + 2)
		{
			for (int tt = 0; tt < num_runways; tt++)
			{
				//do nothing for now
			}
		}
		
		
		
		/*
		 * Loop through every combination of taxiway and taxiway seeing if there
		 * is an intersection between them.
		 */
		for (int pp = 0; pp < num_taxiways; pp++)
		{
			for (int yy = pp + 1; yy < num_taxiways; yy++)
			{
				//do nothing for now
			}
		}
	}
	
	private void findIntersections(Runway one, Runway two)
	{
		
	}
	
	
	/**
	 * Traverse the runway at the rate of the slope and add those points to the airport
	 * @param midpoint of the current runway
	 * @param slope of the current runway
	 */
	private boolean addToAirport()
	{             	
		
		//Loop through all the runways and find intersections and add to airport!!!!
		Point midpoint = null;
		
		
		
		//TODO: change paramters to be a runway. or move this method all together to controller and only do this
		//after we find intersections
		
		Point end_point = null;//stop compiler from complaining
		
		
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
//                        Runway runway = airport.getRunway(
//                                airport.numRunways() - runways_left
//                        );
//                        runway.addPathNode(startNode);
//                        runway.addPathNode(endNode);
         //TODO:Import runway from ADT to this package
                    }
             
                    return true; //stop compiler from complaining
            
	}
	
}
