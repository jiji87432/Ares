<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="include/header.jsp"%>
<!DOCTYPE HTML>
<head>
	<script type="text/javascript">
		var loading_13 = "<img src='<%=request.getContextPath()%>/image/loading_13.gif' />";
		function execJob (taskName){
			$("#"+taskName).html(loading_13);
			$.post("execJobTask.action", 
					{taskName:taskName}, 
					function(data) {
						$("#"+taskName).html(data);
					}
			);
		}
	</script>
</head>

<body>		
	
	<form id="form1"  >
		<table class="global_table"><div class="search_tit"><h2>定时任务执行</h2></div></table>
		<table class="global_table" cellpadding="0" cellspacing="0">
			<tr>
				<th width="23%">CaseJob：</th>
				<td >
					<input type="button" value="执	行" name="caseJob" onclick="execJob(this.name)" class="global_btn" ></input>
					<font color="red"><span id = 'caseJob'></span></font>
				</td>
				
			</tr>
			<tr>
				<th width="23%">CaseOfDayJob：</th>
				<td >
					<input type="button" value="执	行" name="caseOfDayJob" onclick="execJob(this.name)" class="global_btn" />
					<font color="red"><span id = 'caseOfDayJob'></span></font>
				</td>
				
			</tr>
			<tr>
				<th width="23%">CategoryExecPlanJobTask：</th>
				<td >
					<input type="button" value="执	行" name="categoryExecPlanJobTask" onclick="execJob(this.name)" class="global_btn" />
					<font color="red"><span id = 'categoryExecPlanJobTask'></span></font>
				</td>
				
			</tr>
			<tr>
				<th width="23%">TransactionJob：</th>
				<td >
					<input type="button" value="执	行" name="transactionJob" onclick="execJob(this.name)" class="global_btn" />
					<font color="red"><span id = 'transactionJob'></span></font>
				</td>
				
			</tr>
			<tr>
				<th width="23%">TransactionOfDayJob：</th>
				<td >
					<input type="button" value="执	行" name="transactionOfDayJob" onclick="execJob(this.name)" class="global_btn" />
					<font color="red"><span id = 'transactionOfDayJob'></span></font>
				</td>
				
			</tr>
											
		</table>
		<br/>
		
		
	</form>
	<br>	
	
</body>
