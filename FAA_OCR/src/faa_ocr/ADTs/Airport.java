/**
 * Purpose: Software Engineering I: FAA OCR Project 
 * Status: Complete and thoroughly tested
 * Last update: 12/02/14
 * Submitted: 
 * Comment: Airport ADT
 * @author: Greg Richards, Kevin Dittmar
 * @version: 2014.11.20
 */

package faa_ocr.ADTs;
import faa_ocr.image_parser.PDFToImage;
import faa_ocr.text_parser.PDFToText;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Airport class is an abstract data type that represents the entire Airport
 * diagram .pdf file.  It contains all of the textual identification information that
 * makes each airport diagram unique, as well as all of the nodes that are to be
 * mapped onto the .xml file.
 */
public class Airport {
    private final String pdf_file_path;
    private String name;
    private String location;
    private String abbreviation;
    private float variation;
    private ArrayList<Runway> runways = new ArrayList<Runway>();
    //Not used because we didn't get to them.
    private ArrayList<Taxiway> taxiways = new ArrayList<Taxiway>();
    
    //Y-coordinate to latitude conversion factor.
    private int pixels_per_unit_lat;
    
    //X-coordinate to longitude conversion factor.
    private int pixels_per_unit_long;
    
    //Offset from top of diagram to first unit marker.
    private int latitude_offset;
    
    //Offset from left side of diagram to first unit marker.
    private int longitude_offset;
    
    /* The margins are the distance from edge of the diagram to the
     * first whitespace pixel of the diagram.  The x_margin is the
     * length of the left and right margins in pixels, and the y_margin
     * is the length of the top and bottom margins in pixels.
     */
    private int x_margin;
    private int y_margin;
    
    //True if half minutes are used; false if whole minutes are used.
    private final boolean USES_HALF_MINUTES;
    
    //The latitude that coordinates will be based on for this airport.
    private final float BASE_LATITUDE;
    
    //The latitude that coordinates will be based on for this airport.
    private final float BASE_LONGITUDE;
    
    public Airport (String pdf_file_path)
    {
        this.pdf_file_path = pdf_file_path;
        
        /* Set the scales for pixel coordinates to latitude and longitude
         * coordinates as well as the necessary offsets from the upper left
         * corner of the diagram.
         */
        findPixelConversionScales();
        String diagram_text = PDFToText.getDiagramText(pdf_file_path);
        USES_HALF_MINUTES = containsHalfMinutes(diagram_text);
        BASE_LATITUDE = findBaseLatitude(diagram_text);
        BASE_LONGITUDE = findBaseLongitude(diagram_text);
    }
    
    /**
     * Get the file path of the airport diagram PDF file.
     * 
     * @return the airport diagram file path as a String.
     */
    public String getFilePath()
    {
        return pdf_file_path;
    }
    
    /**
     * Get the name of the airport.
     * 
     * @return the name of the airport as a String.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Set the name of the airport to be the given String.
     * 
     * @param name is the String representation of the Airport's name.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Get the name of the location of the airport.
     * 
     * @return the location of the airport as a String.
     */
    public String getLocation()
    {
        return location;
    }
    
    /**
     * Set the location of the airport based on the given location.
     * 
     * @param location is the String representation of the airport location.
     */
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    /**
     * Get the abbreviation for the airport.
     * 
     * @return the abbreviation of the airport as a String.
     */
    public String getAbbreviation()
    {
        return abbreviation;
    }
    
