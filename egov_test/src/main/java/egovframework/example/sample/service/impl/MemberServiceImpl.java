package egovframework.example.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.example.sample.service.MemberService;

@Service("memberService")
public class MemberServiceImpl  implements MemberService {
	
@Autowired private MemberMapper membermapper;
	
	@Override
	public int select_membercount() throws Exception {
		return membermapper.select_membercount();
	}
	
	@Override
	public String selectName() throws Exception {
		return membermapper.selectName();
	}

}
