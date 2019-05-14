package org.weixin.weixin.processors.Impl;

import org.li.commons.domain.event.EventInMessage;
import org.li.commons.processors.EventMessageProcessor;
import org.springframework.stereotype.Service;

@Service("unsubscribeMessageProcessor")
public class UnsubscribeEventMessageProcessor implements EventMessageProcessor {

	@Override
	public void onMessage(EventInMessage msg) {
		// 1.  把用户的数据删除，或者标记为已经取消关注即可
		
	}
	

}
