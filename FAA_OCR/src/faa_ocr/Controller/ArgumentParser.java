package faa_ocr.Controller;

import java.io.File;
import java.nio.file.Files;


public class ArgumentParser 
{
	
	/**
	 * Verify any paths that are inputted and make sure they are .pdf
	 * @param path	path to file you want to verify
	 * @return true if path is valid and it is a .pdf, false otherwise
	 */
	public static boolean verifyPDFPaths(String[] paths)
	{
		
		for (int i = 0; i < paths.length; i++)
		{
			File file = new File(paths[i]);
			if(file.exists())
			{
				if(Files.probeContentType(file.toPath()).equals("pdf"))
				{
					//TODO: How do we want to handle good/bad pdf files?
				}
			}
			
			
		}
	}
	
	/**
	 * Verify an inputted path and make sure it is a .pdf
	 * @param path	path to file you want to verify
	 * @return true if path is valid and it is a .pdf, false otherwise
	 */
	public static boolean parseArguement(String path)
	{
                    File file = new File(path);
                    if(file.exists())
                    {
                            if(Files.probeContentType(file.toPath()).equals("pdf"))
                            {
                                    //TODO: How do we want to handle good/bad pdf files?
                                    return true;
                            }
                            else
                            {
                                    return false;
                            }
                    }
	}
	
}
