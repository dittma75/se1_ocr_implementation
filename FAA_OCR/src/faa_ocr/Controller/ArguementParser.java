import java.nio.file.Files;



public class ArguementParser 
{
	
	public ArguementParser()
	{
		//do nothing
	}
	
	
	/**
	 * Verify any paths that are inputted and make sure they are .pdf
	 * @param path	path to file you want to verify
	 * @return true if path is valid and it is a .pdf, false otherwise
	 */
	public boolean verifyPDFPaths(String[] paths)
	{
		
		for (int i = 0; i < paths.length; i++)
		{
			File file = new File(paths[i]);
			if(file.exisits())
			{
				if(Files.probeContentType(file.toPath()).equals("pdf"))
				{
					//TODO: How do we want to handle good/bad pdf files?
				}
			}
			
			
		}
	}
}
