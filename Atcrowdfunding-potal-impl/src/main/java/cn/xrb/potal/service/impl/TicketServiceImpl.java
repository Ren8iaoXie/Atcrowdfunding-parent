package cn.xrb.potal.service.impl;

import cn.xrb.domain.Member;
import cn.xrb.domain.Ticket;
import cn.xrb.potal.dao.TicketMapper;
import cn.xrb.potal.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketMapper ticketMapper ;

	@Override
	public Ticket getTicketByMemberId(Integer id) {
		return ticketMapper.getTicketByMemberId(id);
	}

	@Override
	public void saveTicket(Ticket ticket) {
		ticketMapper.saveTicket(ticket);
	}

	@Override
	public void updatePstep(Ticket ticket) {
		ticketMapper.updatePstep(ticket);
	}

	@Override
	public void updatePiidAndPstep(Ticket ticket) {
		ticketMapper.updatePiidAndPstep(ticket);
	}

	@Override
	public Member getMemberByPiid(String processInstanceId) {
		return ticketMapper.getMemberByPiid(processInstanceId);
	}

	@Override
	public void updateStatus(Member member) {
		ticketMapper.updateStatus(member);
	}

}
