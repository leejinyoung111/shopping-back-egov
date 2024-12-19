package main.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import main.service.CartUpdateVO;
import main.service.CartVO;

@Mapper("cartMapper")
public interface CartMapper {
	
	// 장바구니 조회
	List<CartVO> cartList(int user_id) throws Exception;
	
	// 상품 체크
	int productCheck(CartVO vo) throws Exception;
	
	// 장바구니 추가
	void insertCart(CartVO vo) throws Exception;
	
	// 장바구니 삭제
	void deleteCart(int id) throws Exception;
	
	// 상품 수량 변경
	void updateProductCount(CartUpdateVO vo) throws Exception;

}
