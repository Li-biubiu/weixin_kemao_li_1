package org.weixin.weixin.service;

public interface TokenManager {
	/*
	 * 	返回有效的令牌，如果在本地缓存的令牌已经过去，那么直接自动调用远程返回获取有效的令牌才返回
	 * 
	 * 	@param account 传入公众号的微信号，在内部要根据微信号找到appid，根据appid才能获取对应的令牌。
	 */
	public String getToken(String account);
	
}
