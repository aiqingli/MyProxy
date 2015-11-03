<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Feature 2</title>

</head>
<body>

<h1>Feature 2</h1>
<p>Random number
<%
session.setAttribute("gss.random", (Integer) new Random().nextInt());
boolean found = false;
for (Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) {     
    String attribName = (String) e.nextElement();
    
    if (attribName.equals("gss.test")) {
    	found = true;
    }
    
   	Object attribValue = session.getAttribute(attribName);
%>
<br /><%= attribName %>: <%= attribValue %>
<%
}

if (! found) {
	session.setAttribute("gss.test", new Long(System.currentTimeMillis()));
}
else {
	session.removeAttribute("gss.test");
}

%>
<br />

</p>

<p>
<img src="/feature2/hpe.png" title="Hewlett Packard Enterprise" alt="Hewlett Packard Enterprise" />
</p>

</body>
</html>