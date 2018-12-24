package com.lk.controller;

import com.lk.custom.result.YHResult;
import com.lk.pojo.User;
import com.lk.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AdminLoginController {
    @Autowired
    private LoginService loginService;
    /**
     * 用户登入
     * @param user
     * @param request
     * @return
     */
    @RequestMapping("/loginVerify")
    @ResponseBody
    public YHResult userVerify(User user,String rememberme,HttpServletRequest request, HttpServletResponse response){
        YHResult result = loginService.loginVerify(user);
        if (result.getStatus()==200) {
            //将后台拿到的用户数据放入session域中
            User dbuser = (User) result.getData();
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", dbuser);
            //登录时如果勾选了记住密码，将密码放入cookie保存1分钟
            if (rememberme!=null) {
                Cookie cookie1 = new Cookie("username", dbuser.getUserName());
                Cookie cookie2 = new Cookie("password", dbuser.getUserPass());
                cookie1.setMaxAge(600);
                cookie2.setMaxAge(600);
                response.addCookie(cookie1);
                response.addCookie(cookie2);
            }
        }
        return result;
    }

    /**
     * 用户登出
     * @param request
     * @return
     */
    @RequestMapping("/admin/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        //登出账户时删除站点的用户Cookie
        Cookie[] cookies = request.getCookies();
        if (null!=cookies) {
            for (Cookie cookie : cookies) {//对cookies中的数据进行遍历，找到用户名、密码的数据
                if ("username".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                } else if ("password".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        //登出时删除session中的用户信息
        session.setAttribute("loginUser",null);
        return "Admin/login";
    }
}