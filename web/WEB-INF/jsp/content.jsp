
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>编辑内容预览</title>
</head>

<%
    String content = (String) request.getAttribute("content");
%>

<body>
    <div>
        <%
            out.print(content);
        %>
    </div>

</body>
</html>
