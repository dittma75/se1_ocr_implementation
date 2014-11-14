package faa_ocr.Controller;

import faa_ocr.Controller.ArgumentParser;
import java.io.File;
import faa_ocr.ADTs.Airport;


/**
 * Class to control all airport behaviors
 * @author Joe Kvedaras
 *
 */
public class AirportController 
{	
	
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
		System.out.println(ArgumentParser.parseArgument("./res/ACY/00669AD.PDF"));
	}
	
	/**
	 * Control all behaviors of an airport
	 */
	public AirportController()
	{
		//initialize AirportController object
		//TODO: may choose to hold instances of all the objects so we don't have to create them again for every pdf file.
	}
	
	/**
	 * Create an airport and gather all information from the PDF
	 * @param path
	 */
	//only classes in same package can call getInformationFromPDF
	private void getInformationFromPDF(String path)
	{
		//create a new airport with Sting path to PDF
		Airport airport = new Airport(path);
		
		//get textual data from PDF
		AirportDataParser.parseTextData(airport);
		
		//get visual data from PDF
		ImageDataParser.parseVisualData(airport);
		
		//turn Airport into an XML and save path to XML
		String path_to_xml = XMLParser.writeXML(airport);
		
		//turn xml file into a kml file.
		//TODO: Strings or Files from xml???
		String path_to_kml = KMLParser.writeKML(new File(path_to_xml));
		
		//print out results of transformations
		printResults(airport, path_to_xml,path_to_xml);
		
	}
	
	/**
	 * Print results from xml and kml
	 * @param airport
	 * @param path_to_xml
	 * @param path_to_kml
	 */
	private void printResults(Airport airport, String path_to_xml, String path_to_kml)
	{
		System.out.println();
		System.out.println(airport.name)
		System.out.println("Path to XML: " + path_to_xml);
		System.out.println("Path to KML: " + path_to_kml);
		System.out.println();
	}
	
	
//	private String writeKML(File xml_file)
//	{
//		return KMLParser.writeKML(xml_file);
//	}
//	
//	private String writeXML(Airport airport)
//	{
//		return XMLParser.writeXML(airport);
//	}
//	
//	
//	private void parseVisualData(Airport airport)
//	{
//		
//	}
//	
//	private void parseTextData(Airport airport)
//	{
//		
//	}
	
}
