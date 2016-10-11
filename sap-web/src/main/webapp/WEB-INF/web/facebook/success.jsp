<%@ page isELIgnored="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/core" prefix="c" %>
<html>
<head>
<meta name="viewport" content="user-scalable=no, width=device-width" />
<style type="text/css">
body {
	background-color: black;
	color: white;
	text-align: center;
	background-color: black;
	font-family: Helvetica;
	font-size: 14px;
	margin: 0px;
	padding: 14px;
}

a {
	color: white;
}

.error {
	background-color: #F5A9A9;
	padding: 6px;
	border-color: #DF0101;
	border-style: solid;
	border-width: 1px;
}
</style>
</head>
<body>
<br style="line-height: 20px" />
<img alt="DiiLo"
	src="<%= request.getContextPath() %>/interface/img/diiloLogo.png"
	width="200" height="52" />
	<p><spring:message code="jsp.facebook.link.yes"/></p>
	<p class="error"><spring:message code="jsp.facebook.clickclose"/></p>
</body>
</html>