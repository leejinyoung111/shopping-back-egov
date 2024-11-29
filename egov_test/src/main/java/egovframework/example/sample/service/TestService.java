package egovframework.example.sample.service;

import java.util.Map;

public interface TestService {
	
	// 비밀번호 암호화 테스트
	String insertUser(TestVO vo) throws Exception;
	
	// 비밀번호 복호화 테스트
	Map<String, Object> getUser(TestVO vo) throws Exception;

}
