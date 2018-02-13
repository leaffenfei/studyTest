package com.gpf.study.activemq;

import javax.jms.Destination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gpf.study.activemq.spring.ConsumerService;
import com.gpf.study.activemq.spring.ProducerService;
import com.gpf.study.activemq.spring.TopicProvider;

import junit.framework.TestCase;

/**
 * 测试Spring JMS
 * 
 * 1.测试生产者发送消息
 * 
 * 2. 测试消费者接受消息
 * 
 * 3. 测试消息监听
 * 
 * 4. 测试主题监听
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//ApplicationContext context = new
//ClassPathXmlApplicationContext("applicationContext.xml");
@ContextConfiguration("/spring-activemq.xml")
public class SpringJmsTest extends TestCase {
	/**
	 * 队列名queue1
	 */
	@Autowired
	private Destination queueDestination;

	/**
	 * 队列名queue2
	 */
	@Autowired
	private Destination queueDestination2;

	/**
	 * 主题 guo_topic
	 */
	@Autowired
	@Qualifier("topicDestination")
	private Destination topic;

	/**
	 * 主题消息发布者
	 */
	@Autowired
	private TopicProvider topicProvider;

	/**
	 * 队列消息生产者
	 */
	@Autowired
	@Qualifier("producerService")
	private ProducerService producer;

	/**
	 * 队列消息生产者
	 */
	@Autowired
	@Qualifier("consumerService")
	private ConsumerService consumer;

	/**
	 * 测试生产者向queue1发送消息
	 */
	@Test
	public void testProduce() {
		String msg = "Hello world!";
		producer.sendMessage(msg);
	}

	/**
	 * 测试消费者从queue1接受消息
	 */
	@Test
	public void testConsume() {
		consumer.receive(queueDestination);
	}

	/**
	 * 测试消息监听
	 * 
	 * 1.生产者向队列queue2发送消息
	 * 
	 * 2.ConsumerMessageListener监听队列，并消费消息
	 */
	@Test
	public void testSend() {
		producer.sendMessage(queueDestination2, "Hello China~~~~~~~~~~~~~~~");
	}

	/**
	 * 测试主题监听
	 * 
	 * 1.生产者向主题发布消息
	 * 
	 * 2.ConsumerMessageListener监听主题，并消费消息
	 */
	@Test
	public void testTopic() throws Exception {
		topicProvider.publish(topic, "Hello T-To-Top-Topi-Topic!");
	}
}
