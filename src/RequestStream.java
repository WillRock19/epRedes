import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import helpers.MessageProperties;
import helpers.RequestHeader;

public class RequestStream
{
	private InputStream byteReader;
	private DataOutputStream outputStream;
	private BufferedReader lineReader;
	private RequestHeader requestHeader;
	
	public RequestStream(Socket socket) throws Exception
	{
		//Get references to input and output request streams
		defineByteReader(socket);
		defineOutputStream(socket);
				
		//Create line reader to read input char stream
		defineLineReader();
		
		//Create object that represents important properties in request header
		defineRequestHeader();
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
		return requestHeader.RequestLine().FullValue();
	}
	
	public String requestedURI()
	{
		return requestHeader.RequestLine().URI();
	}
	
	public String requestedMethod()
	{
		return requestHeader.RequestLine().Method();
	}
	
	public RequestHeader RequestHeader()
	{
		return requestHeader;
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
	
	public BufferedReader lineReader()
	{
		return lineReader;
	}
	
	public void sendSimpleHeaderToOutputStream(MessageProperties message) throws Exception
	{
		// Send status line to outputStream
		sendToOutputStream(message.statusLine());

		// Send contentType line to outputStream
		sendToOutputStream(message.contentType());

		// Send CRLF line to outputStream
		sendToOutputStream(message.crlf());
	}
	
	public void sendNotAuthenticatedHeaderToOutputStream(MessageProperties message) throws Exception
	{
		// Enviar a linha de status.
		sendToOutputStream(message.statusLine());

		// Enviar a linha de tipo de autenticacao.
		sendToOutputStream(message.authentication());

		// Enviar uma linha em branco para indicar o fim das linhas de cabecalho.
		sendToOutputStream(message.crlf());
	}
	
	private void defineRequestHeader()
	{
		requestHeader = new RequestHeader(lineReader);
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
	
	private byte[] createOneKbyteArray()
	{
		return new byte[1024];
	}
}
