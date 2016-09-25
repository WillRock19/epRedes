package helpers;

import java.util.ArrayList;

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
		return headerOpen + createHeadTag("Index") + createBodyTag("This is the Index Page") + headerClose;
	}
	
	public String listPage(ArrayList<String> list, String pageTitle)
	{ 
		return headerOpen + createHeadTag(pageTitle) + createBodyTag(createUlTag(list)) + headerClose ;  
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
	
	private String createUlTag(ArrayList<String> list)
	{
		String liTags = "";
		
		for(String element : list)
		{
			liTags = createLiTag(element);
		}
		
		return String.format("<UL> %s </UL>", liTags);
	}
	
	private String createLiTag(String liContent)
	{
		return String.format("<LI> %s </LI>", liContent);
	}
}
