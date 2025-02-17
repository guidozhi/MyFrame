package com.lsts.advicenote.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.rbac.impl.bean.User;
import com.khnt.utils.StringUtil;
import com.khnt.weixin.enums.EnumMethod;
import com.khnt.weixin.interceptor.OAuthRequired;
import com.khnt.weixin.pojo.AccessToken;
import com.khnt.weixin.util.Constants;
import com.khnt.weixin.util.HttpRequestUtil;
import com.khnt.weixin.util.Result;
import com.khnt.weixin.util.WxUtil;
import com.lsts.advicenote.bean.MsgLinkAudit;
import com.lsts.advicenote.service.MsgLinkAuditService;
import com.scts.weixin.app.service.WeixinAppInfoManager;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("msgLinkAuditAction")
public class MsgLinkAuditAction extends SpringSupportAction<MsgLinkAudit, MsgLinkAuditService> {

	@Autowired
	private MsgLinkAuditService msgLinkAuditService;
	@Autowired
    private WeixinAppInfoManager  weixinAppInfoManager;
	
	
	/**
	 * 加载个人信息，此处添加了@OAuthRequired注解
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "weixinUserInfo")
	@OAuthRequired
	public String weixinUserInfo(HttpServletRequest request,Model model){
		System.out.println("Load a User!");
        System.out.println("getRequestURL = " + request.getSession().getId());
        System.out.println("getRequestURL = " + request.getRequestURL());
        System.out.println("getRequestURI = " + request.getRequestURI());
        System.out.println("code = " + request.getParameter("code"));
        System.out.println("businessId = " + request.getParameter("id"));
        HttpSession session = request.getSession();
        log.debug(this.getClass().getName() + " method weixinUserInfo Load a User start");
        if(session.getAttribute("Userid")==null || ((String)session.getAttribute("Userid")).equals("noUserId")) {
        	String token = WxUtil.getAccessTokenString();
        	log.debug(this.getClass().getName() + " method weixinUserInfo 获取到code："+request.getParameter("code"));
        	log.debug(this.getClass().getName() + " method weixinUserInfo 获取到token："+token);
            if (StringUtil.isNotEmpty(token) && request.getParameter("code")!=null) {
            	//System.out.println("-----------------------------in--------------");
                Result<String> result = WxUtil.oAuth2GetUserByCode(token, request.getParameter("code"), 7);
                System.out.println("result=" + result);
                String menuUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+token+"&userid="+result.getObj() ;
                log.debug(this.getClass().getName() + " method weixinUserInfo menuUrl："+menuUrl);
                JSONObject jo = HttpRequestUtil.httpRequest(menuUrl, EnumMethod.GET.name(), null);
                if(jo!=null){
                	if(jo.has("mobile")) {
						User user;
						try {
							user = weixinAppInfoManager.getUser(jo.getString("mobile"));
							if(user!=null) {
								session.setAttribute("Name", user.getName());
								session.setAttribute("Phone", jo.getString("mobile"));
								session.setAttribute("Account",user.getAccount());
								log.debug(this.getClass().getName() + " method weixinUserInfo 获取到用户信息，用户姓名："+user.getName()+"，用户手机号：" + jo.getString("mobile")+"，用户Account:"+user.getAccount());
							}else {
								session.setAttribute("error_msg", "亲，检验平台中未找到与该手机号匹配的用户信息");
								log.debug("亲，检验平台中未找到与该手机号匹配的用户信息");
								return "app/weixininfo/app_info/weixin_error_page";
							}
						} catch (Exception e) {
							log.debug(e.getMessage());
							return "app/weixininfo/app_info/weixin_error_page";
						}
					}else {
						session.setAttribute("error_msg", "亲，微信接口服务出现异常，未找到手机号");
						log.debug("亲，微信接口服务出现异常，未找到手机号");
						return "app/weixininfo/app_info/weixin_error_page";
					}
                }else {
					session.setAttribute("error_msg", "亲，微信接口服务出现异常，未获取到微信企业用户信息");
					log.debug("亲，微信接口服务出现异常，未获取到微信企业用户信息");
					return "app/weixininfo/app_info/weixin_error_page";
				}
                session.setAttribute("Userid", result.getObj());
                //session.setAttribute("AccessToken", accessToken.getToken());
                
            } else {
            	//System.out.println("-----------------------------out--------------");
            	session.setAttribute("Userid", "noUserId");
            	session.setAttribute("Name", "noUserName");
            	session.setAttribute("Phone", "noUserPhone");
            }
        }
    /*  session.setAttribute("Userid", "402884c4477c9bac01477fe0d188001b");
        session.setAttribute("Account", wxLeaveManager.getAccount("1890818454"));
    	session.setAttribute("Name", "高雅");
    	session.setAttribute("Phone", "1890818454");*/
        
      /*  session.setAttribute("Userid", "402884c447802e51014780553e3b0012");
        session.setAttribute("Account", wxLeaveManager.getAccount("13548199448"));
    	session.setAttribute("Name", "黄坚");
    	session.setAttribute("Phone", "13548199448");*/
        
        
     /*  session.setAttribute("Userid", "402884c4477c9bac01477fe0ae7b001a");
        session.setAttribute("Account", wxLeaveManager.getAccount("1355184410"));
    	session.setAttribute("Name", "张展彬");
    	session.setAttribute("Phone", "1355184410");*/
        
    /*	HashMap<String,Object> map = msgLinkAuditService.getAuditDetail(request.getParameter("id"));

    	model.addAttribute("data", map.get("data"));
    	model.addAttribute("datas", map.get("datas"));
    	*/
    	session.setAttribute("url", "msgLinkAuditAction/t.do?id="+request.getParameter("id"));
        session.setAttribute("businessId", request.getParameter("id"));
		model.addAttribute("Userid", session.getAttribute("Userid"));
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!"+session.getAttribute("Userid"));
		//return "app/weixin/qxj/transfer_page";
		return "app/message/weixin_transfer_page";
	}
	
	/**
	 * 标记待审核信息已经审核
	 * author pingZhou
	 * @param id
	 * @return
	 */
	@RequestMapping("setAuditStatus")
	@ResponseBody
	public HashMap<String,Object> setAuditStatus(String id){
		HashMap<String,Object> map = new HashMap<String, Object>();
		
		try {
			msgLinkAuditService.setAuditStatus(id);
			map.put("success", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "标记已处理失败！");
		}
		
		return map;
	}
	
	
	@RequestMapping("t")
	public ModelAndView testGetAudit(String id){
		HashMap<String,Object> map = new HashMap<String, Object>();
		
		try {
			map = msgLinkAuditService.getAuditDetail(id);
			map.put("success", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "查询失败！");
		}
		
		return new ModelAndView("app/message/common_audit_detail",map);
	}
	
	/**
	 * 根据sql查询人员
	 * author pingZhou
	 * @param sql
	 * @return
	 */
	@RequestMapping("getSqltabl")
	@ResponseBody
	public HashMap<String,Object>  getSqltabl(String sql) {
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		
		try {
			map = msgLinkAuditService.getSqltabl(sql);
			map.put("success", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "查询失败！");
		}
		
		return map;
	}
	
}
