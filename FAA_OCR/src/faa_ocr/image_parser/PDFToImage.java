package faa_ocr.image_parser;

import faa_ocr.ADTs.Airport;
import faa_ocr.ADTs.DiagramRunway;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;


/**
 * 
 * @author joe kvedaras
 * @author Kevin Dittmar
 */
public class PDFToImage 
{
	
    /**
     * Creates the BufferedImage of the airport diagram to be used by parsing
     * methods.
     * @param file_path is the path to the airport diagram PDF.
     * @return the BufferedImage representation of the airport diagram PDF.
     */
    public static BufferedImage makeImage(String file_path)
    {
        /*Information about getting an image from a PDF in PDFBox from:
         *http://pdfbox.apache.org/docs/1.8.3/javadocs/org/apache/pdfbox/util/\
         *PDFImageWriter.html
         */
        BufferedImage diagram_image = null;
        File image_file = new File(file_path.replaceAll("\\.pdf", "1\\.jpg"));
        try
        {
            if (!image_file.exists())
            {
                File diagram_file = new File(file_path);
                String image_path_prefix = file_path.replaceAll("\\.pdf", "");
                if (diagram_file.exists())
                {
                    //Turn the PDF into a JPEG image.
                    PDDocument doc = PDDocument.load(diagram_file);
                    PDFImageWriter pdf_image_writer = new PDFImageWriter();
                    /*Write an image version of the document in JPEG format
                     *that includes just the first page with the prefix given
                     *by image_path_prefix.
                     */
                    pdf_image_writer.writeImage(doc,
                                                "jpg",
                                                "", 
                                                1, 
                                                1,
                                                image_path_prefix);
                    doc.close();
                    /*Set up the new image file.  PDFBox appends the page
                     *number, which is 1, and the prefix doesn't include the
                     *extension.
                     */
                    image_file = new File(image_path_prefix + "1.jpg");
                }
            }
            diagram_image = ImageIO.read(image_file);
        }
        catch (IOException ex)
        {
            Logger.getLogger(PDFToImage.class.getName()).log(
                Level.SEVERE, null, ex
            );
        }
        return diagram_image;
    }
    


    /**
     * Get visual data from Airport
     * @param airport	from which we extract data from
     */
    public ArrayList<DiagramRunway> parseVisualData(Airport airport)
    {
    	ArrayList <DiagramRunway> runways;
  
      BufferedImage airport_image = PDFToImage.makeImage(airport.getFilePath());
    	
      //get runway data from image
      runways = new RunwayDiagramParser().parseRunways(airport_image, airport);

      //get taxiway date from image
      new TaxiwayDiagramParser().parseTaxiways(airport_image, airport);
    	
    	
    	return runways;
    	
    	
//        //create buffered image with the pdf file
//        BufferedImage airport_image;
//        try 
//        {
//            airport_image = ImageIO.read(new File(airport.getFilePath()));
//
//            //get runway data from image
//            new RunwayDiagramParser().parseRunways(airport_image, airport);
//
//            //get taxiway date from image
//            new TaxiwayDiagramParser().parseTaxiways(airport_image, airport);
//        }
//        catch (IOException e) 
//        {
//                System.err.println("Error when making PDF an image");
//                e.printStackTrace();
//        }
    }
}
