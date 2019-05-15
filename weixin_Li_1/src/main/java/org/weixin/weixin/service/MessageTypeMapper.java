package org.weixin.weixin.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.weixin.commons.domain.InMessage;
import org.weixin.commons.domain.event.EventInMessage;
import org.weixin.commons.domain.image.ImageInMessage;
import org.weixin.commons.domain.link.LinkInMessage;
import org.weixin.commons.domain.location.LocationInMessage;
import org.weixin.commons.domain.shortvideo.ShortVideoInMessage;
import org.weixin.commons.domain.text.TextInMessage;
import org.weixin.commons.domain.video.VideoInMessage;
import org.weixin.commons.domain.voice.VoiceInMessage;

public class MessageTypeMapper {
	
	private static Map<String , Class<? extends InMessage>> typeMap = new ConcurrentHashMap<>();
	
	// 通过一个Map记录了消息类型和类的关系
	static {
		typeMap.put("text", TextInMessage.class);
		typeMap.put("image", ImageInMessage.class);
		
		typeMap.put("video", VideoInMessage.class);
		typeMap.put("voice", VoiceInMessage.class);
		typeMap.put("shortvideo", ShortVideoInMessage.class);
		typeMap.put("location", LocationInMessage.class);
		typeMap.put("link", LinkInMessage.class);
		
		
		
		typeMap.put("event", EventInMessage.class);
	}
	
	// 通过消息类型获取对应的类
	@SuppressWarnings("unchecked")
	public static <T extends InMessage> Class<T> getClass(String type) {
		return (Class<T>) typeMap.get(type);
	}
	
}






















