import java.net.*;

final class HttpRequest implements Runnable
{
	private Socket socket;
	
	public HttpRequest(Socket socket) throws Exception
	{
		this.socket = socket;
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
		//Create stream that will manipulate client request
		RequestStream stream = new RequestStream(socket);
		stream.printRequestLine();
		stream.printRequestHeaderLines();
		
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
	}
}