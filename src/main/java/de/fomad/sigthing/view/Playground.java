package de.fomad.sigthing.view;

import de.fomad.sigthing.controller.DatabaseController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 *
 * @author binary gamura
 */
public class Playground 
{
    public static void main(String[] args) throws URISyntaxException
    {
//	String test = "/hello?test=me";
//	URI uri = new URI(test);
//	List<NameValuePair> params = URLEncodedUtils.parse(uri, "UTF-8");
//	params.stream().forEach((param) ->
//	{
//	    System.out.println(param.getName()+" => "+param.getValue());
//	});
//	uri.getQuery();
	
	try
	{
	    DatabaseController dbController = new DatabaseController();
	    dbController.initDatabase();
	}
	catch(Exception ex)
	{
	    ex.printStackTrace(System.err);
	}
    }
}
