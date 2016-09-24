import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import helpers.CustomHeaderLogFormatter;

public class LogHandler 
{
	private FileHandler fileHandler;
	private Logger logger;
	private String logFileName = "\\ServerLog.log";
	
	public LogHandler()
	{
		configureLogger();
	}
	
	public void printLogInfo(String message)
	{
		logger.info(message);
	}
		
	public void printLogInfo(String[] messages)
	{
		for(int index = 0; index < messages.length; index++)
		{
			logger.info(messages[index]);
		}
	}
	
	public void printLogWarning(String message)
	{
		logger.warning(message);
	}
	
	public void setHeaderFormatter(String clientIP)
	{
		CustomHeaderLogFormatter formatter = new CustomHeaderLogFormatter(clientIP);
		fileHandler.setFormatter(formatter);
	}
	
	public void closeFileHandler()
	{
		fileHandler.close();
	}
	
	public void printInLogTheConnectionInformation(Socket socket)
	{
		String clientIP = socket.getRemoteSocketAddress().toString();
		
		setHeaderFormatter(clientIP);
		printLogInfo("Request captured time: " + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date()));
		printLogInfo("Server Port: " + 6789);
		printLogInfo("Client Adress: " + clientIP);
		printLogInfo("Client Name: " + socket.getInetAddress().getHostName());
	}
	
	private void configureLogger()
	{
		try
		{
			defineLogger();
			defineFileHandler();
		}
		catch (SecurityException e) 
		{
			System.out.println("An security error has occurred on logger configuration!");
            e.printStackTrace();
        } 
		catch (IOException e) 
		{
			System.out.println("An IO error has occurred on logger configuration!");
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
		String fullDirectory;
		String directory;
		
		try
		{
			directory = System.getProperty("user.dir");	
		}
		catch(Exception e)
		{
			directory = ".\\";
		}
		
		fullDirectory = directory + "\\Log_Files";	
		createDirectoryIfNotExists(fullDirectory);
		
		return fullDirectory + logFileName;
	}
	
	private void createDirectoryIfNotExists(String directoryPath)
	{
		File file = new File(directoryPath);
		
		if(!file.exists())
		{
			file.mkdirs();
		}
	}
}
