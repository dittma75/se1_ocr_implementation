package faa_ocr.Controller;

import java.io.File;
import java.util.ArrayList;

import faa_ocr.ADTs.Airport;
import faa_ocr.ADTs.DiagramRunway;
import faa_ocr.ADTs.LineSegment;
import faa_ocr.ADTs.Node;
import faa_ocr.ADTs.Point;
import faa_ocr.ADTs.Runway;
import faa_ocr.image_parser.PDFToImage;
import faa_ocr.text_parser.PDFToText;
import faa_ocr.xml_parser.AirportToXML;
import faa_ocr.kml_parser.XMLtoKML;

import java.lang.Math;

/**
 * Class to control all airport behaviors
 *
 * @author Joe Kvedaras
 *
 */
public class AirportController
{
    /* For unit testing Argument Parser */
    public static void main(String[] args)
    {
        AirportController airport_controller = new AirportController();
        boolean is_rotated = false;

        //Accept list of list of PDFS
        for (String arg : args)
        {
            if (arg.startsWith("-"))
            {
                arg = arg.replaceAll("[- ]*", "");
                if (arg.equals("r"))
                {
                    is_rotated = true;
                }
            }
            else if (ArgumentParser.parseArgument(arg))
            {
                //Retrieve all information from a pdf
                airport_controller.getInformationFromPDF(arg, is_rotated);

                //A PDF has been parsed, so reset is_rotated flag.
                is_rotated = false;
            }
            else
            {
                System.out.println(arg + " is not a valid PDF");

                /* The file that was supposed to be rotated was not a PDF,
                 * so consume the rotation flag.
                 */
                is_rotated = false;
            }
        }
    }

    private PDFToText pdf_to_text;
    private PDFToImage pdf_to_image;
    private AirportToXML xml_parser;
    private XMLtoKML kml_parser;

    /**
     * Control all behaviors of an airport
     */
    public AirportController()
    {
        pdf_to_text = new PDFToText();
        pdf_to_image = new PDFToImage();
        xml_parser = new AirportToXML();
        kml_parser = new XMLtoKML();
    }

    /**
     * Create an airport and gather all information from the PDF
     *
     * @param path
     */
    private void getInformationFromPDF(String path, boolean rotated)
    {
        ArrayList<DiagramRunway> runways;

        //create a new airport with Sting path to PDF
        Airport airport = new Airport(path, rotated);

        //get textual data from PDF
        pdf_to_text.parseTextData(airport);

        //get visual data from PDF
        runways = pdf_to_image.parseVisualData(airport);

        //All all runways to airport and convert x/y to lat/long
        addToAirport(airport, runways);

        //turn Airport into an XML and save path to XML
        String path_to_xml = xml_parser.convertToXml(airport);

        //turn xml file into a kml file.
        String path_to_kml = kml_parser.writeKML(new File(path_to_xml));

        //print out results of transformations
        printResults(airport, path_to_xml, path_to_kml);
    }

    /**
     * Print resulting files from XML and KML
     *
     * @param airport
     * @param path_to_xml
     * @param path_to_kml
     */
    private void printResults(Airport airport, String path_to_xml, String path_to_kml)
    {
        System.out.println();
        System.out.println(airport.getName());
        System.out.println("Path to XML: " + path_to_xml);
        System.out.println("Path to KML: " + path_to_kml);
        System.out.println();
    }

    /**
     * Find all intersections between runways and runways, runways and taxiways,
     * taxiways and taxiways.
     *
     * @param airport
     */
    private void findIntersections(ArrayList<DiagramRunway> runways)
    {
        int num_runways = runways.size();

        /*
         * Loop through every combination of runways and runways seeing if
         * there is an intersection. If we find an intersection, we will add
         * that intersection to the pair of runways.
         */
        for (int ii = 0; ii < num_runways; ii = ii + 1)
        {
            DiagramRunway alpha = runways.get(ii);

            for (int kk = ii + 1; kk < num_runways; kk = kk + 1)
            {
                DiagramRunway beta = runways.get(kk);

                //Check to see if two runways are intersecting
                Point intersecting_point;
                intersecting_point = findIntersections(alpha, beta);
                if (intersecting_point != null)
                {
                    //add intersecting point to both runways
                    alpha.addIntersection(intersecting_point);
                    beta.addIntersection(intersecting_point);
                }
                else
                {
                    //intersecting_point was not found
                }
            }
        }

    }

