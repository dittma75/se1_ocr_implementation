package faa_ocr.image_parser;

import java.awt.image.BufferedImage;

import faa_ocr.ADTs.Airport;

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
	//TODO: hold BufferedImage diagram as a field so I don't have to keep passing it around. same 
			//for airport
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
	
	//traveseLeft, traverseRight, and traverseBottom function to go in all directions.
	//or one function with parameters of what direction to look


	private void look (Point point)
	{
		
		
	}
	
	
	
	
	
	private int[] traverseSlope(int[] initial_point, int[] slope)
	{
		int x = initial_point[0];
		int y = initial_point[1];
		
		int pixel_color = diagram.getRGB(x, y);
        int  red = (pixel_color & 0x00ff0000) >> 16;
        int  green = (pixel_color & 0x0000ff00) >> 8;
        int  blue = pixel_color & 0x000000ff;
		
        //check to see if next point is going to be black.
        // if it is, get that new point and loop again
        
        //return the point where the next point is NOT going to be black
		
		return null;
	}
	

	private void findEdges(int x, int y)
	{
		//the initial pixel WILL be a corner. We just need to find the other 3 corners now.
		
		
		//find the initial 2 edges, find the middle pixel, save it, traverse at slope of the runway to find
		//the end of the runway. save that point.
		
		//we also need a place to store what pixels we have already visited so we don't waste
		//time on them
		
		//need to find slope of the runway so we can traverse it.
	}
	
	
	private void findSlope(int x, int y)
	{
		
		
	}
	
	
	
}
