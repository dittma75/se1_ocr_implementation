package faa_ocr.image_parser;

import java.awt.image.BufferedImage;

import faa_ocr.ADTs.Airport;
import faa_ocr.ADTs.Node;


/**
 * 
 * @author Joe Kvedaras
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
		//do nothing
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

		for (int y = 0; y < diagram.getHeight(); y++) 
		{
            for (int x = 0; x < diagram.getWidth(); x++) 
            {
                Point pixel = new Point(x,y);
                if (pixel.isBlack(diagram))
                {
                	//Can I add this if to the if above. will it short circuit complete?
                	if (checkPixel(pixel))
                	{
                		
                	}
                	else
                	{
                		//skip pixel
                	}
                }
                else
                {
                	//skip pixel
                }
                

            }
		}
		
		System.out.println("Size of diagram: " + diagram.getHeight() + " " + diagram.getWidth());
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
            boolean noBlack = true;
            int x = pixel.getX();
            int y = pixel.getY();
            
            //Point(0,0) is the top left corner of the document so the pixels
            //above a certain point have a smaller y coordinate
            Point left = new Point(x-1, y);
            Point topLeft = new Point(x-1, y-1);
            Point top = new Point(x, y-1);
            Point topRight = new Point(x+1, y-1);
            
            if(left.isBlack(diagram)) 
            {
            	noBlack = false;
            } else if(topLeft.isBlack(diagram)) 
            {
                noBlack = false;
            } else if(top.isBlack(diagram)) 
            {
                noBlack = false;
            } else if(topRight.isBlack(diagram)) 
            {
                noBlack = false;
            } else 
            {
                //do nothing, no black pixels were found 
            } 
            return noBlack;
        }

	
	
	/**
	 * Check the pixels to the right, bottom-right, bottom, bottom-left
	 * of the parameter point. 3 pixels must be black to traverse the 
	 * two outermost. If less than 3 are black, do nothing.
	 * @param pixel
	 */
	//TODO: we can return the 2 outermost pixels or traverse them from here
	//TODO: how will we know which point is the right point of the runway after traversing the edges?
	private void checkCorner(Point pixel)
	{
		int x = pixel.getX();
        int y = pixel.getY();
		
        Point bottom_left = new Point(x - 1, y + 1);
        Point bottom = new Point(x, y + 1);
        Point bottom_right = new Point(x + 1, y + 1);
        Point right = new Point(x + 1, y);
        
        //Check to see if 3 pixels are black
        boolean bottom_left_black = bottom_left.isBlack(diagram);
        boolean bottom_black = bottom.isBlack(diagram);
        boolean bottom_right_black = bottom_right.isBlack(diagram);
        boolean right_black = right.isBlack(diagram);
        
//        if( (bottom_left_black && bottom_black && bottom_right_black) ||
//        		(bottom_left_black && bottom_black && right_black) ||
//        		(bottom_left_black && bottom_right_black && right_black) ||
//        		(bottom_black && bottom_right_black && right_black))
//        {
//        	//return 2 outermost pixels or we can traverse them from here
//        	
//        }
        
        
        
        //TODO: we only need to 
        /* check r+br, r+br+b, r+b+bl, bl+b+br, bl+b */
        if(bottom_left_black && bottom_black && bottom_right_black)
        {
        	//traverse bottom left and right
        }
        else if(bottom_left_black && bottom_black && right_black)
        {
        	//traverse bottom left and right
        }
        else if(bottom_left_black && bottom_right_black && right_black)
        {
            //TODO: will this condition ever be true???
        	//traverse bottom left and right
        	//pass
        }
        else if(bottom_black && bottom_right_black && right_black)
        {
        	
        }
        else if(right_black && bottom_right_black)
        {
        	
        }
        else if(bottom_left_black && bottom_black)
        {
        	
        }
        else
        {
        	//do nothing
        }
        
        
	}
	
	//TODO: make a method with a traverseLeft pixel and a traverseRight pixel parameter
			//that will use the pixels to get the info needed.
	
	
	
	//TODO:convert recursive structure to iterative and traverse both (length and width) points at the same time
	
	/**
	 * Check left, bottom-left, and bottom pixels.
	 * traverse the left-most black pixel
	 * Stop traversing when all 3 pixels are not black. Return that
	 * pixel location
	 * @return the left-most Point that is black or null if none of
         * the three pixels tested are black.
	 */
	private Point traverseLeft(Point point)
	{
            Point left = new Point(point.getX() - 1, point.getY());
            Point bottom_left = new Point(point.getX() - 1, point.getY() + 1);
            Point bottom = new Point(point.getX(), point.getY() + 1);
            if (left.isBlack(diagram))
            {
                return traverseLeft(left);
            }
            else if (bottom_left.isBlack(diagram))
            {
                return traverseLeft(bottom_left);
            }
            else if (bottom.isBlack(diagram))
            {
                return traverseLeft(bottom);
            }
            else
            {
                return point;
            }
        }
	
	/**
	 * Check right, bottom-right, and bottom pixels.
	 * traverse the right-most black pixel
	 * Stop traversing when all 3 pixels are not black. Return that
	 * pixel location
	 * @return the right-most Point that is black or null if none of
         * the three pixels tested are black.
	 */
	private Point traverseRight(Point point)
	{
            Point right = new Point(point.getX() + 1, point.getY());
            Point bottom_right = new Point(point.getX() + 1, point.getY() + 1);
            Point bottom = new Point(point.getX(), point.getY() + 1);
            if (right.isBlack(diagram))
            {
                return traverseRight(right);
            }
            else if (bottom_right.isBlack(diagram))
            {
                return traverseRight(bottom_right);
            }
            else if (bottom.isBlack(diagram))
            {
                return traverseRight(bottom);
            }
            else
            {
                return point;
            }	
	}
	
	
//	//TODO: This method might have to look at bottom, bl, br pixel.
//	//If the black pixel is in the bl,b, and br call traverseLeft
//	//if the black pixel is just on the br, traverseRight
//	//if the pixel is b and br, call this method again
//	/**
//	 * Check bottom-left, bottom, and bottom-right pixels.
//	 * traverse the bottom-most black pixel
//	 * Stop traversing when all 3 pixels are not black. Return that
//	 * pixel location
//	 * @return the Point returned by traverseLeft if the bottom left,
//         * bottom, and bottom right pixels are black, the Point returned by
//         * traverseRight if the bottom right pixel is black, or the Point
//         * returned by another call to traverseBottom if the bottom and bottom
//         * right pixels are black.
//	 */
//	private Point traverseBottom(Point point)
//	{
//            Point bottom_left = new Point(point.getX() - 1, point.getY() + 1);
//            Point bottom_right = new Point(point.getX() + 1, point.getY() + 1);
//            Point bottom = new Point(point.getX(), point.getY() + 1);
//            if (bottom_left.isBlack(diagram) &&
//                bottom.isBlack(diagram) &&
//                bottom_right.isBlack(diagram))
//            {
//                return traverseLeft(bottom_left);
//            }
//            else if (bottom.isBlack(diagram) &&
//                     bottom_right.isBlack(diagram))
//            {
//                return traverseBottom(bottom);
//            }
//            else if (bottom_right.isBlack(diagram))
//            {
//                return traverseRight(bottom_right);
//            }
//            else
//            {
//                return point;
//            }
//	}
	
	
	
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
