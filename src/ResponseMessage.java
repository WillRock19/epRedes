import java.util.ArrayList;

import helpers.HTMLGenerator;
import helpers.MessageProperties;

public class ResponseMessage 
{
	private MessageProperties messageProperties;
	private HTMLGenerator htmlGenerator;

	public ResponseMessage()
	{
		this.htmlGenerator = new HTMLGenerator();
		this.messageProperties = new MessageProperties();
	}
		
	public void createResponseMessage(boolean fileExists, String fileName)
	{
		if(fileExists) 
		{
			defineSuccessProperties(fileName);
		} 
		else 
		{
			defineNotFoundProperties();
		}
	}
	
	public void createResponse(RequestElementBuilder element)
	{
		if(element.elementExists())
		{
			if(element.isDirectory())
			{
				defineSuccessPropertiesToHTMLContent();
				defineEntityBodyAsListPage(element.subItens(), element.elementName());
			}
			else
			{
				defineSuccessProperties(element.elementName());
			}
		}
		else
		{
			defineNotFoundProperties();
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
	
	private void defineEntityBodyAsListPage(ArrayList<String> list, String pageName)
	{
		messageProperties.setEntityBody(htmlGenerator.listPage(list, pageName));
	}
		
	private void defineSuccessProperties(String fileName)
	{
		messageProperties.setStatusLine("HTTP/1.0 200");
		messageProperties.setContentType("Content-type: " + contentType(fileName));
	}
	
	private void defineSuccessPropertiesToHTMLContent()
	{
		messageProperties.setStatusLine("HTTP/1.0 200");
		messageProperties.setContentType("Content-type: text/html");
	}
	
	
	private void defineNotFoundProperties()
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
