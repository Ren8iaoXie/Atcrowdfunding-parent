package cn.xrb.listener;

import cn.xrb.domain.Permission;
import cn.xrb.manager.service.PermissionService;
import cn.xrb.util.Const;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xieren8iao
 * @create 2019/10/21 - 15:51
 */
public class StartSystemListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //1.将项目上下文路径(request.getContextPath())放置到application域中.
        ServletContext application = sce.getServletContext();
        String contextPath = application.getContextPath();
        application.setAttribute("APP_PATH", contextPath);
        System.out.println("APP_PATH...");

        //查询所有用户的权限
        WebApplicationContext ioc = WebApplicationContextUtils.getWebApplicationContext(application);
        PermissionService permissionService = ioc.getBean(PermissionService.class);
        List<Permission> permissions = permissionService.queryAllPermiisions();
        Set<String> allUris=new HashSet<>();
        for (Permission permission : permissions) {
            allUris.add("/"+permission.getUrl());
        }
        application.setAttribute(Const.ALL_PERMISSION_URI, allUris);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
