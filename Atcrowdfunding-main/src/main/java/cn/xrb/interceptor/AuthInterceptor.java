package cn.xrb.interceptor;

import cn.xrb.manager.service.PermissionService;
import cn.xrb.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author xieren8iao
 * @create 2019/10/21 - 15:18
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1、查询所有权限
//        List<Permission> allPermissions = permissionService.queryAllPermiisions();
//        Set<String> allUris=new HashSet<>();
//        for (Permission permission : allPermissions) {
//            allUris.add("/"+permission.getUrl());
//        }
        Set<String> allUris = (Set<String>) request.getSession().getServletContext().getAttribute(Const.ALL_PERMISSION_URI);
        //登陆的用户所拥有的权限
        Set<String> authUris= (Set<String>) request.getSession().getAttribute(Const.LOGIN_USER_URIS);

        //2、判断请求路径是否在范围内
        String requestPath=request.getServletPath();

        if (allUris.contains(requestPath)){
                if (authUris.contains(requestPath)){
                    return true;
                }else
                    request.getSession().setAttribute("error_login", "权限不足，请重新登陆！");
                    response.sendRedirect(request.getContextPath()+"/login.htm");
                return false;
        }else {
            return true;
        }
    }
}
