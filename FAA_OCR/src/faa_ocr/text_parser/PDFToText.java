package faa_ocr.text_parser;
import faa_ocr.ADTs.*;
import java.io.IOException;
import static java.lang.Runtime.getRuntime;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Kevin Dittmar
 */
public class PDFToText
{
    public void parseTextData(Airport airport)
    {
        
    }
    
    private void makeTextFile(String file_path)
    {
        //Needs documentation about the usage of Xpdftotext
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
