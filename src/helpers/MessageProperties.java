package helpers;

public class MessageProperties 
{
	//End of each HTTP request, with a carriage return (CR) and a line feed (LF).
	//CRLF, basically, is an ASCII specification that says to "jump a line and go to the beginning of this new line".
	final static String CRLF = "\r\n";
	private String statusLine;
	private String contentTypeLine;
	private String entityBody;
	private String WWW_Authentication;
	
	public MessageProperties(){ }
	
	public String entityBody()
	{
		return statusLine;
	}
	
	public String contentType()
	{
		return contentTypeLine;
	}
	
	public String statusLine()
	{
		return entityBody;
	}
	
	public String authentication()
	{
		return WWW_Authentication;
	}
	
	public String crlf()
	{
		return CRLF;
	}
	
	public void setEntityBody(String value)
	{
		statusLine = value + crlf();
	}
	
	public void setContentType(String value)
	{
		contentTypeLine = value + crlf();
	}
	
	public void setStatusLine(String value)
	{
		entityBody = value + crlf();
	}
	
	public void setAuthentication(String value)
	{
		WWW_Authentication = value + crlf();
	}
}
