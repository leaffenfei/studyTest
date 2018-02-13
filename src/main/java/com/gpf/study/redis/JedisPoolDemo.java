package com.gpf.study.redis;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        // 构建连接池配置信息
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置最大连接数
        jedisPoolConfig.setMaxTotal(50);

        // 构建连接池
        //JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "10.1.1.172", 6379);

        // 从连接池中获取连接
        Jedis jedis = jedisPool.getResource();

        // 读取数据
        System.out.println(jedis.get("mytest"));

        // 将连接还回到连接池中
        jedisPool.returnResource(jedis);

        // 释放连接池
        jedisPool.close();
	}

}
