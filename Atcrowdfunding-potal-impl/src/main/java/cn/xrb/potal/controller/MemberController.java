package cn.xrb.potal.controller;

import cn.xrb.domain.Cert;
import cn.xrb.domain.Member;
import cn.xrb.domain.MemberCert;
import cn.xrb.domain.Ticket;
import cn.xrb.manager.service.CertService;
import cn.xrb.potal.listener.PassListener;
import cn.xrb.potal.listener.RefuseListener;
import cn.xrb.potal.service.MemberService;
import cn.xrb.potal.service.TicketService;
import cn.xrb.util.AjaxResult;
import cn.xrb.util.Const;
import cn.xrb.util.Data;
import cn.xrb.util.MD5Util;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

import static cn.xrb.util.Const.LOGIN_MEMBER;

/**
 * @author xieren8iao
 * @create 2019/11/9 - 15:15
 */
@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CertService certService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;


    @Autowired
    private TaskService taskService ;
    @RequestMapping("/member")
    public String toMember(){
        return "member/member";
    }



    @RequestMapping("/minecrowdfunding")
    public String minecrowdfunding(){
        return "member/minecrowdfunding";
    }
    @RequestMapping("/basicinfo")
    public String toBasicinfo(){
        return "member/basicinfo";
    }

    @RequestMapping("/checkauthcode")
    public String checkauthcode(){
        return "member/checkauthcode";
    }
    @RequestMapping("/checkemail")
    public String checkemail(){
        return "member/checkemail";
    }

    @RequestMapping("/accttype")
    public String toAccttype(){
        return "member/accttype";
    }

    @RequestMapping("/uploadCert")
    public String toUploadCert(){
        return "member/uploadCert";
    }

    @RequestMapping("/apply")
    public String apply(HttpSession session){
        Member member = (Member) session.getAttribute(LOGIN_MEMBER);
        Ticket ticket = ticketService.getTicketByMemberId(member.getId());
        if (ticket==null)
        {
            ticket=new Ticket();
            ticket.setMemberid(member.getId());
            ticket.setPstep("apply");
            ticket.setStatus("0");
            ticketService.saveTicket(ticket);

        }else {
            if ("accttype".equals(ticket.getPstep())){
                return "redirect:/member/basicinfo.htm";
            }else if ("basicinfo".equals(ticket.getPstep())){
                List<Cert> queryCertByAccttype = certService.queryCertByAccttype(member.getAccttype());
                session.setAttribute("queryCertByAccttype", queryCertByAccttype);
                return "redirect:/member/uploadCert.htm";
            }
        }
        return "member/accttype";
    }

    @ResponseBody
    @RequestMapping("/finishApply")
    public Object finishApply( HttpSession session, String authcode) {
        AjaxResult result = new AjaxResult();

        try {

            // 获取登录会员信息
            Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);


            //让当前系统用户完成:验证码审核任务.
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;

                //完成审核验证码任务
                Task task = taskService.createTaskQuery().processInstanceId(ticket.getPiid()).taskAssignee(loginMember.getLoginacct()).singleResult();
                taskService.complete(task.getId());

                //更新用户申请状态
                loginMember.setAuthstatus("1");
                memberService.updateAuthstatus(loginMember);


                //记录流程步骤:
                ticket.setPstep("finishapply");
                ticketService.updatePstep(ticket);
                result.setSuccess(true);


        } catch( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;

    }

    @ResponseBody
    @RequestMapping("/doUploadCert")
    public Object doUploadCert( HttpSession session, Data ds) {
        AjaxResult result = new AjaxResult();

        try {

            // 获取登录会员信息
            Member loginMember = (Member)session.getAttribute(LOGIN_MEMBER);

            String realPath = session.getServletContext().getRealPath("/pics");

            List<MemberCert> certimgs = ds.getCertimgs();
            for (MemberCert memberCert : certimgs) {

                MultipartFile fileImg = memberCert.getFileImg();
                String extName = fileImg.getOriginalFilename().substring(fileImg.getOriginalFilename().lastIndexOf("."));
                String tmpName = UUID.randomUUID().toString() +  extName;
                String filename = realPath + "/cert" +"/" + tmpName;
                if (!new File(filename).exists()){
                    new File(filename).mkdirs();
                }
                fileImg.transferTo(new File(filename));	//资质文件上传.

                //准备数据
                memberCert.setIconpath(tmpName); //封装数据,保存数据库
                memberCert.setMemberid(loginMember.getId());
            }

            // 保存会员与资质关系数据.
            certService.saveMemberCert(certimgs);

            //记录流程步骤:
//            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
//            ticket.setPstep("uploadcert");
//            ticketService.updatePstep(ticket);

            //启动实名认证流程 -
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("auth").singleResult();

            Map<String,Object> variables= new HashMap<String,Object>();
            variables.put("loginacct", loginMember.getLoginacct());
            variables.put("passListener", new PassListener());
            variables.put("refuseListener", new RefuseListener());


            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("auth",variables);
//            ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);

            //记录流程步骤:
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
            ticket.setPiid(processInstance.getId());
            ticketService.updatePiidAndPstep(ticket);

            Task task = taskService.createTaskQuery().processInstanceId(ticket.getPiid()).taskAssignee(loginMember.getLoginacct()).singleResult();
            taskService.complete(task.getId());

            //更新用户申请状态
            loginMember.setAuthstatus("1");
            memberService.updateAuthstatus(loginMember);

            //记录流程步骤:
            ticket.setPstep("finishapply");
            result.setSuccess(true);
        } catch( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;

    }


    @ResponseBody
    @RequestMapping("/startProcess")
    public Object startProcess( HttpSession session, String email) {
        AjaxResult result = new AjaxResult();

        try {

            // 获取登录会员信息
            Member loginMember = (Member)session.getAttribute(LOGIN_MEMBER);

//            // 如果用户输入新的邮箱,将旧的邮箱地址替换
//            if(!loginMember.getEmail().equals(email)){
//                loginMember.setEmail(email);
//                memberService.updateEmail(loginMember);
//            }

            //启动实名认证流程 - 系统自动发送邮件,生成验证码.验证邮箱地址是否合法(模拟:银行卡是否邮箱).
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("auth").singleResult();

            //toEmail
            //authcode
            //loginacct
            //flag  审核实名认证时提供
            //passListener
            //refuseListener
//            StringBuilder authcode = new StringBuilder();
//            for (int i = 1; i <= 4; i++) {
//                authcode.append(new Random().nextInt(10));
//            }

            Map<String,Object> variables= new HashMap<String,Object>();
            variables.put("loginacct", loginMember.getLoginacct());
            variables.put("passListener", new PassListener());
            variables.put("refuseListener", new RefuseListener());


            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("auth",variables);
//            ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);


            //记录流程步骤:
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
            ticket.setPstep("checkemail");
            ticket.setPiid(processInstance.getId());
//            ticket.setAuthcode(authcode.toString());
            ticketService.updatePiidAndPstep(ticket);

            result.setSuccess(true);
        } catch( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;

    }


    @RequestMapping("/updateBasicinfo")
    @ResponseBody
    public AjaxResult updateBasicinfo(Member member,HttpSession session){
        AjaxResult ajaxResult=new AjaxResult();
        Member loginMember= (Member) session.getAttribute(LOGIN_MEMBER);
       loginMember.setRealname(member.getRealname());
       loginMember.setCardnum(member.getCardnum());
       loginMember.setTel(member.getTel());
        memberService.updateBasicinfo(loginMember);
        List<Cert> queryCertByAccttype = certService.queryCertByAccttype(loginMember.getAccttype());
        session.setAttribute("queryCertByAccttype", queryCertByAccttype);
        Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
        ticket.setPstep("basicinfo");
        ticketService.updatePstep(ticket);

        ajaxResult.setSuccess(true);
        return ajaxResult;
    }
    @RequestMapping("/updateAcctType")
    @ResponseBody
    public AjaxResult updateAcctType(String accttype,HttpSession session){
        AjaxResult ajaxResult=new AjaxResult();
        Member member= (Member) session.getAttribute(LOGIN_MEMBER);
        member.setAccttype(accttype);
        memberService.updateAcctType(member);
        //流程单记录
        Ticket ticket = ticketService.getTicketByMemberId(member.getId());
        ticket.setPstep("accttype");
        ticketService.updatePstep(ticket);

        ajaxResult.setSuccess(true);
        return ajaxResult;
    }


    @RequestMapping("/doLogin")
    @ResponseBody
    public AjaxResult doLogin(Member member, HttpSession session, HttpServletRequest request){
//        System.out.println(user.getUserpswd());//用户输入的密码的123
        member.setUserpswd(MD5Util.digest(member.getUserpswd()));//将123转换成MD5
//        System.out.println(user);
        AjaxResult ajaxResult = new AjaxResult();
        Member loginMember = memberService.getMemberByUP(member);//查看数据看是否有 MD5的123存在
        /**
         * 关于以下逻辑 视频中
         * 1. 视频中在业务层关于查询异常的时候 他是抛出一个异常，而我是捕获异常，捕获之后同一返回一个null
         */
        if (loginMember!=null) {
            //如果用户名存在，将对象存入session域并跳转到main.jsp
            System.out.println(loginMember.getUsername());
            session.setAttribute(LOGIN_MEMBER,loginMember);
            ajaxResult.setSuccess(true);//{"success",true}
            //-------------------------------------------------------
            //-------------------------------------------------------
            return ajaxResult;
        }
        request.setAttribute("error_login","用户名或密码错误");
        ajaxResult.setMessage("登陆失败,用户名或密码错误");
        ajaxResult.setSuccess(false);
        return ajaxResult;//{"success":true,"message":"登陆失败"}
    }


}
