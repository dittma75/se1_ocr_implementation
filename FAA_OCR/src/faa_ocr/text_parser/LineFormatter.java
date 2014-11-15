package faa_ocr.text_parser;

/**
 *
 * @author Kevin Dittmar
 */
public class LineFormatter
{
    public LineFormatter()
    {
        
    }
    
    String getFormattedString(String raw_data)
    {
        //Replace multiple newlines with one newline.
        String formatted_data = raw_data.replaceAll("\n+", "\n");
        //Replace multiple spaces with one space.
        formatted_data = formatted_data.replaceAll(" +", " ");
        return formatted_data;
    }
}
