import java.io.*;
import java.net.*;

public class RequestStream
{
	private InputStream byteReader;
	private DataOutputStream outputStream;
	private BufferedReader lineReader;
	private String requestLine;
	
	public RequestStream(Socket socket) throws Exception
	{
		//Get references to input and output request streams
		defineByteReader(socket);
		defineOutputStream(socket);
				
		//Create line reader to read input char stream
		defineLineReader();
				
		// Read request line from HTTP request.
		defineRequestLine();
	}
	
	public void printRequestLine()
	{
		System.out.println();
		System.out.println(requestLine);
	}
	
	public void printRequestHeaderLines() throws Exception
	{
		String headerLine = null;
		while ((headerLine = lineReader.readLine()).length() != 0) 
		{
			System.out.println(headerLine);
		}
	}
	
	public String requestLine()
	{
		return requestLine;
	}
	
	public DataOutputStream outputStream()
	{
		return outputStream;
	}
	
	public void sendToOutputStream(String value) throws Exception
	{
		outputStream.writeBytes(value);
	}
	
	public void closeOutputStreamAndLineReader() throws Exception
	{
		outputStream.close();
		lineReader.close();
	}
		
	public void sendFileBytesToOutputStream(FileInputStream inputStream) throws Exception
	{
		// Build buffer to Store bytes for the socket.
		byte[] buffer = createOneKbyteArray();
		int bytes = 0;
		
		// Copy required file to socket outputStream.
		while((bytes = inputStream.read(buffer)) != -1 ) 
		{
			outputStream.write(buffer, 0, bytes);
		}
	}
	
	private void defineByteReader(Socket socket) throws Exception
	{
		this.byteReader = socket.getInputStream();
	}
	
	private void defineOutputStream(Socket socket) throws Exception
	{
		outputStream = new DataOutputStream(socket.getOutputStream());
	}
	
	private void defineLineReader()
	{
		lineReader = new BufferedReader(new InputStreamReader(byteReader));
	}
	
	private void defineRequestLine() throws Exception
	{
		requestLine = lineReader.readLine();
	}
	
	private byte[] createOneKbyteArray()
	{
		return new byte[1024];
	}
}
