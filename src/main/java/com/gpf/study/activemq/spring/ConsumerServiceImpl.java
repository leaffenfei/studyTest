package com.gpf.study.activemq.spring;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public class ConsumerServiceImpl implements ConsumerService {
	@Autowired
	private JmsTemplate jmsTemplate;

	public void receive(Destination destination) {
		// TODO Auto-generated method stub
		TextMessage tm = (TextMessage) jmsTemplate.receive(destination);
		try {
			System.out.println("从队列" + destination.toString() + "收到了消息：\t"
					+ tm.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

}
