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
		//String path = "/Users/jokvedaras/Documents/workspace/faa_implementation/FAA_OCR/res/ACY/00669AD1.jpg";  //ACY
		//String path = "/Users/jokvedaras/Documents/workspace/faa_implementation/FAA_OCR/res/ATL/00026AD1.jpg";  //ATL
		String path = "/Users/jokvedaras/Documents/workspace/faa_implementation/FAA_OCR/res/DFW/06039AD1.jpg";  //DFW
		//String path = "/Users/jokvedaras/Documents/workspace/faa_implementation/FAA_OCR/res/PHX/00322AD1.jpg";  //PHX
		
		File image = new File(path);
		
		
		String acy_pdf_path = "/Users/jokvedaras/Documents/workspace/faa_implementation/FAA_OCR/lib/00669AD.PDF";
		
		BufferedImage airport_image = ImageIO.read(image);
		
		//make airport with ACY
//		Airport airport = new Airport(acy_pdf_path,false);
		Airport airport = null;
		
		
	    //get runway data from image
	    new RunwayDiagramParser().parseRunways(airport_image, airport);
	    
	    
	}
}
