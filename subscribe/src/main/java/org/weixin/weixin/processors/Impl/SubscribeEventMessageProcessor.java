package org.weixin.weixin.processors.Impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weixin.commons.domain.User;
import org.weixin.commons.domain.event.EventInMessage;
import org.weixin.commons.processors.EventMessageProcessor;
import org.weixin.commons.repository.UserRepository;
import org.weixin.commons.service.WeixinProxy;

@Service("subscribeMessageProcessor")
public class SubscribeEventMessageProcessor implements EventMessageProcessor {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private WeixinProxy weixinProxy;
	
	@Override
	public void onMessage(EventInMessage msg) {
		if (!msg.getEvent().equals("subscribe")) {
			// 非关注事件，不处理
			return;
		}
		
		//发送操作的用户OpenId
		String openId = msg.getFromUserName(); 
		
		// 1.  检查用户是否已经关注
		User user = this.userRepository.findByOpenId(openId);
		
		// 2.  如果用户还未关注，则调用远程接口获取用户信息 
		if (user == null || user.getStatus() != User.Status.IS_SUBSCRIBE) {
			
			// 3. 调用远程接口 
			// TODO 根据ToUserName找到对应的微信号
			String account = "" ; 
			
			User weixinUser = weixinProxy.getUser(account , openId);
			
			if (weixinUser == null) {
				return;
			}
			
			if (user != null) {
				user.setStatus(User.Status.IS_SUBSCRIBE);
				user.setUnsubTime(new Date());
			}
			
			// 4.  存储到数据库
			if (user != null) {
				// 原来关注过
				weixinUser.setId(user.getId());
				weixinUser.setSubTime(user.getSubTime());
				weixinUser.setUnsubTime(null);
			}
			weixinUser.setStatus(User.Status.IS_SUBSCRIBE);
			
			// 如果有id的值，会自动update；没有id的值会insert
			this.userRepository.save(weixinUser);
			
			// 通过客服接口，发生一条信息给用户
			weixinProxy.sendText(account , openId , "欢迎关注我的公众号，回复帮助可获得人工智能菜单");
			
		}
	}
}
