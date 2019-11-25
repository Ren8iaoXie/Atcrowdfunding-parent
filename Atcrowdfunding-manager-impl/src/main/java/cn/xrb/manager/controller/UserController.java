package cn.xrb.manager.controller;

import cn.xrb.domain.Permission;
import cn.xrb.domain.Role;
import cn.xrb.domain.User;
import cn.xrb.manager.service.UserService;
import cn.xrb.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/assignRole")
    public String assignPermission(Integer id,Model model){
        List<Role> allRoleList=userService.queryAllRole();
        List<Integer> roleIds=userService.queryRoleByUserId(id);

        List<Role> leftRoleList=new ArrayList<Role>();//已分配的角色
        List<Role> rightRoleList=new ArrayList<Role>();//未分配的角色

        for (Role role : allRoleList) {
            if (roleIds.contains(role.getId())){
                rightRoleList.add(role);
            }else {

                leftRoleList.add(role);
            }
        }
        model.addAttribute("leftRoleList", leftRoleList);
        model.addAttribute("rightRoleList", rightRoleList);

        return "user/assignRole";
    }
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "user/add";
    }
    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Model model){
        User user =userService.getUserById(id);
        model.addAttribute("user", user);
        return "user/update";
    }
    @RequestMapping("/index")
    public String toIndex(){
        return "user/index";
    }
    /**
     * 分页查询用户功能
     * @param pageno 查第几页
     * @param pagesize 查几条数
     * @return
     */

    @RequestMapping("/queryUser")
    public String queryUser(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                            @RequestParam(value = "pagesize",required = false,defaultValue = "10")Integer pagesize, Model model){
        Page page=userService.queryPage(pageno,pagesize);
        model.addAttribute("page", page);

        return "user/index";
    }
    /**
     * 用于测试框架搭建是否成功
     * @return
     */
    @RequestMapping("/getUser")
    public String getUser(){
        List<User> users = userService.selectAll();
        for (User user : users) {
            System.out.println(user);
        }
        return "success";
    }

