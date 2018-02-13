package com.gpf.study.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.junit.Test;

public class RPCTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
		new Thread(new Runnable() {
            public void run() {
                try {
                    ServerCenter serverCenter = new ServerCenterImpl(8088);
                    serverCenter.register(HelloService.class, HelloServiceImpl.class);
                    serverCenter.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        HelloService service = RPCClient.getRemoteProxyObj(HelloService.class, new InetSocketAddress("localhost", 8088));
        System.out.println(service.sayHi("test"));
	}

}
