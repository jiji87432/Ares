<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./include/header.jsp"%>
<h1>您选择操作的数据有误，请检查数据是否正确！</h1>
<br/>
<br/>
<%--  跳转到该通用错误显示页时，需要传递变量值 ERROR_INFO_DETAIL ，它记录具体错误原因！  --%>
<font color="red">
	${ERROR_INFO_DETAIL }
</font>