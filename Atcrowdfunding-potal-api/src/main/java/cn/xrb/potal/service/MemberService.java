package cn.xrb.potal.service;

import cn.xrb.domain.Member;

import java.util.List;
import java.util.Map;

/**
 * @author xieren8iao
 * @create 2019/11/9 - 15:28
 */
public interface MemberService {
    int deleteByPrimaryKey(Integer id);

    int insert(Member record);

    Member selectByPrimaryKey(Integer id);

    List<Member> selectAll();

    int updateByPrimaryKey(Member record);

    Member getMemberByUP(Member member);

    void updateAcctType(Member loginMember);

    void updateBasicinfo(Member loginMember);

    void updateEmail(Member loginMember);

    void updateAuthstatus(Member loginMember);

    Member getMemberById(Integer memberid);

    List<Map<String, Object>> queryCertByMemberid(Integer memberid);
}
