import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestStream
{
	private InputStream byteReader;
	private DataOutputStream outputStream;
	private BufferedReader lineReader;
	private RequestLine requestLine;
	
	public RequestStream(Socket socket) throws Exception
	{
		//Get references to input and output request streams
		defineByteReader(socket);
		defineOutputStream(socket);
				
		//Create line reader to read input char stream
		defineLineReader();
				
		// Read request line from HTTP client request.
		defineRequestLine();
	}
	
	public void printRequestLine()
	{
		System.out.println();
		System.out.println(requestLine());
	}
	
	public void printRequestHeaderLines() throws Exception
	{
		String headerLine = null;
		BufferedReader newReader = lineReader;
		
		while ((headerLine = newReader.readLine()).length() != 0) 
		{
			System.out.println(headerLine);
		}
		
		newReader.close();
	}
	
	public String requestLine()
	{
		return requestLine.FullValue();
	}
	
	public String requestedURI()
	{
		return requestLine.URI();
	}
	
	public String requestedMethod()
	{
		return requestLine.Method();
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
	
	public int outputStreamSize()
	{
		return outputStream.size();
	}
	
	
	
/*	public ArrayList<String> getAllLinesInRequest() throws Exception
	{
		String currentLine;
		ArrayList<String> allLines = new ArrayList<String>();
		BufferedReader newReader = lineReader;
			
		while ((currentLine = newReader.readLine()).length() != 0) 
		{
			allLines.add(currentLine);
		}
				
		return allLines;
	}
	*/
	
	
	
	
	
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
		requestLine = new RequestLine(lineReader.readLine());
	}
	
	private byte[] createOneKbyteArray()
	{
		return new byte[1024];
	}
	
	private void defineHeaderJson() throws Exception
	{
		String newLine = lineReaderInString();

		
		
	}
	
	private String lineReaderInString() throws Exception
	{
		StringBuilder sb = new StringBuilder();

	    String line;
	    while ((line = lineReader.readLine()) != null) 
	    {
	        sb.append(line);
	    }
	    
	    return sb.toString();
	}
}
