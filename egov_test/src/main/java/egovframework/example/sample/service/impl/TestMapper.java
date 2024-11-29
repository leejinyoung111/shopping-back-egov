package egovframework.example.sample.service.impl;

import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.example.sample.service.TestVO;

@Mapper("testMapper")
public interface TestMapper {
	
	// 비밀번호 암호화 테스트
	void insertUser(TestVO vo) throws Exception;
	
	// 비밀번호 복호화 테스트
	Map<String, Object> getUser(TestVO vo) throws Exception;

}
