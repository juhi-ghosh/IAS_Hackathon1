/**
 * 
 */
package mypkg;

//import util.properties packages
import java.util.Properties;

//import KafkaProducer packages
import org.apache.kafka.clients.producer.KafkaProducer;
//import simple producer packages
import org.apache.kafka.clients.producer.Producer;
//import ProducerRecord packages
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.JsonElement;

/**
 * @author juhi
 *
 */
// Create java class named "SimpleProducer"
public class SimpleProducer {

	public void addToMsgQ(JsonElement req) throws Exception {

		// parseJSON() to retrieve below params
		// addToMsgQ();

		// Assign topicName to string variable
		String topicType = req.getAsJsonObject().get("type").toString();
		topicType = topicType.substring(1, topicType.lastIndexOf("\""));

		String sectoken = req.getAsJsonObject().get("sectoken").toString();
		sectoken = sectoken.substring(1, sectoken.lastIndexOf("\""));

		String usernm = req.getAsJsonObject().get("user").toString();
		usernm = usernm.substring(1, usernm.lastIndexOf("\""));

		String passwd = req.getAsJsonObject().get("pass").toString();
		passwd = passwd.substring(1, passwd.lastIndexOf("\""));

		String filenm = req.getAsJsonObject().get("filename").toString();
		filenm = filenm.substring(1, filenm.lastIndexOf("\""));

		String oper = req.getAsJsonObject().get("operation").toString();
		oper = oper.substring(1, oper.lastIndexOf("\""));

		String serviceNm = req.getAsJsonObject().get("servicename").toString();
		serviceNm = serviceNm.substring(1, serviceNm.lastIndexOf("\""));

		// create instance for properties to access producer configs
		Properties props = new Properties();

		// Assign localhost id
		props.put("bootstrap.servers", "localhost:9092");

		// Set acknowledgements for producer requests.
		props.put("acks", "all");

		// If the request fails, the producer can automatically retry,
		props.put("retries", 0);

		// Specify buffer size in config
		props.put("batch.size", 16384);

		// Reduce the no of requests less than 0
		props.put("linger.ms", 1);

		// The buffer.memory controls the total amount of memory available to
		// the producer for buffering.
		props.put("buffer.memory", 33554432);

		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<String, String>(props);

		System.out.println("TT:" + topicType + "GGGGGG");

		switch (topicType) {
		case "SECURITY":
			producer.send(new ProducerRecord<String, String>(topicType, usernm, passwd));
			break;
		case "FILE":
			producer.send(new ProducerRecord<String, String>(topicType, filenm, oper));
			break;
		case "LOGGING":
			break;

		}
		System.out.println("Message sent successfully");
		producer.close();
	}
}