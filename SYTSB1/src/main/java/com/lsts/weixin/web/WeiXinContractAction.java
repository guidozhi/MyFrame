package com.lsts.weixin.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.weixin.enums.EnumMethod;
import com.khnt.weixin.interceptor.OAuthRequired;
import com.khnt.weixin.pojo.AccessToken;
import com.khnt.weixin.util.Constants;
import com.khnt.weixin.util.HttpRequestUtil;
import com.khnt.weixin.util.Result;
import com.khnt.weixin.util.WxUtil;
import com.lsts.employee.service.EmployeesService;
import com.lsts.humanresources.service.WxLeaveManager;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("weiXinContract")
public class WeiXinContractAction {

	@Autowired
	private EmployeesService employeesService;
	@Autowired
	private WxLeaveManager wxLeaveManager;
	/**
	 * 加载个人信息，此处添加了@OAuthRequired注解
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "weiXinContractQuery")
	@OAuthRequired
	public String weiXinContractQuery(HttpServletRequest request,Model model){
		System.out.println("Load a User!");
        System.out.println("getRequestURL = " + request.getSession().getId());
        System.out.println("getRequestURL = " + request.getRequestURL());
        System.out.println("getRequestURI = " + request.getRequestURI());
        System.out.println("code = " + request.getParameter("code"));
        HttpSession session = request.getSession();
        //System.out.println("1111111111111111111111111111111111111");
        if(session.getAttribute("Userid")==null || ((String)session.getAttribute("Userid")).equals("noUserId")) {
           AccessToken accessToken = WxUtil.getAccessToken(Constants.CORPID, Constants.SECRET);
            if (accessToken != null && accessToken.getToken() != null && request.getParameter("code")!=null) {
            	//System.out.println("-----------------------------in--------------");
                Result<String> result = WxUtil.oAuth2GetUserByCode(accessToken.getToken(), request.getParameter("code"), 7);
                //System.out.println("result=" + result);
                String menuUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+accessToken.getToken()+"&userid="+result.getObj() ;
                System.out.println("---------token---------"+accessToken.getToken());
                System.out.println("---------userId---------"+result.getObj());
                JSONObject jo = HttpRequestUtil.httpRequest(menuUrl, EnumMethod.GET.name(), null);
                System.out.println("---------jo---------"+jo.toString());
                //jo.
                if(jo!=null){
                	session.setAttribute("Name", jo.getString("name"));
                	session.setAttribute("Phone", jo.getString("mobile"));
                	List<Employee> emps = null;
                	try {
                		//根据phone获取user
						emps = employeesService.sugguest(jo.getString("mobile"));
						if(emps!=null){
		                	Employee emp = emps.get(0);
		                	String id  = emp.getId();
		                	User u = wxLeaveManager.getUser(id);
		                	String username = u.getAccount();
		                	String password = u.getPassword();
		                	request.setAttribute("username", username);
		                	request.setAttribute("password", password);
						}
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
                	
                    System.out.println("------------------------Name is :"+jo.getString("name"));
                    System.out.println("------------------------Phone is :"+jo.getString("mobile"));
                }
                session.setAttribute("Userid", result.getObj());
                //session.setAttribute("AccessToken", accessToken.getToken());
                
            } else {
            	//System.out.println("-----------------------------out--------------");
            	session.setAttribute("Userid", "noUserId");
            	session.setAttribute("Name", "noUserName");
            	session.setAttribute("Phone", "noUserPhone");
                //session.setAttribute("AccessToken", "");
            }
        }
		model.addAttribute("Userid", session.getAttribute("Userid"));
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!"+session.getAttribute("Userid"));
		return "app/weixin/contract/w-contract-main";
	}
}
