import java.net.*;

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