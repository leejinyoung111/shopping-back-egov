package egovframework.example.sample.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.example.sample.service.TestVO;

@Mapper("testMapper")
public interface TestMapper {
	
	// 비밀번호 테스트
	void insertUser(TestVO vo) throws Exception;

}
