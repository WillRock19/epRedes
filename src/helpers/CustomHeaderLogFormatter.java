package helpers;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.text.SimpleDateFormat;

public class CustomHeaderLogFormatter extends Formatter 
{
	private String clientIP;
	
	public CustomHeaderLogFormatter(String clientIP)
	{
		this.clientIP = clientIP;
	}
	
	@Override
	public String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("<Client: %s - Time: %s> ", clientIP, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())));
		sb.append(record.getMessage());
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}
}
