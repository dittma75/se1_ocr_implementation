package faa_ocr.text_parser;
import faa_ocr.ADTs.*;
import java.util.Scanner;
/**
 * The AirportDataParser extracts airport-specific information from the
 * airport diagram and puts it in the appropriate fields of an Airport object.
 * @author Kevin Dittmar
 */
public class AirportDataParser extends DataParser
{
    /* The pattern for the variation from true north, which is the string "VAR"
     * followed by a string of digits, a decimal point, another string of
     * digits, and N,S,W, or E, which stand for the direction of the variation.
     */
    private final String VARIATION_PATTERN = "VAR (\\d+\\.\\d+ [NSWE])";
    
    /* The pattern for the airport abbreviation, which is three characters in
     * parentheses after the string "INTL"
     */
    private final String ABBREVIATION_PATTERN = ".*INTL\\((...)\\)";
 
    /* The pattern for the name, which comes before the abbreviation code
     * and always includes INTL.
     */
    private final String NAME_PATTERN = "(.*INTL)\\(...\\)";
    
    /*The pattern for the location is "city, state", where city and state are
     *all capital letters.
     */
    private final String LOCATION_PATTERN = "([A-Z ]+, [A-Z ]+)";
    
    /**
     * No initialization is necessary for the constructor.
     */
    public AirportDataParser()
    {

    }
    
    /**
     * Parse the airport-specific data from the airport diagram.
     * @param formatted_string is the String representation of the airport
     * diagram to be analyzed.
     * @param airport is the Airport that will be given the extracted
     * information.
     */
    public void parseAirportData(String formatted_string, Airport airport)
    {
        Scanner scanner = new Scanner(formatted_string);
        boolean found_variation = false;
        boolean found_abbrev = false;
        boolean found_location = false;
        boolean found_name = false;
        
        /*Continue while there are more lines to scan and more information is
         *needed.
         */
        while (scanner.hasNextLine() &&
              (!found_variation || !found_abbrev || !found_location ||
               !found_name))
        {
            String next_line = scanner.nextLine();
            
            //Look for the variation if we haven't found it.
            if (!found_variation)
            {
                String var = searchForItem(VARIATION_PATTERN, next_line);
                if (!var.equals(""))
                {
                    found_variation = true;
                    
                    float var_amount = Float.parseFloat(
                        var.replaceAll("[WE]", "")
                    );
                    
                    if (var.contains("W"))
                    {
                        var_amount *= -1;
                    }
                    airport.setVariation(var_amount);
                }
            }
            
            //Look for the abbreviation if we haven't found it.
            if (!found_abbrev)
            {
                String abbrev = searchForItem(ABBREVIATION_PATTERN, next_line);
                if (!abbrev.equals(""))
                {
                    found_abbrev = true;
                    airport.setAbbreviation(abbrev);
                }
            }
            
            //Look for the location if we haven't found it.
            if (!found_location)
            {
                String loc = searchForItem(LOCATION_PATTERN, next_line);
                if (!loc.equals(""))
                {
                    found_location = true;
                    airport.setLocation(loc);
                }
            }
            
            //Look for the name of the airport if we haven't found it.
            if (!found_name)
            {
                String name = searchForItem(NAME_PATTERN, next_line);
                if (!name.equals(""))
                {
                    found_name = true;
                    airport.setName(name);
                }
            }
        }
        
        //close scanner
        scanner.close();
    }
}
