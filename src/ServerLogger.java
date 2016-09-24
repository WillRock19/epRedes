import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import helpers.CustomHeaderLogFormatter;
import helpers.CustomMessageLogFormatter;

public class ServerLogger 
{
	private FileHandler fileHandler;
	private Logger logger;
	
	public ServerLogger()
	{
		configureLogger();
	}
	
	public void printLogInfo(String message)
	{
		logger.info(message);
	}
	
	public void printLogWarning(String message)
	{
		logger.warning(message);
	}
	
	private void configureLogger()
	{
		try
		{
			defineLogger();
			defineFileHandler();
			setMessageFormatter();
		}
		catch (SecurityException e) 
		{
            e.printStackTrace();
        } 
		catch (IOException e) 
		{
            e.printStackTrace();
        }
	}
	
	private void defineLogger()
	{
		logger = Logger.getLogger("ServerLogger");	
	}
	
	private void defineFileHandler() throws IOException
	{
        fileHandler = new FileHandler(defineFilePathName(), true);
        logger.addHandler(fileHandler);
	}
	
	private String defineFilePathName()
	{
		String directoryPath;		
		try
		{
			directoryPath = System.getProperty("user.dir");	
		}
		catch(Exception e)
		{
			directoryPath = "./";
		}
		
		return directoryPath + "ServerLog.log";
	}
	
	private void setMessageFormatter()
	{
		CustomMessageLogFormatter formatter = new CustomMessageLogFormatter();           
		fileHandler.setFormatter(formatter);
	}
	
	private void setHeaderFormatter()
	{
		CustomHeaderLogFormatter formatter = new CustomHeaderLogFormatter();
		fileHandler.setFormatter(formatter);
	}
}
