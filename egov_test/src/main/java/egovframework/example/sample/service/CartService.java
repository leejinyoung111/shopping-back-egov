package egovframework.example.sample.service;

import java.util.List;

public interface CartService {
	
	// 장바구니 조회
	List<CartVO> cartList(int userId) throws Exception;
	
	// 상품 체크
	int productCheck(CartVO vo) throws Exception;
	
	// 장바구니 추가
	String insertCart(CartVO vo) throws Exception;
	
	// 장바구니 삭제
	String deleteCart(int id) throws Exception;
	
	// 상품 수량 변경
	void updateProductCount(CartUpdateVO vo) throws Exception;
	

}
