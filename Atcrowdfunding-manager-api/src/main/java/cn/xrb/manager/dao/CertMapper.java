package cn.xrb.manager.dao;

import cn.xrb.domain.Cert;
import cn.xrb.domain.MemberCert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cert record);

    Cert selectByPrimaryKey(Integer id);

    List<Cert> selectAll();

    int updateByPrimaryKey(Cert record);

    List<Cert> pageQuery(@Param("name") String name);

    List<Cert> queryCertByAccttype(String accttype);

    void insertMemberCert(MemberCert certimg);
}