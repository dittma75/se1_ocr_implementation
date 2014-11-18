package faa_ocr.text_parser;
import faa_ocr.ADTs.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.cos.COSDocument;
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
    private final Pattern ELEV_PATTERN = Pattern.compile(
        ".*\\b[A-Za-z]*(\\d{3,4}?)\\b.*"
    );
    
    private final Pattern ANGLE_PATTERN = Pattern.compile(
        "(\\d\\d\\d\\.\\d?)"
    );
    
    private final Pattern RUNWAY_PATTERN = Pattern.compile(
        " RWY ([0-9]+[RL]*-[0-9]+[RL]?)"
    );
    
    public RunwayDataParser()
    {
        
    }
    
    public void parseRunwayData(String formatted_string, Airport airport)
    {
        
        ArrayList valid_runways = makeListOfRunways();

        ArrayList valid_headings = makeListOfProperHeadings();

        ArrayList<String> runways;

        ArrayList<Double> headings;

        ArrayList<Integer> elevations;
        Scanner scanner = new Scanner(formatted_string);
        
//START OF PSEUDO-CODE ALGORITHM
        while (scanner.hasNextLine())
        {
            String current_line = scanner.nextLine();
            /*\d is a number, . is a character, and this is the format for a
             *runway name.
             */
            if (current_line.("\d\d."))
            {
                if (valid_runways.contains(current_line.matchedText()))
                {
                    runways.add(current_line.matchedText());
                }
            }
            /*\d is a number, \. is a period character, and this is the format
             *for a runway heading.
             */
            if (current_line.contains(“\d\d\d\.\d”))
            {
                if (valid_headings.contains(current_line.matchedText()))
                {
                    headings.add(current_line.matchedText());
                }
            }
            /*\d+ is a number of at least one digit, .* is any number of
             *characters, and this is the format for a runway elevation.
             */
            if (current_line.contains(“ELEV.*\d+”))
            {
                elevations.add(current_line.matchedText());
            }
            /*At this point, the runway information is in order across the 
             *three lists.  Iterating across all three lists together will get
             *the proper runway data for each runway.
             */
        }
    }
//END OF PSEUDO-CODE ALGORITHM
    
    /**Uses PDFBox to get a list of valid angles, since pdftotext doesn't
     * currently transpose degree signs (might be an option for this).
     * @param file_name the name of the PDF Airport Diagram to parse for angles.
     */
    public void parseAngleList(String file_name)
    {
        String pdf_text = getTextPDFBox(file_name);
        Scanner scanner = new Scanner(pdf_text);
        Pattern pdf_box_angle_pattern = Pattern.compile("(\\d\\d\\d\\.\\d?)°");
        String next_line = "";
        ArrayList<Double> valid_angles = new ArrayList<>();
        while (scanner.hasNextLine())
        {
            //"\d\d\d\.\d°" is the regex to get all of the angles
            next_line = scanner.nextLine();
            Matcher matcher = pdf_box_angle_pattern.matcher(next_line);
            if (matcher.find())
            {
                double angle = Double.parseDouble(matcher.group(1));
                if (!valid_angles.contains(angle))
                {
                    valid_angles.add(angle);
                }
            }
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
    
    private ArrayList<String> makeListOfRunways()
    {
        return null;
    }
    
    private ArrayList<Double> makeListOfProperHeadings()
    {
        return null;
    }
}
