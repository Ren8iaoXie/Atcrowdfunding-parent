package cn.xrb.interceptor;

import cn.xrb.domain.Member;
import cn.xrb.domain.User;
import cn.xrb.util.Const;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * @author xieren8iao
 * @create 2019/10/21 - 13:10
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Set<String> uri=new HashSet<String>();
        uri.add("/index.htm");
        uri.add("/login.htm");
        uri.add("/index.do");
        uri.add("/logout.do");
        uri.add("/user/doLogin.do");
        uri.add("/member/doLogin.do");
        uri.add("/jsp/login.jsp");
        uri.add("/user/logout.do");
        uri.add("/user/regist.htm");
        uri.add("/user/regist.do");
        String servletPath = request.getServletPath();

        if (uri.contains(servletPath)){
            return true;
        }
        User user = (User) request.getSession().getAttribute(Const.LOGIN_USER);
        Member member = (Member) request.getSession().getAttribute(Const.LOGIN_MEMBER);
        if (user!=null ||member!=null)
            return true;
        else {
            request.getSession().setAttribute("error_login","您未登录！请登陆");
            response.sendRedirect(request.getContextPath()+"/login.htm");
            return false;
        }
    }
}
