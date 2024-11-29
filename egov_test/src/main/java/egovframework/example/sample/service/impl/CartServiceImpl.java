package egovframework.example.sample.service.impl;

import java.util.List;
import java.util.Map;

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
		List<CartVO> resultVO = cartDAO.cartList(userId);
		if (resultVO == null)
			throw processException("info.nodata.msg");
		return resultVO;
	};
	
	// 상품 체크
	@Override
	public int productCheck(CartVO vo) throws Exception {
		int resultVO = cartDAO.productCheck(vo);
		return resultVO;
	}
	
	// 장바구니 추가
	@Override
	public String insertCart(CartVO vo) throws Exception{
		cartDAO.insertCart(vo);
		return "장바구니 추가 성공!";
	};
	
	// 장바구니 삭제
	@Override
	public String deleteCart(int id) throws Exception{
		cartDAO.deleteCart(id);
		return "장바구니 삭제 성공!";
	};

}
