package com.lsts.approve.util;

import javax.servlet.http.HttpServletRequest;

import com.khnt.utils.StringUtil;

public class IpUtil {
	public static String getIpAddr(HttpServletRequest request) throws Exception{
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtil.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtil.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
		// 多次反向代理后会有多个IP值，第一个为真实IP。
		int index = ip.indexOf(',');
		if (index != -1) {
			return ip.substring(0, index);
		} else {
			return ip;
		}
		} else {
			return request.getRemoteAddr();
		}
	}
}
