import faa_ocr.Controller.ArgumentParser;



/**
 * Class to control all airport behaviors
 * @author jokvedaras
 *
 */
public class AirportController 
{
	public static void main(String[] args)
	{
		//Accept list of list of PDFS
		for(String arg : args){
			if(ArgumentParser.parseArguement(arg))
			{
				//do everything to pdf document
			}
			else
			{
				//TODO: do we want to do anything if the path is not a pdf??
			}
		}
	}
	
	
	private String writeKML(File xml_file)
	{
		
	}
	
	private String writeXML(Airport airport)
	{
		
	}
	
	
	private void parseVisualData(Airport airport)
	{
		
	}
	
	private void parseTextData(Airport airport)
	{
		
	}
	
}
