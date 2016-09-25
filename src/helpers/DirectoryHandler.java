package helpers;

import java.io.File;
import java.util.ArrayList;

public class DirectoryHandler 
{
	private File element;
	
	public DirectoryHandler(String path) throws Exception
	{
		this.element = new File(path);
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
