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
    static BufferedImage makeImage(String file_path)
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
            BufferedImage airport_image = PDFToImage.makeImage("res/ACY/00669AD.pdf");
    }

    /**
     * Get visual data from Airport
     * @param airport	from which we extract data from
     */
    public void parseVisualData(Airport airport)
    {
        //create buffered image with the pdf file
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
    
    //TODO:  Refactor these two methods.  They are almost the same, but it is
    //really late, and I can't figure out a good way to generalize them without
    //requiring a lot of parameters or implementing other poor programming
    //practices.
    
    /**
     * Gets the conversion factor between pixel length and longitude.
     * @param file_path the path to the airport diagram PDF.
     * @return the number of pixels in one unit of longitude (may be a half
     * degree or a whole degree).
     */
    public static int findLongitudeScale(String file_path)
    {
        /* The margins are the distance from edge of the diagram to the
         * first whitespace pixel of the diagram.  The x_margin is the
         * length of the left and right margins in pixels, and the y_margin
         * is the length of the top and bottom margins in pixels.
         */
        int x_margin = 100;
        int y_margin = 100;
        int black_pixels_found = 0;
        
        /* unit_in_pixels is a counter for the length of one unit on the
         * diagram's grid, which is probably one degree.
         */
        int unit_in_pixels = 0;
        
        BufferedImage diagram = makeImage(file_path);
        Point current = new Point(x_margin, y_margin);
        
        /* Try to find the scale at the top of the diagram.  We know that we
         * found the length of the scale after we've found two black pixels.
         */
        while (black_pixels_found < 2)
        {
            /* We found the start of a full unit's marker on the grid, so add
             * to the counter
             */
            if (black_pixels_found > 0)
            {
                unit_in_pixels++;
            }
            
            /* If we are still looking for the end of the unit at the end of
             * the diagram, then we failed to find the scale.
             */
            if (current.getX() >= diagram.getWidth() - x_margin - 1)
            {
                black_pixels_found = 0;
                unit_in_pixels = 0;
                break;
            }
            //Advance to the next pixel.
            current = new Point(current.getX() + 1, current.getY());
        }
        
        //If we didn't find a scale at the top, we should try the bottom.
        current = new Point(x_margin, diagram.getHeight() - y_margin - 1);
       
        while (black_pixels_found < 2)
        {
            /* We found the start of a full unit's marker on the grid, so add
             * to the counter
             */
            if (black_pixels_found > 0)
            {
                unit_in_pixels++;
            }
            
            if (current.getX() >= diagram.getWidth() - x_margin - 1)
            {
                black_pixels_found = 0;
                unit_in_pixels = 0;
                break;
            }
            //Advance to the next pixel.
            current = new Point(current.getX() + 1, current.getY());
        }
        
        //If we still don't have a scale, then we can't find one.
        if (unit_in_pixels == 0)
        {
            System.err.println("Error:  Longitude scale not found.");
            System.exit(1);
        }
        
        return unit_in_pixels;
    }

    /**
     * Gets the conversion factor between pixel length and latitude.
     * @param file_path the path to the airport diagram PDF.
     * @return the number of pixels in one unit of latitude (may be a half
     * degree or a whole degree).
     */
    public static int findLatitudeScale(String file_path)
    {
        /* The margins are the distance from edge of the diagram to the
         * first whitespace pixel of the diagram.  The x_margin is the
         * length of the left and right margins in pixels, and the y_margin
         * is the length of the top and bottom margins in pixels.
         */
        int x_margin = 100;
        int y_margin = 100;
        int black_pixels_found = 0;
        
        /* unit_in_pixels is a counter for the length of one unit on the
         * diagram's grid, which is probably one degree.
         */
        int unit_in_pixels = 0;
        
        BufferedImage diagram = makeImage(file_path);
        Point current = new Point(x_margin, y_margin);
        
        /* Try to find the scale at the top of the diagram.  We know that we
         * found the length of the scale after we've found two black pixels.
         */
        while (black_pixels_found < 2)
        {
            /* We found the start of a full unit's marker on the grid, so add
             * to the counter
             */
            if (black_pixels_found > 0)
            {
                unit_in_pixels++;
            }
            
            /* If we are still looking for the end of the unit at the end of
             * the diagram, then we failed to find the scale.
             */
            if (current.getY() >= diagram.getHeight() - y_margin - 1)
            {
                black_pixels_found = 0;
                unit_in_pixels = 0;
                break;
            }
            //Advance to the next pixel.
            current = new Point(current.getX(), current.getY() + 1);
        }
        
        //If we didn't find a scale at the left, we should try the right.
        current = new Point(diagram.getWidth() - x_margin - 1, y_margin);
       
        while (black_pixels_found < 2)
        {
            /* We found the start of a full unit's marker on the grid, so add
             * to the counter
             */
            if (black_pixels_found > 0)
            {
                unit_in_pixels++;
            }
            
            if (current.getY() >= diagram.getHeight() - y_margin - 1)
            {
                black_pixels_found = 0;
                unit_in_pixels = 0;
                break;
            }
            //Advance to the next pixel.
            current = new Point(current.getX(), current.getY() + 1);
        }
        
        //If we still don't have a scale, then we can't find one.
        if (unit_in_pixels == 0)
        {
            System.err.println("Error:  Longitude scale not found.");
            System.exit(1);
        }
        
        return unit_in_pixels;
    }
}
