package faa_ocr.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Joe Kvedaras
 *
 */
public class ArgumentParser 
{
	
//	/**
//	 * Verify any paths that are inputted and make sure they are .pdf
//	 * @param path	path to file you want to verify
//	 * @return true if path is valid and it is a .pdf, false otherwise
//	 */
//	public static boolean verifyPDFPaths(String[] paths)
//	{
//		
//		for (int i = 0; i < paths.length; i++)
//		{
//			File file = new File(paths[i]);
//			if(file.exists())
//			{
//				if(Files.probeContentType(file.toPath()).equals("pdf"))
//				{
//					//TODO: How do we want to handle good/bad pdf files?
//				}
//			}
//			
//			
//		}
//	}
	
	/**
	 * Verify an inputed path and make sure it is a .pdf
	 * @param path	path to file you want to verify
	 * @return true if path is valid and it is a .pdf, false otherwise
	 * @throws IOException 
	 */
	public static boolean parseArgument(String pdf_file_path)
	{
			File file = new File(pdf_file_path);
			if(file.exists())
			{
				//Open up the file and check the first 4 characters to make sure it equals %PDF
				try
				{
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String line = reader.readLine();
					System.out.println(line);
					
					if(line.startsWith("%PDF"))
					{
						reader.close();
						return true;
					}
					else
					{
						reader.close();
						return false;
					}
					
				}
				catch (IOException e)
				{
					System.err.println("IO Exception when checking if path is a PDF");
					e.printStackTrace();
				}
				
				return false;
				
			}
			else{
				//file does not exist.
				return false;
			}
	}
	
}
