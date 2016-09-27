import java.util.ArrayList;

import helpers.HTMLGenerator;
import helpers.MessageProperties;
import helpers.PropertiesHandler;

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
	
	public void createResponse(RequestElementBuilder element) throws Exception
	{
		if(element.elementExists())
		{
			if(element.isDirectory())
			{
				defineDirectoryAccessResponseProperties(element);
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
		messageProperties.setAuthentication("WWW-Authenticate: Basic realm='Insira suas credenciais'");
	}
	
	public MessageProperties properties()
	{
		return messageProperties;
	}
	
	private void defineDirectoryAccessResponseProperties(RequestElementBuilder element) throws Exception
	{
		PropertiesHandler prop = new PropertiesHandler();	
		String listPageType = prop.getProperty("prop.server.listPageType");
		
		switch(listPageType)
		{
			case  "1":
				defineSuccessPropertiesToHTMLContent();
				defineEntityBodyAsListPage(element.subItens(), element.elementName());
				break;
			
			case  "2":
				defineForbiddenProperties();
				defineEntityBodyAsForbiddenPage("Data.properties listType is defined with 2. Folder elements cannot be listed!");
				break;
				
			case  "3":
				defineSuccessPropertiesToHTMLContent();
				defineEntityBodyAsIndexPage("It was access because of data.properties listType was defined as 3!");
				break;
				
			default:
				defineSuccessPropertiesToHTMLContent();
				defineEntityBodyAsIndexPage("This was loaded because data.properties is not defined properly!");
		}
	}
	
	private void defineSuccessPropertiesToHTMLContent()
	{
		messageProperties.setStatusLine("HTTP/1.0 200");
		messageProperties.setContentType("Content-type: text/html");
	}
	
	private void defineSuccessProperties(String fileName)
	{
		messageProperties.setStatusLine("HTTP/1.0 200");
		messageProperties.setContentType("Content-type: " + contentType(fileName));
	}
		
	private void defineNotFoundProperties()
	{
		messageProperties.setStatusLine("HTTP/1.0 404");
		messageProperties.setContentType("Content-type: text/html");
		messageProperties.setEntityBody(htmlGenerator.NotFoundPage());
	}
	
	private void defineForbiddenProperties()
	{
		messageProperties.setStatusLine("HTTP/1.0 403");
		messageProperties.setContentType("Content-type: text/html");
	}
	
	private void defineEntityBodyAsIndexPage(String text)
	{
		messageProperties.setEntityBody(htmlGenerator.IndexPage(text));
	}
	
	private void defineEntityBodyAsListPage(ArrayList<String> list, String pageName)
	{
		messageProperties.setEntityBody(htmlGenerator.listPage(list, pageName));
	}
	
	private void defineEntityBodyAsForbiddenPage(String text)
	{
		messageProperties.setEntityBody(htmlGenerator.ForbiddenPage(text));
	}
	
		
	private String contentType(String fileName)
	{
		if(fileName.endsWith(".htm") || fileName.endsWith(".html") || fileName.endsWith(".txt")) 
		{
			return "text/html";
		}
		if(fileName.endsWith(".gif") || fileName.endsWith(".GIF"))
		{
			return "image/gif";
		}
		if(fileName.endsWith(".jpeg") || fileName.endsWith(".JPEG") || fileName.toLowerCase().endsWith(".jpg")) 
		{
			return "image/jpeg";
		}
		return "application/octet-stream";
	}
}
