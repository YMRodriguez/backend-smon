<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Bienvenido!</title>
	</head>
	<body>
		<h2>Acceder a cuenta existente</h2>
		<form action="login" method="post">
			<input type="text" name="email" placeholder="Email">
			<input type="password" name="password" placeholder="Password">
			<button type="submit">Login</button>
		</form>
		

	
	</body>
</html>