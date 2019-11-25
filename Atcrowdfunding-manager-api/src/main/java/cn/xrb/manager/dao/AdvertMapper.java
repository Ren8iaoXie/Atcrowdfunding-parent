package cn.xrb.manager.dao;

import cn.xrb.domain.Advert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdvertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Advert record);

    Advert selectByPrimaryKey(Integer id);

    List<Advert> selectAll();

    int updateByPrimaryKey(Advert record);

    List<Advert> pageQuery(@Param("name") String name);
}