package faa_ocr.ADTs;

import java.awt.image.BufferedImage;

/**
 * Represents a Cartesian (x,y) point
 * @author Joe Kvedaras
 *
 */
public class Point 
{
	double x;
	double y;

	public Point(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}
	
	public Point(double x, double y) 
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Return the X coordinate
	 * 
	 * @return X
	 */
	public int getX() 
	{
		return (int) x;
	}

	/**
	 * Return the Y coordinate
	 * 
	 * @return Y
	 */
	public int getY() 
	{
		return (int) y;
	}

	/**
	 * Find the midpoint between 2, x-y coordinates
	 * @param point_two is the second point to be used to find the
         * midpoint.
	 * @return midpoint of this point and the point passed as an argument.
	 */
	public Point findMidpoint(Point point_two) 
	{
		int xx = (int) Math.floor((this.x + point_two.getX()) / 2);
		int yy = (int) Math.floor((this.y + point_two.getY()) / 2);
		return new Point(xx, yy);
	}

	/**
	 * Determine whether the point provided is black or not.
	 * 
	 * @param diagram
	 *            is the picture to use when getting the color.
	 * @return true if the point is black and false if it isn't.
	 */
	public boolean isBlack(BufferedImage diagram) 
	{
		final int pixel_color = diagram.getRGB(getX(), getY());
		final int red = (pixel_color >> 16) & 0xff;
		final int green = (pixel_color >> 8) & 0xff;
		final int blue = (pixel_color) & 0xff;
		
//TODO: making them equal 0	
//		return (red == 0 && green == 0 && blue == 0);
		return (red < 100 && green < 100 && blue < 100);
	}
	
	
        /**
         * Test for equality between a Point and a given object.
         * @param object is the Object to test for equivalence.
         * @return true if the Object is a Point and has the same x and y
         * values, false otherwise.
         */
	public boolean equals(Object object) 
	{
		// Can't be equal if the object isn't a point.
		if (!(object instanceof Point)) 
                {
                    return false;
		}

		// Cast the object as a point, since it is an instance of Point.
		Point point = (Point) object;

		// If the coordinates are equal, the points are the same.
		return (point.getX() == x && point.getY() == y);
	}
	
	/**
	 * Add two points together returning a new Point object
	 * @param point to add to this point
	 * @return a new point with the x and y values added together
	 */
	public Point add(Point point)
	{
		return new Point(this.x + point.getX(), this.y + point.getY());
	}
	
	/**
	 * Subtract two points together returned a new Point object
	 */
	public Point subtract(Point that)
	{
		return new Point(this.x - that.getX(), this.y - that.getY());
	}
}
