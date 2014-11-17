package faa_ocr.text_parser;

/**
 * LineFormatter removes extra characters that may interfere with regular
 * expression matching in other TextParser modules.
 * @author Kevin Dittmar
 */
public class LineFormatter
{
    /**
     * No intialization is needed for the line formatter.
     */
    public LineFormatter()
    {
        
    }
    
    /**
     * Removes multiple occurrences of newlines and multiple occurrences of
     * spaces from the given String and returns the formatted String.
     * @param raw_data is the String of text to be formatted.
     * @return the formatted String.
     */
    String getFormattedString(String raw_data)
    {
        //Replace multiple newlines with one newline.
        String formatted_data = raw_data.replaceAll("\n+", "\n");
        //Replace multiple spaces with one space.
        formatted_data = formatted_data.replaceAll(" +", " ");
        return formatted_data;
    }
}
