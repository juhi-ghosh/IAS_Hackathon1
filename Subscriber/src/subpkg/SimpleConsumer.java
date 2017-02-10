/**
 * 
 */
package subpkg;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author juhi
 *
 */
public class SimpleConsumer {

	public static void main(String args[]) {
		SimpleConsumer scons = new SimpleConsumer();
		try {
			scons.readMsgQ();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public String readMsgQ() throws Exception {

		// Kafka consumer configuration settings
		Properties props = new Properties();

		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		// Kafka Consumer subscribes list of topics here.
		consumer.subscribe(Arrays.asList("SECURITY", "FILE", "LOGGING"));

		// print the topic name
		System.out.println("Subscribed to topic SECURITY: " + consumer.listTopics().containsKey("SECURITY"));
		int i = 0;

		String response;
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);

			for (ConsumerRecord<String, String> record : records) {
				// System.out.println("No. of rec=" + records.count());
				// print the offset,key and value for the consumer records.
				System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
				System.out.println(record.topic());
				System.out.println(record.key());
				System.out.println(record.value());

				String topicType = record.topic();
				@SuppressWarnings("rawtypes")
				Class[] cArg = null;
				Object[] args1 = null;
				switch (topicType) {
				case "SECURITY":
					// call security module
					cArg = new Class[2];
					cArg[0] = String.class;
					cArg[1] = String.class;

					args1 = new Object[2];
					args1[0] = new String(record.key());
					args1[1] = new String(record.value());
					response = getJavaJar("/home/juhi/IIITH/Sem4/IAS/workspace/Subscriber/AuthenticationService.jar",
							"secpkg.Security", "runExternalCommand", cArg, args1);
					System.out.println("res =" + response);
					addToMsgQ(record.key(), response);
					break;

				case "FILE":
					// call file CRUD operations
					cArg = new Class[2];
					cArg[0] = String.class;
					cArg[1] = String.class;

					args1 = new Object[2];
					args1[0] = new String(record.key());
					args1[1] = new String(record.value());
					response = getJavaJar("/home/juhi/IIITH/Sem4/IAS/workspace/Subscriber/FileService.jar",
							"filepkg.Crud", "getFileOper", cArg, args1);
					System.out.println("res =" + response);

					break;
				case "LOGGING":
					// call utility logging mechanism
					break;
				}
			}

		}
	}

	@SuppressWarnings("unchecked")
	private String getJavaJar(String jarfile, String file_or_serviceName, String methodName, Class[] cArg,
			Object[] args1) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException,
					SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
					InstantiationException {

		System.out.println("jar:" + jarfile);
		System.out.println("HERE:" + file_or_serviceName);
		String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
		File filenm = new File(jarfile);

		@SuppressWarnings("deprecation")
		URL url = filenm.toURL();
		URL[] urls = new URL[] { url };
		@SuppressWarnings("resource")
		ClassLoader cl = new URLClassLoader(urls);

		@SuppressWarnings("rawtypes")
		Class cls = cl.loadClass(file_or_serviceName);
		httpResponse += cls.getMethod(methodName, cArg).invoke(cls.newInstance(), args1);

		return httpResponse;

	}

	public void addToMsgQ(String ID, String resp) throws Exception {

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		System.out.println("From response"+ID+resp);
		producer.send(new ProducerRecord<String, String>("RESPONSE", ID, resp));
		System.out.println("RESPONSE sent successfully!");
		producer.close();
	}
}