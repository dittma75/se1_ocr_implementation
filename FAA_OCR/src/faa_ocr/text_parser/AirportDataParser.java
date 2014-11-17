package faa_ocr.text_parser;
import faa_ocr.ADTs.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * The AirportDataParser extracts airport-specific information from the
 * airport diagram and puts it in the appropriate fields of an Airport object.
 * @author Kevin Dittmar
 */
public class AirportDataParser
{
    /*The pattern for the variation from true north, which is the string "VAR"
     *followed by a string of digits, a decimal point, another string of
     *digits, and N,S,W, or E, which stand for the direction of the variation.
     */
    private final Pattern VARIATION_PATTERN = Pattern.compile(
            "VAR.*(\\d+\\.\\d+[NSWE ]+).*"
    );
    
    /*The pattern for the airport abbreviation, which is three characters in
     *parentheses after the string "INTL"
     */
    private final Pattern ABBREVIATION_PATTERN = Pattern.compile(
            "(.*INTL\\(...\\)?)"
    );
    
    /*The pattern for the location is "city, state", where city and state are
     *all capital letters.
     */
    private final Pattern LOCATION_PATTERN = Pattern.compile(
            "([A-Z ]+, [A-Z ]+)"
    );
    
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
        
        /*Continue while there are more lines to scan and more information is
         *needed.
         */
        while (scanner.hasNextLine() &&
              (!found_variation || !found_abbrev || !found_location))
        {
            String next_line = scanner.nextLine();
            
            //Look for the variation if we haven't found it.
            if (!found_variation)
            {
                String var = searchForVariation(next_line);
                if (!var.equals(""))
                {
                    found_variation = true;
                    airport.setVariation(Float.parseFloat(var));
                }
            }
            
            //Look for the abbreviation if we haven't found it.
            if (!found_abbrev)
            {
                String abbrev = searchForAirportAbbreviation(next_line);
                if (!abbrev.equals(""))
                {
                    found_abbrev = true;
                    airport.setAbbreviation(abbrev);
                }
            }
            
            //Look for the location if we haven't found it.
            if (!found_location)
            {
                String loc = searchForAirportLocation(next_line);
                if (!loc.equals(""))
                {
                    found_location = true;
                    airport.setLocation(loc);
                }
            }
        }
    }
    
    /**
     * See if the variation field is included in this line and return it.
     * @param line is the line to check for the variation field.
     * @return the String representation of the variation value, or the empty
     * String if it is not found.
     */
    private String searchForVariation(String line)
    {
        Matcher var_matcher = VARIATION_PATTERN.matcher(line);
        //If we found something matching the variation pattern...
        if (var_matcher.find())
        {
            //Return the part of the string that matched.
            return var_matcher.group(1);
        }
        return "";
    }
    
    /**
     * See if the airport abbreviation code is included in this line and return
     * it.
     * @param line the line to be checked for the airport abbreviation.
     * @return the airport abbreviation or the empty String if it isn't found.
     */
    private String searchForAirportAbbreviation(String line)
    {
        Matcher abbrev_matcher = ABBREVIATION_PATTERN.matcher(line);
        //If we found something matching the abbreviation pattern...
        if (abbrev_matcher.find())
        {
            //Return the part of the string that matched.
            return abbrev_matcher.group(1);
        }
        return "";
    }
    
    /**
     * Look for the airport location in this line and return it if found.
     * @param line the line to be checked for the airport location.
     * @return the airport location or the empty String if it isn't found.
     */
    private String searchForAirportLocation(String line)
    {
        Matcher loc_matcher = LOCATION_PATTERN.matcher(line);
        //If we found something matching the location pattern...
        if (loc_matcher.find())
        {
            //Return the part of the string that matched.
            return loc_matcher.group(1);
        }
        return "";
    }
}
