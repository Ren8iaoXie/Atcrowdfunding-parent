package cn.xrb.manager.service.impl;

import cn.xrb.domain.Cert;
import cn.xrb.domain.MemberCert;
import cn.xrb.manager.dao.CertMapper;
import cn.xrb.manager.service.CertService;
import cn.xrb.util.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xieren8iao
 * @create 2019/11/6 - 19:12
 */
@Service
public class CertServiceImpl implements CertService {
    @Autowired
    private CertMapper certMapper;

    public List<Cert> pageQuery(String pagetext) {
        return certMapper.pageQuery(pagetext);
    }

    public int deleteByPrimaryKey(Integer id) {
        return certMapper.deleteByPrimaryKey(id);
    }

    public int insert(Cert record) {
        return certMapper.insert(record);
    }

    public Cert selectByPrimaryKey(Integer id) {
        return certMapper.selectByPrimaryKey(id);
    }

    public List<Cert> selectAll() {
        return certMapper.selectAll();
    }

    public int updateByPrimaryKey(Cert record) {
        return certMapper.updateByPrimaryKey(record);
    }

    public Cert queryById(Integer id) {
        return certMapper.selectByPrimaryKey(id);
    }

    public int updateCert(Cert cert) {
        return certMapper.updateByPrimaryKey(cert);
    }

    public int deleteCerts(Data ds) {
        int count=0;
        List<Integer> certIds=ds.getIds();
        for (Integer certId : certIds) {
            certMapper.deleteByPrimaryKey(certId);
            count++;
        }
        return count;
    }

    @Override
    public List<Cert> queryAllCert() {
        return certMapper.selectAll();
    }

    @Override
    public List<Cert> queryCertByAccttype(String accttype) {
        return certMapper.queryCertByAccttype(accttype);
    }

    @Override
    public void saveMemberCert(List<MemberCert> certimgs) {
        for (MemberCert certimg : certimgs) {
            certMapper.insertMemberCert(certimg);
        }

    }
}
