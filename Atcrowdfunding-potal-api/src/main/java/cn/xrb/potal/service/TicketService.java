package cn.xrb.potal.service;


import cn.xrb.domain.Member;
import cn.xrb.domain.Ticket;

public interface TicketService {

	Ticket getTicketByMemberId(Integer id);

	void saveTicket(Ticket ticket);

	void updatePstep(Ticket ticket);

	void updatePiidAndPstep(Ticket ticket);

    Member getMemberByPiid(String processInstanceId);

    void updateStatus(Member member);
}
