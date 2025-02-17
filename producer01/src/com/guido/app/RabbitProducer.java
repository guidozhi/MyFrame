package com.guido.app;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class RabbitProducer {
	private static final String IP_ADDRESS="192.168.70.129";
	private static final int PORT=5672;
	
	public static void main(String[] args) throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(IP_ADDRESS);
		factory.setPort(PORT);
		factory.setUsername("admin");
		factory.setPassword("guido1222");
		
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		
		
		//创建一个type='direct' 持久化的，非自动删除的交换器
		channel.exchangeDeclare("dead", "direct",true,false,null);
		//创建一个持久化，非排他的、非自动删除的队列
		channel.queueDeclare("dead",true,false,false,null);
		//将交换器与队列通过路由键绑定
		channel.queueBind("dead", "dead", "dead");
				
		
		//创建一个type='direct' 持久化的，非自动删除的交换器
		channel.exchangeDeclare("fluit", "direct",true,false,null);
		Map<String,Object> argss = new HashMap<>();
		argss.put("x-dead-letter-exchange","dead");
		argss.put("x-dead-letter-routing-key","dead");
		argss.put("x-message-ttl",10000);
		//创建一个持久化，非排他的、非自动删除的队列
		channel.queueDeclare("banana",true,false,false,argss);
		
		//将交换器与队列通过路由键绑定
		channel.queueBind("banana", "fluit", "hainan-banana");
		
		//发送一条持久化的消息
		String message ="hello world !";
		for(int i=0;i<100;i++) {
			message = "test hello world "+i;
			channel.basicPublish("fluit", "hainan-banana", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}
		channel.close();
		connection.close();
	}

}
