package cn.xrb.manager.controller;

import cn.xrb.domain.Permission;
import cn.xrb.manager.service.PermissionService;
import cn.xrb.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xieren8iao
 * @create 2019/10/14 - 10:09
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/index")
    public String index(){
        return "permission/index";
    }
    @RequestMapping("/toAdd")
    public String toAdd(){

        return "permission/add";
    }


    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id, Model model){
        Permission permission=permissionService.getPermissionById(id);
        model.addAttribute("permission", permission);
        return "permission/update";
    }

    @RequestMapping("/doAdd")
    @ResponseBody
    public Object doAdd(Permission permission){
        AjaxResult ajaxResult = new AjaxResult();

        try {
          int count= permissionService.savePermission(permission);


            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("添加成功");
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("添加失败");
            throw new RuntimeException(e);
        }
        return ajaxResult;
    }
    @RequestMapping("/doDelete")
    @ResponseBody
    public Object doDelete(Integer id){
        AjaxResult ajaxResult = new AjaxResult();

        try {
            int count= permissionService.deletePermissionById(id);


            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("删除成功");
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除失败");
            throw new RuntimeException(e);
        }
        return ajaxResult;
    }
    @RequestMapping("/doUpdate")
    @ResponseBody
    public Object doUpdate(Permission permission){
        AjaxResult ajaxResult = new AjaxResult();

        try {
            int count= permissionService.updatePermission(permission);


            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("修改成功");
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("修改失败");
            throw new RuntimeException(e);
        }
        return ajaxResult;
    }
    //一次性从数据库加载所有数据 demo5,利用map提高性能
    @RequestMapping("/loadData")
    @ResponseBody
    public Object loadData(){
        AjaxResult ajaxResult = new AjaxResult();

        try {
            List<Permission> root=new ArrayList<Permission>();

            //查找所有菜单
            List<Permission> allPermissions=permissionService.queryAllPermiisions();
            Map<Integer,Permission> map=new HashMap<Integer, Permission>();
            //将查到的所有permission存储到map中
            for (Permission innerPermission : allPermissions) {
                map.put(innerPermission.getId(), innerPermission);
            }

            for (Permission permission : allPermissions) {

                Permission child=permission;//假设为子菜单
                if (child.getPid()==null){
                    root.add(permission);
                }else {
                    //父节点
                    Permission parent=map.get(child.getPid());
                    parent.getChildren().add(child);
                }
            }

            ajaxResult.setData(root);
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("加载成功");
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("加载失败");
            throw new RuntimeException(e);
        }
        return ajaxResult;
    }

    /*//一次性从数据库加载所有数据 demo4
    @RequestMapping("/loadData")
    @ResponseBody
    public Object loadData(){
        AjaxResult ajaxResult = new AjaxResult();
        try {
        List<Permission> root=new ArrayList<Permission>();

        //查找所有菜单
        List<Permission> allPermissions=permissionService.queryAllPermiisions();

        for (Permission permission : allPermissions) {
            //通过子查父
            Permission child=permission;
            child.setOpen(true);
            if (child.getPid()==null){
                root.add(permission);
            }else {
                //父节点
                for (Permission innerPermission : allPermissions) {
                    if (child.getPid()==innerPermission.getId()){
                        Permission parent=innerPermission;
                        parent.setOpen(true);
                        parent.getChildren().add(child);
                        break;//找到父节点，跳出当层循环
                    }
                }
            }
        }

        ajaxResult.setData(root);

            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("加载成功");
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("加载失败");
            throw new RuntimeException(e);
        }
        return ajaxResult;
    }*/
//    //递归：从数据库加载数据许可数
//    @RequestMapping("/loadData")
//    @ResponseBody
//    public Object loadData(){
//        AjaxResult ajaxResult = new AjaxResult();
//        List<Permission> root=new ArrayList<Permission>();
//
//        Permission permission= permissionService.getRootPermission();
//       root.add(permission);
//
//       queryChildPermissions(permission);
//
//        ajaxResult.setData(root);
//        try {
//            ajaxResult.setSuccess(true);
//            ajaxResult.setMessage("加载成功");
//        }catch (Exception e){
//            ajaxResult.setSuccess(false);
//            ajaxResult.setMessage("加载失败");
//            throw new RuntimeException(e);
//        }
//        return ajaxResult;
//    }

    //递归查询
    private void queryChildPermissions(Permission permission) {
        permission.setOpen(true);
        List<Permission> children = permissionService.getChildrenPermissionById(permission.getId());
        permission.setChildren(children);


        for (Permission innerChild : children) {
            queryChildPermissions(innerChild);
        }
    }
    /*//从数据库加载数据许可数demo2
    @RequestMapping("/loadData")
    @ResponseBody
    public Object loadData(){
        AjaxResult ajaxResult = new AjaxResult();
        List<Permission> root=new ArrayList<Permission>();

        Permission permission= permissionService.getRootPermission();


        root.add(permission);


        List<Permission> children=permissionService.getChildrenPermissionById(permission.getId());

        //设置父子关系
        permission.setChildren(children);


        for (Permission child : children) {
            child.setOpen(true);
            List<Permission> innerChildren=permissionService.getChildrenPermissionById(child.getId());
            child.setChildren(innerChildren);
        }
        ajaxResult.setData(root);
        try {
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("加载成功");
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("加载失败");
            throw new RuntimeException(e);
        }
        return ajaxResult;
    }*/



    //模拟数据加载许可数demo1
//    @RequestMapping("/loadData")
//    @ResponseBody
//    public Object loadData(){
//        AjaxResult ajaxResult = new AjaxResult();
//        List<Permission> root=new ArrayList<Permission>();
//
//        Permission permission=new Permission();
//        permission.setName("系统权限菜单");
//        permission.setOpen(true);
//
//        root.add(permission);
//
//        List<Permission> children=new ArrayList<Permission>();
//        //设置父子关系
//        permission.setChildren(children);
//
//        Permission permission1=new Permission();
//        permission1.setName("控制面板");
//
//        Permission permission2=new Permission();
//        permission2.setName("权限管理");
//
//
//        children.add(permission1);
//        children.add(permission2);
//
//        ajaxResult.setData(root);
//
//        try {
//            ajaxResult.setSuccess(true);
//            ajaxResult.setMessage("加载成功");
//        }catch (Exception e){
//            ajaxResult.setSuccess(false);
//            ajaxResult.setMessage("加载失败");
//            throw new RuntimeException(e);
//        }
//        return ajaxResult;
//    }

}
