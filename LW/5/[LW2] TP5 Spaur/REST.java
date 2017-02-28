package sepa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.UUID;
import java.util.Vector;

public class REST {
	
	public static void main(String[] args) {

		try {
			
			URL url = new URL("http://10.130.208.232:8080/exist/rest/db/sepa");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/xml");
			conn.setRequestProperty("Accept", "application/xml");

			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
		
			conn.disconnect();
		
		} catch (MalformedURLException e) {
		
			e.printStackTrace();
		
		} catch (IOException e) {
		
			e.printStackTrace();
		
		}
	}
	
	public String getXML(String uri) {
		XmlRpc.setEncoding("UTF-8");
	    XmlRpcClient xmlrpc = new XmlRpcClient( uri );
	    Hashtable options = new Hashtable();
	    options.put("indent", "yes");
	    options.put("encoding", "UTF-8");
	    options.put("expand-xincludes", "yes");
	    options.put("highlight-matches", "elements");
	    
	    Vector params = new Vector();
	    params.addElement( options );
	    String xml = (String)
	        xmlrpc.execute( "getDocumentAsString", params );
	    System.out.println( xml );
	    return xml;
	}
}
