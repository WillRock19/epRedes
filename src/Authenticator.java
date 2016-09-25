//To learn more about this decode method, see: http://www.rgagnon.com/javadetails/java-0598.html

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Authenticator 
{
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
		return decodeAuthentication().equals("restrict-access");
	}
	
	private String decodeAuthentication()
	{	
		
		
		
		byte[] decodedValue = Base64.getDecoder().decode(requestStream.RequestHeader().Authentication());
		return new String(decodedValue, StandardCharsets.UTF_8);
	}
}
