<%@ page contentType="text/html;charset=UTF-8"
	import="si.roskar.diploma.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1250">
<title>Kingdom Mapper</title>

<script type="text/javascript">
	function validate() {
		document.getElementById("errorMessage").innerHTML = "";
		if (document.getElementById("user").value.length < 1) {
			document.getElementById("errorMessage").innerHTML = "* enter a username!"
			return false;
		}

		if (document.getElementById("pass").value.length < 1) {
			document.getElementById("errorMessage").innerHTML = "* enter a password!"
			return false;
		}
		return true;
	}
</script>
</head>
<body>

	<div align="center">
		<form onsubmit="return validate();" method="POST"
			action="<c:url value='j_spring_security_check' />">
			<table border="0" style="margin: 0 auto;">
				<tr height="200">
					<td colspan="2"></td>
				</tr>
				<tr>
					<td colspan="2" height="10"></td>
				</tr>
				<tr>
					<td align="right"><b> Username: </b></td>
					<td align="left"><input type="text" id="user" name="user" />
					</td>
				<tr>
					<td align="right"><b> Password: </b></td>
					<td align="left"><input type="password" id="pass" name="pass" />
					</td>
				</tr>
				<tr align="center">
					<td></td>
					<td align="left"><input type="submit" name="Submit"
						class="button" value="Login" /></td>
				</tr>
				<tr style="color: red;">
					<td colspan="2" style="height: 20px;">
						<div align="left" id="errorMessage">
							<%
								if (request.getParameter("MSGT") != null) {
									out.println(request.getParameter("MSGT"));
								}
							%>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</div>

</body>
</html>
