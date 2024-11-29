package egovframework.example.sample.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import egovframework.example.sample.service.UserService;
import egovframework.example.sample.service.UserVO;

@Service("userService")
public class UserServiceImpl extends EgovAbstractServiceImpl implements UserService {

	@Resource(name="userMapper")
	private UserMapper userDAO;
	
	// 이메일 체크
	@Override
	public int emailCheck(String email) throws Exception {
		int resultVO = userDAO.emailCheck(email);
//		if (resultVO == null)
//			throw processException("info.nodata.msg");
		return resultVO;
	}
	
	// 회원가입
	@Override
	public String register(UserVO vo) throws Exception {
		userDAO.register(vo);
		return "회원가입 성공!";
	}
	
	// 로그인
	@Override
	public Map<String, Object> login(UserVO vo) throws Exception {
		Map<String, Object> resultVO = userDAO.login(vo);
		if (resultVO == null)
			throw processException("info.nodata.msg");
		return resultVO;
	}
}
