package com.gpf.study.rpc;

import java.io.IOException;

/**
 * 服务中心
 * @author GPF
 *
 */
public interface ServerCenter {
	public void stop();
	 
    public void start() throws IOException;
 
    public void register(Class serviceInterface, Class impl);
 
    public boolean isRunning();
 
    public int getPort();
}
