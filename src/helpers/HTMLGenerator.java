package helpers;

public class HTMLGenerator 
{
	private String headerOpen = "<HTML>";
	private String headerClose = "</HTML>";	
	
	public HTMLGenerator() { }
		
	public String NotFoundPage()
	{ 
		return headerOpen + createHeadTag("Not Found") + createBodyTag("Not Found") + headerClose;  
	}
	
	public String IndexPage()
	{ 
		return "";  
	}
	
	public String ListOfFilesPage()
	{ 
		return "";  
	}
	
	public String FileContentPage()
	{ 
		return "";  
	}
	
	private String createHeadTag(String titleContent)
	{
		return String.format("<HEAD> %s </HEAD>", createTitleTag(titleContent));
	}
	
	private String createTitleTag(String content)
	{
		return String.format("<TITLE> %s </TITLE>", content);
	}
	
	private String createBodyTag(String bodyContent)
	{
		return String.format("<BODY> %s </BODY>", bodyContent);
	}
	
}
