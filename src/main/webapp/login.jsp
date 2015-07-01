<%@ page contentType="text/html;charset=UTF-8"
	import="si.roskar.diploma.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>Kingdom Mapper Login</title>

<style>
h1 {
	text-align: center;
}

#header {
	margin: 220px auto 0px;
	width: 400;
}

#login-box {
	margin: 40px auto;
	padding: 10px 10px 0px 10px;
	width: 240;
	border: 1px solid #D6D6D6;
	border-radius: 10px;
}

#submit-box {
	margin: 20px auto 0px;
	text-align: center;
}
</style>

<script type="text/javascript">
	// 	function validate() {
	// 		document.getElementById("errorMessage").innerHTML = "";
	// 		if (document.getElementById("user").value.length < 1) {
	// 			document.getElementById("errorMessage").innerHTML = "* enter a username!"
	// 			return false;
	// 		}

	// 		if (document.getElementById("pass").value.length < 1) {
	// 			document.getElementById("errorMessage").innerHTML = "* enter a password!"
	// 			return false;
	// 		}
	// 		return true;
	// 	}
</script>
</head>
<body onload='document.kingdomLogin.j_username.focus();'>
	<div id="header">
		<h1>Kingdom Mapper</h1>
	</div>
	<div id='login-box'>
		<h2>Log in</h2>

		<form name="kingdom-login"
			action="<c:url value='j_spring_security_check' />" method="POST">
			<table>
				<tr>
					<td>User Name:</td>
					<td><input id="username" name="j_username" type="text" /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input id="password" name="j_password" type="password" /></td>
				</tr>
				<tr>
					<td colspan="2"><div id="submit-box"><input name="submit" type="submit"
						value="Log In" /></div></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
