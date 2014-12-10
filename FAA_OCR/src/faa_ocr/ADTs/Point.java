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
	 * 
	 * @param one
	 * @param two
	 * @return midpoint of two parameters.
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
		return (red < 20 && green < 20 && blue < 20);
	}
	
	
	public boolean equals(Object object) 
	{
		// Can't be equal if the object isn't a point.
		if (!(object instanceof Point)) {
			return false;
		}

		// Cast the object as a point, since it is an instance of Point.
		Point point = (Point) object;

		// If the coordinates are equal, the points are the same.
		return (point.getX() == x && point.getY() == y);
	}
}
