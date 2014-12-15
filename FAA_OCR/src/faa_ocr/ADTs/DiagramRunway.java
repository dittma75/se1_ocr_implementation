package faa_ocr.ADTs;

import java.util.ArrayList;

public class DiagramRunway {
	Point start;
	Point end;
	Slope slope;
	double length;
	ArrayList<Point> intersections;
	
	public DiagramRunway(Point start, Point end, Slope slope, double length)
	{
		this.start = start;
		this.end = end;
		this.slope = slope;
		this.length = length;
		intersections = new ArrayList<Point> ();
	}
	
	public String toString()
	{
		return "So.. Much.. Fun..";
	}
	
	public void printRunway()
	{
    	System.out.println("Starting point: " + " X:" + start.getX() + " Y:" + start.getY());
    	System.out.println("End Point: " + " X:" + end.getX() + " Y:" + end.getY());
    	System.out.println("Slope: Y:" + slope.getY() + " X:" + slope.getX());
    	System.out.println("Length: " + length);
    	System.out.println();
	}
	
	public void addIntersection(Point point)
	{
		intersections.add(point);
	}
	
	public Point getIntersection(int index)
	{
		return intersections.get(index);
	}
	
	public ArrayList<Point> returnIntersections(){
		return intersections;
	}
	
	public Point getStartPoint(){
		return start;
	}
	
	public Point getEndPoint()
	{
		return end;
	}
	
}
