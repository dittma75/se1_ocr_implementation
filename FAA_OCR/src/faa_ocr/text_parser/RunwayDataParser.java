package faa_ocr.text_parser;
import faa_ocr.ADTs.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
/**
 * RunwayDataParser extracts all runway data from an airport diagram and puts
 * each datum in the proper field in the Airport object.
 * @author Kevin Dittmar
 */
public class RunwayDataParser
{
    /******************************TEMPORARY**********************************
     *This elevation pattern works for Atlanta, but it needs tweaking for
     *the rest of the cases.
     */
    private final Pattern ELEV_PATTERN = Pattern.compile(
        ".*\\b[A-Za-z]*(\\d{3,4}?)\\b.*"
    );
    
    /*Angles always follow the pattern of a four significant digit number with
     *accuracy to the tenths place.
     */
    private final Pattern ANGLE_PATTERN = Pattern.compile(
        "(\\d\\d\\d\\.\\d?)"
    );
    
    /*Runways always follow the pattern of a two digits and an optional
     *letter.
     */
    private final Pattern RUNWAY_PATTERN = Pattern.compile(
        "(\\d\\d[LCR]?)"
    );
    
    public RunwayDataParser()
    {
        
    }
    
    public void parseRunwayData(String formatted_string, Airport airport)
    {
        
        ArrayList<String> valid_runways = makeListOfRunways(
                airport.getFilePath()
        );

        ArrayList<String> valid_headings = makeListOfProperHeadings(
                airport.getFilePath()
        );

        ArrayList<String> runways = new ArrayList<>();

        ArrayList<Double> headings = new ArrayList<>();

        ArrayList<Integer> elevations = new ArrayList<>();
        
        Scanner scanner = new Scanner(formatted_string);
        
        while (scanner.hasNextLine())
        {
            String current_line = scanner.nextLine();
            
            //Check this line for runways.
            String runway = searchForRunway(current_line);
            if (valid_runways.contains(runway))
            {
                runways.add(runway);
            }

            /*Check this line for headings  String comparison is used because
             *the headings start as Strings that have a defined format, and
             *Doubles are hard to compare accurately.
             */
            String heading = searchForHeading(current_line);
            if (valid_headings.contains(heading))
            {
                headings.add(Double.parseDouble(heading));
            }
            
            String elevation = searchForElevation(current_line);
            if (!elevation.equals(""))
            {
                elevations.add(Integer.parseInt(elevation));
            }
            /*At this point, the runway information is in order across the 
             *three lists.  Iterating across all three lists together will get
             *the proper runway data for each runway.
             */
        }
    }
    
    /**
     * Get PDFBox output of airport diagram.
     * @param file_name name of airport diagram PDF file.
     * @return text representation of airport diagram.
     */
    private String getTextPDFBox(String file_name)
    {
        PDFParser parser;
        String parsed_text = "";
        PDFTextStripper pdf_stripper;
        File diagram_file = new File(file_name);
        
        //Make sure that the airport diagram exists.
        if (!diagram_file.isFile()) 
        {
            System.err.println("File " + file_name + " does not exist.");
            return null;
        }
        try 
        {
            //Make a new parser from the airport diagram file.
            parser = new PDFParser(new FileInputStream(diagram_file));
        } 
        catch (IOException e) {
            System.err.println("Unable to open PDF Parser. " + e.getMessage());
            return null;
        }
        try
        {
            parser.parse();
            pdf_stripper = new PDFTextStripper();
            pdf_stripper.setSortByPosition(false);
            
            //Get the parsed text from the text stripper.
            parsed_text = pdf_stripper.getText(
                new PDDocument(
                    parser.getDocument()
                )
            );
        } 
        catch (Exception e) 
        {
            System.err.println(
                "An exception occured in parsing the PDF Document." + 
                e.getMessage()
            );
        }
        return parsed_text;
    }
    
    private ArrayList<String> makeListOfRunways(String file_name)
    {
        ArrayList<String> runways = new ArrayList<>();
        File file = new File(file_name);
        try
        {
            Scanner scanner = new Scanner(file);
            String next_line = "";
            while (scanner.hasNextLine())
            {
                next_line = scanner.nextLine();
                next_line = " " + next_line;
                
                /*The runway list pattern looks for "RWY", which comes
                 *before each runway pairing on the list of runways that is
                 *included on every airport diagram.  Runways are named with
                 *digits and an optional letter (L for left, R for right, and
                 *C for center).
                 */
                Pattern runway_list_pattern = Pattern.compile(
                    " RWY ([0-9]+[RLC]*-[0-9]+[RLC]?)"
                );
                
                Matcher matcher = runway_list_pattern.matcher(next_line);
                if (matcher.find())
                {
                    /*This will always be a set of two runways separated by a
                     *minus sign, so split on the "-" character.
                     */
                    String rwy_set[] = matcher.group(1).split("-");
                    
                    runways.add(rwy_set[0]);
                    runways.add(rwy_set[1]);
                }
            }
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
     * @param file_name is the file path of the airport diagram.
     * @return an ArrayList of valid heading Strings for the given airport.
     */
    private ArrayList<String> makeListOfProperHeadings(String file_name)
    {
        //Turn the PDF of the airport diagram into a String of plain text.
        String pdf_text = getTextPDFBox(file_name);
        
        Scanner scanner = new Scanner(pdf_text);
        
        //All headings will have the pattern ###.#°.
        Pattern heading_pattern = Pattern.compile("(\\d\\d\\d\\.\\d?)°");
        
        String next_line = "";
        ArrayList<String> valid_angles = new ArrayList<>();
        while (scanner.hasNextLine())
        {
            next_line = scanner.nextLine();
            Matcher matcher = heading_pattern.matcher(next_line);
            if (matcher.find())
            {
                String angle = matcher.group(1);
                
                //If we don't already have that heading in the list, add it.
                if (!valid_angles.contains(angle))
                {
                    valid_angles.add(angle);
                }
            }
        }
        return valid_angles;
    }
    
    private String searchForHeading(String line)
    {
        Matcher angle_matcher = ANGLE_PATTERN.matcher(line);
        //If we found something matching the variation pattern...
        if (angle_matcher.find())
        {
            //Return the part of the string that matched.
            return angle_matcher.group(1);
        }
        return "";
    }
    
    private String searchForRunway(String line)
    {
        Matcher runway_matcher = RUNWAY_PATTERN.matcher(line);
        //If we found something matching the variation pattern...
        if (runway_matcher.find())
        {
            //Return the part of the string that matched.
            return runway_matcher.group(1);
        }
        return "";
    }
    
    private String searchForElevation(String line)
    {
        Matcher elev_matcher = ELEV_PATTERN.matcher(line);
        //If we found something matching the variation pattern...
        if (elev_matcher.find())
        {
            //Return the part of the string that matched.
            return elev_matcher.group(1);
        }
        return "";
    }
}
