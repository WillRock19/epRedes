/*
 * CLASS DEFINITION: This class process the request inside a run method, that will be call by the thread where this one will run (to see
 * about the thread, check WebServer.java). This will call every other method of program and make the authorize and validate request. Since
 * sometimes the program is receiving an invalid call (from a flavicon.icon), we verify it the call is valid and, if not, just close the 
 * streams.
 * 
 * METHODS: Here we have some private help methods to make the logic. We call the authenticate class here to test if user can access the
 * restrict folder (it only will be tested if he tries to). If user does not want to access the restrict folder or the credentials to access
 * it are valid, the program executes the rest of logic that is defined inside executeServerLogic method.
 * 
 */

import java.net.Socket;

final class HttpRequest implements Runnable
{
	private Socket socket;
	private LogHandler log;
	
	public HttpRequest(Socket socket) throws Exception
	{
		this.socket = socket;
		this.log = new LogHandler();
	}
	
	//Implementation of run() from Runnable interface.
	public void run()
	{
		try{
			processRequest();
		} 
		catch (Exception e)
		{
			log.printLogWarning("The following error has occured: " + e.getMessage());
			
			System.out.println("An error has occurred while processing the request!\n");
			System.out.println(e);
		}
	}
	
	private void processRequest() throws Exception
	{		
		//Put in log the header informations about current connection
		log.printInLogTheConnectionInformation(socket);
		
		//Create stream that will manipulate client request
		RequestStream stream = new RequestStream(socket);
		
		//Save requestLine and other header lines to log file
		log.printLogInfo("Requested method: " + stream.requestedMethod());
		log.printLogInfo("Requested URI file: " + stream.requestedURI());
		
		//Verify if request is a valid one or a flavicon.ico one
		if(!isFaviconRequest(stream))
		{
			//Authenticate access and process user request
			authenticateAndExecuteRequest(stream);
		}
		else{
			//Saving in log the Flavicon.ico`s request abort
			log.printLogInfo("Aborting request processing because requested URI is: " + stream.requestedURI());
		}
		
		//Close stream and socket
		stream.closeOutputStreamAndLineReader();
		socket.close();
		
		//Closing log file handler
		log.closeFileHandler();
	}
	
	private void authenticateAndExecuteRequest(RequestStream stream) throws Exception
	{
		//Create authenticator object
		Authenticator authenticator = new Authenticator(stream);
		
		if(authenticator.requestedUriIsRestrict())
		{
			log.printLogInfo("User is trying to access the restrict URI. Initiating authentication... ");		
			boolean authenticated = authenticator.AuthenticateAccess();
			
			if(authenticated)
			{
				log.printLogInfo("User authenticated!");
				executeServerLogic(stream);
			}
			
			log.printLogInfo("User could not be authenticated!");
		}
		else
		{
			executeServerLogic(stream);			
		}	
	}
	
	private void executeServerLogic(RequestStream stream) throws Exception
	{
		//Create builder of requested file or directory
		RequestElementBuilder elementBuilder = new RequestElementBuilder(stream);
		elementBuilder.open();
		
		//Create response message that will be send to client
		log.printLogInfo("Creating response message...");
		ResponseMessage response = new ResponseMessage();
		response.createResponse(elementBuilder);	
		
		//Send created headers to output Stream
		stream.sendSimpleHeaderToOutputStream(response.properties());
		
		//Write number of bytes from outputStream in the log files 
		int headerSize = stream.outputStreamSize();	
		log.printLogInfo("Size of response message's header (in bytes): " + headerSize);
		
		//Send opened file to OutputStream or, if file does not exist, an entityBody
		stream.sendFileToOutputStream(elementBuilder, response.properties());
		
		//Saving last info in log file
		log.printLogInfo("Size of entity's body message (in bytes): " + (stream.outputStreamSize() - headerSize));			
		log.printLogInfo("Total size of response message: " + stream.outputStreamSize());
	}
	
	private boolean isFaviconRequest(RequestStream stream)
	{
		return stream.requestedURI().contains("/favicon.ico");
	}
}