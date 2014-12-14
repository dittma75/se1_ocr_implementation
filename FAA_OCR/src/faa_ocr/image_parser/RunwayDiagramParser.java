package faa_ocr.image_parser;

import faa_ocr.ADTs.*;

import java.awt.image.BufferedImage;


/**
 * 
 * @author Joe Kvedaras
 * @author Kevin Dittmar
 */
public class RunwayDiagramParser 
{
	private BufferedImage diagram;
	private Airport airport;
	private int runways_left;

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
//TODO:Commented out for testing
//            this.runways_left = airport.numRunways();
            
            traverseImage();
	}
	
	/**
	 * Traverse image looking for black pixels. Once a black pixel is found recursively 
	 * find edges of that black square
	 */
	private void traverseImage()
	{
		//ACY Points
	//114, 357 is first runway
	//361, 85 is second runway
	//285, 365 is giving us trouble in ACY.
        System.out.println("Size of diagram: " + 
                diagram.getHeight() + " " + 
                diagram.getWidth());
		
		
	//Atlanta
        //1st runway: 325,140
        
    //DFW
        //2nd runway: 170,252
        //3rd: 291, 96
		//problem: 474, 97
            for (int y = 503; y < diagram.getHeight(); y++) 
            {
                for (int x = 97; x < diagram.getWidth(); x++) 
                {
                    Point pixel = new Point(x,y);
                    if (pixel.isBlack(diagram))
                    {
                            if (checkPixel(pixel))
                            {
                                    //see if the pixel is a runway
                                    checkCorner(pixel);
                                    //System.out.println("X:" + x + " Y:" + y);
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
            } 
            else if(topLeft.isBlack(diagram)) 
            {
                return false;
            } 
            else if(top.isBlack(diagram)) 
            {
                return false;
            }
//TODO: removed because it gives some problems for a couple runways            
            else if(topRight.isBlack(diagram)) 
            {
                return false;
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
            if((bottom_right_black && bottom_black && bottom_left_black) ||
               (right_black && bottom_right_black && bottom_black) ||
               (right_black && bottom_right_black && bottom_black && bottom_left_black ||
       	   (bottom_right_black && bottom_black)))//TODO: added condition for bottom and bottom right.
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
            
            /* We will need to know where we started for later, so we have to
             * save the initial point.
             */
            Point runway_start = initial_point;

            
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
                    /* Our starting place should two pixels to the left of
                     * our starting place, which was the upper right corner.
                     */
                    runway_start = traverseLeft(traverseLeft(runway_start));
                    
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
                    /* Our starting place should two pixels to the right of
                     * our starting place, which was the upper left corner.
                     */
                    runway_start = traverseRight(traverseRight(runway_start));
                    
                    width_found = true;
                    break;
                }
                else
                {
                	right_point = end_of_width;
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
            
            //Length of the width of the runway used when we traverse at the rate
            	//of the slope
            int width_of_runway = (int) findLength(initial_point, end_of_width);
            
            
            Point midpoint_of_runway = initial_point.findMidpoint(end_of_width);
            
            addToAirport(midpoint_of_runway, slope, width_of_runway);
            
	}
	
	/**
	 * Traverse the runway at the rate of the slope and add those points to the airport
	 * @param midpoint of the current runway
	 * @param slope of the current runway
	 */
	private boolean addToAirport(Point midpoint, Slope slope, int width_of_runway)
	{
		Point end_point = traverseSlope(midpoint, slope, width_of_runway);
                
                double runwayLength = findLength(midpoint.getX(),
                                                 midpoint.getY(),
                                                 end_point.getX(),
                                                 end_point.getY());
                
                if(runwayLength > 150) 
                {
                	
                	
//TODO: For testing only
                	System.out.println("Runway Found!");
                	System.out.println("Starting point: " + " X:" + midpoint.getX() + " Y:" + midpoint.getY());
                	System.out.println("End Point: " + " X:" + end_point.getX() + " Y:" + end_point.getY());
                	System.out.println("Length: " + runwayLength);
                	System.out.println();

                	
//TODO: end testing                	
                	
                	
                	
                	
//TODO:COMMENTED OUT BELOW TO TEST               	
//                    //Translate the midpoint and end_point from x/y to lat/long
//                    float mid_long = airport.longitudeConversion(midpoint);
//                    float mid_lat = airport.latitudeConversion(midpoint);
//                    Node startNode = new Node(mid_long, mid_lat);
//
//                    float end_long = airport.longitudeConversion(end_point);
//                    float end_lat = airport.latitudeConversion(end_point);
//                    Node endNode = new Node(end_long, end_lat);
//
//                    /* Add points to existing Runway instance in airport
//                     * object.  Each physical runway is two runways, and they
//                     * are stored consecutively in pairs.  Hence, we add
//                     * the start and end nodes to the next two runways, since
//                     * they represent the same physical runway.
//                     */
//                    for (int i = 0; i < 2; i++)
//                    {
//                        Runway runway = airport.getRunway(
//                                airport.numRunways() - runways_left
//                        );
//                        runway.addPathNode(startNode);
//                        runway.addPathNode(endNode);
//                    }
                    return true;
                }
                //This line isn't long enough to be a runway.
                else 
                {
                    return false;
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
	 * Midpoint correction
	 */
//TODO: may have to return a point or be void
        private Point midpointCorrection(Point midpoint)
        {
        	Point correctMidpoint;
        	
        	Point leftTraversal;
        	Point rightTraversal;
        	Point leftTemp;
        	Point rightTemp;
        	
        	while(leftTraversal != leftTemp){
        		//keep calling traverse left
        	}
        	//do same for right
        	
        	//find midpoint and return it
        	
        	
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
            
            Point right = new Point(point.getX() + 1, point.getY());
            Point bottom_right = new Point(point.getX() + 1, point.getY() + 1);
            
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
                    return bottom; //TODO: Bottom has higher priority now
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
            Point right = new Point(point.getX() + 1, point.getY());
            Point bottom_right = new Point(point.getX() + 1, point.getY() + 1);
            Point bottom = new Point(point.getX(), point.getY() + 1);
            
            Point left = new Point(point.getX() - 1, point.getY());
            Point bottom_left = new Point(point.getX() - 1, point.getY() + 1);
            
            /*
             * Need to check the left and bottom left pixel to make sure we are still on the runway.
             * In some situations, we can follow random black pixels to no mans land
             */
            if(left.isBlack(diagram) || bottom_left.isBlack(diagram) || right.isBlack(diagram)){ //TODO: Does not take runway 61,517 on atlanta.
            	if (right.isBlack(diagram))
                {
                    return right;
                }
                else if (bottom.isBlack(diagram))
                {
                    return bottom; //TODO: bottom has higher priority now
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
	 * Traverse the slope at the rate of the slope. Stop when
	 * you reach the last black point.
	 * @param initial_point
	 * @param slope
	 * @return last black point
	 */
	private Point traverseSlope(Point initial_point, Slope slope)
	{
                int slopeX = slope.getX();
                int slopeY = slope.getY();
                Point curr_point = initial_point;
                
                boolean lastBlack = false;
                while(lastBlack == false)
                {
                   //To get the next point, add the slope to the current point.
                   Point next_point = new Point(
                           curr_point.getX() + slopeX, 
                           curr_point.getY() + slopeY
                   );
                   
                   if(next_point.isBlack(diagram)) 
                   {
                       curr_point = next_point; 
                   } else 
                   {
                       lastBlack = true;
                   }
                }
                return curr_point;
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
                 * If the slope of the runway is positive, we need to set the left_wing and right_wing normally.
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
                		Point next_point = new Point(curr_point.getX() + 1, curr_point.getY());
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
                        	next_point = new Point(next_point.getX() + 1, next_point.getY());
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
                //The slope of the runway is undefined so we know it is verticle
                else if(Integer.signum(slopeX) == 0 && Integer.signum(slopeY) != 0)
                {
                	left_wing_calculate = new Point(width_of_runway / 2, 0);
                	right_wing_calculate = new Point(- width_of_runway / 2, 0);
                	
                	boolean lastBlack = false;
                	while(lastBlack == false)
                	{
                		Point next_point = new Point(curr_point.getX(), curr_point.getY() + 1);
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
                        	
                        	next_point = new Point(next_point.getX(), next_point.getY() + 1);
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
                	   curr_point = new Point(next_point.getX() - 2, next_point.getY());
                   }
                   else if (!left_wing.isBlack(diagram) && right_wing.isBlack(diagram))
                	   //right wing is black and the left wing is not.
                	   //Correct ourselves to the right so we stay in the middle of the runway
                   {
                	   curr_point = new Point(next_point.getX() + 2, next_point.getY());
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
                	   lastBlack = true;
                   }

                }
                return curr_point;
	}
}
