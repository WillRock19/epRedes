package helpers;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomHeaderLogFormatter extends Formatter 
{
	@Override
	public String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();
/*		sb.append(System.getProperty("line.separator"));
		sb.append("Prefixn ");
		sb.append(record.getMessage());
		sb.append(" Suffixn");
		sb.append(System.getProperty("line.separator"));
		sb.append("n");*/
		return sb.toString();
	}

}
