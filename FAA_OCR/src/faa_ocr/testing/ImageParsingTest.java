package faa_ocr.testing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import faa_ocr.image_parser.RunwayDiagramParser;

public class ImageParsingTest
{

    public static void main(String[] args) throws IOException
    {
        //String path = "/Users/jokvedaras/Documents/workspace/faa_implementation/FAA_OCR/res/ACY/00669AD1.jpg";  //ACY
        //String path = "/Users/jokvedaras/Documents/workspace/faa_implementation/FAA_OCR/res/ATL/00026AD1.jpg";  //ATL
        String path = "/Users/jokvedaras/Documents/workspace/faa_implementation/FAA_OCR/res/DFW/06039AD1.jpg";  //DFW
        //String path = "/Users/jokvedaras/Documents/workspace/faa_implementation/FAA_OCR/res/PHX/00322AD1.jpg";  //PHX

        File image = new File(path);

        BufferedImage airport_image = ImageIO.read(image);

        //get runway data from image
        new RunwayDiagramParser().parseRunways(airport_image);

    }
}
