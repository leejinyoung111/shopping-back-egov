/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.example.sample.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import egovframework.example.sample.service.CartService;
import egovframework.example.sample.service.CartVO;
import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.SampleVO;
import egovframework.example.sample.service.TestService;
import egovframework.example.sample.service.TestVO;
import egovframework.example.sample.service.UserService;
import egovframework.example.sample.service.UserVO;

import org.egovframe.rte.fdl.cryptography.EgovEnvCryptoService;
import org.egovframe.rte.fdl.cryptography.EgovPasswordEncoder;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springmodules.validation.commons.DefaultBeanValidator;

/**
 * @Class Name : EgovSampleController.java
 * @Description : EgovSample Controller Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

// cors 선언
@CrossOrigin(origins = "*")

// rest api 선언
@RestController
public class EgovSampleController {

	/** EgovSampleService */
	@Resource(name = "sampleService")
	private EgovSampleService sampleService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;

	 
	@Autowired
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "cartService")
	private CartService cartService;
	
	@Resource(name = "testService")
	private TestService testService;
	
	
	
    // 메인 페이지
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces="application/json;charset=utf-8")
	public String test() throws Exception {
		return "테스트 페이지";
		
	}
	
	// 비밀번호 암호화 테스트
	@RequestMapping(value = "/insertUser", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public String insertUser(@RequestBody TestVO vo) throws Exception{
		try {
			
			// 인코더 선언
			EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
			
			// 비밀번호 암호화
			String hashed = egovPasswordEncoder.encryptPassword(vo.getPassword());
			
			// 암호화된 비밀번호로 바꾸기
			vo.setPassword(hashed);	
			
			testService.insertUser(vo);
			
			return "비밀번호 테스트중!";
			
		} catch (Exception e) {
			System.out.println("발생 오류:" + e);
			return "발생 오류:" + e;
		}
	}
	
	// 비밀번호 복호화 테스트
	@RequestMapping(value = "/getUser", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public String getUser(@RequestBody TestVO vo) throws Exception{
		try {
			
			// 인코더 선언
			EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
			
			// 유저 정보 가져오기
			Map<String, Object> getUser = testService.getUser(vo);
			
			// 비밀번호 일치 여부
			Boolean result = egovPasswordEncoder.checkPassword(vo.getPassword(), (String) getUser.get("password"));
			
			if (result) {
				return "로그인 성공!";
			} else {
				return "비밀번호가 다릅니다.";
			}
			
		} catch (Exception e) {
            e.printStackTrace();
            return "오류 발생";
		}
	}
	
	
	// 회원가입 기능
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public String register(@RequestBody UserVO vo) {
		try {
			
			// 인코더 선언
			EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
			
			// 비밀번호 암호화
			String hashed = egovPasswordEncoder.encryptPassword(vo.getPassword());
			
			// 이메일 체크
			int isEmail = userService.emailCheck(vo.getEmail());
			
			if (isEmail == 0) {
				// 일치하는 이메일이 없는 경우
				
				// 암호화된 비밀번호로 바꾸기
				vo.setPassword(hashed);
				
				userService.register(vo);
				
				return "회원가입 성공!";
			} else {
				// 일치하는 이메일이 있는 경우
				
				System.out.println("존재하는 이메일입니다.");
				return "존재하는 이메일입니다.";
			}
			
		} catch (Exception e) {
			System.out.println("오류 발생:" + e);
			return "오류 발생:" + e;
		}
	}
	
	// 로그인 기능
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public Map<String, Object>  login(@RequestBody UserVO vo) {
		try {
			
			// 해시맵 선언
			Map<String, Object> response = new HashMap<>();
			
			// 인코더 선언
			EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
			
			// 이메일 체크
			int isEmail = userService.emailCheck(vo.getEmail());
			
			if (isEmail == 0) {
				// 일치하는 이메일이 없는 경우
	            Map<String, Object> errorResponse = new HashMap<>();
	            errorResponse.put("오류 발생", "일치하는 이메일이 없습니다.");
	            
	            return errorResponse;

			} else {
				// 일치하는 이메일이 있는 경우
				
				// 유저 정보 가져오기
				Map<String, Object> login = userService.login(vo);
				
				// 비밀번호 일치 여부
				Boolean result = egovPasswordEncoder.checkPassword(vo.getPassword(), (String) login.get("password"));
				
				if (result) {
					
					// 비밀번호 일치할 경우
					 Map<String, Object> getUser = new HashMap<>();
					 
					 getUser.put("user", login);
					
					System.out.println(getUser);
					
					return getUser;
				} else {
					
					// 비밀번호 일치하지 않을 경우
		            Map<String, Object> errorResponse = new HashMap<>();
		            
		            errorResponse.put("오류 발생", "비밀번호가 일치하지 않습니다.");
		            
		            return errorResponse;
		            
				}
				
			}
			
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "유저정보를 가져오는 중 오류가 발생했습니다.");
            return errorResponse;
		}

	}
	
    // 장바구니 목록 조회
	@RequestMapping(value = "/cart/{userId}", method = RequestMethod.GET, produces="application/json;charset=utf-8")
	public Map<String, Object> cartList(@PathVariable("userId") int userId) throws Exception {
		List<CartVO> getCartList = cartService.cartList(userId);
		Map<String, Object> response = new HashMap<>();
		response.put("getCartList", getCartList);
	        
		return response;
	}
	
	// 장바구니 추가 기능
	@RequestMapping(value = "/addCart", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public String insertCart(@RequestBody CartVO vo) throws Exception{
		try {
			cartService.insertCart(vo);
			return "장바구니 추가 성공!";
			
		} catch (Exception e) {
			System.out.println("발생 오류:" + e);
			return "발생 오류:" + e;
		}
	}

    // 장바구니 삭제 기능
	@RequestMapping(value = "/cart/{id}", method = RequestMethod.DELETE, produces="application/json;charset=utf-8")
	public String deleteCart(@PathVariable("id") int id) throws Exception {
		
		try {
			cartService.deleteCart(id);
			return "장바구니 삭제 성공!";
			
		} catch (Exception e) {
			System.out.println("발생 오류:" + e);
			return "발생 오류:" + e;
		}

	}
	

	/**
	 * 글 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/egovSampleList.do")
	public String selectSampleList(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {

		/** EgovPropertyService.sample */
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<?> sampleList = sampleService.selectSampleList(searchVO);
		model.addAttribute("resultList", sampleList);

		int totCnt = sampleService.selectSampleListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		return "sample/egovSampleList";
	}

	/**
	 * 글 등록 화면을 조회한다.
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */
	@RequestMapping(value = "/addSample.do", method = RequestMethod.GET)
	public String addSampleView(@ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model) throws Exception {
		model.addAttribute("sampleVO", new SampleVO());
		return "sample/egovSampleRegister";
	}

	/**
	 * 글을 등록한다.
	 * @param sampleVO - 등록할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/egovSampleList.do"
	 * @exception Exception
	 */
	@RequestMapping(value = "/addSample.do", method = RequestMethod.POST)
	public String addSample(@ModelAttribute("searchVO") SampleDefaultVO searchVO, SampleVO sampleVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {

		// Server-Side Validation
		beanValidator.validate(sampleVO, bindingResult);

		if (bindingResult.hasErrors()) {
			model.addAttribute("sampleVO", sampleVO);
			return "sample/egovSampleRegister";
		}

		sampleService.insertSample(sampleVO);
		status.setComplete();
		return "forward:/egovSampleList.do";
	}

	/**
	 * 글 수정화면을 조회한다.
	 * @param id - 수정할 글 id
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */
	@RequestMapping("/updateSampleView.do")
	public String updateSampleView(@RequestParam("selectedId") String id, @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model) throws Exception {
		SampleVO sampleVO = new SampleVO();
		sampleVO.setId(id);
		// 변수명은 CoC 에 따라 sampleVO
		model.addAttribute(selectSample(sampleVO, searchVO));
		return "sample/egovSampleRegister";
	}

	/**
	 * 글을 조회한다.
	 * @param sampleVO - 조회할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return @ModelAttribute("sampleVO") - 조회한 정보
	 * @exception Exception
	 */
	public SampleVO selectSample(SampleVO sampleVO, @ModelAttribute("searchVO") SampleDefaultVO searchVO) throws Exception {
		return sampleService.selectSample(sampleVO);
	}

	/**
	 * 글을 수정한다.
	 * @param sampleVO - 수정할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/egovSampleList.do"
	 * @exception Exception
	 */
	@RequestMapping("/updateSample.do")
	public String updateSample(@ModelAttribute("searchVO") SampleDefaultVO searchVO, SampleVO sampleVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {

		beanValidator.validate(sampleVO, bindingResult);

		if (bindingResult.hasErrors()) {
			model.addAttribute("sampleVO", sampleVO);
			return "sample/egovSampleRegister";
		}

		sampleService.updateSample(sampleVO);
		status.setComplete();
		return "forward:/egovSampleList.do";
	}

	/**
	 * 글을 삭제한다.
	 * @param sampleVO - 삭제할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/egovSampleList.do"
	 * @exception Exception
	 */
	@RequestMapping("/deleteSample.do")
	public String deleteSample(SampleVO sampleVO, @ModelAttribute("searchVO") SampleDefaultVO searchVO, SessionStatus status) throws Exception {
		sampleService.deleteSample(sampleVO);
		status.setComplete();
		return "forward:/egovSampleList.do";
	}

}
