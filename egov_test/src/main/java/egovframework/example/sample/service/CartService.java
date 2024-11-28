package egovframework.example.sample.service;

import java.util.List;

public interface CartService {
	
	// 장바구니 조회
	List<CartVO> cartList(int userId) throws Exception;
	
	// 장바구니 추가
	String insertCart(CartVO vo) throws Exception;
	
	// 장바구니 삭제
	String deleteCart(int id) throws Exception;
	

}
