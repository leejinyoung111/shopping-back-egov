package egovframework.example.sample.service;

import java.util.Map;

public interface UserService {
	
	// 회원가입
	String register(UserVO vo) throws Exception;
	
	// 로그인
	Map<String, Object> login(UserVO vo) throws Exception;
	
}
