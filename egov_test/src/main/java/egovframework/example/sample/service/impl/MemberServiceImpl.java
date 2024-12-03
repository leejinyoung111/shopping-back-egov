package egovframework.example.sample.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.example.sample.service.MemberService;
import egovframework.example.sample.service.MemberVO;

@Service("memberService")
public class MemberServiceImpl extends EgovAbstractServiceImpl implements MemberService {

	
	@Resource(name="memberMapper")
	private MemberMapper memberDAO;
	
	// 이메일 체크
	@Override
	public int testEmail(String email) throws Exception {
		int resultVO = memberDAO.testEmail(email);
		return resultVO;
	}
	
	// 회원가입
	@Override
	public String insertMember(MemberVO vo) throws Exception {
		memberDAO.insertMember(vo);
		return "회원가입 성공!";
	}
	
	// 로그인
	@Override
	public Map<String, Object> loginMember(MemberVO vo) throws Exception {
		Map<String, Object> resultVO = memberDAO.loginMember(vo);
		if (resultVO == null)
			throw processException("info.nodata.msg");
		return resultVO;
	}
	
	
}
