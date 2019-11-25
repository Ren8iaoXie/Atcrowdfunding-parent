package cn.xrb.manager.controller;

import cn.xrb.util.AjaxResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xieren8iao
 * @create 2019/11/6 - 12:54
 */
@Controller
@RequestMapping("/process")
public class ProcessController {
    @Autowired
    private RepositoryService repositoryService;
    @RequestMapping("/index")
    public String index(){
        return "process/index";
    }

    @RequestMapping("/showimg")
    public String showimg(){
        return "process/showimg";
    }

    @RequestMapping("/doIndex")
    @ResponseBody
    public Object doIndex(@RequestParam(value = "pageno",defaultValue = "1") Integer pageno,
                          @RequestParam(value = "pagesize",defaultValue = "8")Integer pagesize){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
            PageHelper.startPage(pageno, pagesize);
            List<Map<String,Object>> myList=new ArrayList<Map<String, Object>>();
            for (ProcessDefinition processDefinition : list) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("id", processDefinition.getId());
                        map.put("name", processDefinition.getName());
                        map.put("key", processDefinition.getKey());
                        map.put("version", processDefinition.getVersion());
                        myList.add(map);
            }
             PageInfo pageInfo=new PageInfo(myList);
            ajaxResult.setMessage("查询成功");
            ajaxResult.setSuccess(true);
            ajaxResult.setPageInfo(pageInfo);
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @RequestMapping("/deploy")
    @ResponseBody
    public Object deploy(HttpServletRequest request){
        AjaxResult ajaxResult = new AjaxResult();


        try {
            MultipartHttpServletRequest multipartHttpServletRequest= (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartHttpServletRequest.getFile("processDefFile");
            repositoryService.createDeployment().addInputStream(multipartFile.getOriginalFilename(), multipartFile.getInputStream()).deploy();
            ajaxResult.setMessage("部署成功");
            ajaxResult.setSuccess(true);

        } catch (Exception e) {
            ajaxResult.setMessage("部署失败");
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @RequestMapping("/doDelete")
    @ResponseBody
    public Object doDelete(String id){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);//true表示级联删除
            ajaxResult.setMessage("删除成功");
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }
    @RequestMapping("/showimgProDef")
    @ResponseBody
    public void showimg(String id, HttpServletResponse response) throws IOException {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(resourceAsStream, outputStream);

    }
}

