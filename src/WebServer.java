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
		
		// Extrair o nome do arquivo a linha de requisição.
		StringTokenizer tokens = new StringTokenizer(requestLine);
		
		tokens.nextToken(); // pular o método, que deve ser “GET”
		String fileName = tokens.nextToken();
		
		// Acrescente um “.” de modo que a requisição do arquivo esteja dentro do diretório atual.
		fileName = "." + fileName;

		
		// Abrir o arquivo requisitado.
		FileInputStream fis = null;
		boolean fileExists = true;
		
		try 
		{
			fis = new FileInputStream(fileName);
		} 
		catch (FileNotFoundException e) 
		{
			fileExists = false;
		}

		
		// Construir a mensagem de resposta.
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		
		if(fileExists) 
		{
			statusLine = "Respondendo requisição";
			contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
		} 
		else 
		{
			statusLine = "Página não encontrada";
			contentTypeLine = "no content";
			entityBody = "<HTML>" +
				"<HEAD><TITTLE>Not Found</TITTLE></HEAD>" +
				"<BODY>Not Found</BODY></HTML>";
		}
		
		// Enviar a linha de status.
		os.writeBytes(statusLine);
		// Enviar a linha de tipo de conteúdo.
		os.writeBytes(contentTypeLine);
		// Enviar uma linha em branco para indicar o fim das linhas de cabeçalho.
		os.writeBytes(CRLF);

		// Enviar o corpo da entidade.
		if(fileExists) 
		{
			sendBytes(fis, os);
			fis.close();
		} 
		else 
		{
			os.writeBytes(entityBody);
		}
		
		//Close writers and socket
		os.close();
		lineReader.close();
		socket.close();
	}
	
	private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception
	{
		// Construir um buffer de 1K para comportar os bytes no caminho para o socket.
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		// Copiar o arquivo requisitado dentro da cadeia de saída do socket.
		while((bytes = fis.read(buffer)) != -1 ) {
			os.write(buffer, 0, bytes);
		}
	}
	
	private static String contentType(String fileName)
	{
		if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
			return "text/html";
		}
		if(fileName.endsWith(".gif") || fileName.endsWith(".GIF"))
		{
			return "image/gif";
		}
		if(fileName.endsWith(".jpeg") || fileName.endsWith(".JPEG")) 
		{
			return "image/jpeg";
		}
		return "application/octet-stream";
	}

}


