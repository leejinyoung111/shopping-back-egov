package egovframework.example.sample.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.example.sample.service.MemberVO;

@Mapper("memberMapper")
public interface MemberMapper {
	
	// 이메일 체크
	int testEmail(String email) throws Exception;
	
	// 회원가입
	void insertMember(MemberVO vo) throws Exception;
	
	// 로그인
	Map<String, Object> loginMember(MemberVO vo) throws Exception;
	
	// 리스트 조회
	List<MemberVO> buytList(int userId) throws Exception;

}
