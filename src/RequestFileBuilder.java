import java.io.*;
import java.util.StringTokenizer;

public class RequestFileBuilder 
{
	private StringTokenizer tokens;
	private String fileName;
	private FileInputStream file;
	private boolean fileExists;
	
	public RequestFileBuilder(RequestStream stream)
	{
		
		// Extract file name from requestLine
		generateTokensWithFileNameInRequestLine(stream);
		
		// Jump request method specification, that we assume is a GET type
		advanceAToken();
		
		// Define File Name
		defineFileNameWithTokens();
		defineFileNameAsInCurrentDirectory();
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
	
	private void generateTokensWithFileNameInRequestLine(RequestStream stream)
	{
		tokens = new StringTokenizer(stream.requestLine());
	}
	
	private void advanceAToken()
	{
		tokens.nextToken();
	}
	
	private void defineFileNameWithTokens()
	{
		fileName = tokens.nextToken();
	}
	
	private void defineFileNameAsInCurrentDirectory()
	{
		fileName = "." + fileName;
	}
}
