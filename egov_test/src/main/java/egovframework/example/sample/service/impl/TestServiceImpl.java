package egovframework.example.sample.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import egovframework.example.sample.service.TestService;
import egovframework.example.sample.service.TestVO;
import egovframework.example.sample.service.UserVO;

@Service("testService")
public class TestServiceImpl extends EgovAbstractServiceImpl implements TestService{
	
	@Resource(name="testMapper")
	private TestMapper testDAO;
	
	// 비밀번호 암호화 테스트
	@Override
	public String insertUser(TestVO vo) throws Exception{
		testDAO.insertUser(vo);
		return "비밀번호 테스트 성공!";
	};
	
	// 비밀번호 복호화 테스트
	@Override
	public Map<String, Object> getUser(TestVO vo) throws Exception {
		Map<String, Object> resultVO = testDAO.getUser(vo);
		if (resultVO == null)
			throw processException("info.nodata.msg");
		return resultVO;
	}
	
}
