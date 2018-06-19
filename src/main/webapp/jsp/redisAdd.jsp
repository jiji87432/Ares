<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="include/header.jsp"%>
<!DOCTYPE HTML>
<head>
	<script type="text/javascript">
		var loading_2 = "<img src='<%=request.getContextPath()%>/image/loading_2.gif' />";
		function removeRedisByKey (key){
			$("#result").html(loading_2);
			$.post("removeRedisByKey.action", 
					{key:key}, 
					function(data) {
						$("#result").html(data);
					}
			);
		}
		function getRedisOfDictByKey (key){
			$("#result").html(loading_2);
			$.post("getRedisOfDictByKey.action", 
					{key:key}, 
					function(data) {
						$("#result").html(data);
					}
			);
		}
	</script>
</head>

<body>		
	
	<form id="form1"  method="post" action="${pageContext.request.contextPath}/saveRedis.action">
		<table class="global_table"><div class="search_tit"><h2>redis操作</h2></div></table>
		<table class="global_table" cellpadding="0" cellspacing="0">
		<tr>
				<th width="23%">索引值：</th>
				<td colspan="2">
					<input id = "indexkey"  name="indexKey" />
				</td>			
			</tr>
			<tr>
				<th width="23%">Key值：</th>
				<td colspan="2">
					<input id = "key"  name="key" />
				</td>			
			</tr>
			<tr>
				<th width="23%">Value值：</th>
				<td colspan="2">
					<input id = "value"  name="value" />
				</td>			
			</tr>
			<tr>
				<th width="23%">操作：</th>
				<td >
					<input type="submit" value="添  加"  class="global_btn" />
				</td>				
			</tr>
											
		</table>
		<br/>
		
		
	</form>
	<br>	
	
</body>
