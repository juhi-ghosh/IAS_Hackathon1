package mypkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class APIfront
 */
@WebServlet("/APIfront")
public class APIfront extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Properties props = new Properties();
	public  KafkaConsumer<String, String> consumer;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public APIfront() {
		super();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList("RESPONSE"));
		
	}

	@Override
	public void init() throws ServletException {
		super.init();

		// instantiate consumer
		// servlet init : declares array map
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String resp = "HI!\n";
		resp += request.getContextPath() + "\n" + request.getMethod();

		String json = "";
		String line = "";
		InputStream ip = request.getInputStream();
		InputStreamReader isr = new InputStreamReader(ip);
		BufferedReader br = new BufferedReader(isr);

		while ((line = br.readLine()) != null) {
			json += line + "\n";
		}

		System.out.println("PLAIN JSON:" + json);

		JsonParser jsonParser = new JsonParser();
		JsonElement req = jsonParser.parse(json);
		String token = req.getAsJsonObject().get("sectoken").toString();
		token = token.substring(1, token.lastIndexOf("\""));
		if (!token.equals("****")) {
			SimpleProducer prod = new SimpleProducer();
			// SimpleConsumer cons = new SimpleConsumer();
			String resp_temp = "HHHHHHHHH";
			// add ID and response in the map
			try {
				prod.addToMsgQ(req);
				//readMsgQ("juhi", response);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			try {
				readMsgQ("juhi", response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//writeResponseToClinet("hELLO",response);
	}

	void writeResponseToClinet(String resp, HttpServletResponse response) throws IOException {
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet CALL</title>");	
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>" + resp + "</h1>");
			out.println("</body>");
			out.println("</html>");
		}
	}

	public void readMsgQ(String ID, HttpServletResponse response) throws Exception {

		System.out.println("FINAL READING by client");
		// Kafka consumer configuration settings
		//Properties props = new Properties();

		
		//KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		//consumer.subscribe(Arrays.asList("RESPONSE"));
		String value = "";
		int flag=0;
		
		//while(true){
			ConsumerRecords<String, String> records = consumer.poll(100);
			System.out.println("left : "+ records.isEmpty());
			
			for (ConsumerRecord<String, String> record : records) {
				 System.out.println("No. of rec=" + records.count());
				// print the offset,key and value for the consumer records.
//				System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
//				System.out.println(record.topic());
//				System.out.println(record.key());
//				System.out.println(record.value());

				String topicType = record.topic();
				value = record.value();
				
//				if(records.count()==0)
//					flag=1;
//			}
//			
//			if(flag == 1){
//				break;
			
		}
		

		writeResponseToClinet(value, response);
	}
}
