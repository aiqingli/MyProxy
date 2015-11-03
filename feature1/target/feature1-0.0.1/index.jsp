<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Feature 1</title>

</head>
<body>

<h1>Feature 1</h1>
<p>Timestamp
<%
session.setAttribute("gss.ts", (Date) new Date());
for (Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) {     
    String attribName = (String) e.nextElement();
    Object attribValue = session.getAttribute(attribName);
%>
<br /><%= attribName %>: <%= attribValue %>
<%
}
%>
</p>

</body>
</html>