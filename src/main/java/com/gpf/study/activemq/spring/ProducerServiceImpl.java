package com.gpf.study.activemq.spring;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ProducerServiceImpl implements ProducerService {
	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * 向指定队列发送消息
	 */
	public void sendMessage(Destination destination, final String msg) {
		// TODO Auto-generated method stub
		System.out.println("ProducerService向队列<" + destination.toString() + ">发送了消息：\t" + msg);
		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
	}

	/**
	 * 向默认队列发送消息
	 */
	public void sendMessage(final String msg) {
		// TODO Auto-generated method stub
		String destination = jmsTemplate.getDefaultDestination().toString();
		System.out.println("ProducerService向队列" + destination + "发送了消息：\t" + msg);
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});

	}
	public void sendMessage(Destination destination, final String msg, final Destination response) {
		// TODO Auto-generated method stub
		System.out.println("ProducerService向队列" + destination + "发送了消息：\t" + msg);
		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(msg);
				textMessage.setJMSReplyTo(response);
				return textMessage;
			}
		});
	}
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

}
