package activiti;

import cn.xrb.listener.NoListener;
import cn.xrb.listener.YesListener;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xieren8iao
 * @create 2019/10/23 - 11:28
 */
public class ActivitiTest {
    ApplicationContext ioc=new ClassPathXmlApplicationContext("spring/spring-*.xml");

    @Test
    //创建23张表
    public void test01(){

        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        System.out.println(processEngine);
    }
    //2.部署流程定义
    @Test
    public void test02(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        System.out.println("processEngine="+processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment().addClasspathResource("auth.bpmn").deploy();

        System.out.println("deploy="+deploy);
    }

    //3.查询部署流程定义
    @Test
    public void test03(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");

        System.out.println("processEngine="+processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        List<ProcessDefinition> list = processDefinitionQuery.list(); //查询所有流程定义

        for (ProcessDefinition processDefinition : list) {
            System.out.println("Id="+processDefinition.getId());
            System.out.println("Key="+processDefinition.getKey());
            System.out.println("Name="+processDefinition.getName());
            System.out.println("Version="+processDefinition.getVersion());
            System.out.println("-----------------------");
        }

        long count = processDefinitionQuery.count(); //查询流程定义记录数
        System.out.println("count="+count);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        //查询最后一次部署的流程定义
        ProcessDefinition processDefinition = processDefinitionQuery.latestVersion().singleResult();
        System.out.println("Id="+processDefinition.getId());
        System.out.println("Key="+processDefinition.getKey());
        System.out.println("Name="+processDefinition.getName());
        System.out.println("Version="+processDefinition.getVersion());
        System.out.println("******************************");

        //排序查询流程定义,分页查询流程定义.
        ProcessDefinitionQuery definitionQuery = processDefinitionQuery.orderByProcessDefinitionVersion().desc();
        List<ProcessDefinition> listPage = definitionQuery.listPage(0, 2);
        for (ProcessDefinition processDefinition2 : listPage) {
            System.out.println("Id="+processDefinition2.getId());
            System.out.println("Key="+processDefinition2.getKey());
            System.out.println("Name="+processDefinition2.getName());
            System.out.println("Version="+processDefinition2.getVersion());
            System.out.println("-----------------------");
        }

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        //根据流程定义的 Key 查询流程定义对象
        ProcessDefinition processDefinition2 = processDefinitionQuery.processDefinitionKey("myProcess").latestVersion().singleResult();
        System.out.println("Id="+processDefinition2.getId());
        System.out.println("Key="+processDefinition2.getKey());
        System.out.println("Name="+processDefinition2.getName());
        System.out.println("Version="+processDefinition2.getVersion());

    }
    //4.创建流程实例
    /**
     * act_hi_actinst, 历史的活动的任务表.
     * act_hi_procinst, 历史的流程实例表.
     * act_hi_taskinst, 历史的流程任务表
     * act_ru_execution, 正在运行的任务表.
     * act_ru_task, 运行的任务数据表.
     */
    @Test
    public void test04(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());

        System.out.println(processInstance+"processInstance");
    }

//5.查询流程实例的任务数据
    @Test
    public void test05(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> list1 = taskQuery.taskAssignee("zhangsan").list();
        List<Task> list2 = taskQuery.taskAssignee("lisi").list();

        for (Task task : list1) {
            System.out.println("id="+task.getId());
            System.out.println("name="+task.getName());
//            taskService.complete(task.getId());
        }

        for (Task task : list2) {
            System.out.println("id="+task.getId());
            System.out.println("name="+task.getName());
            taskService.complete(task.getId());
        }
    }

    //6.历史数据查询
    @Test
    public void test06(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        HistoricProcessInstance historicProcessInstance = historicProcessInstanceQuery.processInstanceId("501").finished().singleResult();
        System.out.println("historicProcessInstance="+historicProcessInstance);

    }

    //7.领取任务 claim()
    @Test
    public void test07(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> list1 = taskQuery.taskCandidateGroup("tl").list();

        for (Task task : list1) {
            System.out.println("id="+task.getId());
            System.out.println("name="+task.getName());
            taskService.claim(task.getId(), "zhangsan");
        }


    }
    //8.流程变量
    @Test
    public void test08(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String,Object> variable=new HashMap<>();
        variable.put("tl", "zhangsan");
        variable.put("pm", "lisi");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),variable);

        System.out.println(processInstance+"processInstance");
    }


    //9.排他网关(互斥)
    @Test
    public void test09(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String,Object> varibles=new HashMap<>();
        varibles.put("days", "5");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),varibles);

        System.out.println(processInstance+"processInstance");
    }

    //9.
    @Test
    public void test09_1(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> list = taskQuery.taskAssignee("lisi").list();
        for (Task task : list) {
            taskService.complete(task.getId());
        }
    }
    //10.并行网关(会签)
    @Test
    public void test10(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());

        System.out.println(processInstance+"processInstance");
    }

    //10.
    @Test
    public void test10_1(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> list = taskQuery.taskAssignee("lisi").list();
        for (Task task : list) {
            taskService.complete(task.getId());
        }
    }

    //11.排他+并行网关(互斥)
    @Test
    public void test11(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String,Object> varibles=new HashMap<>();
        varibles.put("days", "5");
        varibles.put("cost", "5000");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),varibles);

        System.out.println(processInstance+"processInstance");
    }

    @Test
    public void test12(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String,Object> varibles=new HashMap<>();
        varibles.put("yesListener", new YesListener());
        varibles.put("noListener", new NoListener());
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),varibles);

        System.out.println(processInstance+"processInstance");
    }

    //10.
    @Test
    public void test12_1(){
        ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> list = taskQuery.taskAssignee("zhangsan").list();
        for (Task task : list) {
            taskService.setVariable(task.getId(), "flag", "true");
            taskService.complete(task.getId());
        }
    }


}
