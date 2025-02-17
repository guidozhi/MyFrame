package com.guido.rabbit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.BindingBuilder.DirectExchangeRoutingKeyConfigurer;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitDirectConfig {
	/**
	 * 有过期时间的队列 10秒
	 * 注意：修改queue的属性是不行的（声明同一名queue） 需要删除原来的queue,2019年10月21日，后面看有没有办法修改
	 * @return
	 */
	@Bean
	public Queue createApple() {
		Map<String,Object> args = new HashMap<String,Object>();
		args.put("x-message-ttl", 30000);
		return new Queue("apple", true, false, false,args);
	}

	@Bean
	public Queue createBanana() {
		Map<String,Object> args = new HashMap<String,Object>();
		args.put("x-dead-letter-exchange", "dlxExchange");
		args.put("x-message-ttl", 10000);
		args.put("x-dead-letter-routing-key", "dead-banana");
		return new Queue("banana",true,false,false,args);
	}

	@Bean
	public Queue createMango() {
		return new Queue("mango");
	}
	@Bean
	DirectExchange directExchange() {
		return new DirectExchange("fruit");
	}
	//死信队列
	@Bean
	public Queue createDeadQueue() {
		return new Queue("deadQueue");
	}
	/**
	 * 死信队列交换机
	 * @return
	 */
	@Bean
	DirectExchange dlx() {
		return new DirectExchange("dlxExchange",true,false);
	}
	/**
	 * 依赖本配置中的bean，方法中对象的名称需要与方法名称对应
	 * @param createApple
	 * @param directExchange
	 * @return
	 */
	@Bean
	DirectExchangeRoutingKeyConfigurer bindingExchangeA(Queue createApple, DirectExchange directExchange) {
		return BindingBuilder.bind(createApple).to(directExchange);
	}
	
	/*
	 * @Bean DirectExchangeRoutingKeyConfigurer bindingExchangeB(Queue createBanana,
	 * DirectExchange directExchange) { return
	 * BindingBuilder.bind(createBanana).to(directExchange); }
	 */
	@Bean 
	Binding bindingExchangeB(Queue createBanana,DirectExchange directExchange) { 
		return BindingBuilder.bind(createBanana).to(directExchange).with("banana"); 
	}
	
	@Bean
	Binding bindingExchangeC(Queue createMango, DirectExchange directExchange) {
		return BindingBuilder.bind(createMango).to(directExchange).with("bigMango");
	}
	/**
	 * 绑定死信队列
	 * @param createDeadQueue
	 * @param dlx
	 * @return
	 */
	@Bean
	Binding bindingDlxExchange(Queue createDeadQueue, DirectExchange dlx) {
		return BindingBuilder.bind(createDeadQueue).to(dlx).with("dead-banana");
	}

}
