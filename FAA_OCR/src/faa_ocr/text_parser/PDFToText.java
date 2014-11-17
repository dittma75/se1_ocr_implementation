package faa_ocr.text_parser;
import faa_ocr.ADTs.*;
import java.io.IOException;
import static java.lang.Runtime.getRuntime;
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
}
