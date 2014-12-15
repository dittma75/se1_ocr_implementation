package faa_ocr.image_parser;

import faa_ocr.ADTs.Point;
import faa_ocr.ADTs.Slope;

public class MyRunway {
	Point start;
	Point end;
	Slope slope;
	double length;
	
	public MyRunway(Point start, Point end, Slope slope, double length)
	{
		this.start = start;
		this.end = end;
		this.slope = slope;
		this.length = length;
	}
	
	public String toString()
	{
		return "Ehhh";
	}
	
	public void printRunway()
	{
    	System.out.println("Starting point: " + " X:" + start.getX() + " Y:" + start.getY());
    	System.out.println("End Point: " + " X:" + end.getX() + " Y:" + end.getY());
    	System.out.println("Slope: Y:" + slope.getY() + " X:" + slope.getX());
    	System.out.println("Length: " + length);
    	System.out.println();
	}
	
}
