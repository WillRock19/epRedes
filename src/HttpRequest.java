import java.io.*;
import java.net.*;
import java.util.*;

final class HttpRequest implements Runnable
{
	//End of each HTTP request, with a carriage return (CR) and a line feed (LF).
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
		RequestStream stream = new RequestStream(socket);
		
		stream.printRequestLine();
		stream.printRequestHeaderLines();
			
		// Extrair o nome do arquivo a linha de requisição.
		StringTokenizer tokens = new StringTokenizer(stream.requestLine());
		
		// pular o método, que deve ser “GET”
		tokens.nextToken(); 
		
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
				"<HEAD><TITLE>Not Found</TITLE></HEAD>" +
				"<BODY>Not Found</BODY></HTML>";
		}
		
		// Enviar a linha de status.
		stream.sendToOutputStream(statusLine);

		// Enviar a linha de tipo de conteúdo.
		stream.sendToOutputStream(contentTypeLine);

		// Enviar uma linha em branco para indicar o fim das linhas de cabeçalho.
		stream.sendToOutputStream(CRLF);

		// Enviar o corpo da entidade.
		if(fileExists) 
		{
			sendBytes(fis, stream.outputStream());
			fis.close();
		} 
		else 
		{
			stream.sendToOutputStream(entityBody);
		}
		
		//Close writers and socket
		stream.closeOutputStreamAndLineReader();
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
		if(fileName.endsWith(".htm") || fileName.endsWith(".html")) 
		{
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