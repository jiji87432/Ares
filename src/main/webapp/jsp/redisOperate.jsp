<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="include/header.jsp"%>
<!DOCTYPE HTML>
<head>
	<script type="text/javascript">
		var loading_2 = "<img src='<%=request.getContextPath()%>/image/loading_2.gif' />";
		function removeRedisByKey (){
			console.log("=="+$("#redisKey").val());
			 $("#result").html(loading_2);
			  $.post("executeRedis.action?redisKey="+$('#redisKey').val(), 
					function(data) {
						$("#result").html(data);
					}
			);  
		}
		
		function confirmdel(str){   
			return confirm(str);   
		}
		
	</script>
</head>

<body>
	<form id="form1"  >
		<table class="global_table"><div class="search_tit"><h2>redis操作</h2></div></table>
		<table class="global_table" cellpadding="0" cellspacing="0">
			<tr>
				<th width="23%">输入命令：</th>
				<td colspan="">
					<textarea rows="4" cols="60" name="redisKey" id="redisKey" ></textarea>
				</td>
			</tr>	
			<tr>
				<th width="15%">操作：</th>
				<td colspan="3">
					<input type="button" value="执行命令" name="" onclick="if(confirmdel('确定执行当前命令吗')){removeRedisByKey();}" class="global_btn" />
				</td>
			</tr>
			<tr>
				<th width="23%">执行结果：</th>
				<td colspan="3">
					<span id="result"></span>
				</td>				
			</tr>											
		</table>
		<br/>				
	</form>
	<br/>	
</body>
