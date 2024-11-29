package egovframework.example.sample.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.example.sample.service.CartVO;

@Mapper("cartMapper")
public interface CartMapper {
	
	// 장바구니 조회
	List<CartVO> cartList(int userId) throws Exception;
	
	// 상품 체크
	int productCheck(CartVO vo) throws Exception;
	
	// 장바구니 추가
	void insertCart(CartVO vo) throws Exception;
	
	// 장바구니 삭제
	void deleteCart(int id) throws Exception;

}
