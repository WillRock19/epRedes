import java.io.*;

public class RequestFileBuilder 
{
	private String fileName;
	private FileInputStream file;
	private boolean fileExists;
	
	public RequestFileBuilder(RequestStream stream)
	{		
		// Define File Name
		defineFileNameUsingRequestedURI(stream);
		establishFileNameAsInCurrentDirectory();
	}
	
	public void openRequestedFile()
	{
		try 
		{
			file = new FileInputStream(fileName);
			fileExists = true;
		} 
		catch (FileNotFoundException e) 
		{
			fileExists = false;
		}
	}
	
	public FileInputStream file()
	{
		return file;
	}
	
	public String fileName()
	{
		return fileName;
	}
	
	public boolean fileExists()
	{
		return fileExists;
	}
	
	public void closeFileStream() throws Exception
	{
		file.close();
	}
	
	private void defineFileNameUsingRequestedURI(RequestStream stream)
	{
		fileName = stream.requestedURI();
	}
	
	private void establishFileNameAsInCurrentDirectory()
	{
		fileName = "." + fileName;
	}
}
