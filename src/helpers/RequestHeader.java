package helpers;

import java.io.BufferedReader;

public class RequestHeader 
{
	private RequestSpecificationLine requestLine;
	private String authentication;
	private String host; 
	private String userAgent;

	
	public RequestHeader(BufferedReader header)
	{
		defineHeaderProperties(header);
	}
	
	public String Authentication()
	{
		return authentication;
	}
	
	public String Host()
	{
		return host;
	}
	
	public String UserAgent()
	{
		return userAgent;
	}
	
	public RequestSpecificationLine RequestLine()
	{
		return requestLine;
	}
	
	private void defineHeaderProperties(BufferedReader header)
	{
		try{
			definePropertiesByBuffer(header);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	private void definePropertiesByBuffer(BufferedReader header) throws Exception
	{
	    String line;
	    
	    while ((line = header.readLine()).length() != 0) 
	        setProperty(line);
	}
	
	private void setProperty(String property)
	{
		property.toLowerCase().contains("".toLowerCase());
		
		// Read request line from HTTP client request.
		if(propertyIsTheSpecificationLine(property))
			requestLine = new RequestSpecificationLine(property);
		
		if(propertyContains(property, "Host"))
			host = property;
		
		if(propertyContains(property, "User-Agent"))
			userAgent = property;
		
		if(propertyContains(property, "Authorization"))
			authentication = property;
	}
	
	private boolean propertyContains(String property, String textToCompare)
	{
		return property.toLowerCase().contains(textToCompare.toLowerCase()); 
	}
	
	private boolean propertyIsTheSpecificationLine(String property)
	{
		return propertyContainsMethodDefinition(property) && propertyContains(property, "HTTP/1.");
	}
	
	private boolean propertyContainsMethodDefinition(String property)
	{
		return propertyContains(property, "GET") || propertyContains(property, "HEAD") || propertyContains(property, "POST");
	}
}
