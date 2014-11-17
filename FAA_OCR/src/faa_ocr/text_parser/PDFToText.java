package faa_ocr.text_parser;
import faa_ocr.ADTs.*;
import java.io.IOException;
import static java.lang.Runtime.getRuntime;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * The PDFToText module turns the given PDF representation of an airport
 * diagram into a text file.
 * @author Kevin Dittmar
 */
public class PDFToText
{
    /**
     * No initialization is necessary for the constructor of PDFToText.
     */
    public PDFToText()
    {
        
    }
    
    /**
     * Parse the text of the given Airport based on the airport diagram
     * associated with it.
     * @param airport is the Airport whose airport diagram is to be parsed,
     * which will be stored in that same Airport object.
     */
    public void parseTextData(Airport airport)
    {
        //Get the airport diagram PDF file's path
        String diagram_path = airport.getPath();
        
        //Make a text representation of the airport diagram.
        makeTextFile(diagram_path);
        
        AirportDataParser airport_parser = new AirportDataParser();
        RunwayDataParser runway_parser = new RunwayDataParser();
        LineFormatter line_formatter = new LineFormatter();
        
        //Format the diagram text for use in the parsers.
        String diagram_text = line_formatter.getFormattedString(
                getDiagramText(diagram_path)
        );
        
        //Parse the diagram for airport-specific information.
        airport_parser.parseAirportData(diagram_text, airport);
        
        //Parse the diagram for runway information.
        runway_parser.parseRunwayData(diagram_text, airport);
    }
    
    /**
     * Turns a PDF airport diagram into a text representation that can be
     * analyzed.
     * @param file_path is the file path of the PDF airport diagram that is to
     * be turned into a text file.
     */
    private void makeTextFile(String file_path)
    {
        //TODO:  Needs documentation about the usage of Xpdftotext
        try
        {
            //Parse the PDF and turn its text into a plain text .txt file.
            Process pdftotext = getRuntime().exec("pdftotext " + file_path);
            
            //Wait for pdftotext to terminate.
            pdftotext.waitFor();
        }
        /*Handles IOException for "pdftotext arg" not existing and the
         *InterruptedException in case there is a problem due to waitFor();
         */
        catch (IOException | InterruptedException ex)
        {
            Logger.getLogger(PDFToText.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
    
    /**
     * Get the raw text of the airport diagram PDF whose file path is
     * diagram_pdf_path.
     * @param diagram_pdf_path is the path to the airport diagram PDF.
     */
    private String getDiagramText(String diagram_pdf_path)
    {
        Scanner scanner = new Scanner(getTextPath(diagram_pdf_path));
        String diagram_text = "";
        while (scanner.hasNextLine())
        {
            diagram_text += scanner.nextLine();
        }
        return diagram_text;
    }
    
    /**
     * Get the airport diagram's text file's path based on the airport
     * diagram's PDF file's path.
     * @param diagram_pdf_path is the path of the airport diagram PDF.
     */
    private String getTextPath(String diagram_pdf_path)
    {
        /*The txt file has the same name as the PDF file and is stored in the
         *same directory, but the extension is .txt instead of .pdf.
         */
        return diagram_pdf_path.replace("\\.pdf", "\\.txt");
    }
}
