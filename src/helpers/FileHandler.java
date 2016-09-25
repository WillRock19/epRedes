package helpers;

import java.io.File;
import java.util.ArrayList;

public class FileHandler 
{
	private File element;
	
	public FileHandler(String path)
	{
		try
		{
			this.element = new File(path);
		}
		catch(Exception e)
		{
			
		}
	}
	
	public boolean isFile()
	{
		return element.isFile();
	}
	
	public boolean isDirectory()
	{
		return element.isDirectory();
	}
	
	public ArrayList<String> listSubItens()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		if(isDirectory())
		{
			for (File subItem : element.listFiles()) 
			{
		        list.add(subItem.getName());
			}
			return list;
		}
		return null;
	}
	
	
}
