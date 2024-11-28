package egovframework.example.sample.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.example.sample.service.CartService;
import egovframework.example.sample.service.CartVO;

@Service("cartService")
public class CartServiceImpl extends EgovAbstractServiceImpl implements CartService {
	

	@Resource(name="cartMapper")
	private CartMapper cartDAO;
	
	// 장바구니 조회
	@Override
	public List<CartVO> cartList(int userId) throws Exception{
		return cartDAO.cartList(userId);
	};
	
	@Override
	// 장바구니 추가
	public String insertCart(CartVO vo) throws Exception{
		return cartDAO.insertCart(vo);
	};
	
	@Override
	// 장바구니 삭제
	public String deleteCart(int id) throws Exception{
		return cartDAO.deleteCart(id);
	};

}
