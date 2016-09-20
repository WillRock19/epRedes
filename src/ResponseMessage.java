
public class ResponseMessage 
{
	//End of each HTTP request, with a carriage return (CR) and a line feed (LF).
	//CRLF, basically, is an ASCII specification that says to "jump a line and go to the beginning of this new line".
	final static String CRLF = "\r\n";
	private String statusLine = null;
	private String contentTypeLine = null;
	private String entityBody = null;
	private RequestStream stream;

	public ResponseMessage(RequestStream stream)
	{
		this.stream = stream;
	}
	
	
	public void createResponseMessage(boolean fileExists, String fileName)
	{
		if(fileExists) 
		{
			defineSuccessMessageProperties(fileName);
		} 
		else 
		{
			defineErrorMessageProperties();
		}
	}
	
	public void sendMessagePropertiesToOutputStream() throws Exception
	{
		// Enviar a linha de status.
		stream.sendToOutputStream(statusLine);

		// Enviar a linha de tipo de conteudo.
		stream.sendToOutputStream(contentTypeLine);

		// Enviar uma linha em branco para indicar o fim das linhas de cabecalho.
		stream.sendToOutputStream(CRLF);
	}
	
	public void sendFileToOutputStream(RequestFileBuilder fileBuilder) throws Exception
	{
		if(fileBuilder.fileExists()) 
		{
			stream.sendFileBytesToOutputStream(fileBuilder.file());
			fileBuilder.closeFileStream();
		} 
		else 
		{
			stream.sendToOutputStream(entityBody);
		}
	}
	
	
	private void defineSuccessMessageProperties(String fileName)
	{
		statusLine = "HTTP/1.0 200" + CRLF;
		contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
	}
	
	private void defineErrorMessageProperties()
	{
		statusLine = "HTTP/1.0 404" + CRLF;
		contentTypeLine = "Content-type: text/html" + CRLF;
		entityBody = "<HTML>" +
			"<HEAD><TITLE>Not Found</TITLE></HEAD>" +
			"<BODY>Not Found</BODY></HTML>" + CRLF;
	}
	
	
	
	private String contentType(String fileName)
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
