import java.io.*;
import java.net.*;
import java.util.*;

public final class WebServer 
{
	public static void main(String[] args) throws Exception
	{
		int port = 6789;
		
		//Establish a listenning socket
		ServerSocket mySocket = new ServerSocket(port);
		
		
		// Process request in a HTTP service in an infinit loop
		while (true)  
		{
			// Listens request of TCP connection			
			Socket connectionSocket = mySocket.accept();
			
			//Build object to process HttpRequesto
			HttpRequest request = new HttpRequest(connectionSocket);
			
			//Create new thread to process request
			Thread thread = new Thread(request);
			
			//Begin thread
			thread.start();	
		}
	}
}

final class HttpRequest implements Runnable
{
	final static String CRLF = "\r\n";
	private Socket socket;
	
	public HttpRequest(Socket socket) throws Exception
	{
		this.socket = socket;
	}
	
	//Implementation of run() from Runnable interface.
	public void run()
	{
		try{
			processRequest();
		} 
		catch (Exception e){
			System.out.println("An error has occurred while processing the request!\n");
			System.out.println(e);
		}
	}
	
	private void processRequest() throws Exception
	{
		//Get references to input and output request streams
		InputStream byteReader = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		
		//Create line reader to read input char stream
		BufferedReader lineReader = new BufferedReader(new InputStreamReader(byteReader));

		// Read request line from HTTP request.
		String requestLine = lineReader.readLine();
		
		//Print request line
		System.out.println();
		System.out.println(requestLine);

		
		//Get request's header lines
		String headerLine = null;
		while ((headerLine = lineReader.readLine()).length() != 0) 
		{
			System.out.println(headerLine);
		}
		
		//Close writers and socket
		os.close();
		lineReader.close();
		socket.close();
	}
}


