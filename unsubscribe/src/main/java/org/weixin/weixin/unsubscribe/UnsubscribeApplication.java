package org.weixin.weixin.unsubscribe;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.li.commons.config.EventListenerConfig;
import org.li.commons.domain.event.EventInMessage;
import org.li.commons.processors.EventMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
 

@SpringBootApplication
@ComponentScan("org.weixin")
@EnableJpaRepositories("org.weixin")
@EntityScan("org.weixin")
													// 实现启动       实现停止        实现接口
public class UnsubscribeApplication implements EventListenerConfig  , ApplicationContextAware
		// 表示命令行执行的抽象，要求实现一个run方法，在run方法里面启动一个线程等待停止通知
		// 当mvn spring-boot:stop命令执行以后，会发送一个停止的命令给spring容器。
		// Spring容器在收到命令以后，会执行停止，于是在停止之前会调用DisposbleBean里面的方法
		// 得到Spring的容器
	{
	private ApplicationContext application; // spring容器
	private static final Logger LOG = LoggerFactory.getLogger(UnsubscribeApplication.class);
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		application = applicationContext;
	}


	@Override
	public void handle(EventInMessage msg) {
		// 1.  当前类实现ApplicationContextAware接口，用于获取Spring容器
		// 2.  把Event全部转换为小写，并且拼接上MessageProcessor作为ID
		String id = msg.getEvent().toLowerCase() + "MessageProcessor";
		// 3.  使用ID到Spring容器获取一个Bean
		try {
			EventMessageProcessor eventMessageProcessor = (EventMessageProcessor) application.getBean(id);
			// 4.  强制类型转换以后，调用onMessage方法
			if ( eventMessageProcessor != null) {
				eventMessageProcessor.onMessage(msg);
			} else {
				LOG.warn("Bean的ID {} 无法调用对应的消息处理器: {} 对应的Bean不存在", id, id);
			}
		} catch (Exception e) {
			LOG.warn("Bean的ID {} 无法调用对应的消息处理器: {}" , id , e.getMessage());
//			LOG.trace(e.getMessage(), e);
		}
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(UnsubscribeApplication.class, args);
//		System.out.println("Spring Boot应用启动成功");
//		//  让程序进入等待、不要退出
//		CountDownLatch countDownLatch = new CountDownLatch(1);
//		countDownLatch.await();
	}
}
