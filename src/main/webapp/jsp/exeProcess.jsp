<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="include/header.jsp"%>
<head>
	<script type="text/javascript">
	function mySubmit(){
		var loading_2 = "<img src='<%=request.getContextPath()%>/image/loading_2.gif' />";
			console.log("=="+$("#param1").val()+'=='+$("#param2").val()+'=='+$("#param2").val());
			$("#result").html(loading_2);
			$.post("executeProcess.action?param1="+$('#param1').val()+"&param2="+$('#param2').val()+"&param3="+$('#param3').val(),
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
<div class="detail_tit" ><h2>功能测试</h2></div>
	<form id="form1" action="${pageContext.request.contextPath}/saveOlrParam.action" method="post">
		<table cellpadding="0" cellspacing="1" class="global_tabledetail">
			<tr>
				<th width="10%">输入参数1：</th>
				<td>
					<input  name="param1" id="param1" style="width: 100%"/>
				</td>
			</tr>
			<tr>
				<th width="10%">输入参数2：</th>
				<td>
					<input  name="param2" id="param2" style="width: 100%"/>
				</td>
			</tr>
			<tr>
				<th width="10%">输入参数3：</th>
				<td>
					<input  name="param3" id="param3" style="width: 100%"/>
				</td>
			</tr>
		</table>
		<br/>
		<center>
			<input class="global_btn" value="提交" onclick="if(confirmdel('确定执行当前命令吗')){mySubmit();}"/>
			<input type="reset" class="global_btn" value="重 置" />

		</center>
		</form>
		<div>
		<span id="result"></span>
		</div>
</body>
