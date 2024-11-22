<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인</title>
</head>
<body>
	<div>
		<h1>메인페이지</h1>
		
		<c:if test="${user == null}">
			<h2>로그인 상태 : X</h2>
		</c:if>
		
		<c:if test="${user != null}">
			<h2>로그인 상태 : O</h2>
			<h2>유저  이메일 :  ${ user.getEmail() }</h2>
		</c:if>
		
		<div>
			<h2>도서 리스트</h2>
			<c:forEach var = 'item' items = '${getBookList }' >
				<div>${item.id}</div>
				<div>${item.title}</div>
				<div>${item.contents}</div>
				<div>${item.isbn}</div>
				<div>${item.price}</div>
			</c:forEach>
		</div>
			
		<div>
			<h2>장바구니 리스트</h2>
			<c:forEach var = 'item' items = '${getCartList }' >
				<div>${item.id}</div>
				<div>${item.title}</div>
				<div>${item.contents}</div>
				<div>${item.isbn}</div>
				<div>${item.price}</div>
			</c:forEach>
		</div>
		
		<a href="/egov_test/register.do">회원가입페이지 이동</a>
		<a href="/egov_test/login.do">로그인페이지 이동</a>
		<a href="/egov_test/cart.do">장바구니 페이지 이동</a>
		
	</div>
</body>
</html>