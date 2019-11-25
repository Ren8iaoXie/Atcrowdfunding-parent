package cn.xrb.util;

import cn.xrb.domain.MemberCert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieren8iao
 * @create 2019/10/11 - 22:46
 */
public class Data {
    private List<Integer> ids;

    private List<MemberCert> certimgs = new ArrayList<MemberCert>();

    public List<MemberCert> getCertimgs() {
        return certimgs;
    }

    public void setCertimgs(List<MemberCert> certimgs) {
        this.certimgs = certimgs;
    }

    public List<Integer> getIds() {
        return ids;
    }



    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