    /**
     * Set the abbreviation of the airport.
     * 
     * @param abbreviation is the airport abbreviation as a String.
     */
    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }
    
    /**
     * Get the value that represents that variation from compass north and
     * true north.
     * 
     * @return the variation from true north.
     */
    public float getVariation()
    {
        return variation;
    }
    
    /**
     * Set the variation from true north.
     * 
     * @param variation is the variation from true north represented as a
     * float.
     */
    public void setVariation(float variation)
    {
        this.variation = variation;
    }
    /*
     * Get a Runway object located at a specified index number
    
     * @param the int index value of the Runway you wish to retrieve
     * @return the Runway object at index i
     */
    public Runway getRunway(int i)
    {
        return runways.get(i);
    }
    
    /**
     * get the number of Runway objects in this Airport
     * 
     * @return the integer value of how many Runways are in the collection
     * runways.
     */
    public int numRunways()
    {
        return runways.size();
    }
    
    /*
     * Add a Path object to the collection paths in the instance of Airport
    
     * @param the Path object to be added to the collection
     */
    public void addRunway(Runway runway)
    {
        runways.add(runway);
    }
    
    /**
     * get the number of Taxiway objects in the collection 
     * 
     * @return int value representing the size of the taxiways collection 
     */
    public int numTaxiways()
    {
        return taxiways.size();
    }
    
    /**
     * get a taxiway object at a specified position in the collection 
     * 
     * @param index int value representing the desired position in the collection
     * @return Taxiway object at the specified position in taxiways 
     */
    public Taxiway getTaxiway(int index)
    {
        return taxiways.get(index);
    }
    
    /**
     * add a taxiway object to the collection 
     * 
     * @param taxiway Taxiway object we are adding to the collection
     */
    public void addTaxiway(Taxiway taxiway)
    {
        taxiways.add(taxiway);
    }
    
    /**
     * Get the String representation of an Airport object, including all of the
     * current values of the object's data fields in an organized manner.
     * 
     * @return the String representation of the airport.
     */
    @Override
    public String toString()
    {
        String airportString = "";
        airportString += "File Path: " + pdf_file_path + "\n"
                        + "Name: " + name + "\n"
                        + "Location: " + location + "\n"
                        + "Abbreviation: " + abbreviation + "\n"
                        + "Variation: " + variation + "\n";
        for(int i = 0; i < numRunways(); i++) {
            airportString += "Path: " + getRunway(i).toString() + "\n";
        }
        return airportString;
    }
    
    /**
     * Gets the conversion factor between pixel length and longitude.
     * 
     * @param file_path the path to the airport diagram PDF.
     * @return the number of pixels in one unit of longitude (may be a half
     * degree or a whole degree).
     */
    private void findPixelConversionScales()
    {
        BufferedImage diagram = PDFToImage.makeImage(this.pdf_file_path);
        
        /* The longer side has a margin of 61 pixels, and the shorter side
         * has a margin of 25 pixels for all airport diagrams.  In other
         * words, the first whitespace pixel of the actual diagram is at
         * (25, 61) if the diagram is portrait and (61, 25) if landscape.
        */
        //landscape
        if (diagram.getWidth() > diagram.getHeight())
        {
            x_margin = 61;
            y_margin = 25;
        }
        //portrait
        else
        {
            x_margin = 25;
            y_margin = 61;
        }
        
        //The right end of the diagram.
        int end_width = diagram.getWidth() - x_margin - 1;
        
        //The bottom end of the diagram.
        int end_height = diagram.getHeight() - y_margin - 1;
        
        //Try to find the longitude scale at the top of the diagram.
        if (!findLongitudeScale(new Point(x_margin, y_margin), end_width))
        {
            //Try to find the longitude scale at the bottom of the diagram.
            if (!findLongitudeScale(new Point(x_margin, end_height),end_width))
            {
                //We failed to find a usable scale.
                System.err.println("Error:  Longitude could not be found.");
                System.exit(1);
            }
        }
        
        //Try to find the latitude scale on the left side of the diagram.
        if (!findLatitudeScale(new Point(x_margin, y_margin), end_height))
        {
            //Try to find the latitude scale on the right side of the diagram.
            if (!findLatitudeScale(new Point(end_width, y_margin), end_height))
            {
                //We failed to find a usable scale.
                System.err.println("Error:  Latitude could not be found.");
                System.exit(1);
            }
        }
        
    }
    
    /**
     * Try to find the longitude scale and report the success of the attempt.
     * 
     * @param current is the point to start searching from.
     * @param diagram_width_end is the end of the diagram when approaching
     * from the left.
     * @return true if a valid scale is found before the end of the diagram
     * and false otherwise.
     */
    private boolean findLongitudeScale(Point current, int diagram_width_end)
    {
        int black_pixels_found = 0;
        
        /* unit_in_pixels is a counter for the length of one unit on the
         * diagram's grid, which is probably one degree.
         */
        int unit_in_pixels = 0;
        
        /* There is an offset between the start of the diagram and the start
         * of a usable portion of the unit grid.
         */
        int grid_offset = 0;
        
        //We found the scale if we find two black pixels when searching.
        while (black_pixels_found < 2)
        {
            /* We found the start of a full unit's marker on the grid, so add
             * to the counter
             */
            if (black_pixels_found > 0)
            {
                unit_in_pixels++;
            }
            /* We haven't found the starting marker yet, so this is still part
             * of the offset of the grid.
             */
            else
            {
                grid_offset++;
            }
            //Advance to the next pixel to the right.
            current = new Point(current.getX() + 1, current.getY());
            /* If we are still looking for the end of the unit at the end of
             * the diagram, then we failed to find the scale.
             */
            if (current.getX() >= diagram_width_end)
            {
                return false;
            }
        }
        
        //Set the offset from the left side of the diagram to our unit marker.
        this.longitude_offset = grid_offset;
        
        //Set the pixels-to-longitude conversion factor
        this.pixels_per_unit_long = unit_in_pixels;
        
        //We found our scale information.
        return true;
    }
    
    /**
     * Try to find the latitude scale and report the success of the attempt.
     * 
     * @param current is the point to start searching from.
     * @param diagram_height_end is the end of the diagram when approaching
     * from the top.
     * @return true if a valid scale is found before the end of the diagram
     * and false otherwise.
     */
    private boolean findLatitudeScale(Point current, int diagram_height_end)
    {
        int black_pixels_found = 0;
        
        /* unit_in_pixels is a counter for the length of one unit on the
         * diagram's grid, which is probably one degree.
         */
        int unit_in_pixels = 0;
        
        /* There is an offset between the start of the diagram and the start
         * of a usable portion of the unit grid.
         */
        int grid_offset = 0;
        
        //We have found the scale if we find two black pixels.
        while (black_pixels_found < 2)
        {
            /* We found the start of a full unit's marker on the grid, so add
             * to the counter.
             */
            if (black_pixels_found > 0)
            {
                unit_in_pixels++;
            }
            /* We haven't found the starting marker yet, so this is still part
             * of the offset of the grid.
             */
            else
            {
                grid_offset++;
            }
            //Advance to the next pixel downward.
            current = new Point(current.getX(), current.getY() + 1);
            /* If we are still looking for the end of the unit at the end of
             * the diagram, then we failed to find the scale.
             */
            if (current.getY() >= diagram_height_end)
            {
                return false;
            }
        }
        
        //Set the offset from the top of the diagram to our unit marker.
        this.latitude_offset = grid_offset;
        
        //Set the pixels-to-latitude conversion factor.
        this.pixels_per_unit_lat = unit_in_pixels;
        
        //We found our scale information.
        return true;
    }
    
    /**
     * Determine whether this airport diagram uses half minutes or whole
     * minutes for measurements.
     * 
     * @param diagram_text is the text representation of the diagram.
     * @return true if the measurement is half minutes and false if it is
     * whole minutes.
     */
    private boolean containsHalfMinutes(String diagram_text)
    {
        //Look for a minute measurement with a ".5" component.
        return diagram_text.contains(".*\\.5\\'[NSWE].*");
    }
    
    /**
     * Convert a y-coordinate into a latitude coordinate.
     * 
     * @param y is the y-coordinate of the diagram.
     * @return the latitude coordinate that corresponds to the pixel at the
     * given y-coordinate.
     */
    public float latitudeConversion(int y)
    {
        return BASE_LATITUDE + (y - y_margin) / pixels_per_unit_lat;
    }
    
    /**
     * Convert the x-coordinate into a longitude coordinate.
     * 
     * @param x is the x-coordinate of the diagram.
     * @return the longitude coordinate that corresponds to the pixel at the
     * given x-coordinate.
     */
    public float longitudeConversion(int x)
    {
        return BASE_LONGITUDE + (x - x_margin) / pixels_per_unit_lat;
    }
    
    /**
     * Get the longitude of the topmost longitude marker on the diagram.
     * For Western scales, it will be the smallest number.  For Eastern
     * scales, it will be the largest number.
     * @param diagram_text is the String representation of the diagram.
     * @return the base longitude to use for coordinate conversion.
     */
    private float findBaseLongitude(String diagram_text)
    {
       Scanner scanner = new Scanner(diagram_text);
       int degrees = -1;
       int minutes = -1;
       String direction = "";
       Pattern long_pattern = Pattern.compile("(\\d+?) (\\d+?)'([WE]?)");
       while (scanner.hasNextLine())
       {
           Matcher long_matcher = long_pattern.matcher(scanner.nextLine());
           if (long_matcher.find())
           {
               int new_degrees = Integer.parseInt(long_matcher.group(1));
               int new_minutes = Integer.parseInt(long_matcher.group(2));
               
               if (direction.isEmpty())
               {
                   direction = long_matcher.group(3);
               }
               //The longitude scale is in the Western hemisphere.
               else if (direction.equals("W"))
               {
                  /* If the degrees number that we have hasn't been set,
                   * is larger than the number we found,
                   * or is the same as the number we found, but the new
                   * minutes value is less than the value that we have, then
                   * replace the old degree value and minutes value.
                   */
                  if (degrees < 0 ||
                      new_degrees < degrees ||
                     (new_degrees == degrees && new_minutes < minutes)
                     )
                  {
                      degrees = new_degrees;
                      minutes = new_minutes;
                  }
               }
               //The longitude scale is in the Eastern hemisphere.
               else if (direction.equals("E"))
               {
                   /* If the degrees number that we have hasn't been set,
                   * is smaller than the number we found,
                   * or is the same as the number we found, but the new
                   * minutes value is greater than the value that we have,
                   * then replace the old degree value and minutes value.
                   */
                  if (degrees < 0 ||
                      new_degrees > degrees ||
                     (new_degrees == degrees && new_minutes > minutes)
                     )
                  {
                      degrees = new_degrees;
                      minutes = new_minutes;
                  }
               }
           }
       }
       
       float base_longitude = (float) (degrees + (minutes / 60));
       
       //Western longitudes are negative in numeric coordinates
       if (direction.equals("W"))
       {
           base_longitude *= -1;
       }
       return base_longitude;
    }
    
    /**
     * Get the latitude of the topmost latitude marker on the diagram.
     * For Northern scales, it will be the smallest number.  For Southern
     * scales, it will be the largest number.
     * @param diagram_text is the String representation of the diagram.
     * @return the base latitude to use for coordinate conversion.
     */
    private float findBaseLatitude(String diagram_text)
    {
       Scanner scanner = new Scanner(diagram_text);
       int degrees = -1;
       int minutes = -1;
       String direction = "";
       Pattern lat_pattern = Pattern.compile("(\\d+?) (\\d+?)'([NS]?)");
       while (scanner.hasNextLine())
       {
           Matcher lat_matcher = lat_pattern.matcher(scanner.nextLine());
           if (lat_matcher.find())
           {
               int new_degrees = Integer.parseInt(lat_matcher.group(1));
               int new_minutes = Integer.parseInt(lat_matcher.group(2));
               
               if (direction.isEmpty())
               {
                   direction = lat_matcher.group(3);
               }
               //The latitude scale is in the Southern hemisphere.
               else if (direction.equals("S"))
               {
                  /* If the degrees number that we have hasn't been set,
                   * is larger than the number we found,
                   * or is the same as the number we found, but the new
                   * minutes value is less than the value that we have, then
                   * replace the old degree value and minutes value.
                   */
                  if (degrees < 0 ||
                      new_degrees < degrees ||
                     (new_degrees == degrees && new_minutes < minutes)
                     )
                  {
                      degrees = new_degrees;
                      minutes = new_minutes;
                  }
               }
               //The latitude scale is in the Northern hemisphere.
               else if (direction.equals("N"))
               {
                   /* If the degrees number that we have hasn't been set,
                   * is smaller than the number we found,
                   * or is the same as the number we found, but the new
                   * minutes value is greater than the value that we have,
                   * then replace the old degree value and minutes value.
                   */
                  if (degrees < 0 ||
                      new_degrees > degrees ||
                     (new_degrees == degrees && new_minutes > minutes)
                     )
                  {
                      degrees = new_degrees;
                      minutes = new_minutes;
                  }
               }
           }
       }
       
       float base_latitude = (float) (degrees + (minutes / 60));
       
       //Southern latitudes are negative in numeric coordinates
       if (direction.equals("S"))
       {
           base_latitude *= -1;
       }
       return base_latitude;
    }
} //end Airport
