package cn.xrb.manager.controller;

import cn.xrb.domain.Cert;
import cn.xrb.manager.service.CertService;
import cn.xrb.util.AjaxResult;
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

import java.util.List;


@Controller
@RequestMapping("/cert")
public class CertController {

	@Autowired
	private CertService certService;
	

	@RequestMapping("/index")
	public String index() {
		return "cert/index";
	}
	
	@RequestMapping("/add")
	public String add() {
		return "cert/add";
	}
	
	@RequestMapping("/edit")
	public String edit( Integer id, Model model ) {

		// 根据主键查询资质信息
		Cert cert = certService.queryById(id);
		model.addAttribute("cert", cert);

		return "cert/edit";
	}

	@ResponseBody
	@RequestMapping("/deletes")
	public Object deletes( Data ds ) {
		AjaxResult result = new AjaxResult();

		try {
			int count = certService.deleteCerts(ds);
			if ( count == ds.getIds().size() ) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;
	}

	@ResponseBody
	@RequestMapping("/delete")
	public Object delete( Integer id ) {
		AjaxResult result = new AjaxResult();

		try {
			int count = certService.deleteByPrimaryKey(id);
			if ( count == 1 ) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;
	}

	@ResponseBody
	@RequestMapping("/update")
	public Object update( Cert cert ) {
		AjaxResult result = new AjaxResult();

		try {
			int count = certService.updateCert(cert);
			if ( count == 1 ) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;
	}

	/**
	 * 新增资质数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/insert")
	public Object insert( Cert cert ) {
		AjaxResult result = new AjaxResult();

		try {
			certService.insert(cert);
			result.setSuccess(true);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;
	}

	/**
	 * 分页查询资质数据
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(@RequestParam(value = "pagetext",defaultValue = "") String pagetext,
							@RequestParam(value = "pageno",defaultValue = "1") Integer pageno,
							@RequestParam(value = "pagesize",defaultValue = "8") Integer pagesize) {
		AjaxResult result = new AjaxResult();
		PageHelper.startPage(pageno, pagesize);
		try {
			// 查询资质数据

			if ( StringUtil.isNotEmpty(pagetext) ) {
				pagetext = pagetext.replaceAll("%", "\\\\%");
			}


			// 分页查询数据
			List<Cert> list = certService.pageQuery(pagetext);
			PageInfo pageInfo=new PageInfo(list);
			result.setSuccess(true);
			result.setPageInfo(pageInfo);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
}
