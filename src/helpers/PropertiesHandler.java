// To see more about property in java: http://www.devmedia.com.br/utilizando-arquivos-de-propriedades-no-java/25546

package helpers;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesHandler 
{
	private Properties properties;
	
	public PropertiesHandler() throws Exception
	{
		properties = getPropertiesFile();
	}
	
	public String getProperty(String property)
	{
		return properties.getProperty(property);
	}
	
	private Properties getPropertiesFile() throws Exception
	{
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("./properties/dados.properties");
		props.load(file);
		
		return props;	
	}
}
