package secpkg;

import java.io.DataInputStream;
import java.io.IOException;

public class Security {
	
	public String runExternalCommand(String username, String passwd) throws IOException
	   {
	       String line,res=null;
	       Runtime runtime = Runtime.getRuntime();
	       Process process = null;
	       
	       process = runtime.exec("python passcheck.py " + username + " " + passwd);
	       // process = runtime.exec("pwd");
	       DataInputStream in = new DataInputStream(process.getInputStream());
	       res = in.readLine();
	           while ((line = in.readLine()) != null) {
	               res+=line;
	     }
	           System.out.println(username+" "+passwd+" "+res);
	           return res;
	   }
	}
