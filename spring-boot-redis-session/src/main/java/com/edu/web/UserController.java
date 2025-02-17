package com.edu.web;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.model.User;
import com.edu.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/setSession")
    @ResponseBody
    public Map<String, Object> setSession (HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        request.getSession().setAttribute("message", request.getRequestURL());
        map.put("request Url", request.getRequestURL());
        return map;
    }

    @RequestMapping(value = "/getSession")
    @ResponseBody
    public Object getSession (HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        map.put("message", request.getSession().getAttribute("user"));
        return map;
    }


    @RequestMapping(value = "/index")
    public String index (HttpServletRequest request){
        String msg="index content";
        Object user= request.getSession().getAttribute("user");
        if (user==null){
            msg="please login first！";
        }
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login (HttpServletRequest request,String userName,String password){
        String msg="logon failure!";
        User user= userRepository.findByUserName(userName);
        System.out.println(userName);
        if (user!=null && user.getPassword().equals(password)){
            request.getSession().setAttribute("user",user);
            msg="login successful!";
        }
        return msg;
    }

    @RequestMapping(value = "/loginout")
    public String loginout (HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "loginout successful!";
    }

}