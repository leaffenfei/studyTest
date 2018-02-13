package com.gpf.study.activemq.protogenesis;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.benchmark.Consumer;

public class Subscriber {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String user=ActiveMQConnection.DEFAULT_USER;
		String pwd=ActiveMQConnection.DEFAULT_PASSWORD;
		String url=ActiveMQConnection.DEFAULT_BROKER_URL;
		String strQueueName="demoSubPubGPF";
		ConnectionFactory connectionFactory=new ActiveMQConnectionFactory();
		
		try {
			Connection connection=connectionFactory.createConnection();
			connection.start();
			final Session session=connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
			Topic topic=session.createTopic(strQueueName);
			MessageConsumer consumer=session.createConsumer(topic);
			consumer.setMessageListener(new MessageListener() {
				
				public void onMessage(Message msg) {
					// TODO Auto-generated method stub
					TextMessage textMessage=(TextMessage)msg;
					try {
						String value=textMessage.getStringProperty("hello");
						session.commit();
					} catch (JMSException  e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
