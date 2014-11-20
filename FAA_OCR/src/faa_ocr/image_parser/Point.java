package faa_ocr.image_parser;

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
		return new Point(xx,yy);
	}
	
	
}
