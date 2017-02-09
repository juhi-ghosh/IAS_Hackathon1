package mypkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class APIfront
 */
@WebServlet("/APIfront")
public class APIfront extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public APIfront() {
        super();
        // TODO Auto-generated constructor stub
    }

	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
        String resp = "HI!\n";
        resp+= request.getContextPath()+"\n"+request.getMethod();
        
        Gson gson = new Gson();
        
        String json = "";
        String line = "";
        InputStream ip = request.getInputStream();
        InputStreamReader isr = new InputStreamReader(ip);
        BufferedReader br = new BufferedReader(isr);

        while((line = br.readLine()) != null)
        {
            json += line+"\n";
        }
        
        System.out.println("PLAIN JSON:" + json);
		
		
		JsonParser jsonParser = new JsonParser();
		JsonElement req = jsonParser.parse(json);
		String token = req.getAsJsonObject().get("sectoken").toString();
		token = token.substring(1, token.lastIndexOf("\""));
		if(!token.equals("****")){
		SimpleProducer prod = new SimpleProducer();
        // SimpleConsumer cons = new SimpleConsumer();
        String resp_temp = "HHHHHHHHH";
		
        try {
			prod.addToMsgQ(req);
			//cons.readMsgQ();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        
        
	   //String resp_temp=runExternalCommand(u_name,u_pass);
        writeResponseToClinet(resp_temp,response);
        }
	}
	    
	    void writeResponseToClinet(String resp,HttpServletResponse response) throws IOException
	    {
	        try (PrintWriter out = response.getWriter()) {
	            /* TODO output your page here. You may use following sample code. */
	            out.println("<!DOCTYPE html>");
	            out.println("<html>");
	            out.println("<head>");
	            out.println("<title>Servlet CALL</title>");            
	            out.println("</head>");
	            out.println("<body>");
	            out.println("<h1>" + resp +"</h1>");
	            out.println("</body>");
	            out.println("</html>");
	        }
	    }
}
