import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		logConnectionInformation();
		
		//Create stream that will manipulate client request
		RequestStream stream = new RequestStream(socket);
		
		//Save requestLine and other header lines to log file
		log.printLogInfo("Requested method: " + stream.requestedMethod());
		log.printLogInfo("Requested URI file: " + stream.requestedURI());
		
		//Print header Lines information in console
/*		stream.printRequestLine();
		stream.printRequestHeaderLines();*/
		
		//Create builder of requested file
		RequestFileBuilder fileBuilder = new RequestFileBuilder(stream);
		fileBuilder.openRequestedFile();
		
		//Create response message that will be send to client
		ResponseMessage response = new ResponseMessage(stream);
		response.createResponseMessage(fileBuilder.fileExists(), fileBuilder.fileName());
		response.sendFileToOutputStream(fileBuilder);
		
		//Close stream and socket
		stream.closeOutputStreamAndLineReader();
		socket.close();
		log.closeFileHandler();
	}
	
	private void logConnectionInformation()
	{
		String clientIP = socket.getRemoteSocketAddress().toString();
		
		log.setHeaderFormatter(clientIP);
		log.printLogInfo("Request captured time: " + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date()));
		log.printLogInfo("Server Port: " + 6789);
		log.printLogInfo("Client Adress: " + clientIP);
		log.printLogInfo("Client Name: " + socket.getInetAddress().getHostName());
	} 
}