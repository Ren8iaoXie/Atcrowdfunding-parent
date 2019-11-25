package cn.xrb.manager.service.impl;

import cn.xrb.domain.Advert;
import cn.xrb.manager.dao.AdvertMapper;
import cn.xrb.manager.service.AdvertService;
import cn.xrb.util.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertServiceImpl implements AdvertService {

	@Autowired
	private AdvertMapper advertDao;
//
//	public Advert queryAdvert(Map<String, Object> advertMap) {
//		return advertDao.queryAdvert(advertMap);
//	}
//
//	public Page<Advert> pageQuery(Map<String, Object> paramMap) {
//		Page<Advert> advertPage = new Page<Advert>((Integer)paramMap.get("pageno"),(Integer)paramMap.get("pagesize"));
//		paramMap.put("startIndex", advertPage.getStartIndex());
//		List<Advert> advertList= advertDao.pageQuery(paramMap);
//		// 获取数据的总条数
//		int count = advertDao.queryCount(paramMap);
//
//		advertPage.setData(advertList);
//		advertPage.setTotalsize(count);
//		return advertPage;
//	}
//
//	public int queryCount(Map<String, Object> advertMap) {
//		return advertDao.queryCount(advertMap);
//	}
//
	public int insertAdvert(Advert advert) {
		return advertDao.insert(advert);
	}

	public List<Advert> pageQuery(String pagetext) {
		return advertDao.pageQuery(pagetext);
	}

	public Advert queryById(Integer id) {
		return advertDao.selectByPrimaryKey(id);
	}

	public int updateAdvert(Advert advert) {
		return advertDao.updateByPrimaryKey(advert);
	}

	public int deleteAdvert(Integer id) {
		return advertDao.deleteByPrimaryKey(id);
	}

	public int deleteAdverts(Data ds) {
		int count=0;
		List<Integer> advertIds=ds.getIds();
		for (Integer advertId : advertIds) {
			advertDao.deleteByPrimaryKey(advertId);
			count++;
		}
		return count;
	}
//
//	public Advert queryById(Integer id) {
//		return advertDao.queryById(id);
//	}
//
//	public int updateAdvert(Advert advert) {
//		return advertDao.updateAdvert(advert);
//	}
//
//	public int deleteAdvert(Integer id) {
//		return advertDao.deleteAdvert(id);
//	}
//
//	public int deleteAdverts(Data ds) {
//		return advertDao.deleteAdverts(ds);
//	}

}
