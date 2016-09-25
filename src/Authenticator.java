//To learn more about this decode method, see: http://www.rgagnon.com/javadetails/java-0598.html
//To learn about the regular expression used to test if a string is in Base64, see: http://stackoverflow.com/questions/8571501/how-to-check-whether-the-string-is-base64-encoded-or-not

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Authenticator 
{
	private String validCredentials = "restrict-access";
	private String restrictUrl = "restrict-access";
	private RequestStream requestStream;
	private ResponseMessage messager;
	
	
	public Authenticator(RequestStream requestStream)
	{
		this.requestStream = requestStream;
		this.messager = new ResponseMessage(requestStream);
	}
	
	public boolean requestedUriIsRestrict()
	{
		return requestStream.requestedURI().toLowerCase().contains(restrictUrl.toLowerCase());
	}
	
	public void AuthenticateAccess()
	{
		try
		{
			if(!userHasAuthenticated())
			{
				messager.defineNotAuthenticatedProperties();
				messager.sendNotAuthenticatedHeaderToOutputStream();
			}
			else
			{
				testUserAuthentication();
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	
	private boolean userHasAuthenticated()
	{
		return requestStream.RequestHeader().Authentication() != null;
	}
	
	private void testUserAuthentication()
	{
		if(userCredentialsAreValid())
		{
			System.out.println("Valid credentials");
		}
		else
		{
			System.out.println("Invalid credentials");
		}
	}
	
	private boolean userCredentialsAreValid()
	{
		return decodeAuthentication().equals(validCredentials);
	}
	
	private String decodeAuthentication()
	{	
		String[] authenticationData = requestStream.RequestHeader().Authentication().split(" ");
		
		
		byte[] decodedValue = Base64.getDecoder().decode(getUserInformationData(authenticationData));
		return new String(decodedValue, StandardCharsets.UTF_8);
	}
	
	private String getUserInformationData(String[] authenticationData)
	{
		for(int i = 0; i < authenticationData.length; i++)
		{
			if(textIsBase64Encoded(authenticationData[i]))
				return authenticationData[i];
		}
		
		return "";
	}
	
	private boolean textIsBase64Encoded(String text)
	{
		return text.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");
	}
}