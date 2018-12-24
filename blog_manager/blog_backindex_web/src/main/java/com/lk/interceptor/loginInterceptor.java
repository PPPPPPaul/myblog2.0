package com.lk.interceptor;

import com.lk.pojo.User;
import com.lk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 未登录不得访问内部页面的拦截器
 */
public class loginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求地址
        String uri = request.getRequestURI();
        //请求地址包含login则为登陆界面
        if(uri.indexOf("login")>=0){
            return true;
        }
        //否则请求不是登陆界面，需要查看session中是否有登录用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        //session中没有用户信息，去cookie中找
        if (null==user){
            String loginCookieUserName="";
            String loginCookiePassword="";
            //获取当前站点的所有Cookie，检查是否存在已经登陆的账户信息，存在就放入session中并跳转到后台主界面
            Cookie[] cookies = request.getCookies();
            if (null!=cookies) {
                for (Cookie cookie : cookies) {//对cookies中的数据进行遍历，找到用户名、密码的数据
                    if ("username".equals(cookie.getName())) {
                        loginCookieUserName = cookie.getValue();
                    } else if ("password".equals(cookie.getName())) {
                        loginCookiePassword = cookie.getValue();
                    }
                }
                if(!"".equals(loginCookieUserName) && !"".equals(loginCookiePassword)){
                    User user1 = userService.getUser(loginCookieUserName);
                    if(loginCookiePassword.equals(user1.getUserPass())){
                        session.setAttribute("loginUser", user1);
                        request.getRequestDispatcher("/admin").forward(request,response);
                        return true;
                    }
                }
            }
            //如果当前session中和cookie都不存在已经登陆的用户信息，就不允许页面到登陆界面以外的后台界面，自动回到登陆界面
            request.getRequestDispatcher("WEB-INF/view/Admin/login.jsp").forward(request,response);
            return false;
        }else{
            //session中存在用户信息
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
