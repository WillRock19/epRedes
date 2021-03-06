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
	
	public String IndexPage(String text)
	{ 
		String containerText = "<H2>This is the Index Page.</H2>" + String.format("<DIV> %s </DIV>", text);
		return headerOpen + createHeadTag("Index") + createBodyTag(createContainerDiv(containerText)) + headerClose;
	}
	
	public String listPage(ArrayList<String> list, String directoryName)
	{ 
		return headerOpen + createHeadTag(defineListPageTitle(directoryName)) 
						  + createBodyTag(createContainerDiv(list, directoryName)) 
						  + headerClose ;  
	}
	
	public String ForbiddenPage(String text)
	{ 
		String containerText = "<H2>Access Forbidden.</H2>" + String.format("<DIV> %s </DIV>", text);
		return headerOpen + createHeadTag("Access Denied") + createBodyTag(createContainerDiv(containerText)) + headerClose;  
	}
	
	
	
	private String defineListPageTitle(String directoryName)
	{
		return String.format("Listing %s Directory", defineDirectoryName(directoryName));
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
	
	private String createUlTag(ArrayList<String> list, String directoryName)
	{
		String liTags = "";
		
		for(String element : list)
		{
			liTags += createLiTag(element, directoryName);
		}
		
		return String.format("<UL> %s </UL>", liTags);
	}
	
	private String createLiTag(String liContent, String directoryName)
	{
		return String.format("<LI> %s </LI>", creatLinkTag(liContent, directoryName));
	}
	
	private String creatLinkTag(String content, String directoryName)
	{
		return String.format("<a href='%s'> %s </a>", (directoryName + "/" + content), content);
	}
	
	private String createContainerDiv(String content)
	{
		return String.format("<DIV class='container'> %s </DIV>", content);
	}
	
	private String createContainerDiv(ArrayList<String> list, String directoryName)
	{
		return String.format("<DIV class='container'> %s %s </DIV>", createH2Tag("Arquivos do diretório: " + defineDirectoryName(directoryName)), createUlTag(list, directoryName));
	}
	
	private String createH2Tag(String headerName)
	{
		return String.format("<H2> %s </H2>", headerName);
	}
	
	private String defineDirectoryName(String name)
	{
		if(name.equals("./"))
			return " root ";
		
		else
			return name;
	}
}
