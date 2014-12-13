/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package faa_ocr.testing;
import faa_ocr.text_parser.*;
import faa_ocr.ADTs.Airport;

/**
 *
 * @author Kevin Dittmar
 */
public class TextParserTest
{
    public static void main(String[] args)
    {
        //testACYAirport();
        testATLAirport();
        //testDFWAirport();
        //testPHXAirport();
    }
    
    public static void testACYAirport()
    {
        Airport airport = new Airport("res/ACY/00669AD.pdf", true);
        PDFToText pdftotext = new PDFToText();
        pdftotext.parseTextData(airport);
        System.out.println(airport.toString());
    }
    
    public static void testATLAirport()
    {
        Airport airport = new Airport("res/ATL/00026AD.pdf", false);
        PDFToText pdftotext = new PDFToText();
        pdftotext.parseTextData(airport);
        System.out.println(airport.toString());
    }
    
    public static void testDFWAirport()
    {
        Airport airport = new Airport("res/DFW/06039AD.pdf", true);
        PDFToText pdftotext = new PDFToText();
        pdftotext.parseTextData(airport);
        System.out.println(airport.toString());
    }
    
    public static void testPHXAirport()
    {
        Airport airport = new Airport("res/PHX/00322AD.pdf", true);
        PDFToText pdftotext = new PDFToText();
        pdftotext.parseTextData(airport);
        System.out.println(airport.toString());
    }
}
