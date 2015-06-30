<%@ page contentType="text/html;charset=UTF-8"
	import="si.roskar.diploma.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1250">
<title>Kingdom Mapper</title>

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
<body>
      <form action="/j_spring_security_check" method="POST">
        <label for="username">User Name:</label>
        <input id="username" name="j_username" type="text"/>
        <label for="password">Password:</label>
        <input id="password" name="j_password" type="password"/>
        <input type="submit" value="Log In"/>
      </form>
    </body>
</html>
