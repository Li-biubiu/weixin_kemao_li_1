package org.weixin.weixin;


import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.weixin.commons.config.EventListenerConfig;
import org.weixin.commons.domain.event.EventInMessage;
import org.weixin.commons.processors.EventMessageProcessor;


@SpringBootApplication
@ComponentScan("org.weixin")
@EnableJpaRepositories("org.weixin")
@EntityScan("org.weixin")
													
public class SubscribeApplication implements  EventListenerConfig  , ApplicationContextAware
		// 表示命令行执行的抽象，要求实现一个run方法，在run方法里面启动一个线程等待停止通知
		// 当mvn spring-boot:stop命令执行以后，会发送一个停止的命令给spring容器。
		// Spring容器在收到命令以后，会执行停止，于是在停止之前会调用DisposbleBean里面的方法
		// 得到Spring的容器
	{
	private ApplicationContext application; // spring容器
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		application = applicationContext;
		
	}
	private static final Logger LOG = LoggerFactory.getLogger(SubscribeApplication.class);

	
	
	/*
	// 这是一个停止监听器，等待是否停止的通知
	private final Object stopMonitor = new Object();
	
	@Override
	public void run(String... args) throws Exception {
		new Thread(() -> {
			synchronized (stopMonitor) {
				try {
					// 等待停止通知
					stopMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	public void destroy() throws Exception {
		
		// 发送停止通知
		synchronized (stopMonitor) {
			stopMonitor.notify();
		}
	}
	*/

	/*
	// 相当于Spring的XML配置方式中的<bean>元素
	@Bean
	public RedisTemplate<String, InMessage> inMessageTemplate(//
			@Autowired RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, InMessage> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		// 设置一个序列化程序，就可以非常方便自动序列化！
		// Redis是键值对方式存储数据的，所以其实KeySerializer是把键序列化成可以传输的数据。
		// 由于泛型的时候已经确定，Key其实是String，所以可以使用系统默认的
//		template.setKeySerializer(new StringRedisSerializer());

		// 由于不确定是哪个类型，InMessage只是一个父类，它有许多不同的子类。
		// 因此扩展Jackson2JsonRedisSerializer变得极其重要：重写方法、不要构造参数
		template.setValueSerializer(new JsonRedisSerializer());
//		template.setDefaultSerializer(new JsonRedisSerializer());

		return template;
	}

	@Bean
	public MessageListenerAdapter messageListener(
			@Autowired RedisTemplate<String, InMessage> inMessageTemplate) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
		// 共用模板里面的序列化程序
		messageListenerAdapter.setSerializer(inMessageTemplate.getValueSerializer());///

		// 设置消息处理程序的代理对象
		messageListenerAdapter.setDelegate(this);
		// 设置代理对象里面哪个方法用于处理消息，设置方法名
		messageListenerAdapter.setDefaultListenerMethod("handle");
		return messageListenerAdapter;
	}

	@Bean
	public RedisMessageListenerContainer messageListenerContainer(//
			@Autowired RedisConnectionFactory redisConnectionFactory, //
			@Autowired MessageListener l) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);

		// 给容器增加监听器
//		MessageListener l = new MessageListener() {
//
//			@Override
//			public void onMessage(Message message, byte[] pattern) {
//
//			}
//		};

		// 可以监听多个通道的消息
		List<Topic> topics = new ArrayList<>();

		// 支持通配符，监听多个通道
//		topics.add(new PatternTopic("ljh_1_*"));
		// 监听具体某个通道
		topics.add(new ChannelTopic("ljh_1_event"));
		container.addMessageListener(l, topics);

		return container;
	}
	*/

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
		SpringApplication.run(SubscribeApplication.class, args);
//		System.out.println("Spring Boot应用启动成功");
//		//  让程序进入等待、不要退出
//		CountDownLatch countDownLatch = new CountDownLatch(1);
//		countDownLatch.await();
	}
}
