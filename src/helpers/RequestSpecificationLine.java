package helpers;

public class RequestSpecificationLine 
{
	private String method;
	private String requestURI;
	private String httpVersion;
	
	public RequestSpecificationLine(String entireLine)
	{
		String[] brokeLine = entireLine.split(" ");
		
		this.method = brokeLine[0];
		this.requestURI = brokeLine[1];
		this.httpVersion = brokeLine[2];
	}
	
	public String Method()
	{
		return method;
	}
	
	public String URI()
	{
		return requestURI;
	}
	
	public String HttpVersion()
	{
		return httpVersion;
	}
	
	public String FullValue()
	{
		return String.format("%s %s %s", method, requestURI, httpVersion);
	}
}
