package egovframework.example.sample.service.impl;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import egovframework.example.sample.service.TestService;
import egovframework.example.sample.service.TestVO;

@Service("testService")
public class TestServiceImpl extends EgovAbstractServiceImpl implements TestService{
	
	@Resource(name="testMapper")
	private TestMapper testDAO;
	
	// 비밀번호 테스트
	@Override
	public String insertUser(TestVO vo) throws Exception{
		testDAO.insertUser(vo);
		return "비밀번호 테스트 성공!";
	};
	
}
