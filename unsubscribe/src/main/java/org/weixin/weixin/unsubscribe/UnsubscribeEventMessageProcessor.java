package org.weixin.weixin.unsubscribe;

import java.util.Date;

import javax.transaction.Transactional;

import org.li.commons.domain.User;
import org.li.commons.domain.event.EventInMessage;
import org.li.commons.processors.EventMessageProcessor;
import org.li.commons.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("unsubscribeMessageProcessor")
public class UnsubscribeEventMessageProcessor implements EventMessageProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(UnsubscribeEventMessageProcessor.class);
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public void onMessage(EventInMessage msg) {
		if (!msg.getEvent().equals("unsubscribe")) {
			// 非取消关注事件，不处理
			return;
		}
		
		LOG.trace("处理取消关注的消息：" + msg);
		
		// 由于方法的上面有@Transactional注解，调用对象的set方法，会自动更新到数据库
		User user = this.userRepository.findByOpenId(msg.getFromUserName());
		if (user != null) {
			user.setStatus(User.Status.IS_UNSUBSCRIBE);
			user.setUnsubTime(new Date());
		}
		
	}
}
