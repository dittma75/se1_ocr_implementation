package faa_ocr.text_parser;
import faa_ocr.ADTs.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RunwayDataParser extracts all runway data from an airport diagram and puts
 * each datum in the proper field in the Airport object.
 * @author Kevin Dittmar
 */
public class RunwayDataParser extends DataParser
{
    /******************************TEMPORARY**********************************
     *This elevation pattern works for Atlanta, but it needs tweaking for
     *the rest of the cases.
     */
    private final String ELEV_PATTERN = ".*\\b[A-Za-z]*(\\d{1,4}?)\\b.*";
    
    /*Headings always follow the pattern of a four significant digit number
     *with accuracy to the tenths place.
     */
    private final String HEADING_PATTERN = "(\\d\\d\\d\\.\\d?)";
    
    /*Runways always follow the pattern of a two digits and an optional
     *letter.
     */
    private final String RUNWAY_PATTERN = "(\\d\\d[LCR]?)";
    
    /**
     * No initialization is necessary for the constructor.
     */
    public RunwayDataParser()
    {
        
    }
    
    /**
     * Parse the formatted text version of the airport diagram and extract all
     * runway data.  This data will then be added as Paths to the Airport's
     * Path List.
     * @param formatted_string is the formatted representation of the textual
     * data of the Airport's airport diagram.
     * @param airport is the Airport that will receive the parsed runway data
     * as Runways, which will be added to the Airport's Path List.
     */
    public void parseRunwayData(String formatted_string, Airport airport)
    {
        
        ArrayList<String> valid_runways = makeListOfRunways(
                PDFToText.getTextPath(airport.getFilePath())
        );

        ArrayList<String> valid_headings = makeListOfProperHeadings(
                PDFToText.getTextPath(airport.getFilePath())
        );

        ArrayList<String> runways = new ArrayList<>();

        ArrayList<Float> headings = new ArrayList<>();

        ArrayList<Integer> elevations = new ArrayList<>();
        
        Scanner scanner = new Scanner(formatted_string);
        
        while (scanner.hasNextLine())
        {
            String current_line = scanner.nextLine();
            
            //Check this line for runways.
            String runway = searchForItem(RUNWAY_PATTERN, current_line);
            if (valid_runways.contains(runway) &&
                !runways.contains(runway))
            {
                runways.add(runway);
            }

            /*Check this line for headings  String comparison is used because
             *the headings start as Strings that have a defined format, and
             *Doubles are hard to compare accurately.
             */
            String heading = searchForItem(HEADING_PATTERN, current_line);
            if (valid_headings.contains(heading))
            {
                headings.add(Float.parseFloat(heading));
            }
            
            String elevation = searchForItem(ELEV_PATTERN, current_line);
            if (!elevation.equals(""))
            {
                elevations.add(Integer.parseInt(elevation));
            }
        }
        scanner.close();
        
        /*At this point, the runway information is in order across the 
         *three lists.  Iterating across all three lists together will get
         *the proper runway data for each runway.
         */
        for (int i = 0; i < runways.size(); i++)
        {
            airport.addRunway(
                new Runway(
                    elevations.get(i),
                    headings.get(i),
                    runways.get(i)
                )
            );
        }
    }
    
    /**
     * Make a list of runway names that the parser will be looking for based
     * on the runway list that is included somewhere in every airport diagram.
     * @param file_name is the path to the airport diagram.
     * @return an ArrayList of runway name Strings that are valid for the
     * airport.
     */
    private ArrayList<String> makeListOfRunways(String file_name)
    {
        File file = new File(file_name);
        try
        {
            Scanner scanner = new Scanner(file);
            String next_line = "";
            ArrayList<String> runways = new ArrayList<>();
            while (scanner.hasNextLine())
            {
                next_line = scanner.nextLine();
                
                /* This pattern looks for runway pairs.  They usually come
                 * in single pairs, but sometimes they come in a comma
                 * delimited list of pairs.  The first capturing group will
                 * be the list that is wanted, so searchForItem() still
                 * works.
                 */
                String runway_pairs_string = searchForItem(
                        "R *W *Y *(( *\\d\\d[RCL]*-\\d\\d[RCL]*,*)*)",
                        next_line
                );
                
                //We found a line with at least one runway pair.
                if (!runway_pairs_string.equals(""))
                {
                    /* Remove all spaces from the String to ensure proper
                     * String comparison.
                     */
                    runway_pairs_string = 
                        runway_pairs_string.replaceAll(" ", "");
                    
                    String runway_pairs[];
                    //We found multiple pairs of runways delimited by commas
                    if (runway_pairs_string.contains(","))
                    {
                        runway_pairs = runway_pairs_string.split(",");
                    }
                    /* Otherwise, there is just one pair.  An array is used
                     * so that it is possible to iterate over the list
                     * in a more general way.
                     */
                    else
                    {
                        runway_pairs = new String[1];
                        runway_pairs[0] = runway_pairs_string;
                    }
                    for (String runway_pair : runway_pairs)
                    {
                        /* This will always be a set of two runways separated
                         * by a minus sign, so split on the "-" character.
                         */
                        String[] rwy_set = runway_pair.split("-");
                        for (String runway : rwy_set)
                        {
                            //Add the runway to the list if it's not there.
                            if (!runways.contains(runway))
                            {
                                runways.add(runway);
                            }
                        }
                    }
                }
            }
            scanner.close();
            return runways;
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(RunwayDataParser.class.getName()).log(
                Level.SEVERE, null, ex
            );
            return null;
        }
    }
    
    /**
     * Make a list of all of the valid headings for the given airport diagram.
     * PDFBox is used because pdftotext doesn't transfer ° characters to the
     * plain text file.
     * @param file_name is the file path of the airport diagram.
     * @return an ArrayList of valid heading Strings for the given airport.
     */
    private ArrayList<String> makeListOfProperHeadings(String file_name)
    {
        /*Turn the PDF of the airport diagram into a String of plain text.
         *pdftotext reads the PDF more correctly in most cases, but doesn't
         *preserve the ° characters, which are the best way to find the
         *heading angles.
         */
        String pdf_text = PDFToText.getTextPDFBox(file_name);
        
        Scanner scanner = new Scanner(pdf_text);
        String next_line;
        ArrayList<String> valid_headings = new ArrayList<>();
        while (scanner.hasNextLine())
        {
            next_line = scanner.nextLine();
            
            /*All headings will have the pattern ###.#°, which is the same as
             *the heading pattern except that the '°' character comes after
             *the angle that we want.  
             */
            String heading = searchForItem(HEADING_PATTERN + "°", next_line);
            
            if (!heading.equals(""))
            {
                //If we don't already have that heading in the list, add it.
                if (!valid_headings.contains(heading))
                {
                    valid_headings.add(heading);
                }
            }
        }
        scanner.close();
        return valid_headings;
    }
}
