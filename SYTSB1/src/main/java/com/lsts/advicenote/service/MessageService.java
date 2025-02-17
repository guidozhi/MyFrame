package com.lsts.advicenote.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.dao.UserDao;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.StringUtil;
import com.lsts.advicenote.bean.MessageContentCon;
import com.lsts.advicenote.bean.MessageContentMod;
import com.lsts.advicenote.bean.MessageHistory;
import com.lsts.advicenote.bean.MessageInfo;
import com.lsts.advicenote.dao.MessageContentConDao;
import com.lsts.advicenote.dao.MessageContentModDao;
import com.lsts.advicenote.dao.MessageDao;
import com.lsts.advicenote.dao.MessageHistoryDao;
import com.lsts.constant.Constant;
import com.lsts.log.service.SysLogService;

import net.sf.json.JSONObject;

/**
 * 短信
 * @ClassName MessageService
 * @JDK 1.7
 * @author GaoYa
 * @date 2014-03-24 下午03:43:00
 */
@Service("messageService")
public class MessageService extends
		EntityManageImpl<MessageInfo, MessageDao> {
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private MessageHistoryDao messageHistoryDao;
	@Autowired
	private SysLogService logService;
	
	@Autowired
	private MessageContentConDao messageContentConDao;
	@Autowired
	private MessageContentModDao messageContentModDao;
	@Autowired
	private UserDao userDao;
	
	
	public List  getMessage() throws Exception
    {	
		
		
	
		

		List list = messageDao.createSQLQuery("select  t2.report_name,t.report_com_name,t.advance_time,t1.security_tel,count(1) as cot from tzsb_inspection_info t ,base_device_document t1,base_reports t2   where t.flow_note_name ='报告领取'"
				+ " and t.fk_tsjc_device_document_id=t1.id and t.report_type=t2.id  and t.print_time is not null "
				+ " and t.data_status <> '99' and t1.device_sort_code like '3%' and not exists (select * from tzsb_report_draw t2 where t2.fk_inspection_info_id = t.id )"
				+ " group by t.report_com_name,t2.report_name,t.advance_time,t1.security_tel").list();
		
		
		
		
		return list;
    }
	
	
	
	
//	//报告打印过后发送短信
//	// 保存
	public void sendDrawMessage()
					throws Exception {
//
//				URL url = null;
//				String CorpID = "kh.scsei.org.cn";// 账户名
//				String Pwd = "2774ab4e730554c8a0b097d610fefe16";// 密码
//			
//
//				
//				//获取要发送短信数据
//				List list=getMessage();
//				
//				
//				if (list != null && list.size() > 0) {
//					
//					Object[] obj = list.toArray();
//					for (int i = 0; i < obj.length; i++) {
//						Object[] oo = (Object[]) obj[i];
//						String temp="贵单位于AAA在我院检验的BBB共CCC台(套)已出具,请到我院2楼报检大厅领取,地址:四川省成都市东风路北二巷4号，联系电话：028-86607888。【四川特检】";
//						
//						String dt=oo[2]==null?null:oo[2].toString();
//						String aa="";
//						if(dt.indexOf(" ")!=-1){
//							String ot=dt.split(" ")[0];
//							aa = temp.replace("AAA",ot);
//						}else{
//							aa = temp.replace("AAA",dt );
//						}
//						
//						String bb = aa.replace("BBB", oo[0]==null?null:oo[0].toString());
//						
//						
//						
//						String cc = bb.replace("CCC", oo[4]==null?null:oo[4].toString());
//						
//						String tel =oo[3]==null?null:oo[3].toString();
//					
//
//						
//						String send_content = URLEncoder.encode(cc
//								.replaceAll("<br/>", " "), "UTF-8");// 发送内容
//						
//
//						String str = "http://192.168.3.15/sms_service.php?action=send&apiIdentity="
//								+ CorpID + "&apiKey=" + Pwd + "&destNumbers="+tel+"&exNumber=30"+"&content=" + send_content;
//						
//						url = new URL(str.trim());
//						
//						BufferedReader in = null;
//						String inputLine = "";
//						String flag="";
//						
//						System.out.println("`````````````````````````````````" + url);
//						try {
//							// System.out.println("开始发送短信手机号码为 ："+Mobile);
//							in = new BufferedReader(new InputStreamReader(url.openStream()));
//							System.out.println("`````````````````````````````````" + in);
//							inputLine = in.readLine();
//							
//							JSONObject jo = JSONObject.fromObject(inputLine);
//							 flag = jo.getString("errorcode");
//							
//						} catch (Exception e) {
//							System.out.println("结束发送短信返回值:"+e.getMessage());
//							inputLine = "-9";
//						}
//						
//						// 0，发送成功；-10、用户认证失败；-11、ip或域名错误；-12、余额不足；-14、提交手机号超量；-15、短信内容含屏蔽关键字；-22、发送为空；
//						System.out.println("结束发送短信返回值:" + inputLine+flag);
//						
////					
//						//放入MAP 准备回写
//						MessageHistory story = new MessageHistory();
//						
//						story.setContent(cc);
//						story.setMobile(tel);
//						story.setSend_time(new Date());
//						story.setType("1");
//						story.setStatus(flag);
//						story.setCreate_date(new Date());
//						
//						saveHistroy(story);
//						
//						
//					}
//					
//				}
	}
	
	public void  saveHistroy(MessageHistory history) throws Exception
    {	
		
		messageHistoryDao.save(history);
		
		
		
    }
	
	/**
	 * 保存发送历史记录
	 * @param business_id --
	 *            业务id
	 * @param content --
	 *            发送内容
	 * @param destNumber --
	 *            发送目标手机号码
	 * @param flag --
	 * 			  发送状态（0，发送成功；-10、用户认证失败；-11、ip或域名错误；-12、余额不足；-14、提交手机号超量；-15、短信内容含屏蔽关键字；-22、发送为空；）
	 * @param message --
	 * 			  发送错误或成功提示
	 * @param msg_type --
	 * 			  文本消息类型（0：短信 1：微信）
	 * @author GaoYa
	 * @date 2015-12-18 16:12:00
	 * @throws Exception
	 */
	public void  saveMsgHistroy(String business_id, String content, String destNumber, String flag, String message, String msg_type) throws Exception{	
		MessageHistory story = new MessageHistory();
		try {
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			story.setUser_id(user != null ? user.getId() : "");
			story.setUser_name(user != null ? user.getName() : "系统");
		} catch (Exception e) {
			story.setUser_id("");
			story.setUser_name("系统");
		}
		story.setBusiness_id(business_id);
		story.setContent(content);
		story.setMobile(destNumber);
		story.setSend_time(new Date());
		story.setType("1");			// 消息类型（1：文本消息）
		story.setMsg_type(msg_type);// 文本消息类型（0：短信 1：微信）
		story.setStatus(flag);
		story.setMessage(message);
		story.setCreate_date(new Date());
		
		messageHistoryDao.save(story);
    }
	/**
	 * d定时保存发送历史记录
	 * @param business_id --
	 *            配置业务id
	 * @param business_ids --
	 *           业务id
	 * @param content --
	 *            发送内容
	 * @param destNumber --
	 *            发送目标手机号码
	 * @param flag --
	 * 			  发送状态（0，发送成功；-10、用户认证失败；-11、ip或域名错误；-12、余额不足；-14、提交手机号超量；-15、短信内容含屏蔽关键字；-22、发送为空；）
	 * @param message --
	 * 			  发送错误或成功提示
	 * @param msg_type --
	 * 			  文本消息类型（0：短信 1：微信）
	 * @author GaoYa
	 * @date 2015-12-18 16:12:00
	 * @throws Exception
	 */
	public void  saveMsgHistroy(String business_id,String business_ids, String content, String destNumber, String flag, String message, String msg_type,String corpID,String pwd) throws Exception{	
		MessageHistory story = new MessageHistory();
		try {
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			story.setUser_id(user != null ? user.getId() : "");
			story.setUser_name(user != null ? user.getName() : "系统");
		} catch (Exception e) {
			story.setUser_id("");
			story.setUser_name("系统");
		}
		//清除未发送的信息
		String hql="from MessageHistory where status='1' and business_id='"+business_id+"' and business_ids='"+business_ids+"' and mobile='"+destNumber+"'";
		List<MessageHistory> list=messageHistoryDao.createQuery(hql).list();
		for (int i = 0; i < list.size(); i++) {
			MessageHistory mess=list.get(i);
			messageHistoryDao.removeById(mess.getId());
		}
		story.setBusiness_id(business_id);
		story.setContent(content);
		story.setMobile(destNumber);
		story.setSend_time(new Date());
		story.setType("1");			// 消息类型（1：文本消息）
		story.setMsg_type(msg_type);// 文本消息类型（0：短信 1：微信）
		story.setStatus(flag);
		story.setMessage(message);
		story.setCreate_date(new Date());
		story.setBusiness_ids(business_ids);//定时任务 业务id
		story.setWx_corpid(corpID);
		story.setWx_pwd(pwd);
		messageHistoryDao.save(story);
    }
	/**
	 * 发送短信
	 * 
	 * @param business_id
	 *            -- 业务id
	 * @param content
	 *            -- 发送内容
	 * @param destNumber
	 *            -- 发送目标手机号码
	 * @return flag --
	 *         发送状态（0，发送成功；-10、用户认证失败；-11、ip或域名错误；-12、余额不足；-14、提交手机号超量；-15、
	 *         短信内容含屏蔽关键字；-22、发送为空；）
	 * @author GaoYa
	 * @date 2015-11-10 11:05:00
	 * @throws Exception
	 */
	public String sendMoMsg(HttpServletRequest request, String business_id, String content, String destNumber) {
		return null ;
//		URL url = null;
//		String CorpID = Constant.MESSAGE_CORPID;// 账户名
//		String Pwd = Constant.MESSAGE_PWD;// 密码
//		String flag = ""; // 返回错误代码，成功为0
//		String inputLine = "";
//		String message = ""; // 返回的错误或成功提示
//		try {
//			String send_content = URLEncoder.encode(content.replaceAll("<br/>", " "), "UTF-8");// 发送内容
//
//			String str = "http://192.168.3.15/sms_service.php?action=send&apiIdentity=" + CorpID + "&apiKey=" + Pwd
//					+ "&destNumbers=" + destNumber + "&exNumber=30" + "&content=" + send_content;
//
//			url = new URL(str.trim());
//			System.out.println("短信发送地址：" + url);
//			System.out.println("开始发送短信手机号码为 ：" + destNumber);
//
//			BufferedReader in = null;
//			in = new BufferedReader(new InputStreamReader(url.openStream()));
//			inputLine = in.readLine();
//
//			JSONObject jo = JSONObject.fromObject(inputLine);
//			flag = jo.getString("errorcode");
//			message = jo.getString("message");
//
//			// 0，发送成功；-10、用户认证失败；-11、ip或域名错误；-12、余额不足；-14、提交手机号超量；-15、短信内容含屏蔽关键字；-22、发送为空；
//			System.out.println("结束发送短信返回值:" + inputLine + flag);
//			System.out.println("结束发送短信返回值:" + message);
//
//			// 2、记录短信发送日志
//			saveMsgHistroy(business_id, content, destNumber, flag, message, "0");
//			try {
//				CurrentSessionUser user = SecurityUtil.getSecurityUser(); // 获取当前用户登录信息
//				// 3、写入系统日志
//				logService.setLogs(business_id, "发送短信", "发送短信到" + destNumber, user != null ? user.getId() : "",
//						user != null ? user.getName() : "系统", new Date(),
//						request != null ? request.getRemoteAddr() : "");
//			} catch (Exception e) {
//				logService.setLogs(business_id, "发送短信", "发送短信到" + destNumber, "", "系统", new Date(),
//						request != null ? request.getRemoteAddr() : "");
//			}
//
//		} catch (Exception e) {
//			System.out.println("结束发送短信返回值:" + e.getMessage());
//			try {
//				saveMsgHistroy(business_id, content, destNumber, flag, message, "0");
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//		return flag;
	}
	
	/**
	 * 发送微信
	 * 
	 * @param corpID
	 *            -- 应用账户名
	 * @param pwd
	 *            -- 密码
	 * @param business_id
	 *            -- 业务id
	 * @param content
	 *            -- 发送内容
	 * @param destNumber
	 *            -- 发送目标手机号码
	 * @return flag --
	 *         发送状态（0，发送成功；-10、用户认证失败；-11、ip或域名错误；-12、余额不足；-14、提交手机号超量；-15、
	 *         短信内容含屏蔽关键字；-22、发送为空；）
	 * @author GaoYa
	 * @date 2015-11-25 16:35:00
	 * @throws Exception
	 */
	public String sendWxMsg(HttpServletRequest request, String business_id, String corpID, String pwd, String content,
			String destNumber) {
				return null;
//		URL url = null;
//		if (StringUtil.isEmpty(corpID)) {
//			corpID = Constant.INSPECTION_CORPID; // 微信企业号应用-检验平台-账户名
//		}
//		if (StringUtil.isEmpty(pwd)) {
//			pwd = Constant.INSPECTION_PWD; // 微信企业号应用-检验平台-密码
//		}
//		String flag = "";
//		String inputLine = "";
//		String message = ""; // 返回的错误或成功提示
//		
//		try {
//			//saveMsgHistroy(business_id, content, destNumber, flag, message, "1");
//			String send_content = URLEncoder.encode(content.replaceAll("<br/>", " "), "UTF-8");// 发送内容
//			//System.out.println("-------------截取到的字符串：" + corpID.substring(0, corpID.indexOf(".")) + "------------");
//			String str = "http://192.168.3.15/sms_service.php?action=send&apiIdentity=" + corpID + "&apiKey=" + pwd
//					+ "&gateways=wechat-" + corpID.substring(0, corpID.indexOf(".")) + "&msgType=text&destNumbers="
//					+ destNumber + "&content=" + send_content;
//			url = new URL(str.trim());
//			System.out.println("微信发送地址：" + url);
//			System.out.println("开始发送微信手机号码为 ：" + destNumber);
//
//			BufferedReader in = null;
//			in = new BufferedReader(new InputStreamReader(url.openStream()));
//			inputLine = in.readLine();
//
//			JSONObject jo = JSONObject.fromObject(inputLine);
//			flag = jo.getString("errorcode");
//			message = jo.getString("message");
//
//			// 0，发送成功；-10、用户认证失败；-11、ip或域名错误；-12、余额不足；-14、提交手机号超量；-15、短信内容含屏蔽关键字；-22、发送为空；
//			System.out.println("结束发送微信返回值:" + inputLine + flag);
//			System.out.println("结束发送微信返回值:" + message);
//
//			// 2、记录微信发送日志
//			saveMsgHistroy(business_id, content, destNumber, flag, message, "1");
//			try {
//				CurrentSessionUser user = SecurityUtil.getSecurityUser(); // 获取当前用户登录信息
//				// 3、写入系统日志
//				logService.setLogs(business_id, "发送微信", "发送微信到" + destNumber, user != null ? user.getId() : "",
//						user != null ? user.getName() : "系统", new Date(),
//						request != null ? request.getRemoteAddr() : "");
//			} catch (Exception e) {
//				logService.setLogs(business_id, "发送微信", "发送微信到" + destNumber, "", "系统", new Date(),
//						request != null ? request.getRemoteAddr() : "");
//			}
//		} catch (Exception e) {
//			System.out.println("结束发送微信返回值:" + e.getMessage());
//			try {
//				saveMsgHistroy(business_id, content, destNumber, flag, message, "1");
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//		return flag;
	}
	
	/**
	 * 
	 * author pingZhou
	 * @param request
	 * @param business_id 业务id
	 * @param destNumber 电话号码
	 * @param content 默认发送内容 可为null
	 * @param code 配置消息功能模块code
	 * @param userId 人员id（没有取手机号时可以直接传用户id）
	 * @param params 模块配置的参数传入值
	 * @param pageParams 通用审核页面传参 不用时可为null
	 * @param sendType 发送方式（业务确定了发送方式时使用  0 短信 1 微信 2短信和微信）
	 * @param corpID 公众号模块id
	 * @param pwd 公众号模块密码
	 * @return
	 * @throws Exception
	 */
	public String sendMassageByConfig(HttpServletRequest request, String business_id,String destNumber,String content,
			String code,String userId, HashMap<String, Object> params
			,HashMap<String, Object> pageParams,String sendType,String corpID, String pwd) throws Exception {
	
		if (StringUtil.isEmpty(corpID)) {
			corpID = Constant.PEOPLE_CORPID; // 微信企业号应用-检验平台-账户名
		}
		if (StringUtil.isEmpty(pwd)) {
			pwd = Constant.PEOPLE_PWD; // 微信企业号应用-检验平台-密码
		}
		
		String flag = "";
		String flag1 = "1";
		String flag2 = "";
		String sendContent = "";
		String business_ids=business_id;//保存业务id
		MessageContentMod module = messageContentModDao.getByCode(code);
		if(module==null) {
			//没有模板配置
			if(content==null&&StringUtil.isEmpty(content)) {
				return "没有消息配置信息！";
			}else {
				sendContent = content; 
			}
			
		}else {
			MessageContentCon config = messageContentConDao.getByModuleId(module.getId());
			
			if(config==null) {
				//没有消息内容配置
				if(content==null&&StringUtil.isEmpty(content)) {
					return "没有消息配置信息！";
				}else {
					sendContent = content; 
				}
			}else {
				if("1".equals(config.getData_status())) {
					return "消息内容配置已禁用！";
				}else {
					business_id=config.getId();
					String contentConfig = config.getContent().trim();
					//contentConfig = contentConfig.replace(" ", "");
					for(String key:params.keySet()) {
						if(params.get(key)!=null&&StringUtil.isNotEmpty(params.get(key).toString())) {
							contentConfig = contentConfig.replace("${"+key+"}", params.get(key).toString());
						}
						
					}
					sendContent = contentConfig;
					if(config.getSend_time().equals("1")){//获取发送类型
						flag1="1";//及时
					}else if(config.getSend_time().equals("2")){//获取发送类型
						flag1="2";//延时
					}else if(config.getSend_time().equals("3")){//获取发送类型
						flag1="3";//定时
					}
					
					if(sendType==null||StringUtil.isEmpty(sendType)) {
						
						
						if(config.getSend_type().equals("0")){//获取发送消息类型
							flag2="0";//短信
						}else if(config.getSend_type().equals("1")){//获取发送消息类型
							flag2="1";//微信
						}else if(config.getSend_type().equals("0,1")||config.getSend_type().equals("1,0")){//获取发送消息类型
							flag2="2";//短信 微信
						}
					}else{
						
						if("0".equals(sendType)){//获取发送消息类型
							if(config.getSend_type().contains("0")||config.getSend_type().equals("0,1")||config.getSend_type().equals("1,0")) {
								flag2="0";//短信
							}else {
								logService.setLogs(business_id, "发送短信", "发送短信到" + destNumber, "", "系统", new Date(),
										request != null ? request.getRemoteAddr() : "");
							}
						}else if("1".equals(sendType)&&config.getSend_type().contains("1")){//获取发送消息类型
							if(config.getSend_type().contains("1")||config.getSend_type().equals("0,1")||config.getSend_type().equals("1,0")) {
								flag2="1";//微信
							}else {
								logService.setLogs(business_id, "发送微信", "发送微信到" + destNumber, "", "系统", new Date(),
										request != null ? request.getRemoteAddr() : "");
							}
						}else if("2".equals(sendType)){//获取发送消息类型
							if(config.getSend_type().equals("0,1")||config.getSend_type().equals("1,0")){//获取发送消息类型
								
								flag2="2";//短信 微信
							}else {
								logService.setLogs(business_id, "发送微信", "发送微信到" + destNumber, "", "系统", new Date(),
										request != null ? request.getRemoteAddr() : "");
								logService.setLogs(business_id, "发送短信", "发送短信到" + destNumber, "", "系统", new Date(),
										request != null ? request.getRemoteAddr() : "");
							}
						}
						
					}
				}
				
			}
			
		}
		
		if(destNumber==null&&userId!=null&&StringUtil.isNotEmpty(userId)) {
			String[] userIds = userId.split(",");
			for (int i = 0; i < userIds.length; i++) {
				User user = userDao.get(userIds[i]);
					Employee employee = user.getEmployee();
					// 获取发送目标号码
					destNumber = employee.getMobileTel();
					//System.out.println("---发送对象为:"+destNumber+"---消息内容为:"+sendContent+"----------");
					
					String wxContent = sendContent;
					if(pageParams!=null&&pageParams.get("url")!=null) {
						wxContent = "<a href='"+pageParams.get("url").toString()+"' >"+sendContent+"</a>";
					}
					
					if(flag1.equals("1")){//及时
						if(flag2.equals("0")){//短信
							sendMoMsg(request, business_id, sendContent, destNumber);
						}
						if(flag2.equals("1")){//维信
							sendWxMsg(request, business_id,corpID,pwd, wxContent, destNumber);
						}
						if(flag2.equals("2")){//短信 微信
							sendWxMsg(request, business_id, corpID, pwd, wxContent, destNumber);
							sendMoMsg(request, business_id, sendContent, destNumber);
						}
					}
					// 2、记录发送日志
					
					if(flag1.equals("2")||flag1.equals("3")){//延时和定时
						if(flag2.equals("0")){//短信
							saveMsgHistroy(business_id,business_ids, content, destNumber, "1", sendContent, "0","","");
						}
						if(flag2.equals("1")){//维信
							saveMsgHistroy(business_id,business_ids, content, destNumber, "1", wxContent, "1", corpID, pwd);
						}
						if(flag2.equals("2")){//短信 微信
							saveMsgHistroy(business_id,business_ids, content, destNumber, "1", sendContent, "0","","");
							saveMsgHistroy(business_id,business_ids, content, destNumber, "1", wxContent, "1", corpID, pwd);
						}
					}
			}
		}else {
			String wxContent = sendContent;
			if(pageParams!=null&&pageParams.get("url")!=null) {
				wxContent = "<a href='"+pageParams.get("url").toString()+"' >"+sendContent+"</a>";
			}
			
			//System.out.println("---发送对象为:"+destNumber+"------消息内容为:"+sendContent+"----------");
			if(flag1.equals("1")){//及时
				if(flag2.equals("0")){//短信
					sendMoMsg(request, business_id, sendContent, destNumber);
				}
				if(flag2.equals("1")){//维信
					sendWxMsg(request, business_id,corpID, pwd, wxContent, destNumber);
				}
				if(flag2.equals("2")){//短信 微信
					sendWxMsg(request, business_id, corpID, pwd, wxContent, destNumber);
					sendMoMsg(request, business_id, sendContent, destNumber);
				}
			}
			// 2、记录发送日志
			
			if(flag1.equals("2")||flag1.equals("3")){//延时和定时
				if(flag2.equals("0")){//短信
					saveMsgHistroy(business_id,business_ids, content, destNumber, "1", sendContent, "0","","");
				}
				if(flag2.equals("1")){//维信
					saveMsgHistroy(business_id,business_ids, content, destNumber, "1", wxContent, "1", corpID, pwd);
				}
				if(flag2.equals("2")){//短信 微信
					saveMsgHistroy(business_id,business_ids, content, destNumber, "1", sendContent, "0","","");
					saveMsgHistroy(business_id,business_ids, content, destNumber, "1", wxContent, "1", corpID, pwd);
				}
			}
		}
		
		
		return flag;
	}
	
	
	public static void main(String[] args) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("applyOp", "周定萍");
		
		map.put("applyTime", "");
		//sendMassageByConfig("","","","消息测试！","overWork",map,"");
	}
	//定时任务发送消息
	public void sendMassageByTiming() throws Exception {
		SimpleDateFormat sim=new SimpleDateFormat("HH:mm:ss");
		//查询出符合条件配置
		//每天定时
		String hql="from MessageContentCon where SEND_TIME='3' and ((to_date('"+sim.format(new Date())+"','hh24:mi:ss')- to_date(to_char(PREVIEW_TIME,'hh24:mi:ss'),'hh24:mi:ss')) * 24 * 60 * 60) >=0  "
				+ " and ((to_date('"+sim.format(new Date())+"','hh24:mi:ss')- to_date(to_char(PREVIEW_TIME,'hh24:mi:ss'),'hh24:mi:ss')) * 24 * 60 * 60) <90 "
				+ " and DATA_STATUS='0' and preview_type='1' ";
		List<MessageContentCon> list= messageContentConDao.createQuery(hql).list();
	    //查询日志符合条件信息
		for (int i = 0; i < list.size(); i++) {
			String hql1="from MessageHistory where BUSINESS_ID='"+list.get(i).getId()+"' and status='1'";
			List<MessageHistory> list1= messageHistoryDao.createQuery(hql1).list();
			for (int j = 0; j < list1.size(); j++) {
				MessageHistory his=list1.get(j);
				String  sendContent=his.getMessage();
				//发送消息
					if(list1.get(j).getMsg_type().equals("0")){//短信
						sendMoMsg(null, list.get(i).getId(), sendContent, list1.get(j).getMobile());
					}
					if(list1.get(j).getMsg_type().equals("1")){//维信
						sendWxMsg(null, list.get(i).getId(), his.getWx_corpid(),his.getWx_pwd() , sendContent, list1.get(j).getMobile());
					}
					if(list1.get(j).getMsg_type().equals("2")){//维信
						sendMoMsg(null, list.get(i).getId(), sendContent, list1.get(j).getMobile());
						sendWxMsg(null, list.get(i).getId(), his.getWx_corpid(),his.getWx_pwd(), sendContent, list1.get(j).getMobile());
					}
					//his.setStatus("2");//记录已发送定时任务
					messageHistoryDao.save(his);
				
			}
		}
	}
	
	//延时任务发送消息
		public void sendMassageByDelayed() throws Exception {
			SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//查询出符合条件配置
			//每天定时
			String hql="from MessageContentCon where SEND_TIME='2' and PREVIEW_TIME=to_date("+sim.format(new Date())+",'yyyy-mm-dd hh24:mi:ss') and DATA_STATUS='0' ";
			List<MessageContentCon> list= messageContentConDao.createQuery(hql).list();
		    //查询日志符合条件信息
			for (int i = 0; i < list.size(); i++) {
				String hql1="from MessageHistory where BUSINESS_ID='"+list.get(i).getId()+"' and status='1'";
				List<MessageHistory> list1= messageHistoryDao.createQuery(hql1).list();
				for (int j = 0; j < list1.size(); j++) {
					MessageHistory his=list1.get(j);
					String  sendContent=list1.get(j).getContent();
					//发送消息
						if(list1.get(j).getMsg_type().equals("0")){//短信
							sendMoMsg(null, list.get(i).getId(), sendContent, list1.get(j).getMobile());
						}
						if(list1.get(j).getMsg_type().equals("1")){//维信
							sendWxMsg(null, list.get(i).getId(), Constant.INSPECTION_CORPID, Constant.INSPECTION_PWD, sendContent, list1.get(j).getMobile());
						}
						if(list1.get(j).getMsg_type().equals("2")){//维信
							sendMoMsg(null, list.get(i).getId(), sendContent, list1.get(j).getMobile());
							sendWxMsg(null, list.get(i).getId(), Constant.INSPECTION_CORPID, Constant.INSPECTION_PWD, sendContent, list1.get(j).getMobile());
						}
						his.setStatus("2");//记录已发送延时任务
						messageHistoryDao.save(his);
					
				}
			}
		}
}
