package cn.xrb.controller;

import cn.xrb.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class DispatcherController {

    @Autowired
    UserService userService;
    @RequestMapping("/index")
    public String toIndex(){
        return "index";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "login";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/main")
    public String toMain(HttpSession session){
//        User user = (User) session.getAttribute(Const.LOGIN_USER);
        return "main";
    }
}
