import helpers.HTMLGenerator;
import helpers.MessageProperties;

public class ResponseMessage 
{
	private MessageProperties messageProperties;
	private HTMLGenerator htmlGenerator; 	
	private RequestStream stream;

	public ResponseMessage(RequestStream stream)
	{
		this.stream = stream;
		this.htmlGenerator = new HTMLGenerator();
		this.messageProperties = new MessageProperties();
	}
		
	public void createResponseMessage(boolean fileExists, String fileName)
	{
		if(fileExists) 
		{
			defineSuccessMessageProperties(fileName);
		} 
		else 
		{
			defineNotFoundMessageProperties();
		}
	}
	
	public void sendFileToOutputStream(RequestElementBuilder fileBuilder) throws Exception
	{
		if(fileBuilder.elementExists()) 
		{
			stream.sendFileBytesToOutputStream(fileBuilder.file());
			fileBuilder.closeFileStream();
		} 
		else 
		{
			stream.sendToOutputStream(messageProperties.entityBody());
		}
	}
	
	public void defineNotAuthenticatedProperties()
	{
		messageProperties.setStatusLine("HTTP/1.0 401");
		messageProperties.setAuthentication("WWW-Authenticate: Basic realm='myRealm'");
	}
	
	public MessageProperties properties()
	{
		return messageProperties;
	}
		
	private void defineSuccessMessageProperties(String fileName)
	{
		messageProperties.setStatusLine("HTTP/1.0 200");
		messageProperties.setContentType("Content-type: " + contentType(fileName));
	}
	
	private void defineNotFoundMessageProperties()
	{
		messageProperties.setStatusLine("HTTP/1.0 404");
		messageProperties.setContentType("Content-type: text/html");
		messageProperties.setEntityBody(htmlGenerator.NotFoundPage());
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
