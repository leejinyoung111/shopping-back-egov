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
import egovframework.example.sample.service.CartUpdateVO;
import egovframework.example.sample.service.CartVO;
import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.ErrorCode;
import egovframework.example.sample.service.JwtService;
import egovframework.example.sample.service.MemberService;
import egovframework.example.sample.service.MemberVO;
import egovframework.example.sample.service.ResultService;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.SampleVO;
import egovframework.example.sample.service.SuccessCode;
import egovframework.example.sample.service.TokenVO;
import egovframework.example.sample.service.UserService;
import egovframework.example.sample.service.UserVO;
import io.jsonwebtoken.Claims;

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
	
	@Resource(name = "memberService")
	private MemberService memberService;
	
	@Resource(name = "resultService")
	private ResultService resultService;
	
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "cartService")
	private CartService cartService;
	
	@Resource(name = "jwtService")
	private JwtService jwtService;
	


	
    // 메인 페이지
	@RequestMapping(value = "/main", method = RequestMethod.GET, produces="application/json;charset=utf-8")
	public String main() throws Exception {
		
			
		return "메인 페이지";
		
	}
	
	// 회원가입 에러처리 테스트
	@RequestMapping(value = "/errorInsertMember", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public Map<String, Object> errorInsertMember(@RequestBody MemberVO vo) throws Exception {

		try {
			
			// 해시맵 선언
			Map<String, Object> testHashMap = new HashMap<>();
			
			// 인코더 선언
			EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
			
			// 이메일 체크
			int isEmail = memberService.testEmail(vo.getEmail());
			
			// 비밀번호 길이
			int passwordLen = vo.getPassword().length();
			
	        if(isEmail == 0) {
				// 일치하는 이메일이 없는 경우
	        	
	        	if (passwordLen >= 8 && passwordLen <= 16) {
	        		// 비밀번호 길이 체크
	        		
	    			// 비밀번호 암호화
	    			String hashed = egovPasswordEncoder.encryptPassword(vo.getPassword());
	        		
					// 암호화된 비밀번호로 바꾸기
					vo.setPassword(hashed);
					
					// 회원가입
					memberService.insertMember(vo);
					
		        	SuccessCode successCode = SuccessCode.REGISTER;
		        	testHashMap.put("test", "회원가입 성공");
		        	
		        	Map<String, Object> result =  resultService.successResult(successCode, testHashMap);
		        	
		        	
		            return result;
	        		
	        	} else {
	        		
		        	ErrorCode errorCode = ErrorCode.PASSWORD_LENGTH;
		        	testHashMap.put("test", "비밀번호 길이 오류");
		        	
		        	Map<String, Object> result =  resultService.errorResult(errorCode, testHashMap);
		       
		        	
			        return result;
			        
	        	}

	        } else {
	        	// 일치하는 이메일이 있는 경우
	        	
	        	ErrorCode errorCode = ErrorCode.DUPLICATE_EMAIL;
	        	testHashMap.put("test", "이메일 있음");
	        	
	        	Map<String, Object> result =  resultService.errorResult(errorCode, testHashMap);

		        return result;
	        }
			
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "오류가 발생했습니다.");
            return errorResponse;
		}
	}
	
	// 로그인 에러처리 테스트
	@RequestMapping(value = "/loginMember", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public Map<String, Object> errorLoginMember(@RequestBody MemberVO vo) throws Exception {
		try {
			
			// 해시맵 선언
			Map<String, Object> testHashMap = new HashMap<>();
			
			
			// 인코더 선언
			EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
			
			// 이메일 체크
			int isEmail = memberService.testEmail(vo.getEmail());
			
			if (isEmail == 0) {
				
	        	ErrorCode errorCode = ErrorCode.EMAIL_NOT_FOUND;
	        	testHashMap.put("test", "이메일 없음");
	        	
	        	Map<String, Object> result =  resultService.errorResult(errorCode, testHashMap);
	        	
	            return result;

			} else {
				// 일치하는 이메일이 있는 경우
				
				// 유저 정보 가져오기
				Map<String, Object> user = memberService.loginMember(vo);
				
				// 비밀번호 일치 여부
				Boolean passwordCheck = egovPasswordEncoder.checkPassword(vo.getPassword(), (String) user.get("password"));
				
				if (passwordCheck) {
					
					// 비밀번호 일치할 경우
					 
					 SuccessCode successCode = SuccessCode.LOGIN;
					 testHashMap.put("test", "로그인 성공");
					 
					 Map<String, Object> result =  resultService.successResult(successCode, testHashMap);
					 
					 return result;
				} else {
					// 비밀번호 일치하지 않을 경우
					
		        	ErrorCode errorCode = ErrorCode.PASSWORD_NOT_COMPARE;
		        	
				   	testHashMap.put("test", "비밀번호 다름");
		        	
		        	Map<String, Object> result =  resultService.errorResult(errorCode, testHashMap);
		        	
		            return result;
				}
			}
			
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "유저정보 확인 중 오류가 발생했습니다.");
            return errorResponse;
		}
	}
	
	// 유저 정보 가져오기 에러처리 테스트
	@RequestMapping(value = "/getErrorMember", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public Map<String, Object> getErrorMember(@RequestBody TokenVO token) throws Exception {
		try {
			
			// 해시맵 선언
			Map<String, Object> testHashMap = new HashMap<>();
			
			// 토큰으로 유저 정보 가져오기
			Claims userInfo = jwtService.getData(token.getToken());
			
			 SuccessCode successCode = SuccessCode.GET_USER;
			 testHashMap.put("userInfo", userInfo);
			 
			 Map<String, Object> result =  resultService.successResult(successCode, testHashMap);
			 
			 return result;

		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "유저정보를 가져오는 중 오류가 발생했습니다.");
            return errorResponse;
		}

	}
	
	// 회원가입
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public Map<String, Object> register(@RequestBody UserVO vo) throws Exception {
		try {
			
			// 해시맵 선언
			Map<String, Object> dataHashMap = new HashMap<>();
			
			// 인코더 선언
			EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
			
			// 이메일 체크
			int isEmail = userService.emailCheck(vo.getEmail());
			
			// 비밀번호 길이
			int passwordLen = vo.getPassword().length();
			
			if (isEmail == 0) {
				// 일치하는 이메일이 없는 경우
				
				if (passwordLen >= 8 && passwordLen <= 16) {
	        		// 비밀번호 길이 맞는 경우
	        		
	    			// 비밀번호 암호화
	    			String hashed = egovPasswordEncoder.encryptPassword(vo.getPassword());
	        		
					// 암호화된 비밀번호로 바꾸기
					vo.setPassword(hashed);
					
					// 회원가입
					userService.register(vo);
					
					// 결과 전달
		        	SuccessCode successCode = SuccessCode.REGISTER;
		        	dataHashMap.put("data", "회원가입 성공");
		        	Map<String, Object> result = resultService.successResult(successCode, dataHashMap);
		        	
		            return result;
	        		
	        	} else {
	        		// 비밀번호 길이 다른 경우
	        		
					// 결과 전달
		        	ErrorCode errorCode = ErrorCode.PASSWORD_LENGTH;
		        	dataHashMap.put("data", "비밀번호 길이 오류");
		        	Map<String, Object> result = resultService.errorResult(errorCode, dataHashMap);

			        return result;
	        	}
				
			} else {		
	        	// 일치하는 이메일이 있는 경우
				
				// 결과 전달
	        	ErrorCode errorCode = ErrorCode.DUPLICATE_EMAIL;
	        	dataHashMap.put("data", "이메일이 존재함");
	        	Map<String, Object> result = resultService.errorResult(errorCode, dataHashMap);

		        return result;
			}
			
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            Map<String, Object> errorHashMap = new HashMap<>();
            errorHashMap.put("error", "오류가 발생했습니다.");
            return errorHashMap;
		}
	}
	
	// 로그인
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public Map<String, Object> login(@RequestBody UserVO vo) throws Exception {
		try {
			
			// 해시맵 선언
			Map<String, Object> dataHashMap = new HashMap<>();
			
			// 인코더 선언
			EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
			
			// 이메일 체크
			int isEmail = userService.emailCheck(vo.getEmail());
			
			if (isEmail == 0) {
				// 일치하는 이메일이 없는 경우
				
				// 결과 전달
	        	ErrorCode errorCode = ErrorCode.EMAIL_NOT_FOUND;
	        	dataHashMap.put("data", "일치하는 이메일 없음");
	        	Map<String, Object> result = resultService.errorResult(errorCode, dataHashMap);
	        	
	            return result;

			} else {
	        	// 일치하는 이메일이 있는 경우

				// 유저 정보 가져오기
				Map<String, Object> user = userService.login(vo);
				
				// 비밀번호 체크
				Boolean passwordCheck = egovPasswordEncoder.checkPassword(vo.getPassword(), (String) user.get("password"));
				
				if (passwordCheck) {
					// 비밀번호 일치할 경우
					
					String accessToken =  jwtService.createJwt(vo);
					
					// 결과 전달
					SuccessCode successCode = SuccessCode.LOGIN;
					dataHashMap.put("accessToken", accessToken);
					Map<String, Object> result = resultService.successResult(successCode, dataHashMap);
					 
					 return result;
				} else {
					// 비밀번호 일치하지 않을 경우
					
					// 결과 전달
		        	ErrorCode errorCode = ErrorCode.PASSWORD_NOT_COMPARE;
		        	dataHashMap.put("data", "비밀번호 다름");
		        	Map<String, Object> result = resultService.errorResult(errorCode, dataHashMap);
		        	
		            return result;
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            Map<String, Object> errorHashMap = new HashMap<>();
            errorHashMap.put("error", "오류가 발생했습니다.");
            return errorHashMap;
		}

	}
	
	// 유저 정보 가져오기
	@RequestMapping(value = "/getUser", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public Map<String, Object> getUser(@RequestBody TokenVO token) throws Exception {
		try {
			
			// 해시맵 선언
			Map<String, Object> dataHashMap = new HashMap<>();
			
			// 토큰으로 유저 정보 가져오기
			Claims userInfo =  jwtService.getData(token.getToken());
			
			// 결과 전달
			SuccessCode successCode = SuccessCode.GET_USER;
			dataHashMap.put("userInfo", userInfo);
			Map<String, Object> result = resultService.successResult(successCode, dataHashMap);
			 
			return result;

		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            Map<String, Object> errorHashMap = new HashMap<>();
            errorHashMap.put("error", "오류가 발생했습니다.");
            return errorHashMap;
		}

	}
	
	// 유저정보 변경
	@RequestMapping(value = "/updateUser", method = RequestMethod.PATCH, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public Map<String, Object> updateUser(@RequestBody UserVO vo) throws Exception{
		try {
			
			// 해시맵 선언
			Map<String, Object> response = new HashMap<>();
			
			// 인코더 선언
			EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
			
			// 비밀번호 암호화
			String hashed = egovPasswordEncoder.encryptPassword(vo.getPassword());
			
			// 암호화된 비밀번호로 바꾸기
			vo.setPassword(hashed);
			
			// 정보 변경
			userService.updateUser(vo);
			
			// 새로운 토큰 발급
			String accessToken = jwtService.createJwt(vo);
		 
			response.put("accessToken", accessToken);

			return response;

			
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "정보 수정을 하는 중 오류가 발생했습니다.");
            return errorResponse;
		}
	}
	
    // 장바구니 목록 조회
	@RequestMapping(value = "/cart/{userId}", method = RequestMethod.GET, produces="application/json;charset=utf-8")
	public Map<String, Object> cartList(@PathVariable("userId") int userId) throws Exception {
		try {
			
			// 해시맵 선언
			Map<String, Object> dataHashMap = new HashMap<>();
			
			// 장바구니 목록 가져오기
			List<CartVO> getCartList = cartService.cartList(userId);
			
			// 결과 전달
			SuccessCode successCode = SuccessCode.GET_CART_LIST;
			dataHashMap.put("getCartList", getCartList);
			Map<String, Object> result = resultService.successResult(successCode, dataHashMap);
			
			return result;
			
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            Map<String, Object> errorHashMap = new HashMap<>();
            errorHashMap.put("error", "오류가 발생했습니다.");
            return errorHashMap;
		}
	}
	
	// 장바구니 추가
	@RequestMapping(value = "/addCart", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public Map<String, Object> insertCart(@RequestBody CartVO vo) throws Exception{
		try {
			
			// 해시맵 선언
			Map<String, Object> dataHashMap = new HashMap<>();
			
			// 상품 체크
			int isProduct = cartService.productCheck(vo);
			
			if (isProduct == 0) {
				// 장바구니에 없는 경우
				
				// 장바구니 추가
				cartService.insertCart(vo);
				
				// 결과 전달
				SuccessCode successCode = SuccessCode.ADD_CART;
				dataHashMap.put("data", "장바구니 추가 성공");
				Map<String, Object> result = resultService.successResult(successCode, dataHashMap);
				 
				 return result;
			} else {
				// 장바구니에 있는 경우
				
				// 결과 전달
	        	ErrorCode errorCode = ErrorCode.DUPLICATE_PRODUCT;
	        	dataHashMap.put("data", "일치하는 상품 있음");
	        	Map<String, Object> result = resultService.errorResult(errorCode, dataHashMap);
	        	
	            return result;
			}
			
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            Map<String, Object> errorHashMap = new HashMap<>();
            errorHashMap.put("error", "오류가 발생했습니다.");
            return errorHashMap;
		}
	}

	// 상품 수량 변경
	@RequestMapping(value = "/updateProductCount", method = RequestMethod.PATCH, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public Map<String, Object> updateProductCount(@RequestBody CartUpdateVO vo) throws Exception {
		
		try {
			// 해시맵 선언
			Map<String, Object> dataHashMap = new HashMap<>();
			
			// 수량 변경
			cartService.updateProductCount(vo);
			
			// 결과 전달
			SuccessCode successCode = SuccessCode.PATCH_COUNT;
			dataHashMap.put("data", "상품 수량 변경 성공");
			Map<String, Object> result = resultService.successResult(successCode, dataHashMap);
			 
			 return result;
			
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            Map<String, Object> errorHashMap = new HashMap<>();
            errorHashMap.put("error", "오류가 발생했습니다.");
            return errorHashMap;
		}
	}

    // 장바구니 삭제
	@RequestMapping(value = "/cart/{id}", method = RequestMethod.DELETE, produces="application/json;charset=utf-8")
	public Map<String, Object> deleteCart(@PathVariable("id") int id) throws Exception {
		try {
			// 해시맵 선언
			Map<String, Object> dataHashMap = new HashMap<>();
			
			// 장바구니 삭제
			cartService.deleteCart(id);
			
			// 결과 전달
			SuccessCode successCode = SuccessCode.DELETE_PRODUCT;
			dataHashMap.put("data", "장바구니 삭제 성공");
			Map<String, Object> result = resultService.successResult(successCode, dataHashMap);
			
			return result;
			
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류발생 :" + e);
            Map<String, Object> errorHashMap = new HashMap<>();
            errorHashMap.put("error", "오류가 발생했습니다.");
            return errorHashMap;
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
