package cn.xrb.manager.controller;

import cn.xrb.domain.Permission;
import cn.xrb.domain.Role;
import cn.xrb.manager.service.PermissionService;
import cn.xrb.manager.service.RoleService;
import cn.xrb.util.AjaxResult;
import cn.xrb.util.BaseController;
import cn.xrb.util.Data;
import cn.xrb.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**角色模块控制器
 * @author xieren8iao
 * @create 2019/10/12 - 17:07
 */
@RequestMapping("/role")
@Controller
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;
    /**
     * role模块主页
     * @return
     */
    @RequestMapping("/add")
    public String add(){
        return "role/add";
    }

    @RequestMapping("/doAdd")
    @ResponseBody
    public AjaxResult doAdd(Role role){
        AjaxResult ajaxResult = new AjaxResult();
        int i=roleService.addRole(role);
        if (i == 1) {
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("保存成功");
        } else {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("保存失败");
        }

        return ajaxResult;
    }

    @RequestMapping("/index")
    public String index(){
        return "role/index";
    }

    @RequestMapping("/edit")
    public String edit(Integer id, Model model){
        Role role=roleService.queryById(id);
       model.addAttribute("role", role);
        return "role/edit";
    }
    @RequestMapping("/assignPermission")
    public String assignPermission(){
        return "role/assignPermission";
    }
    @RequestMapping("/doEdit")
    @ResponseBody
    public AjaxResult doEdit(Role role,Model model){
        AjaxResult ajaxResult = new AjaxResult();
        int count=roleService.updateRole(role);
        if (count==1){
            ajaxResult.setMessage("修改成功");
            ajaxResult.setSuccess(true);
        }else {
            ajaxResult.setMessage("修改失败");
            ajaxResult.setSuccess(false);
        }
        model.addAttribute("role", role);

        return ajaxResult;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxResult delete(@RequestParam("id") Integer id){
        int count=roleService.deleteById(id);
        AjaxResult ajaxResult=new AjaxResult();
        if (count==1){
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("删除成功");
        }else {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除失败");
        }
        return ajaxResult;
    }


    @RequestMapping("/doAssignPermission")
    @ResponseBody
    public Object doAssignPermission(Integer roleid,Data data){
        start();
        int count=roleService.saveRolePermissionRelationship(roleid,data);
        if (count==data.getIds().size()){
            success(true);
        }else {
            error("分配失败");
        }
        return end();
    }
    @RequestMapping("/loadDataAsync")
    @ResponseBody
    public Object loadDataAsync(Integer roleid){
       start();
        List<Permission> root=new ArrayList<Permission>();

            //查找所有菜单
            List<Permission> allPermissions=permissionService.queryAllPermiisions();

            List<Integer> roleids=permissionService.queryPermissionIdByRoleId(roleid);
            Map<Integer,Permission> map=new HashMap<Integer, Permission>();
            //将查到的所有permission存储到map中
            for (Permission innerPermission : allPermissions) {
                map.put(innerPermission.getId(), innerPermission);
                if (roleids.contains(innerPermission.getId())){
                    innerPermission.setChecked(true);
                }
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

        success(true);
        return root;
    }

    @RequestMapping("/batchDelete")
    @ResponseBody
    public AjaxResult batchDelete(Data data){
        System.out.println("***"+data.getIds());
        int count=roleService.batchDelete(data);
        AjaxResult ajaxResult=new AjaxResult();
        if (count==data.getIds().size()){
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("删除成功");
        }else {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除失败");
        }
        return ajaxResult;
    }



//    @RequestMapping("/queryRoleByPage")
//    @ResponseBody
//    public AjaxResult queryRoleByPage(@RequestParam(value = "pageno",defaultValue ="1")Integer pageno,
//                                      @RequestParam(value ="pagesize",defaultValue ="10")Integer pagesize,
//                                      ModelAndView mv
//    ){
//        System.out.println(pageno);
//        System.out.println(pagesize);
//
//        AjaxResult ajaxResult=new AjaxResult();
//        PageHelper.startPage(pageno, pagesize);
//        List<Role> roleList=roleService.queryRole();
//
//        PageInfo pageInfo=new PageInfo(roleList);
////        System.out.println("*********");
////        System.out.println(pageInfo.getPageNum());//当前页：1
////        System.out.println(pageInfo.getPages());//一共多少页2
////        System.out.println(pageInfo.getStartRow());//当前页的第一行：1
////        System.out.println(pageInfo.getEndRow());//当前页的最后一行：8
////        System.out.println(pageInfo.getTotal());//总记录数：9
//        ajaxResult.setSuccess(true);
//        ajaxResult.setMessage("查询成功");
//        ajaxResult.setPageInfo(pageInfo);
//        mv.addObject("pageInfo",pageInfo);
//        mv.setViewName("role/index");
//        return ajaxResult;
//    }

    @RequestMapping("/queryRoleByPage")
    @ResponseBody
    public AjaxResult queryRoleByPage(@RequestParam(value = "pageno",defaultValue ="1")Integer pageno,
                                      @RequestParam(value ="pagesize",defaultValue ="8")Integer pagesize,
                                      @RequestParam(value = "pagetext",defaultValue = "")String pagetext
    ){
        System.out.println(pageno);
        System.out.println(pagesize);
        System.out.println(pagetext);
        AjaxResult ajaxResult=new AjaxResult();
        PageHelper.startPage(pageno, pagesize);
        if (StringUtil.isNotEmpty(pagetext)){
            pagetext=pagetext.replaceAll("%", "\\\\%");
        }
        List<Role> roleList=roleService.queryRoleByCondition(pagetext);

        PageInfo pageInfo=new PageInfo(roleList);
//        System.out.println("*********");
//        System.out.println(pageInfo.getPageNum());//当前页：1
//        System.out.println(pageInfo.getPages());//一共多少页2
//        System.out.println(pageInfo.getStartRow());//当前页的第一行：1
//        System.out.println(pageInfo.getEndRow());//当前页的最后一行：8
//        System.out.println(pageInfo.getTotal());//总记录数：9
        ajaxResult.setSuccess(true);
        ajaxResult.setMessage("查询成功");
        ajaxResult.setPageInfo(pageInfo);
        return ajaxResult;
    }

}
