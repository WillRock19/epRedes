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
		
/*		stream.printRequestLine();
		stream.printRequestHeaderLines();*/
		
		Authenticator authenticator = new Authenticator(stream);
		
		if(!authenticator.requestedUriIsRestrict())
		{
			
		}
		else
		{
			authenticator.AuthenticateAccess();
		}
		
		
		
		
		
		
/*		if(stream.requestedURI().toLowerCase().contains("restrict-access".toLowerCase()))
		{
			String CRLF = "\r\n";
			String statusLine = "HTTP/1.0 401" + CRLF;
			String WWW_Authentication = "WWW-Authenticate: Basic realm='myRealm'" + CRLF;
			
			
			stream.sendToOutputStream(statusLine);
			stream.sendToOutputStream(WWW_Authentication);
			stream.sendToOutputStream(CRLF);
		}*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
/*		//Create builder of requested file
		RequestFileBuilder fileBuilder = new RequestFileBuilder(stream);
		fileBuilder.openRequestedFile();
		
		//Create response message that will be send to client
		ResponseMessage response = new ResponseMessage(stream);
		response.createResponseMessage(fileBuilder.fileExists(), fileBuilder.fileName());
		
		//Send created headers to output Stream
		response.sendMessageHeaderToOutputStream();
		
		//Write number of bytes from outputStream in the log files 
		int headerSize = stream.outputStreamSize();	
		log.printLogInfo("Size of response message's header (in bytes): " + headerSize);
		
		//Send opened file to OutputStream
		response.sendFileToOutputStream(fileBuilder);
		
		//Saving last info in log file
		log.printLogInfo("Size of entity's body message (in bytes): " + (stream.outputStreamSize() - headerSize));			
		log.printLogInfo("Total size of response message: " + stream.outputStreamSize());*/
		
		//Close stream and socket
		stream.closeOutputStreamAndLineReader();
		socket.close();
		
		//Closing log file handler
		log.closeFileHandler();
	}
}