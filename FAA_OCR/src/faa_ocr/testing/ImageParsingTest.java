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
	    
		
//TODO: make method in point that will take this point and return a new point with adjusted x and y parameters.!!!!!!!!!!!!!!	    
	    
//------------------Errors and possible fixes for runways at airports	    
	    //TODO: PHX has outliers!!!! We could possiblly prevent by adding all runways to a list and take
	    //out any duplicate end points but keep longest one and only take say 3 longest runways.
	    
	    //TODO: ACY WORKS!!!!!!!!!!!
	    
	    //TODO: ATL WORKS!!! with 1 outlier that can be removed!!!
	    
	    //
	    
	    
	    
	    
	    
	    
	    
	    
//--------------------End Error reports------------------------------	    
	    
	    
	    
	    
	    
	    
	    
	  //TODO: left_point and right_point need to change after each iteration of while
        //right now, they do not move and is an infinite loop
	  //TODO: Program starts to find slope of single pixels. Example at 15,639 I believe
	    //TODO:For ACY, the slope is getting thrown off by the bottom pixel of the first runway
	    	//this will make our traverse runway method go off the runway real quick
	    
	  //TODO: Bug was found in traverse slope. If the runway was found while traversing left, it would return the right point	

	    //TODO: Bug found when runway is verticle. Search algos go straight down after going right
	    
	    //TODO: DFW: 226, 650 is bad runway. end at 152, 718
	    
		
//TODO: findPixelConversionScales throws exception
		//PDFToImage.parseVisualData(airport);
	    
	}
}
