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

import egovframework.example.sample.service.BookService;
import egovframework.example.sample.service.BookVO;
import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.LoginDTO;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.SampleVO;
import egovframework.example.sample.service.UserService;
import egovframework.example.sample.service.UserVO;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springmodules.validation.commons.DefaultBeanValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    
	
	@Resource(name = "bookService")
	private BookService bookService;
	
	@Resource(name = "userService")
	private UserService userService;
	
	
    // 메인 페이지
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces="application/json;charset=utf-8")
	public String test() throws Exception {
		List<BookVO> getBookList = bookService.bookList();
		Map<String, Object> response = new HashMap<>();
		response.put("getBookList", getBookList);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(response);
		
		System.out.println(jsonString);
	        
		return jsonString;
	}
	
	// 회원가입 기능
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public String register(@RequestBody UserVO vo) throws Exception{
		try {
			userService.register(vo);
			return "회원가입 성공!";
			
		} catch (Exception e) {
			System.out.println("발생 오류:" + e);
			return "발생 오류:" + e;
		}
	}
	
	// 로그인 기능
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces="application/json;charset=utf-8", consumes="application/json;charset=utf-8")
	public String login(@RequestBody LoginDTO dto) throws Exception{
		try {
			Boolean login = userService.login(dto);
			
			System.out.println(login);
			return "로그인 성공!";
			
		} catch (Exception e) {
			System.out.println("발생 오류:" + e);
			return "발생 오류:" + e;
		}
	}
	
	// 회원가입 페이지
//	@RequestMapping(value = "/register.do", method = RequestMethod.GET)
//	public String register() throws Exception {
//		return "sample/register";
//	}
	
	// 회원가입 기능
//	@RequestMapping(value = "/registerSave.do", method = RequestMethod.POST)
//	public String registerSave(UserVO vo) throws Exception {
//		
//		// 이메일 검색
//		UserVO resultVO = userService.isEmail(vo);
//		
//		if (resultVO == null) {
//			userService.insertUser(vo);
//			return "redirect:/login.do";
//		} else {
//			System.out.println("이미 존재하는 이메일입니다.");
//			return null;
//		}
//	}
	
	// 로그인 페이지
//	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
//	public String login() throws Exception {
//		return "sample/login";
//	}
	
	// 로그인 기능
//	@RequestMapping(value = "/loginAction.do", method = RequestMethod.POST)
//	public String loginAction(UserVO vo, Model model) throws Exception {
//		
//		// 이메일 검색
//		UserVO resultVO = userService.isEmail(vo);
//		
//		// 비밀번호 매치
//		UserVO passwordMatchVO = userService.passwordMatch(vo);
//		
//		if (resultVO == null) {
//			System.out.println("존재하지 않은 이메일입니다.");
//			return null;
//		} else {
//			
//			if (passwordMatchVO == null) {
//				System.out.println("비밀번호가 일치하지 않습니다.");
//				return null;
//			} else {
//				model.addAttribute("user", passwordMatchVO);
//				return "forward:/main.do";
//			}
//		}
//
//	}

	// 장바구니 페이지
//	@RequestMapping(value = "/cart.do", method = RequestMethod.GET)
//	public String card() throws Exception {
//		return "sample/cart";
//	}
	
	// 장바구니 추가 기능
//	@RequestMapping(value = "/cartAction.do", method = RequestMethod.POST)
//	public String cartAction(CartVO vo) throws Exception {
//		
//		// 상품 존재 체크
//		CartVO resultVO = cartService.cartCheck(vo);
//		
//		if (resultVO == null) {
//			cartService.insertCart(vo);
//			return "redirect:/main.do";
//		} else {
//			System.out.println("이미 존재하는 상품입니다.");
//		return null;
//		}
//	}

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
