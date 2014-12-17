/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package faa_ocr.testing;

import faa_ocr.kml_parser.*;
import java.io.*;

/**
 *
 * @author g_ric_000
 */
public class KMLDriver
{

    public static void main(String[] args)
    {
        XMLtoKML kmlMaker = new XMLtoKML();
        File f = new File("FAA_OCR\\ATL_Airport.xml");
        kmlMaker.writeKML(f);
    }
}
