<%@page import="sopraturage.ApplicationData"%>
<%@page import="sopraturage.models.tables.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<%
	
	ApplicationData data=(ApplicationData)session.getAttribute("data");
	

		User user = (User) request.getAttribute("user");
		out.println(user);
		
		if (user.equals(data.localUser)){
			out.println("C est ton profil");
		}
	%>

</body>
</html>