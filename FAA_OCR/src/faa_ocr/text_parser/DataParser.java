/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package faa_ocr.text_parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The purpose of the DataParser class is combine similar functionality in
 * the AirportDataParser and RunwayDataParser classes into one class.
 * @author Kevin Dittmar
 */
public abstract class DataParser
{
    /**
     * Take a String pattern and test the text given to see if it
     * matches the given pattern.
     * @param pattern the String representation of a regular expression 
     * pattern.
     * @param text the String text to match against the pattern.
     * @return the String result that matches, or the empty string if there
     * is no match.
     */
    protected String searchForItem(String pattern, String text)
    {
        //Compile the pattern given.
        Pattern matcher_pattern = Pattern.compile(pattern);
        
        //Set up the matcher for the pattern.
        Matcher matcher = matcher_pattern.matcher(text);
        
        //If we found something that matches the pattern...
        if (matcher.find())
        {
            //return the part of the string that matched.
            return matcher.group(1);
        }
        return "";
    }
}
