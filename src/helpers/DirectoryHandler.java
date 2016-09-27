/*
 * CLASS DEFINITION: This class represents an object that handles with the Directory files. Basically, it will be instantiate when 
 * the client request a directory instead of a simple file. 
 * 
 * METHODS: Here we got two methods: first, that return the confirmation if this is a directory. The second returns a list of directory
 * subItens - it iterate through the files list that the directory has and add the ones that are not restrict to a new list that it will
 * be returned to client. 
 * 
 * OBS: The restrict items are the ones associated with Eclipse internal structure (bin or .settings files) or git structure (.git).
 * 
 */

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
				if(!itemIsRestrict(subItem.getName()))
					list.add(subItem.getName());
			}
			return list;
		}
		return null;
	}
	
	private boolean itemIsRestrict(String item)
	{
		return itemIsInternalEclipse(item) || item.equals(".git") || itemIsLockFileFromLog(item);
	}
	
	private boolean itemIsInternalEclipse(String item)
	{
		return item.equals("bin") || item.equals("src") || item.equals(".settings") || item.equals(".classpath") || item.equals(".project");
	}
	
	private boolean itemIsLockFileFromLog(String item)
	{
		return item.toLowerCase().endsWith(".lck");
	}
}
