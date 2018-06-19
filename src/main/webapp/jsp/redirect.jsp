<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="include/header.jsp"%>
<!DOCTYPE HTML>
<head>
	<script type="text/javascript">
		function redirect(){
			var url = "/boss/authorityProxy.action?url=/posprocess/welcomeQuery.action" ;
			window.location.href = url;
		}	
	</script>
</head>

<body onload="redirect();">		

</body>
