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
		catch (Exception e){
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
		
		//Authenticate access and process user request
		authenticateAndExecuteRequest(stream);
				
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
			boolean authenticated = authenticator.AuthenticateAccess();
			
			if(authenticated) 
				executeServerLogic(stream);
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
}