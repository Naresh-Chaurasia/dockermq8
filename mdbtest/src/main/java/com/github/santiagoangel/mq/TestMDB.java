package com.github.santiagoangel.mq;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;





import org.jboss.ejb3.annotation.ResourceAdapter;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "useJNDI", propertyValue = "false"),
		@ActivationConfigProperty(propertyName = "hostName", propertyValue = "${websphere.hostName}"),
		@ActivationConfigProperty(propertyName = "username", propertyValue = "${websphere.username}"),
		@ActivationConfigProperty(propertyName = "password", propertyValue = "${websphere.password}"),
		@ActivationConfigProperty(propertyName = "port", propertyValue = "${websphere.port}"),
		@ActivationConfigProperty(propertyName = "channel", propertyValue = "${websphere.channel}"),
		@ActivationConfigProperty(propertyName = "queueManager", propertyValue = "${websphere.queueManager}"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "${websphere.queueName}"),
		@ActivationConfigProperty(propertyName = "transportType", propertyValue = "CLIENT"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
@ResourceAdapter(value = "wmq.jmsra.rar")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class TestMDB implements MessageListener {

	@Override
	public void onMessage(Message message) {

		if (message instanceof TextMessage) {
			try {
				String mqmsg=((TextMessage) message).getText();
				System.out.println("MSG RECEIVED: "+mqmsg);
				if(uploadFile(mqmsg)){
					System.out.println("MSG WRITE TO FILE ");
				}
			} catch (JMSException e) {
				throw new IllegalArgumentException(e);
			}
		}

	}
	private boolean uploadFile(String text) {

		String name = "mqmsg" + "-" + System.currentTimeMillis() + "-" 
				+ java.util.UUID.randomUUID() + ".txt.in";

		String rootPath = "";

		try {
			

			// Creating the directory to store file
			rootPath = ".." + File.separator + "vpe_messages" + File.separator + "Inputs" + File.separator + "MQ";

			File dir = new File(rootPath);
			if (!dir.exists())
				dir.mkdirs();

			StringWriter sw = new StringWriter();
			sw.append(text);
			

			Files.write(Paths.get(rootPath + File.separator + name), sw.toString().getBytes());

			System.out.println("File created:" + rootPath + File.separator + name);

			return true;
		} catch (Exception e) {
			
			System.out.println("Unable to generate File:" + rootPath + File.separator + name);

			return false;
		}

	}
}