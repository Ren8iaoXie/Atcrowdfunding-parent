package cn.xrb.manager.service;

import cn.xrb.domain.Advert;
import cn.xrb.util.Data;

import java.util.List;

public interface AdvertService {
//	public Advert queryAdvert(Map<String, Object> advertMap);
//
//	public Page<Advert> pageQuery(Map<String, Object> advertMap);
//
//	public int queryCount(Map<String, Object> advertMap);
//
	public int insertAdvert(Advert advert);

    List<Advert> pageQuery(String pagetext);
//
	public Advert queryById(Integer id);

	public int updateAdvert(Advert advert);

	public int deleteAdvert(Integer id);

	public int deleteAdverts(Data ds);
}
