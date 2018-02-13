package com.gpf.study.activemq.protogenesis;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher {
	  public static void main(String[] args) {
		  Publisher publisher=new Publisher();
		  String strMessage="这是发送的消息内容!gpf";
		  publisher.sendMessage(strMessage);
		  System.out.println("发送消息<"+strMessage+">");
	  }
	
	public void sendMessage(String strMessage){
		Connection connection;
		Session session;
		String user ="";//ActiveMQConnection.DEFAULT_USER;
		String pwd=ActiveMQConnection.DEFAULT_PASSWORD;
		String url=ActiveMQConnection.DEFAULT_BROKER_URL;
		String subject="demoSubPubGPF";
		//1初始化连接工厂
		ConnectionFactory connectionFactory=new ActiveMQConnectionFactory(user,pwd,url);
		try{
			//2.创建连接
			connection=connectionFactory.createConnection();
			connection.start();
			//3创建会话
			//true：支持事务,此时参数2无用。 	不支持事务时，
			//Session.AUTO_ACKNOWLEDGE自动确认
			//Session.CLIENT_ACKNOWLEDGE：为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会删除消息。
			//DUPS_OK_ACKNOWLEDGE：允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。在需要考虑资源使用时，这种模式非常有效
			session=connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			//4.创建要发布的主题  此处与queue有区别
			Destination destination=session.createTopic(subject);
			//5.创建消息发送者
			MessageProducer messageProducer =session.createProducer(destination);
			TextMessage textMessage=session.createTextMessage();
			textMessage.setStringProperty("hello", strMessage);
			//6发送消息
			messageProducer.send(textMessage);
			//7.提交
			session.commit();
			//8.关闭会话和连接
			session.close();
			connection.close();
			}
		catch(JMSException e){
			e.printStackTrace();
		}
		finally{

		}
		
	}
}
