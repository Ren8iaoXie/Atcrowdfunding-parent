package cn.xrb.potal.dao;


import cn.xrb.domain.Member;
import cn.xrb.domain.Ticket;

import java.util.List;

public interface TicketMapper {
    int deleteByPrimaryKey(Integer id);

    Ticket selectByPrimaryKey(Integer id);

    List<Ticket> selectAll();

	Ticket getTicketByMemberId(Integer memberid);

	void saveTicket(Ticket ticket);

	void updatePstep(Ticket ticket);

	void updatePiidAndPstep(Ticket ticket);

    Member getMemberByPiid(String processInstanceId);

    void updateStatus(Member member);
}