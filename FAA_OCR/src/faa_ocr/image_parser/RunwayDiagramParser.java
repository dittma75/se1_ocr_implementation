package faa_ocr.image_parser;

import java.awt.image.BufferedImage;

import faa_ocr.ADTs.Airport;
import faa_ocr.ADTs.Node;


/**
 * 
 * @author joe kvedaras
 *
 */
public class RunwayDiagramParser 
{
	private BufferedImage diagram;
	private Airport airport;
	
	//TODO: create helper method for getting red, blue, and green pixel
	//TODO: create helper method to get pixel_color
	//TODO: create class to hold red,green,blue values
	//TODO: I can look for each individual pixel value being under 20 or the pixel color
			//of -16777216
	
	
	
	
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
		this.diagram = diagram;
		this.airport = airport;
		traverseImage();
	}
	
	/**
	 * Traverse image looking for black pixels. Once a black pixel is found recursively 
	 * find edges of that black square
	 */
	private void traverseImage()
	{
		
		//26813 pixels that are less than 20,20,20 rgb
		//26184 pixels that are truly 0,0,0
		
		int black_counter = 0;

		for (int y = 0; y < diagram.getHeight(); y++) 
		{
            for (int x = 0; x < diagram.getWidth(); x++) 
            {
                final int pixel_color = diagram.getRGB(x, y);
                final int red = (pixel_color >> 16) & 0xff;
                final int green = (pixel_color >> 8) & 0xff;
                final int blue = (pixel_color) & 0xff;
                
                if(pixel_color == -16777216){
                	System.out.println(red);
                	System.out.println(green);
                	System.out.println(blue);
                	
                	black_counter ++;
                }
                
                
                //check and see if color is grey or darker
                if(red < 20 && green < 20 && blue < 20){
                	System.out.println(x +  "  " + y);
                	System.out.println();
                	
                	
                	System.out.println(pixel_color);
                	
                	findWidth(x,y);
                	findEdges(x,y);
                	findSlope(x,y);
                }
                

            }
		}
		
		System.out.println("Size of diagram: " + diagram.getHeight() + " " + diagram.getWidth());
		System.out.println(black_counter);
	}
	
	
	/**
	 * Check the pixels to the above-right, above, above-left, and left
	 * to the parameter pixel and return True if they are not black
	 * @param Starting pixel
	 * @return true if none the pixels checked are black
	 * 			false if there is a black pixel
	 */
	private boolean checkPixel(Point pixel)
	{
		return (Boolean) null;
	}
	
	/**
	 * Check the pixels to the right, bottom-right, bottom, bottom-left
	 * of the parameter point. 3 pixels must be black to traverse the 
	 * two outermost. If less than 3 are black, do nothing.
	 * @param pixel
	 */
	//TODO: we can return the 2 outermost pixels or traverse them from here
	private void checkCorner(Point pixel)
	{
		
		
	}
	
	/**
	 * Check left, bottom-left, and bottom pixels.
	 * traverse the left-most black pixel
	 * Stop traversing when all 3 pixels are not black. Return that
	 * pixel location
	 * @return
	 */
	private Point traverseLeft(Point point)
	{
		
	}
	
	/**
	 * Check right, bottom-right, and bottom pixels.
	 * traverse the right-most black pixel
	 * Stop traversing when all 3 pixels are not black. Return that
	 * pixel location
	 * @return
	 */
	private Point traverseRight(Point point)
	{
		
	}
	
	
	//TODO: This method might have to look at bottom, bl, br pixel.
	//If the black pixel is in the bl,b, and br call traverseLeft
	//if the black pixel is just on the br, traverse right
	//if the pixel is b and br, call this method again
	/**
	 * Check bottom-left, bottom, and bottom-right pixels.
	 * traverse the bottom-most black pixel
	 * Stop traversing when all 3 pixels are not black. Return that
	 * pixel location
	 * @return
	 */
	private Point traverseBottom(Point point)
	{
		
	}
	
	
	
	//May use to control the finding of the width of the runway
	private void findWidth(int x, int y)
	{
		//look at right, bottom-right, bottom, bottom-left pixel to see what is black
		//and what is white/grey
		
		//traverse the two outermost pixels
		
		//only go in one direction at most 20 pixels
		
		//we traverse until we find a point that does not find a black pixel
		// in the (left, bottom-left, bottom) direction or (right,bottom-right,bottom) direction
		//-------------Directions change by the way we are traversing
		//return that point once found
		
	}	
	
	/**
	 * Traverse the slop at the rate of the slope. Stop when
	 * you reach the last black point.
	 * @param initial_point
	 * @param slope
	 * @return last black point
	 */
	private Point traverseSlope(Point initial_point, Point slope)
	{
		int x = initial_point.getX();
		int y = initial_point.getY();
		
        //return the point where the next point is NOT going to be black
		
		return null;
	}
	
	
	
}
