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
    private final ArrayList<Runway> runways;
   //Not used because we didn't get to them.
    private final ArrayList<Taxiway> taxiways;
    
    //Y-coordinate to latitude conversion factor.
    private float pixels_per_unit_lat;
    
    //X-coordinate to longitude conversion factor.
    private float pixels_per_unit_long;
    
    //Offset from top of diagram to first unit marker.
    private float latitude_offset;
    
    //Offset from left side of diagram to first unit marker.
    private float longitude_offset;
    
    /* The margins are the distance from edge of the diagram to the
     * first whitespace pixel of the diagram.  The x_margin is the
     * length of the left and right margins in pixels, and the y_margin
     * is the length of the top and bottom margins in pixels.
     */
    private float x_margin;
    private float y_margin;
    
    //The latitude that coordinates will be based on for this airport.
    private final float BASE_LATITUDE;
    
    //The latitude that coordinates will be based on for this airport.
    private final float BASE_LONGITUDE;
    
    /* True if the diagram is a rotated landscape diagram instead of a
     * portrait diagram.  This is important because rotation affects how
     * the longitude scale should be calculated.
     */
    private final boolean DIAGRAM_IS_ROTATED;
    
    public Airport (String pdf_file_path, boolean rotated)
    {
        DIAGRAM_IS_ROTATED = rotated;
        this.runways = new ArrayList<Runway>();
        this.taxiways = new ArrayList<Taxiway>();
        this.pdf_file_path = pdf_file_path;
        
        /* Set the scales for pixel coordinates to latitude and longitude
         * coordinates as well as the necessary offsets from the upper left
         * corner of the diagram.
         */
        findPixelConversionScales();
        String diagram_text = PDFToText.getDiagramText(pdf_file_path);
        
        /* If we use half minutes, a unit of latitude and longitude is
         * twice the number of pixels that we found.
         */
        if (usesHalfMinutes(diagram_text))
        {
            pixels_per_unit_lat *= 2;
            pixels_per_unit_long *= 2;
        }
        
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
    
    /**
     * Get a Runway object located at a specified index number
     * @param i int index value of the Runway to be retrieved
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
    
    /**
     * Add a Path object to the collection paths in the instance of Airport
    
     * @param runway the Path object to be added to the collection
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
                        + "Variation: " + variation + "\n"
                        + "Base longitude: " + BASE_LONGITUDE + "\n"
                        + "Base latitude: " + BASE_LATITUDE + "\n"
                        + "Longitude scale: " + pixels_per_unit_long + "\n"
                        + "Latitude scale: " + pixels_per_unit_lat + "\n"
                        + "Longitude offset: " + longitude_offset + "\n"
                        + "Latitude offset: " + latitude_offset + "\n";
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
        
        /* The left margin is 25 pixels; the right margin is 61 pixels
         */
        x_margin = 25;
        y_margin = 61;
        
        //The right end of the diagram.
        int end_width = (int) (diagram.getWidth() - x_margin - 1);
        
        //The bottom end of the diagram.
        int end_height = (int) (diagram.getHeight() - y_margin - 1);
        
        //Try to find the longitude scale at the top of the diagram.
        if (!findHorizontalScale(
                diagram,
                new Point(x_margin, y_margin),
                end_width))
        {
            //Try to find the longitude scale at the bottom of the diagram.
            if (!findHorizontalScale(
                    diagram,
                    new Point(x_margin, end_height),
                    end_width))
            {
                //We failed to find a usable scale.
                System.err.println("Error:  Horizontal scale not be found.");
                System.exit(1);
            }
        }
        
        //Try to find the latitude scale on the left side of the diagram.
        if (!findVerticalScale(
                diagram,
                new Point(x_margin, y_margin),
                end_height))
        {
            //Try to find the latitude scale on the right side of the diagram.
            if (!findVerticalScale(
                    diagram,
                    new Point(end_width, y_margin),
                    end_height))
            {
                //We failed to find a usable scale.
                System.err.println("Error:  Vertical scale not be found.");
                System.exit(1);
            }
        }
        
    }
    
    /**
     * Try to find the longitude scale and report the success of the attempt.
     *
     * @param diagram is the image representation of the airport diagram.
     * @param current is the point to start searching from.
     * @param diagram_width_end is the end of the diagram when approaching
     * from the left.
     * @return true if a valid scale is found before the end of the diagram
     * and false otherwise.
     */
    private boolean findHorizontalScale(BufferedImage diagram, 
                                       Point current, 
                                       int diagram_width_end)
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
        
        int timeout_width = 180;
        
        //We found the scale if we find two black pixels when searching.
        while (black_pixels_found < 2)
        {
            if (current.isBlack(diagram))
            {
                black_pixels_found++;
            }
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
            /* If we don't find the start of the grid unit before the timeout
             * or we are still looking for the end of the unit at the end of
             * the diagram, then we failed to find the scale.
             */
            if ((black_pixels_found < 1 && current.getX() > timeout_width) ||
                (current.getX() >= diagram_width_end))
            {
                return false;
            }
        }
        
        //If the diagram is rotated, then we just found the latitude scale
        if (DIAGRAM_IS_ROTATED)
        {
            /* Set the offset from the left side of the diagram to our
             * latitude unit marker.
             */
            this.latitude_offset = grid_offset;
            
            //Set the pixels-to-latitude conversion factor.
            this.pixels_per_unit_lat = unit_in_pixels;
        }
        else
        {
            /* Set the offset from the left side of the diagram to our
             * longitude unit marker.
             */
            this.longitude_offset = grid_offset;
            
            //Set the pixels-to-longitude conversion factor.
            this.pixels_per_unit_long = unit_in_pixels;
        }
        
        //We found our scale information.
        return true;
    }
    
    /**
     * Try to find the latitude scale and report the success of the attempt.
     * 
     * @param diagram is the image representation of the airport diagram.
     * @param current is the point to start searching from.
     * @param diagram_height_end is the end of the diagram when approaching
     * from the top.
     * @return true if a valid scale is found before the end of the diagram
     * and false otherwise.
     */
    private boolean findVerticalScale(BufferedImage diagram,
                                      Point current, 
                                      int diagram_height_end)
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
        
        //Don't look for the start of the scale past 300 pixels down.
        int timeout_height = 300;
        //We have found the scale if we find two black pixels.
        while (black_pixels_found < 2)
        {
            if (current.isBlack(diagram))
            {
                black_pixels_found++;
            }
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
            /* If we look for the start of the grid unit for too long or we
             * are still looking for the end of the unit at the end of
             * the diagram, then we failed to find the scale.
             */
            if ((black_pixels_found < 1 && current.getY() > timeout_height) ||
                (current.getY() >= diagram_height_end))
            {
                return false;
            }
        }
        
        //If the diagram is rotated, then we just found the longitude scale
        if (DIAGRAM_IS_ROTATED)
        {
            /* Set the offset from the left side of the diagram to our
             * longitude unit marker.
             */
            this.longitude_offset = grid_offset;
            
            //Set the pixels-to-longitude conversion factor.
            this.pixels_per_unit_long = unit_in_pixels;
        }
        else
        {
            /* Set the offset from the left side of the diagram to our
             * latitude unit marker.
             */
            this.latitude_offset = grid_offset;
            
            //Set the pixels-to-longitude conversion factor.
            this.pixels_per_unit_lat = unit_in_pixels;
        }
        
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
    private boolean usesHalfMinutes(String diagram_text)
    {
        Scanner scanner = new Scanner(diagram_text);
        while (scanner.hasNextLine())
        {
            //Look for a minute measurement with a ".5" component.
            if (scanner.nextLine().matches(".*\\.5 *'[NSWE].*"))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Convert a y-coordinate into a latitude coordinate.
     * 
     * @param point is the point in the diagram whose latitude needed.
     * @return the latitude coordinate that corresponds to the pixel at the
     * given point.
     */
    public float latitudeConversion(Point point)
    {
        /* If the diagram is rotated, x values should be used, and latitude
         * decreases.
         */
        if (DIAGRAM_IS_ROTATED)
        {
            return BASE_LATITUDE -
                   ((point.getX() - x_margin) / pixels_per_unit_lat);
        }
        //Otherwise y values should be used, and latitude decreases.
        else
        {
            return BASE_LATITUDE -
                   ((point.getY() - y_margin) / pixels_per_unit_lat);
        }
    }
    
    /**
     * Convert the x-coordinate into a longitude coordinate.
     * 
     * @param point is the point in the diagram whose longitude are needed.
     * @return the longitude coordinate that corresponds to the pixel at the
     * given point.
     */
    public float longitudeConversion(Point point)
    {
        /* If the diagram is rotated, then the y values should be used, and
         * longitude decreases
         */
        if (DIAGRAM_IS_ROTATED)
        {
            return BASE_LONGITUDE -
                   ((point.getY() - y_margin) / pixels_per_unit_lat);
        }
        //Otherwise, x values should be used, and longitude increases.
        else
        {
            return BASE_LONGITUDE +
                   ((point.getX() - x_margin) / pixels_per_unit_lat);
        }
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
       
       /* Longitude regular expression:
        * (\\d+) matches the number of degrees, which could be any number of
        * digits from 1 to 3.
        * (\\d\\d) matches the number of minutes, which is always two digits.
        * ([WE]) matches the direction, which is either West or East.
        */
       Pattern long_pattern = Pattern.compile(
            "(\\d{1,3}) *(\\d\\d)[ \\.\\d]*' *([WE])"
       );
       
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
               /* The longitude scale is in the Western hemisphere, or
                * it is in the Eastern hemisphere and the diagram is rotated.
                */
               if ((direction.equals("W") && !DIAGRAM_IS_ROTATED) ||
                   (direction.equals("E") && DIAGRAM_IS_ROTATED))
               {
                  /* If the degrees number that we have hasn't been set,
                   * is larger than the number we found,
                   * or is the same as the number we found, but the new
                   * minutes value is less than the value that we have, then
                   * replace the old degree value and minutes value.
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
               /*The longitude scale is in the Eastern hemisphere, or it is
                *in the Western hemisphere and the diagram is rotated.
                */
               else if ((direction.equals("E") && !DIAGRAM_IS_ROTATED) ||
                        (direction.equals("W") && DIAGRAM_IS_ROTATED))
               {
                   /* If the degrees number that we have hasn't been set,
                   * is smaller than the number we found,
                   * or is the same as the number we found, but the new
                   * minutes value is greater than the value that we have,
                   * then replace the old degree value and minutes value.
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
           }
       }
       float base_longitude = (float) (degrees + ((float)minutes / 60.0f));
       
       /* If the diagram is rotated, then the longitude offset should be
        * subtracted.
        */
       if (DIAGRAM_IS_ROTATED)
       {
            base_longitude -=
                (float)longitude_offset / (float)pixels_per_unit_long / 60.0f;
       }
       
       /* If the diagram is not rotated, then the longitude offset should be
        * added.
        */
       else
       {
            base_longitude +=
                (float)longitude_offset / (float)pixels_per_unit_long / 60.0f;
       }
       
       
       //Western longitudes are negative in numeric coordinates
       if (direction.equals("W"))
       {
           base_longitude *= -1;
       }
       scanner.close();
       
       /* The leftmost longitude marker isn't the left side of the diagram, so
        * we need to subtract the offset between the left side of the diagram\
        * and the first marker.
        */
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
       
       /* Latitude regular expression:
        * (\\d+) matches the number of degrees, which could be any number of
        * digits from 1 to 3.
        * (\\d\\d) matches the number of minutes, which is always two digits.
        * ([NS]) matches the direction, which is either North or South.
        */
       Pattern lat_pattern = Pattern.compile(
            "(\\d{1,3}) *(\\d\\d)[ \\.\\d]*' *([NS])"
       );
       
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
               if (direction.equals("S"))
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
       
       float base_latitude = (float) (degrees + ((float)minutes / 60.0f));
       base_latitude += ((float)latitude_offset / pixels_per_unit_lat / 60);
       //Southern latitudes are negative in numeric coordinates
       if (direction.equals("S"))
       {
           base_latitude *= -1;
       }
       scanner.close();
       
       /* The topmost latitude marker isn't the top of the diagram, so we
        * need to subtract the offset between the top of the diagram and the
        * first marker.
        */
       return base_latitude;
    }
} //end Airport
