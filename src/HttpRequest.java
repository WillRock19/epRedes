import java.net.*;

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
		
		RequestFileBuilder fileBuilder = new RequestFileBuilder(stream);
		fileBuilder.openRequestedFile();
		
		// Construir a mensagem de resposta.
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		
		if(fileBuilder.fileExists()) 
		{
			statusLine = "HTTP/1.0 200" + CRLF;
			contentTypeLine = "Content-type: " + contentType(fileBuilder.fileName()) + CRLF;
		} 
		else 
		{
			statusLine = "HTTP/1.0 404" + CRLF;
			contentTypeLine = "text/html" + CRLF;
			entityBody = "<HTML>" +
				"<HEAD><TITLE>Not Found</TITLE></HEAD>" +
				"<BODY>Not Found</BODY></HTML>" + CRLF;
		}
		
		// Enviar a linha de status.
		stream.sendToOutputStream(statusLine);

		// Enviar a linha de tipo de conte�do.
		stream.sendToOutputStream(contentTypeLine);

		// Enviar uma linha em branco para indicar o fim das linhas de cabe�alho.
		stream.sendToOutputStream(CRLF);

		// Enviar o corpo da entidade.
		if(fileBuilder.fileExists()) 
		{
			stream.sendFileBytesToOutputStream(fileBuilder.file());
			fileBuilder.closeFileStream();
		} 
		else 
		{
			stream.sendToOutputStream(entityBody);
		}
		
		//Close writers and socket
		stream.closeOutputStreamAndLineReader();
		socket.close();
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