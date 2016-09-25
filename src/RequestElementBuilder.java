import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import helpers.DirectoryHandler;

public class RequestElementBuilder 
{
	private String elementName;
	private FileInputStream file;
	private DirectoryHandler directory;
	private boolean elementExists;

	public RequestElementBuilder(RequestStream stream)
	{		
		// Define File Name
		defineFileNameUsingRequestedURI(stream);
		establishFileNameAsInCurrentDirectory();
	}
	
	public void open()
	{
		try 
		{
			file = new FileInputStream(elementName);
			elementExists = true;
		} 
		catch (FileNotFoundException e) 
		{
			checkRequestedElementIsDirectory();
		}
	}
	
	public FileInputStream file()
	{
		return file;
	}
	
	public String elementName()
	{
		return elementName;
	}
	
	public boolean elementExists()
	{
		return elementExists;
	}
	
	public void closeFileStream() throws Exception
	{
		file.close();
	}
	
	public ArrayList<String> subItens()
	{
		if(elementExists)
		{
			return directory.listSubItens();
		}
		return null;
	}
	
	private void checkRequestedElementIsDirectory()
	{
		try
		{
			directory = new DirectoryHandler(elementName);
			elementExists = true;
		}
		catch(Exception e)
		{
			elementExists = false;
		}	
	}
	
	private void defineFileNameUsingRequestedURI(RequestStream stream)
	{
		elementName = stream.requestedURI();
	}
	
	private void establishFileNameAsInCurrentDirectory()
	{
		elementName = "." + elementName;
	}
}
