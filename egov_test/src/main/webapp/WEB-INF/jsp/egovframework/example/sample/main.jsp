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
			<h2>전체회원 수 ${ num }</h2>
			<h2>이름 ${ getName }</h2>
			<h2>로그인 하지 않음</h2>
	
		</c:if>
		
		<c:if test="${user != null}">
			<h2>유저  이메일 :  ${ user.getEmail() }</h2>
		</c:if>
		
		<a href="/egov_test/register.do">회원가입페이지 이동</a>
		<a href="/egov_test/login.do">로그인페이지 이동</a>
		
	</div>
</body>
</html>