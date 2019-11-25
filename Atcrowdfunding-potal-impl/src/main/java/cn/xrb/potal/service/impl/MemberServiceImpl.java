package cn.xrb.potal.service.impl;

import cn.xrb.domain.Member;
import cn.xrb.potal.dao.MemberMapper;
import cn.xrb.potal.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xieren8iao
 * @create 2019/11/9 - 15:41
 */
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    public int insert(Member record) {
        return 0;
    }

    public Member selectByPrimaryKey(Integer id) {
        return null;
    }

    public List<Member> selectAll() {
        return null;
    }

    public int updateByPrimaryKey(Member record) {
        return 0;
    }

    public Member getMemberByUP(Member member) {
        Member loginMember = memberMapper.getMemberByUP(member);
        if (loginMember!=null){
            return loginMember;
        }
        else
        return null;
    }

    public void updateAcctType(Member loginMember) {
        memberMapper.updateAcctType(loginMember);
    }

    public void updateBasicinfo(Member loginMember) {
        memberMapper.updateBasicinfo(loginMember);
    }

    public void updateEmail(Member loginMember) {

    }

    public void updateAuthstatus(Member loginMember) {
        memberMapper.updateAuthstatus(loginMember);
    }

    @Override
    public Member getMemberById(Integer memberid) {
        return memberMapper.selectByPrimaryKey(memberid);
    }

    @Override
    public List<Map<String, Object>> queryCertByMemberid(Integer memberid) {
        return memberMapper.queryCertByMemberid(memberid);
    }
}