//    //同步方式
//    @RequestMapping("/login")
//    public  String getUserByUAP(@RequestBody User user, HttpSession session, HttpServletRequest request){
//        User loginUser = userService.getUserByUAP(user);
//        /**
//         * 关于以下逻辑 视频中
//         * 1. 视频中在业务层关于查询异常的时候 他是抛出一个异常，而我是捕获异常，捕获之后同一返回一个null
//         */
//        if (loginUser!=null) {
//            //如果用户名存在，将对象存入session域并跳转到main.jsp
//            session.setAttribute(Const.LOGIN_USER,loginUser.getUsername());
//            return "main";
//        }
//        request.setAttribute("error_login","用户名或密码错误");
//        return "login";
//    }

    //异步方式
    @RequestMapping("/doLogin")
    @ResponseBody
    public AjaxResult getUserByUAP(User user, HttpSession session, HttpServletRequest request){
//        System.out.println(user.getUserpswd());//用户输入的密码的123
        user.setUserpswd(MD5Util.digest(user.getUserpswd()));//将123转换成MD5
//        System.out.println(user);
        AjaxResult ajaxResult = new AjaxResult();
        User loginUser = userService.getUserByUAP(user);//查看数据看是否有 MD5的123存在
        /**
         * 关于以下逻辑 视频中
         * 1. 视频中在业务层关于查询异常的时候 他是抛出一个异常，而我是捕获异常，捕获之后同一返回一个null
         */
        if (loginUser!=null) {
            //如果用户名存在，将对象存入session域并跳转到main.jsp
            System.out.println(loginUser.getUsername());
            session.setAttribute(Const.LOGIN_USER,loginUser);
            ajaxResult.setSuccess(true);//{"success",true}
            //-------------------------------------------------------
            List<Permission> permissions=userService.queryPermissionByUserId(loginUser.getId());

            //存放登陆的用户拥有的权限,用于拦截器拦截地址栏
            Set<String> authUris=new HashSet<String>();

            //权限菜单menu
            Permission permissionRoot=null;
            Map<Integer,Permission> map=new HashMap<Integer,Permission>();
            for (Permission permission : permissions) {
                map.put(permission.getId(), permission);
                authUris.add("/"+permission.getUrl());
            }

            session.setAttribute(Const.LOGIN_USER_URIS, authUris);
            for (Permission permission : permissions) {
                Permission child=permission;
                if (child.getPid()==null){
                    permissionRoot=permission;
                }else {
                    Permission parent=map.get(child.getPid());
                    parent.getChildren().add(child);
                }
            }
            session.setAttribute("permissionRoot", permissionRoot);

            //-------------------------------------------------------
            return ajaxResult;

        }
        request.setAttribute("error_login","用户名或密码错误");
        ajaxResult.setMessage("登陆失败,用户名或密码错误");
        ajaxResult.setSuccess(false);
        return ajaxResult;//{"success":true,"message":"登陆失败"}
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }



    @RequestMapping("/getUserByPage")
    public ModelAndView getUserByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
        PageHelper.startPage(page, size);
        List<User> userList = userService.getUserByPage();
        PageInfo pageInfo = new PageInfo(userList);
        System.out.println(pageInfo.getPageNum());//1
        List list = pageInfo.getList();
        for (Object o : list) {
            System.out.println(o);
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("user/index");
        return mv;
    }


    @RequestMapping("/getUserByPageByCondition")
    @ResponseBody
    public AjaxResult getUserByPageByCondition(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "8") int size,@RequestParam(defaultValue = "")String loginacct){
        System.out.println("**********************************");
        System.out.println(page);
        System.out.println(size);
        System.out.println(loginacct);
        PageHelper.startPage(page, size);
        if (loginacct.contains("%")){
            loginacct=loginacct.replaceAll("%", "\\\\%");
        }
        List<User> userList = userService.getUserByPageByCondition(loginacct);
        PageInfo pageInfo = new PageInfo(userList);
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setSuccess(true);
        ajaxResult.setPageInfo(pageInfo);
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        System.out.println(ajaxResult.isSuccess());
        System.out.println(ajaxResult);
        return ajaxResult;
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public AjaxResult addUser(User user){
        AjaxResult ajaxResult = new AjaxResult();

        int i = userService.addUser(user);
        if (i == 1) {
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("保存成功");
        } else {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("保存失败");
        }
        return ajaxResult;//{success:true,message:"保存成功"}
    }
    @RequestMapping("/doUpdate")
    @ResponseBody
    public AjaxResult doUpdate(User user){
        AjaxResult ajaxResult = new AjaxResult();

        int i = userService.updateUser(user);
        if (i == 1) {
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("修改成功");
        } else {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("修改失败");
        }
        return ajaxResult;//{success:true,message:"保存成功"}
    }


    @RequestMapping("/doAssignRole")
    @ResponseBody
    public AjaxResult doAssignRole(Integer userid, Data data){
        AjaxResult ajaxResult = new AjaxResult();

        try {


//        int i = userService.saveUserRoleRelationship(userid,data.getIds());
            userService.saveUserRoleRelationship2(userid,data);

            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("添加角色成功");

        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("添加角色失败");
            e.printStackTrace();
        }

        return ajaxResult;//{success:true,message:"保存成功"}
    }
    @RequestMapping("/undoAssignRole")
    @ResponseBody
    public AjaxResult undoAssignRole(Integer userid, Data data){
        AjaxResult ajaxResult = new AjaxResult();

        try {
            userService.deleteUserRoleRelationship(userid,data.getIds());
//        int i = userService.deleteUserRoleRelationship2(userid,data);

            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("删除角色成功");
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除角色失败");
            e.printStackTrace();
        }


        return ajaxResult;//{success:true,message:"保存成功"}
    }
    @RequestMapping("/doDeleteBatch")
    @ResponseBody
    public AjaxResult doDeleteBatch(Integer[] id){
        AjaxResult ajaxResult = new AjaxResult();

        int i = userService.deleteBatchUser(id);
        if (i == id.length) {
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("删除成功");
        } else {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除失败");
        }
        return ajaxResult;//{success:true,message:"保存成功"}
    }
    @RequestMapping("/doDelete")
    @ResponseBody
    public AjaxResult doDelete(Integer id){
        AjaxResult ajaxResult = new AjaxResult();

        int i = userService.deleteUser(id);
        if (i == 1) {
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("删除成功");
        } else {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除失败");
        }
        return ajaxResult;//{success:true,message:"保存成功"}
    }
}
