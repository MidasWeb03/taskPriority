<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login Page</title>
</head>
<body>
	<div>
		<form action="loginPage.jsp" method="post">
			<input type="text" name="email" value="" placeholder="Email" /><br />
			<input type="password" name="password" value="" placeholder="Password" /><br />
			<input type="submit" value="LOGIN" />
			<a href="signup.jsp">SIGN UP</a>
		</form>
	</div>
</body>
</html>