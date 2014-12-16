
package faa_ocr.image_parser;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import faa_ocr.ADTs.Airport;
import faa_ocr.ADTs.DiagramRunway;
import faa_ocr.ADTs.Point;
import faa_ocr.ADTs.Slope;


/**
 * 
 * @author Joe Kvedaras
 * @author Kevin Dittmar
 */
public class RunwayDiagramParser 
{
	int startY = 0;
	int startX = 0;
	
	//Min acceptance length for any runway
	private final int runway_acceptance_length = 150;
	//min and max width of any runway
	private final int runway_width_min = 2;
	private final int runway_width_max = 20;
	//min and max right counter for a runway to be vertical
	private final int runway_rightcounter_min = 3;
	private final int runway_rightcounter_max = 10;
	//min difference between starting points of runways
	private final int runway_start_difference = 4;
	//slope correction
	private final int slope_correction = 2;
	
	private BufferedImage diagram;
//	private Airport airport;
//	private int runways_left;
	private ArrayList <DiagramRunway> runways;

	public RunwayDiagramParser()
	{
            //do nothing
		runways = new ArrayList <DiagramRunway>();
	}
	
	/**
	 * Find the paths of all runways in the diagram and adds their paths to
	 * the Airport object specified.
	 * @param diagram	is the airport diagram image to parse for runways
	 * @param airport	the airport to which runway path data should be added
	 */
	public ArrayList<DiagramRunway> parseRunways(BufferedImage diagram, Airport airport)
	{
            this.diagram = diagram;
//            this.airport = airport;    
//TODO:Commented out for testing
//            this.runways_left = airport.numRunways();
            
            traverseImage();
            return runways;
	}
	
