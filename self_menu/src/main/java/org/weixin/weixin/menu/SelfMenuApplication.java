package org.weixin.weixin.menu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.weixin.commons.config.EventListenerConfig;
import org.weixin.commons.domain.event.EventInMessage;

@SpringBootApplication 
@EntityScan("org.weixin")
@ComponentScan("org.weixin")
public class SelfMenuApplication implements EventListenerConfig {
	public static void main(String[] args) {
		SpringApplication.run(SelfMenuApplication.class, args);
	}

	@Override
	public void handle(EventInMessage msg) {
		// 目前此模块不处理任何消息，所以这里留空即可
		
	}
}
