package faa_ocr.image_parser;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import faa_ocr.ADTs.Airport;

public class PDFToImage 
{
	
	/**
	 * Get visual data from Airport
	 * @param airport	from which we extract data from
	 */
	public void parseVisualData(Airport airport)
	{
		//create buffered image with the pdf file
		//TODO: if it doesn't work, we need to use PDFbox to create a jpeg
		BufferedImage airport_image = ImageIO.read(new File(airport.getPath()));
		
		//get runway data from image
		new RunwayDiagramParser().parseRunways(airport_image, airport);
		
		//get taxiway date from image
		new TaxiwayDiagramParser().parseTaxiways(airport_image, airport);
	}
	
	
	
}
