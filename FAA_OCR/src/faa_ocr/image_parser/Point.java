package faa_ocr.image_parser;

import java.awt.image.BufferedImage;

/** Represents a Cartesian (x,y) point */
public class Point {
	private int x;
	private int y;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	

	/**
	 * Return the X coordinate
	 * @return X
	 */
	public int getX()
	{
		return x;
	}
	
	
	/**
	 * Return the Y coordinate
	 * @return Y
	 */
	public int getY()
	{
		return y;
	}
	
	
	/**
	 * Find the midpoint between 2, x-y coordinates
	 * @param one
	 * @param two
	 * @return midpoint of two parameters.
	 */
	public Point findMidpoint(Point point_two)
	{
		int xx = (int) Math.floor( (this.x + point_two.getX())/ 2 );
		int yy = (int) Math.floor( (this.y + point_two.getY())/ 2 );
		return new Point(xx,yy);
	}
	
	
	/**
	 * Calculate the slope between 2, x-y coordinates
	 * Slope = y2 - y1 / x2 - x1
	 * @param one
	 * @param two
	 * @return slope of the two points
	 */
	public Point calculateSlope(Point point_two)
	{
		int yy = point_two.getY() - this.y;
		int xx = point_two.getX() - this.x;
		//TODO: it may have to divide y by x to get a smaller slope.
		//if it is a decimal, make into a whole number.
		return new Point(yy,xx);
	}
	
	/**
	 * Inverse the slope to find a perpendicular line to that slope
	 * y/x = -x/y
	 * We assume the slope is the current point
	 * @param slope
	 * @return inverse slope 
	 */
	public Point inverseSlope()
	{
		return (new Point(-this.getX(), this.getY()));
	}
	
	/**
         * Determine whether the point provided is black or not.
         * @param diagram is the picture to use when getting the color.
         * @return true if the point is black and false if it isn't.
         */
        boolean isBlack(BufferedImage diagram)
        {
            final int pixel_color = diagram.getRGB(getX(), getY());
            final int red = (pixel_color >> 16) & 0xff;
            final int green = (pixel_color >> 8) & 0xff;
            final int blue = (pixel_color) & 0xff;
            return (red < 20 && green < 20 && blue < 20);
        }
        
        public boolean equals(Object object)
        {
            //Can't be equal if the object isn't a point.
            if (!(object instanceof Point))
            {
                return false;
            }
            
            //Cast the object as a point, since it is an instance of Point.
            Point point = (Point) object;
            
            //If the coordinates are equal, the points are the same.
            return (point.getX() == x && point.getY() == y);
        }
}
