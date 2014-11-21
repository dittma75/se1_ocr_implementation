package faa_ocr.image_parser;

import faa_ocr.ADTs.Airport;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;


/**
 * 
 * @author joe kvedaras
 *
 */
public class PDFToImage 
{
	
    /**
     * TODO:  Doc for this method.
     * @param file_path
     * @return 
     * @author Kevin Dittmar
     */
    BufferedImage makeImageFile(String file_path)
    {
        /*Information about getting an image from a PDF in PDFBox from:
         *http://stackoverflow.com/questions/21059403/text-is-missing-when-\
         *converting-pdf-file-into-image-in-java-using-pdfbox
         */
        BufferedImage diagram_image = null;
        try
        {
            File diagram_file = new File(file_path);
            if (diagram_file.exists())
            {
                //The document will only be one page, so get the first page.
                PDDocument doc = PDDocument.load(diagram_file);
                PDPage page;
                page = (PDPage)doc.getDocumentCatalog().getAllPages().get(0);
                diagram_image = page.convertToImage();
//                System.out.println("printing image");
//                File test = new File("written_image.png");
//                ImageIO.write(diagram_image, "png", test);
                doc.close();
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(PDFToImage.class.getName()).log(
                Level.SEVERE, null, ex
            );
        }
        return diagram_image;
    }
    /* Main method for testing */
    public static void main(String[] args)
    {
            BufferedImage airport_image = new PDFToImage().makeImageFile("res/ACY/00669AD.pdf");
//            try {
//                    airport_image = ImageIO.read(new File("./FAA_OCR/lib/00669AD.jpg"));
//                    Airport airport = new Airport("./FAA_OCR/res/ACY/00669AD.pdf");
//
//                    //get runway data from image
//                    new RunwayDiagramParser().parseRunways(airport_image, airport);
//
//                    //get taxiway date from image
//                    new TaxiwayDiagramParser().parseTaxiways(airport_image, airport);
//
//            } catch (IOException e) {
//                    System.err.println("Error when making PDF an image");
//                    e.printStackTrace();
//            }

    }

    /**
     * Get visual data from Airport
     * @param airport	from which we extract data from
     */
    public void parseVisualData(Airport airport)
    {
        //create buffered image with the pdf file
        //TODO: if it doesn't work, we need to use PDFbox to create a jpeg
        BufferedImage airport_image;
        try 
        {
            airport_image = ImageIO.read(new File(airport.getFilePath()));

            //get runway data from image
            new RunwayDiagramParser().parseRunways(airport_image, airport);

            //get taxiway date from image
            new TaxiwayDiagramParser().parseTaxiways(airport_image, airport);
        }
        catch (IOException e) 
        {
                System.err.println("Error when making PDF an image");
                e.printStackTrace();
        }
    }	
}
