package faa_ocr.ADTs;

/**
 * Represents a Line Segments
 * @author Joe Kvedaras
 *
 */
public class LineSegment {

	Point first;
    Point second;
    String name;

    /**
     * @param first the first point of this line
     * @param second the second point of this line
     */
    public LineSegment(Point a, Point b) 
    {
        this.first = a;
        this.second = b;
    }
    

    /**
     * Get the bounding box of this line by two points. The first point is in
     * the lower left corner, the second one at the upper right corner.
     *
     * @return the bounding box
     */
    public Point[] getBoundingBox() 
    {
        Point[] result = new Point[2];
        result[0] = new Point(Math.min(first.x, second.x), Math.min(first.y,
                second.y));
        result[1] = new Point(Math.max(first.x, second.x), Math.max(first.y,
                second.y));
        return result;
    }
    
    
    /**
     * Check if bounding boxes do intersect. If one bounding box
     * touches the other, they do intersect.
     * @param a first bounding box
     * @param b second bounding box
     * @return true if they intersect,false otherwise.
     */
    public static boolean doBoundingBoxesIntersect(Point[] a, Point[] b) 
    {
        return a[0].x <= b[1].x && a[1].x >= b[0].x && a[0].y <= b[1].y
                && a[1].y >= b[0].y;
    }

}
