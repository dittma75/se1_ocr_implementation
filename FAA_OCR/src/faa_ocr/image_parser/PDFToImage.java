package faa_ocr.image_parser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import faa_ocr.ADTs.Airport;


/**
 * 
 * @author joe kvedaras
 *
 */
public class PDFToImage 
{
	
	
	/* Main method for testing */
	public static void main(String[] args)
	{
		BufferedImage airport_image;
		try {
			airport_image = ImageIO.read(new File("./FAA_OCR/lib/00669AD.jpg"));
			Airport airport = new Airport("./FAA_OCR/res/ACY/00669AD.pdf");
			
			//get runway data from image
			new RunwayDiagramParser().parseRunways(airport_image, airport);
			
			//get taxiway date from image
			new TaxiwayDiagramParser().parseTaxiways(airport_image, airport);
			
		} catch (IOException e) {
			System.err.println("Error when making PDF an image");
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	/**
	 * Get visual data from Airport
	 * @param airport	from which we extract data from
	 */
	public void parseVisualData(Airport airport)
	{
		//create buffered image with the pdf file
		//TODO: if it doesn't work, we need to use PDFbox to create a jpeg
		BufferedImage airport_image;
		try {
			airport_image = ImageIO.read(new File(airport.getFilePath()));

			//get runway data from image
			new RunwayDiagramParser().parseRunways(airport_image, airport);
			
			//get taxiway date from image
			new TaxiwayDiagramParser().parseTaxiways(airport_image, airport);
			
		} catch (IOException e) {
			System.err.println("Error when making PDF an image");
			e.printStackTrace();
		}
		
	}
	
	
	
}
