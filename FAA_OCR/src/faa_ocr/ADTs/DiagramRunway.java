package faa_ocr.ADTs;

import java.util.ArrayList;

/**
 * We make the initial find of a runway a DiagramRunway so we can
 * save the start point, end point, and length. This allows us to
 * go back after we have found all the runways for the diagram and
 * remove any duplicates and find intersections of the runways
 * in x-y space.
 *
 * @author Joe Kvedaras
 *
 */
public class DiagramRunway
{
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
        intersections = new ArrayList<Point>();
    }

    public String toString()
    {
        return "Starting point: " + " X:" + start.getX() + " Y:" + start.getY()
               + "\nEnd Point: " + " X:" + end.getX() + " Y:" + end.getY()
               + "\nSlope: Y:" + slope.getY() + " X:" + slope.getX()
               + "\nLength: " + length;
    }

    /**
     * Print the current runway
     */
    public void printRunway()
    {
        System.out.println("Starting point: " + " X:" + start.getX() + " Y:" + start.getY());
        System.out.println("End Point: " + " X:" + end.getX() + " Y:" + end.getY());
        System.out.println("Slope: Y:" + slope.getY() + " X:" + slope.getX());
        System.out.println("Length: " + length);
        System.out.println();
    }

    /**
     * Add an intersection point to this runway
     *
     * @param point of intersection
     */
    public void addIntersection(Point point)
    {
        intersections.add(point);
    }

    /**
     * Get intersection at a specific index
     *
     * @param index
     * @return point at that index
     */
    public Point getIntersection(int index)
    {
        return intersections.get(index);
    }

    /**
     * Return the array list of intersections
     *
     * @return array list of intersections
     */
    public ArrayList<Point> returnIntersections()
    {
        return intersections;
    }

    /**
     * Get the start point of this runway
     *
     * @return start point
     */
    public Point getStartPoint()
    {
        return start;
    }

    /**
     * Get the end point of this runway
     *
     * @return end point
     */
    public Point getEndPoint()
    {
        return end;
    }

    /**
     * Get the length of the runway
     *
     * @return length of this runway
     */
    public double getLength()
    {
        return length;
    }

}
