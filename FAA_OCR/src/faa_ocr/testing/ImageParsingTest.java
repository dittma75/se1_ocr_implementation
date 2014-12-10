package faa_ocr.testing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import faa_ocr.ADTs.Airport;
import faa_ocr.image_parser.PDFToImage;
import faa_ocr.image_parser.RunwayDiagramParser;

public class ImageParsingTest {
	
	public static void main(String[] args) throws IOException
	{
		String acy_path = "res/written_image.png";
		String acy_pdf_path = "res/ACY/00669AD.PDF";
		
		BufferedImage airport_image = ImageIO.read(new File(acy_path));
		
		//make airport with ACY
		Airport airport = new Airport(acy_pdf_path);
		
	    //get runway data from image
	    new RunwayDiagramParser().parseRunways(airport_image, airport);
	    
		
		
//TODO: findPixelConversionScales throws exception
		//PDFToImage.parseVisualData(airport);
	    
	}
}
