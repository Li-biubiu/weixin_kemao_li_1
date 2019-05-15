package org.weixin.commons.processors;

import org.weixin.commons.domain.event.EventInMessage;

public interface EventMessageProcessor {

	public void onMessage(EventInMessage msg);
}
