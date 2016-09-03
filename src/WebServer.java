import java.io.*;
import java.net.*;
import java.util.*;

public final class WebServer 
{
	public static void main(String[] args) throws Exception
	{
		int port = 6789;
		
		//Estabelecer um socket de escuta
		ServerSocket mySocket = new ServerSocket(port);
		
		
		// Processar a requisi��o de servi�o HTTP em um la�o infinito.
		while (true)  {
			// Escutar requisi��o de conex�o TCP.
			
			Socket connectionSocket = mySocket.accept();
			
			//Construir um objeto para processar a mensagem de requisi��o HTTP.
			HttpRequest request = new HttpRequest(connectionSocket);
			
			// Criar um novo thread para processar a requisi��o.
			Thread thread = new Thread(request);
			//Iniciar o thread.
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
	
	// Implemente o m�todo run() da interface Runnable.
	public void run()
	{
		try{
			processRequest();
		} 
		catch (Exception e){
			System.out.println("An error has occurred when processing the request!\n");
			System.out.println(e);
		}
	}
	
	private void processRequest() throws Exception
	{
		// Obter uma refer�ncia para os trechos de entrada e sa�da do socket.
		InputStream byteReader = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		
		
		BufferedReader lineReader = new BufferedReader(new InputStreamReader(byteReader));

		// Read request line from HTTP request.
		String requestLine = lineReader.readLine();
		
		//  Exibir a linha de requisi��o.
		System.out.println();
		System.out.println("\n\n\nA LINHA DE REQUISI��O EH: \n\n");
		System.out.println(requestLine);

		
		// Obter e exibir as linhas de cabe�alho.
		String headerLine = null;
		while ((headerLine = lineReader.readLine()).length() != 0) 
		{
			System.out.println(headerLine);
		}
		
		// Feche as cadeias e socket.
		os.close();
		lineReader.close();
		socket.close();
	}
}


