package main.service.impl;

import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import main.service.UserVO;

@Mapper("userMapper")
public interface UserMapper {

	// 이메일 체크
	int emailCheck(String email) throws Exception;
	
	// 회원가입
	void register(UserVO vo) throws Exception;
	
	// 로그인
	Map<String, Object> login(UserVO vo) throws Exception;
	
	// 유저 정보 수정
	void updateUser(UserVO vo) throws Exception;

}
