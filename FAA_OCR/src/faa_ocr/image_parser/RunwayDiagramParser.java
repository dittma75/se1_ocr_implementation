package faa_ocr.image_parser;

import java.awt.image.BufferedImage;
import faa_ocr.ADTs.Airport;
import javax.media.jai.*;

/**
 * 
 * @author joe kvedaras
 *
 */
public class RunwayDiagramParser 
{
	
	public RunwayDiagramParser()
	{
		
	}
	
	/**
	 * Find the paths of all runways in the diagram and adds their paths to
	 * the Airport object specified.
	 * @param diagram	is the airport diagram image to parse for runways
	 * @param airport	the airport to which runway path data should be added
	 */
	public void parseRunways(BufferedImage diagram, Airport airport)
	{
		traverseImage(diagram);
	}
	
	/**
	 * Traverse image looking for black pixels. Once a black pixel is found recursively 
	 * find edges of that black square
	 */
	private void traverseImage(BufferedImage diagram)
	{
		
		int black_counter = 0;

		for (int y = 0; y < diagram.getHeight(); y++) 
		{
            for (int x = 0; x < diagram.getWidth(); x++) 
            {
                final int pixel_color = diagram.getRGB(x, y);
                final int  red = (pixel_color & 0x00ff0000) >> 16;
                final int  green = (pixel_color & 0x0000ff00) >> 8;
                final int  blue = pixel_color & 0x000000ff;
                

                
                //check and see if color is grey or darker
                if(red < 110 && green < 110 && blue < 110){
                	System.out.println(x +  "  " + y);
                	System.out.println();
                	black_counter ++;
                	
                	findEdges(diagram,x,y);
                	findSlope(diagram,x,y);
                }
                

            }
		}
		
		System.out.println("Size of diagram: " + diagram.getHeight() + " " + diagram.getWidth());
		System.out.println(black_counter);
	}
	
	
	private void findEdges(BufferedImage diagram, int x, int y)
	{
		//the initial pixel WILL be a corner. We just need to find the other 3 corners now.
		
		
		
		
		
		//find the initial 2 edges, find the middle pixel, save it, traverse at slope of the runway to find
		//the end of the runway. save that point.
		
		//we also need a place to store what pixels we have already visited so we don't waste
		//time on them
		
		//need to find slope of the runway so we can traverse it.
	}
	
	
	private void findSlope(BufferedImage diagram, int x, int y)
	{
		
		
	}
	
	
	
}
