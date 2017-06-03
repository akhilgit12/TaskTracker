package hello;

import javax.servlet.http.HttpServletRequest;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map;

public class APIRequest{
	private static APIRequest apireq =null;
	private APIRequest(){

	}
	public synchronized static APIRequest getInstance(){
		if(apireq == null){
			apireq = new APIRequest();
		}
		return apireq;
	}
	public Properties getParams(HttpServletRequest request){
	    Properties params = new Properties();
		try{
	        JSONParser parser = new JSONParser();
	        JSONObject obj = (JSONObject) parser.parse(request.getReader());
	        Iterator it = (Iterator) obj.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry ent = (Map.Entry)it.next();
	            params.put(ent.getKey(),ent.getValue());
	        }
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return params;
    }	
}