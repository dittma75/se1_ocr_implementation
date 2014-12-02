package faa_ocr.image_parser;

import java.awt.image.BufferedImage;

import faa_ocr.ADTs.Airport;


/**
 * 
 * @author Joe Kvedaras
 *
 */
public class RunwayDiagramParser 
{
	private BufferedImage diagram;
	private Airport airport;
	

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
		for (int y = 0; y < diagram.getHeight(); y++) 
		{
            for (int x = 0; x < diagram.getWidth(); x++) 
            {
                Point pixel = new Point(x,y);
                if (pixel.isBlack(diagram))
                {
                	if (checkPixel(pixel))
                	{
                		//see if the pixel is a runway
                		checkCorner(pixel);
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
            	return false;
            } else if(topLeft.isBlack(diagram)) 
            {
                return false;
            } else if(top.isBlack(diagram)) 
            {
                return false;
            } else if(topRight.isBlack(diagram)) 
            {
                return false;
            } else 
            {
                //no black pixels were found
            	return true;
            } 
        }

	
	
	/**
	 * Check the pixels to the right, bottom-right, bottom, bottom-left
	 * of the parameter point. 3 pixels must be black to traverse the 
	 * two outermost. If less than 3 surrounding pixels are black, do nothing.
	 * @param pixel
	 */
	private void checkCorner(Point pixel)
	{
		int x = pixel.getX();
        int y = pixel.getY();
		
        Point bottom_left = new Point(x - 1, y + 1);
        Point bottom = new Point(x, y + 1);
        Point bottom_right = new Point(x + 1, y + 1);
        Point right = new Point(x + 1, y);
        
        //Check to see if pixels around the initial point are black
        boolean bottom_left_black = bottom_left.isBlack(diagram);
        boolean bottom_black = bottom.isBlack(diagram);
        boolean bottom_right_black = bottom_right.isBlack(diagram);
        boolean right_black = right.isBlack(diagram);
        
        //3 pixels must be black so we know it is a runway
        /* check r+br+b, bl+b+br, r+br+b+bl */
        if(bottom_right_black && bottom_black && bottom_left_black ||
        		right_black && bottom_right_black && bottom_black ||
        		right_black && bottom_right_black && bottom_black && bottom_left_black)
        {
        	findSlope(pixel);
        }
        else
        {
        	//do nothing
        }  
	}

            
	
	/**
	 * Starting from the corner of the runway, follow the left side of
         * the rectangular runway and the right side of the rectangular
         * runway until the end of the short side is found.
	 * @param initial_point is the starting point at the corner of the
         * runway.  This point will either be the upper right corner or the
         * upper left corner of the runway depending on the runway's
         * orientation.
	 */
	private void findSlope(Point initial_point)
	{
            /* Initialize the left point and right point.  We will traverse
             * a black pixel path going left from the left point and going
             * right from the right point.  We will stop when we can no longer
             * find a black pixel in one of the paths.  This path represents
             * the width of the runway because the width is always shorter than
             * the length.
             */
            Point left_point = traverseLeft(initial_point);
            Point right_point = traverseRight(initial_point);

            
            /* There is no point for the end of the width of the runway yet.
             * The best starting point for the endpoint is the initial point.
             */
            Point end_of_width = initial_point;
            
            boolean width_found = false;
            
            //Until the width is found, keep traversing.
            while (!width_found)
            {
                /* The next point on the left traversal path may be the end
                 * of the width of the runway.
                 */
                end_of_width = traverseLeft(left_point);
                if (left_point.equals(end_of_width))
                {
                    width_found = true;
                }
                
                /* The next point on the right traversal path may be the end
                 * of the width of the runway.
                 */
                end_of_width = traverseRight(right_point);
                if (right_point.equals(end_of_width))
                {
                    width_found = true;
                }
            }
            
            /* The width of the runway is now a line segment from the
             * Point intial_point to the Point end_of_width.  The
             * slope of the length of the runway is the negative
             * reciprocal of the slope of the width of the runway
             * since the length and width are perpendicular.  Hence, the
             * x component of the slope that we want is the difference between
             * the y components that we have, and the y component is the
             * difference between the x components that we have.
             */
//            int slope_x = -1 *(end_of_width.getY() - initial_point.getY());
//            int slope_y = end_of_width.getX() - initial_point.getX();
            
            //Calculate slope between initial point and end point. Invert that slope to get
            //slope of the runway
            Point slope_of_runway = initial_point.calculateSlope(end_of_width).inverseSlope();
            Point midpoint_of_runway = initial_point.findMidpoint(end_of_width);
            
            addToAirport(midpoint_of_runway, slope_of_runway);
	}
	
	
	/**
	 * Traverse the runway at the rate of the slope and add those points to the airport
	 * @param midpoint of the current runway
	 * @param slope of the current runway
	 */
	private void addToAirport(Point midpoint, Point slope)
	{
		Point end_point = traverseSlope(midpoint, slope);
		
		//Translate the midpoint and end_point from x/y to lat/long
		
		//Add points to airport object.
		
		
	}
	
	
	
	
	
	/**
	 * Get the location of the left-most adjacent black point or the
         * location of the parameter point if all of the pixels to the left
         * are white.
	 * @return the left-most Point that is black or the given point
         * if none of the three pixels tested are black.
	 */
	private Point traverseLeft(Point point)
	{
            Point left = new Point(point.getX() - 1, point.getY());
            Point bottom_left = new Point(point.getX() - 1, point.getY() + 1);
            Point bottom = new Point(point.getX(), point.getY() + 1);
            
            if (left.isBlack(diagram))
            {
                return left;
            }
            else if (bottom_left.isBlack(diagram))
            {
                return bottom_left;
            }
            else if (bottom.isBlack(diagram))
            {
                return bottom;
            }
            
            //If no adjacent points were black, return the given point.
            else
            {
                return point;
            }
        }
		
	/**
	 * Get the location of the right-most adjacent black point or the
         * location of the parameter point if all of the pixels to the right
         * are white.
	 * @return the right-most Point that is black or the given point
         * if none of the three pixels tested are black.
	 */
	private Point traverseRight(Point point)
	{
            Point right = new Point(point.getX() + 1, point.getY());
            Point bottom_right = new Point(point.getX() + 1, point.getY() + 1);
            Point bottom = new Point(point.getX(), point.getY() + 1);
            
            if (right.isBlack(diagram))
            {
                return right;
            }
            else if (bottom_right.isBlack(diagram))
            {
                return bottom_right;
            }
            else if (bottom.isBlack(diagram))
            {
                return bottom;
            }
            
            //If no adjacent points were black, return the given point.
            else
            {
                return point;
            }	
	}
	

	/**
	 * Traverse the slope at the rate of the slope. Stop when
	 * you reach the last black point.
	 * @param initial_point
	 * @param slope
	 * @return last black point
	 */
	private Point traverseSlope(Point initial_point, Point slope)
	{
                int slopeX = slope.getX();
                int slopeY = slope.getY();
                Point currPoint = initial_point;
                
                boolean lastBlack = false;
                while(lastBlack = false) {
                   Point next_point = new Point(slopeX - currPoint.getX(), slopeY - currPoint.getY()); //I'm not sure if I should be adding or subtracting these values
                   // also I'm not entirely sure why netbeans is telling me I'm not using next_point because I am in the next line
                   if(next_point.isBlack(diagram)) {
                       currPoint = next_point; 
                   } else {
                       lastBlack = true;
                   }
                }
                return currPoint;
        }
                
                
		
        //return the point where the next point is NOT going to be black
		
		return null;
	}
        
}
