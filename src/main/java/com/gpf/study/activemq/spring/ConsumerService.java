package com.gpf.study.activemq.spring;

import javax.jms.Destination;

public interface ConsumerService {
	void receive(Destination queueDestination);
}
