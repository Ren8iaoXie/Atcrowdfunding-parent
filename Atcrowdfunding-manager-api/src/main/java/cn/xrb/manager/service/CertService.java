package cn.xrb.manager.service;

import cn.xrb.domain.Cert;
import cn.xrb.domain.MemberCert;
import cn.xrb.util.Data;

import java.util.List;

/**
 * @author xieren8iao
 * @create 2019/11/6 - 19:11
 */
public interface CertService {
    List<Cert> pageQuery(String name);

    int deleteByPrimaryKey(Integer id);

    int insert(Cert record);

    Cert selectByPrimaryKey(Integer id);

    List<Cert> selectAll();

    int updateByPrimaryKey(Cert record);

    Cert queryById(Integer id);

    int updateCert(Cert cert);

    int deleteCerts(Data ds);

    List<Cert> queryAllCert();

    List<Cert> queryCertByAccttype(String accttype);

    void saveMemberCert(List<MemberCert> certimgs);
}
