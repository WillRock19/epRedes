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
		
		
		// Processar a requisição de serviço HTTP em um laço infinito.
		while (true)  {
			// Escutar requisição de conexão TCP.
			
			Socket connectionSocket = mySocket.accept();
			
			//Construir um objeto para processar a mensagem de requisição HTTP.
			HttpRequest request = new HttpRequest(connectionSocket);
			
			// Criar um novo thread para processar a requisição.
			Thread thread = new Thread(request);
			//Iniciar o thread.
			thread.start();

			
		}
	}
	
	static final class HttpRequest implements Runnable
	{
		final static String CRLF = "\r\n";
		private Socket socket;
		
		public HttpRequest(Socket socket) throws Exception
		{
			this.socket = socket;
		}
		
		// Implemente o método run() da interface Runnable.
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
			// Obter uma referência para os trechos de entrada e saída do socket.
			InputStream is = this.socket.getInputStream();
			DataOutputStream os = new DataOutputStream(this.socket.getOutputStream());
			
			

		}
	}

}