    private Point findIntersections(DiagramRunway one, DiagramRunway two)
    {

        Point returnPoint = null;
        //See if two runways intersect
        LineSegment alpha = new LineSegment(
                one.getStartPoint(), one.getEndPoint()
        );
        
        LineSegment beta = new LineSegment(
                two.getStartPoint(), two.getEndPoint()
        );

        if (LineSegment.doBoundingBoxesIntersect(alpha.getBoundingBox(), 
                                                 beta.getBoundingBox()))
        {
            float x1 = one.getStartPoint().getX();
            float y1 = one.getStartPoint().getY();

            float x2 = one.getEndPoint().getX();
            float y2 = one.getEndPoint().getY();

            float x3 = two.getStartPoint().getX();
            float y3 = two.getStartPoint().getY();

            float x4 = two.getEndPoint().getX();
            float y4 = two.getEndPoint().getY();

            float one_slope = (y2 - y1) / (x2 - x1);
            float two_slope = (y4 - y3) / (x4 - x3);

            float b1 = y1 - (one_slope * x1);
            float b2 = y3 - (two_slope * x3);

            //x intersection
            float x_value = (b1 - b2) / (two_slope - one_slope);

            float y_value = (one_slope * x_value) + b1;

            if (Math.max(x1, x2) > x_value && Math.min(x1, x2) < x_value
                && Math.max(y1, y2) > y_value && Math.min(y1, y2) < y_value
                && Math.max(x3, x4) > x_value && Math.min(x3, x4) < x_value
                && Math.max(y3, y4) > y_value && Math.min(y3, y4) < y_value)
            {
                //System.out.println(x_value);
                //System.out.println(y_value);
                returnPoint = new Point((double) x_value, (double) y_value);
            }
            return returnPoint;

        }
        else
        {
            return returnPoint;
        }

    }

    /**
     * Traverse the runway at the rate of the slope and add those points to the
     * airport
     *
     * @param midpoint of the current runway
     * @param slope of the current runway
     */
    private boolean addToAirport(Airport airport, ArrayList<DiagramRunway> runways)
    {

        int runways_left = airport.numRunways();
        findIntersections(runways);

        for (int kk = 0; kk < runways.size(); kk++)
        {
            Point midpoint = runways.get(kk).getStartPoint();
            Point end_point = runways.get(kk).getEndPoint();
            ArrayList<Point> intersections = runways.get(kk).returnIntersections();

            //Translate the midpoint and end_point from x/y to lat/long
            float mid_long = airport.longitudeConversion(midpoint);
            float mid_lat = airport.latitudeConversion(midpoint);
            Node startNode = new Node(mid_long, mid_lat);

            float end_long = airport.longitudeConversion(end_point);
            float end_lat = airport.latitudeConversion(end_point);
            Node endNode = new Node(end_long, end_lat);

            /* Add points to existing Runway instance in airport
             * object.  Each physical runway is two runways, and they
             * are stored consecutively in pairs.  Hence, we add
             * the start and end nodes to the next two runways, since
             * they represent the same physical runway.
             */
            for (int i = 0; i < 2; i++)
            {
                Runway runway = airport.getRunway(
                        airport.numRunways() - runways_left
                );
                runway.addPathNode(startNode);
                runway.addPathNode(endNode);

                //Add all intersections to runway
                for (Point point : intersections)
                {
                    float point_long = airport.longitudeConversion(point);
                    float point_lat = airport.latitudeConversion(point);
                    Node pointNode = new Node(point_long, point_lat);
                    runway.addIntNode(pointNode);
                }

                runways_left--;

            }
        }
        return true;

    }

}