	/**
	 * Traverse image looking for black pixels. Once a black pixel is found recursively 
	 * find edges of that black square
	 */
	private void traverseImage()
	{
            for (int y = startY; y < diagram.getHeight(); y++) 
            {
                for (int x = startX; x < diagram.getWidth(); x++) 
                {
                    Point pixel = new Point(x,y);
                    if (pixel.isBlack(diagram))
                    {
                            if (checkPixel(pixel))
                            {
                                    //see if the pixel is a runway
                                    checkForCorner(pixel);
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
            
            //Clean up runways we received from the diagram to remove bad data
            cleanUpRunways();

	}
	
	
	//clear out duplicate runways
	//if 2 runways have same end point take longest runway!
	private void cleanUpRunways()
	{
		for (int i = 0; i < runways.size(); i++)
		{
			DiagramRunway alpha = runways.get(i);
			for(int k = i + 1; k < runways.size(); k++)
			{
				DiagramRunway beta = runways.get(k);
				if(alpha.getEndPoint().equals(beta.getEndPoint())) //if the ends are equal remove shortest runway
				{
					if(alpha.getLength() > beta.getLength()){
						runways.remove(k);
					}
					else{
						runways.remove(i);
					}
				}
				
				//both starts are equal
				if(alpha.getStartPoint().equals(beta.getStartPoint())) //if the starts are equal remove shortest runway
				{
					if(alpha.getLength() > beta.getLength()){
						runways.remove(k);
					}
					else{
						runways.remove(i);
					}
				}
				
				
			}
		}
		
//		for(DiagramRunway charlie: runways){
//			charlie.printRunway();
//		}
	}
	
	
	
	private boolean checkPixelInRunways(Point point)
	{
		for(DiagramRunway runway: runways)
		{ 
			if(Math.abs(runway.getStartPoint().getX() - point.getX()) < runway_start_difference && 
					Math.abs(runway.getStartPoint().getY() - point.getY()) < runway_start_difference){
				return true;
			}
		}
		return false;
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
            
            //Point(0,0) is the top left corner of the document so the pixels
            //above a certain point have a smaller y coordinate
            Point left = pixel.adjustPoint(-1, 0);
            Point topLeft = pixel.adjustPoint(-1, -1);
            Point top = pixel.adjustPoint(0,-1);
            Point topRight = pixel.adjustPoint(1,-1);
            
            if(left.isBlack(diagram)) 
            {
            	return false;
            } 
            else if(topLeft.isBlack(diagram)) 
            {
                return false;
            } 
            else if(top.isBlack(diagram)) 
            {
            	Point doubleCheckRight = top.adjustPoint(1, 0);
            	Point doubleCheckTop = top.adjustPoint(0,-1);
            	Point right = pixel.adjustPoint(1, 0);
            	Point bottom_right = pixel.adjustPoint(1,1);
            	
            	if(!doubleCheckRight.isBlack(diagram) && right.isBlack(diagram) && bottom_right.isBlack(diagram) && !doubleCheckTop.isBlack(diagram))
            	{
            		return true;
            	}
            	else
            	{
            		return false;
            	}
            }
            else if(topRight.isBlack(diagram)) 
            {
            	//To handle the one condition on DWR with the horizontal runway not being taken
            	Point doubleCheckRight = topRight.adjustPoint(2,-1);
            	Point doubleCheckTopRight = topRight.adjustPoint(1,-1);
            	
                if(!doubleCheckRight.isBlack(diagram) && doubleCheckTopRight.isBlack(diagram)){
                	return true;
                }
                else{
                	return false;
                }
            } 
            else 
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
	private void checkForCorner(Point pixel)
	{

            Point bottom_left = pixel.adjustPoint(-1,1);
            Point bottom = pixel.adjustPoint(0,1);
            Point bottom_right = pixel.adjustPoint(1,1);
            Point right = pixel.adjustPoint(1,0);

            //Check to see if pixels around the initial point are black
            boolean bottom_left_black = bottom_left.isBlack(diagram);
            boolean bottom_black = bottom.isBlack(diagram);
            boolean bottom_right_black = bottom_right.isBlack(diagram);
            boolean right_black = right.isBlack(diagram);
            
            //The surrounding pixels must be black so we know it is a runway
            /* check r+br+b, bl+b+br, r+br+b+bl, and br + b */
            if((bottom_right_black && bottom_black && bottom_left_black) ||
               (right_black && bottom_right_black && bottom_black) ||
               (right_black && bottom_right_black && bottom_black && bottom_left_black ||
               (bottom_right_black && bottom_black)))
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
            
            //right counter is used to count the traversals to the right.
            //If we reach between 3-5 right traversals then start going down,
            //that runway must be vertical.
            int right_counter = 0;
            
            
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
                    break;
                }
                else
                {
                	left_point = end_of_width;
                }
                

                /* The next point on the right traversal path may be the end
                 * of the width of the runway.
                 */
                end_of_width = traverseRight(right_point);
                if (right_point.equals(end_of_width))
                {   
                    width_found = true;
                    break;
                }
                else
                {
                	/*
                	 * We check to see if pixel returned from end_of_width is to the right of the right_point,
                	 * If it is, increase right counter because the runway may be a vertical runway.
                	 */
                	if(end_of_width.getX() - right_point.getX() == 1 && (end_of_width.getY() == right_point.getY()) )
                	{
                		right_counter++;
                		right_point = end_of_width;
                	}
                	else
                	{
                		/*
                		 * Since we did not go right, we need to see how many times we have gone to the right before
                		 * changing direction. If it is between 3 and 10, we can safely assume the runway is vertical
                		 */
                		if(right_counter > runway_rightcounter_min && right_counter < runway_rightcounter_max 
                				&& end_of_width.getY() - right_point.getY() == 1){ 
                			width_found = true;
                			end_of_width = right_point;
                			break;
                		}
                		else{
                			//reset right counter because the runway is not vertical
                			right_counter = 0;
                			right_point = end_of_width;
                		}
                	}
                	//right_point = end_of_width;
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
            int slope_x = end_of_width.getX() - initial_point.getX();
            int slope_y = end_of_width.getY() - initial_point.getY();
            Slope slope = new Slope(slope_y, slope_x);
            slope.invertSlope();
            
            //The width of the runway will be used to check if a runway is valid
            int width_of_runway = (int) findLength(initial_point, end_of_width) + 1;
            
            Point midpoint_of_runway = initial_point.findMidpoint(end_of_width);
            
            if(width_of_runway > runway_width_min && width_of_runway < runway_width_max && !checkPixelInRunways(midpoint_of_runway)){
 //           	addToAirport(midpoint_of_runway, slope, width_of_runway);
            	addToRunways(midpoint_of_runway, slope, width_of_runway);
            }
            else
            {
            	//runway was not wide enough
            }            
            
	}
	
	/**
	 * Find the end point of the runway and add the runway we find 
	 * to the list of possible runways for this diagram
	 * @param midpoint
	 * @param slope
	 * @param width_of_runway
	 */
	private void addToRunways(Point midpoint, Slope slope, int width_of_runway)
	{
		Point end_point = traverseSlope(midpoint, slope, width_of_runway);
		
		double runwayLength = findLength(midpoint.getX(),
                midpoint.getY(),
                end_point.getX(),
                end_point.getY());
		
		if (runwayLength > runway_acceptance_length)
		{
			runways.add(new DiagramRunway(midpoint, end_point, slope, runwayLength));
		}
	}
	
        
        /**
         * Find the length in pixels of two x,y coordinates
         * 
         * @param x coordinate for the first point
         * @param y coordinate for the first point
         * @param x coordinate for the second point
         * @param y coordinate for the second point
         */
        private double findLength(int x1, int y1, int x2, int y2) 
        {
            return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
        }
        
        /**
         * Find the length in pixels of two points
         * 
         * @param x coordinate for the first point
         * @param y coordinate for the first point
         * @param x coordinate for the second point
         * @param y coordinate for the second point
         */
        private double findLength(Point one, Point two) 
        {
            int x1 = one.getX();
            int y1 = one.getY();
            int x2 = two.getX();
            int y2 = two.getY();
        	
        	return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
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

            Point left = point.adjustPoint(-1, 0);
            Point bottom_left = point.adjustPoint(-1,1);
            Point bottom = point.adjustPoint(0,1);
            
            Point right = point.adjustPoint(1,0);
            Point bottom_right = point.adjustPoint(1,1);
            
            /*
             * Need to check the right and bottom right pixel to make sure we are still on the runway.
             * In some situations, we can follow random black pixels to no mans land
             */
            if(right.isBlack(diagram) || bottom_right.isBlack(diagram))
            {
            	if (left.isBlack(diagram))
                {
                    return left;
                }
                else if (bottom.isBlack(diagram))
                {
                    return bottom;
                }
                else if (bottom_left.isBlack(diagram))
                {
                    return bottom_left;
                }

                
                //If no adjacent points were black, return the given point.
                else
                {
                    return point;
                }
            }
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
            Point right = point.adjustPoint(1,0);
            Point bottom_right = point.adjustPoint(1,1);
            Point bottom = point.adjustPoint(0,1);
            
            Point left = point.adjustPoint(-1,0);
            Point bottom_left = point.adjustPoint(-1,1);
            
            /*
             * Need to check the left and bottom left pixel to make sure we are still on the runway.
             * In some situations, we can follow random black pixels to no mans land
             */
            if(left.isBlack(diagram) || bottom_left.isBlack(diagram) || right.isBlack(diagram)){
            	if (right.isBlack(diagram))
                {
                    return right;
                }
                else if (bottom.isBlack(diagram))
                {
                    return bottom;
                }
                else if (bottom_right.isBlack(diagram))
                {
                    return bottom_right;
                }

                
                //If no adjacent points were black, return the given point.
                else
                {
                    return point;
                }	
            }
            else
            {
            	return point;
            }
            
	}
	
	/**
	 * Return the greatest common divisor of two longs
	 */
	 private static int gcd(int a, int b) {
	   if (b == 0) 
		   return a;
	   else
		   return gcd(b, a % b);
	 } 
	
	
	
	/**
	 * Traverse the slope at the rate of the slope. Stop when
	 * you reach the last black point.
	 * @param initial_point
	 * @param slope
	 * @return last black point
	 */
	private Point traverseSlope(Point initial_point, Slope slope, int width_of_runway)
	{
                int slopeX = slope.getX();
                int slopeY = slope.getY();
                Point curr_point = initial_point;
                
                //Find the slope of the width of the runway
                slope.invertSlope();
                int slope_width_X = slope.getX();
                int slope_width_Y = slope.getY();
                //simplify slope with gcd. Slope is too big otherwise
                int gcd = gcd(slope_width_X, slope_width_Y);
                if (gcd > 0)
                {
                	slope_width_X = slope_width_X / gcd;
                    slope_width_Y = slope_width_Y / gcd;
                }
                else //We have to simplify the slope in every situation because it will be to big otherwise.
                {
                	//easiest way to accomplish this is to make both numbers even so we can simplify by 2 no matter what
                	if(slope_width_X % 2 != 0)
                	{
                		slope_width_X ++;
                	}
                	if (slope_width_Y % 2 != 0)
                	{
                		slope_width_Y ++;
                	}
                	slope_width_X = slope_width_X / 2;
                    slope_width_Y = slope_width_Y / 2;
                }
                
       
                /*
                 * Runways may be vertical, horizontal, or on some sort of a slant. a vertical runway has a slope of 
                 * y / 0, so we have to check for that condition and handle it appropiatly. A horizontal runway will have
                 * a slope of 0/0 so we have to check for that condition and handle it. Other wise If the slope of the 
                 * runway is positive, we need to set the left_wing and right_wing normally.
                 * If the slope is negative, we need to flip them so they work correctly
                 */
                Point left_wing;
                Point right_wing;
                Point left_wing_calculate;
                Point right_wing_calculate;
                //The runway is horizontal and will not have a slope.
                if(Integer.signum(slopeX) == 0 && Integer.signum(slopeY) == 0)
                {
                	left_wing_calculate = new Point(0, width_of_runway / 2);
                	right_wing_calculate = new Point(0, - width_of_runway / 2);
                	
                	boolean lastBlack = false;
                	while(lastBlack == false)
                	{
                		Point next_point = curr_point.adjustPoint(1, 0);
                        //calculate wings for the next point
                        left_wing = next_point.add(left_wing_calculate);
                        right_wing = next_point.add(right_wing_calculate);
                        
                        if(next_point.isBlack(diagram))
                        {
                        	curr_point = next_point;
                        }
                        else if(left_wing.isBlack(diagram) || right_wing.isBlack(diagram))
                        {
                        	curr_point = next_point;
                        }
                        else
                        {
                        	next_point = next_point.adjustPoint(1,0);
                        	//Check 2 pixels ahead of the current point to make sure we are at the end of the runway
                        	if(next_point.isBlack(diagram))
                        	{
                        		curr_point = next_point;
                        	}
                        	//The next point is still not black so we must be at the end of the runway
                        	else 
                        	{
                        		return curr_point;
                        	}
                        	
                        }
                	}
                }
                //The slope of the runway is undefined so we know it is vertical
                else if(Integer.signum(slopeX) == 0 && Integer.signum(slopeY) != 0)
                {
                	left_wing_calculate = new Point(width_of_runway / 2, 0);
                	right_wing_calculate = new Point(- width_of_runway / 2, 0);
                	
                	boolean lastBlack = false;
                	while(lastBlack == false)
                	{
                		Point next_point = curr_point.adjustPoint(0,1);
                        //calculate wings for the next point
                        left_wing = next_point.add(left_wing_calculate);
                        right_wing = next_point.add(right_wing_calculate);
                        
                        if(next_point.isBlack(diagram))
                        {
                        	curr_point = next_point;
                        }
                        else if(left_wing.isBlack(diagram) || right_wing.isBlack(diagram))
                        {
                        	curr_point = next_point;
                        }
                        else
                        {
                        	
                        	next_point = next_point.adjustPoint(0,1);
                        	//Check 2 pixels ahead of the current point to make sure we are at the end of the runway
                        	if(next_point.isBlack(diagram))
                        	{
                        		curr_point = next_point;
                        	}
                        	//The next point is still not black so we must be at the end of the runway
                        	else 
                        	{
                        		return curr_point;
                        	}
                        }
                	}
                }
                else if(Integer.signum(slopeX) == 1)
                {
                	left_wing_calculate = new Point(slope_width_X, slope_width_Y);
                	right_wing_calculate = new Point(-slope_width_X, -slope_width_Y);
                }
                //The slope of X must be negative so flip right_wing and left_wing
                else
                {
                	right_wing_calculate = new Point(slope_width_X, slope_width_Y);
                	left_wing_calculate = new Point(-slope_width_X, -slope_width_Y);
                }

                
                boolean lastBlack = false;
                while(lastBlack == false)
                {
                   //To get the next point, add the slope to the current point.
                   Point next_point = new Point(
                           curr_point.getX() + slopeX, 
                           curr_point.getY() + slopeY
                   );
                   
                   //calculate wings for the next point
                   left_wing = next_point.add(left_wing_calculate);
                   right_wing = next_point.add(right_wing_calculate);
                   
                                      
                   if(left_wing.isBlack(diagram) && right_wing.isBlack(diagram))
                	   //Both wings are black so we are still in the middle of the runway.
                	   //continue forward
                   {
                	   curr_point = next_point;
                   }
                   else if (left_wing.isBlack(diagram) && !right_wing.isBlack(diagram))
                	   //Left wing is black and the right wing is not.
                	   //Correct ourselves to the left so we stay in the middle of the runway
                   {
                	   curr_point = next_point.adjustPoint(-slope_correction,0);
                   }
                   else if (!left_wing.isBlack(diagram) && right_wing.isBlack(diagram))
                	   //right wing is black and the left wing is not.
                	   //Correct ourselves to the right so we stay in the middle of the runway
                   {
                	   curr_point = next_point.adjustPoint(slope_correction,0);
                   }
                   else if (next_point.isBlack(diagram))
                   {
                	   //Even though the wings are not black, the next pixel is black so we can
                	   //continue to traverse the runway
                	   curr_point = next_point;
                   }
                   else
                   {
                	   /*
                	    * The next_point is not black and the wings are not black. We are either
                	    * at the end of the runway, off the runway, or in white text. Look
                	    * around at the surrounding pixels to decide what we should do.
                	    */
                	   //Lets check to see if the next pixel ahead of next_point && next pixel ahead of curr pixel is black,
                	   //if they both are true, keep traversing
                	   Point probe_current_point = curr_point.adjustPoint(0,1);
                	   Point probe_next_point = next_point.adjustPoint(0,1);
                	   if(probe_current_point.isBlack(diagram) && probe_next_point.isBlack(diagram))
                	   {
                		   curr_point = next_point;
                	   }
                	   else
                	   {   
                		   lastBlack = true;
                		   
                	   }
                	   
                   }

                }
                return curr_point;
	}

	
}