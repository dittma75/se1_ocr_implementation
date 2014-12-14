package faa_ocr.text_parser;
import faa_ocr.ADTs.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final String ELEV_PATTERN = "[^\\d]*([1-9]\\d{0,3})[^.\\d]*";
    
    /*Headings always follow the pattern of a four significant digit number
     *with accuracy to the tenths place.
     */
    private final String HEADING_PATTERN = "(\\d\\d\\d\\.\\d)";
    
    /* Runways always follow the pattern of one or two digits and an optional
     * letter.
     */
    private final String RUNWAY_PATTERN = ".(\\d{1,2}[LCR]*)";
    
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
                airport.getFilePath()
        );

        ArrayList<String> runways = new ArrayList<>();

        ArrayList<Float> headings = new ArrayList<>();

        ArrayList<Integer> elevations = new ArrayList<>();
        
        Scanner scanner = new Scanner(formatted_string);
        
        /* Keep track of how many "ELEV"s were found, which indicate
         * a runway elevation.
         */
        int elev_counter = 0;
        int field_elev_index = -1;
        
        while (scanner.hasNextLine())
        {
            String current_line = scanner.nextLine();
            
            //Check this line for runways.
            ArrayList<String> runways_found;
            runways_found = searchForRunways(current_line);
            
            for (String runway : runways_found)
            {
                runway = correctRunway(runway);
                if (valid_runways.contains(runway) &&
                    !runways.contains(runway))
                {
                    runways.add(runway);
                }
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
            
            /* Field elevation may or may not be needed.  It will be
             * added along with the other elevations, but the index that
             * it will occupy needs to be saved so it can possibly be
             * removed later.
             */
            if (current_line.matches(".*FIELD *ELEV.*"))
            {
                field_elev_index = elevations.size();
            }
            //Increment the elevation counter if "ELEV" is found in the line.
            if (current_line.matches(".*E *L *E *V *.*"))
            {
                elev_counter++;
            }
            
            String elevation = searchForItem(ELEV_PATTERN, current_line);
            if (!elevation.equals("") && elev_counter > 0)
            {
                int elev = Integer.parseInt(elevation);
                //There is no base to use, so just add the elevation.
                if (elevations.isEmpty())
                {
                    elev_counter--;
                    elevations.add(elev);
                }
                /* We can see if the new potential elevation is close to our
                 * previous elevation samples.
                 */
                else
                {
                    int sample_elev = elevations.get(0);
                    
                    /* If the potential elevation is within 100 of previous
                     * elevations, then add it.
                     */
                    if (Math.abs(sample_elev - elev) < 100)
                    {
                        elev_counter--;
                        elevations.add(Integer.parseInt(elevation));
                    }
                }
            }
        }
        scanner.close();
        
        //We have too many elevations.  Drop the field elevation.
        if (elevations.size() > runways.size())
        {
            elevations.remove(field_elev_index);
        }    
                
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
                    /* Remove all spaces and padding zeros from the String to
                     * ensure proper String comparison.
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
        ArrayList<String> valid_headings = new ArrayList<>();
        while (scanner.hasNextLine())
        {
            String next_line = scanner.nextLine();
            
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
        
        if (valid_headings.isEmpty())
        {
            pdf_text = PDFToText.getDiagramText(file_name);
            scanner = new Scanner(pdf_text);
            while (scanner.hasNextLine())
            {
                String next_line = scanner.nextLine();
                String heading = searchForItem(
                        HEADING_PATTERN + "[^\\d]*",
                        next_line
                );
                if (!heading.equals("") && !valid_headings.contains(heading))
                {
                    valid_headings.add(heading);
                }
            }
            validateHeadings(valid_headings);
        }
        scanner.close();
        return valid_headings;
    }
    
    /**
     * Fix disparity between the runway names near the runways on the diagram
     * and the names given in the listing on the diagram.
     * @param runway is the runway to correct.
     * @return is the corrected runway name.
     */
    private String correctRunway(String runway)
    {
        /* If the runway has one digit and one letter, pad with a zero.
         * This is necessary because all one-digit runways are padded with
         * a zero in the listing, but not in the actual diagrams.
         */
        if (runway.length() < 3 && runway.matches("\\d[LRC]*"))
        {
            return "0" + runway;
        }
        else
        {
            return runway;
        }
    }
    
    /**
     * Get an ArrayList of runway name Strings from the given text.
     * @param text is the String to search for runway names.
     * @return an ArrayList of runway Strings if present in the line of text
     * or an empty ArrayList if there are none.
     */
    private ArrayList<String> searchForRunways(String text)
    {
        //Compile the pattern given.
        Pattern matcher_pattern = Pattern.compile(RUNWAY_PATTERN);
        
        //Set up the matcher for the pattern.
        Matcher matcher = matcher_pattern.matcher(text);
        
        ArrayList<String> runways = new ArrayList<>();
        
        //While there are parts of the string that match the runway pattern...
        while (matcher.find())
        {   
            //Add to the runways list.
            runways.add(matcher.group(1));
        }
        return runways;
    }
    
    private void validateHeadings(ArrayList<String> headings)
    {
        ArrayList<Boolean> has_associated_heading = new ArrayList<>();
        
        //No headings are associated to start.
        for (int i = 0; i < headings.size(); i++)
        {
            has_associated_heading.add(false);
        }
        
        //Valid headings should have a counterpart that varies by 180 degrees.
        for (int i = 0; i < headings.size(); i++)
        {
            int first = Math.round(Float.parseFloat(headings.get(i)));
            for (int j = i + 1; j < headings.size(); j++)
            {
                int second = Math.round(Float.parseFloat(headings.get(j)));
                if (Math.abs(first - second) == 180)
                {
                    has_associated_heading.set(i, Boolean.TRUE);
                    has_associated_heading.set(j, Boolean.TRUE);
                    break;
                }
            }
        }
        
        //Remove non-validated headings.
        for (int i = 0; i < headings.size(); i++)
        {
            if (!has_associated_heading.get(i))
            {
                headings.remove(i);
            }
        }
    }
}
