package egovframework.example.sample.service;

import java.util.Map;

public interface MemberService {
	
	// 이메일 체크
	int testEmail(String email) throws Exception;
	
	// 회원가입
	String insertMember(MemberVO vo) throws Exception;
	
	// 로그인
	Map<String, Object> loginMember(MemberVO vo) throws Exception;
	

}
