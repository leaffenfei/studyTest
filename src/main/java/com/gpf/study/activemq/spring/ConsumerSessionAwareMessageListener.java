package com.gpf.study.activemq.spring;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.listener.SessionAwareMessageListener;

/**
 * SessionAwareMessageListener提供了一个快捷的方法，方便我们在接受消息以后，发送一条回复消息
 * 
 * onMessage()有2个参数，一个是收到的消息，另一个Session可以用于发送回复消息。
 */
public class ConsumerSessionAwareMessageListener
    implements
      SessionAwareMessageListener<TextMessage> {

  private Destination destination;

  public void onMessage(TextMessage message, Session session) throws JMSException {
    // 接受消息
    System.out.println("SessionAwareMessageListener收到一条消息：\t" + message.getText());

    // 发送消息
    MessageProducer producer = session.createProducer(destination);
    TextMessage tm = session.createTextMessage("I am ConsumerSessionAwareMessageListener");
    producer.send(tm);

  }

  public void setDestination(Destination destination) {
    this.destination = destination;
  }

}
