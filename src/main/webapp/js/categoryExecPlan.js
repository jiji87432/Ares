//验证风险规则的唯一约束
function mySubmit(){
	var message = "";
	var execPlanType = $("select[name=execPlanType]").find("option:selected").val();
	var execTime = new Array();
	$("input[name='execTime']").each(function(i, o){
		execTime.push($(o).val());
	});
	console.info("execPlanType = " + execPlanType);
	console.info("execTime = " + execTime);
	if(execPlanType == ""){
		message += "执行方式不能为空<br/>";
	}
	if('TIMER' == execPlanType){
		if(execTime == ""){
			message+= "请选择执行时间点<br/>";
		}else{
			var vl = $.inArray('', execTime);
			console.info("null value index in arry = " + vl );
			if(vl >= 0){
				message += "执行时间列表存在空值<br/>";
			}
		}
	}
	
	if(message != ""){
		showMsgDiv(message);
	}else{
		mySubmitConfirm();	
	}
}
// 提交二次确认框
function mySubmitConfirm(){
	$( "#dialog").dialog( "destroy" );	
	$( "#dialog-confirm-info").html("确认提交该信息吗?");
	$( "#dialog-confirm" ).dialog({	resizable: false,height:140,modal: true,					
		buttons: { "确定": function() { 
						$( this ).dialog( "close" );
				   		$("#ctgryExecPlanForm").submit();
				   },
				   "取消": function() {$( this ).dialog( "close" );}
				 }
	});
}
// 弹出框
function showMsgDiv(msg){
	$( "#dialog").dialog( "destroy" );	
	$( "#dialog-message-info").html(msg);
	$( "#dialog-message" ).dialog({
		resizable: false,
		height:260,
		modal: true,				
		buttons: {"取消": function() { $( this ).dialog( "close" );}}
	});
}

//显示日期格式
function dateFmt(){
	WdatePicker({dateFmt:'HH:mm:ss'});
}
// 显示定时时间选择列
function showTime(value){
	console.info('execPlanType = ' + value);
	if('TIMER' == value){//定时,显示时刻和定时种类单选框
		var est = $("#execPlanExist").val();
		if(est == 'Y'){
			var ary = $("#execPlanArray").html();
			console.info("ary = " + ary);
			listExistConfig(ary);
		}else{
			addExecTime();
		}	
	}else if('DAY' == value){//去掉显示时刻行和定时种类单选框
		$("tr[name='tr_exectime']").remove();
		$("#rowsCount").val(1);
	}
}
//单选框值发生变更
function tmTypChg(objVlu, objId){
	console.info('objVlu = ' + objVlu);
	console.info('objId = ' + objId);
	var ary = objId.split('_');
	var tgt = 'execTimerType_' + ary[1];
	document.getElementById(tgt).value=objVlu;
}
//添加时间点
function addExecTime(){
	var obj = $("#rowsCount").val();
	console.info("tr-id = " + obj);
	var trTop = "<tr id='"+obj+"' name='tr_exectime'>";
	var tr0 = "<th onClick='removeExecTime(this);'>&nbsp;&nbsp;&nbsp;<a href='#'> - 删除时间点 </a>&nbsp;&nbsp;&nbsp;<br></th><td>";
	var tr1 = "<th><font color='red'>*</font></th><td>";
	var tr2 = "<input type='text'  name='execTime' id='exectime"+obj+"' onfocus='dateFmt();' class='Wdate width170' />";		
	var tr3 = "&nbsp;&nbsp;&nbsp;<a href='javascript:addExecTime("+obj+");'> + 添加时间点 </a>";
	var tr4 = "</td><td><input type='radio' name='timerType_"+obj+"' id='timerT1_"+obj+"' value='T1' onclick='tmTypChg(this.value, this.id)'>&nbsp;&nbsp;依赖型定时&nbsp;&nbsp;";
	var tr5 = "</td><td><input type='radio' name='timerType_"+obj+"' id='timerT2_"+obj+"' value='T2' checked onclick='tmTypChg(this.value, this.id)'>&nbsp;&nbsp;非依赖型定时&nbsp;&nbsp;";
	var trEnd = "<input type='hidden' name='execTimeType' id='execTimerType_"+obj+"' value='T2'/></td></tr>";
	var tr = trTop+tr1+tr2+tr3+tr4+tr5+trEnd;
	if(obj != 1){
		tr = trTop+tr0+tr2+tr4+tr5+trEnd;
	}
	$("#before").before(tr); 
	obj = Number(obj) + 1;
	$("#rowsCount").val(obj);
}

//删除时间点
function removeExecTime(id){
	$(id).parent().remove(); 
}

//列出该风险类别已配置的执行时间计划
function listExistConfig(ary){
	var info = eval(ary);
	$.each(info, function(i, etm){
		var obj = Number(i) + 1;
		console.info("array_index + 1 = " + obj);
		var tp = etm.categoryExecType;
		console.info('exist categoryExecType = ' + tp);
		var trTop = "<tr id='"+obj+"' name='tr_exectime'>";
		var tr0 = "<th onClick='removeExecTime(this);'>&nbsp;&nbsp;&nbsp;<a href='#'> - 删除时间点 </a>&nbsp;&nbsp;&nbsp;<br></th><td>";
		var tr1 = "<th><font color='red'>*</font></th><td>";
		var tr2 = "<input type='text'  name='execTime' id='exectime"+obj+"' onfocus='dateFmt();' class='Wdate width170' value='"+etm.execTime+"' />";		
		var tr3 = "&nbsp;&nbsp;&nbsp;<a href='javascript:addExecTime("+obj+");'> + 添加时间点 </a>";
		
		var tr4 = "";
		if('T1' == tp){
			tr4 = "</td><td><input type='radio' name='timerType_"+obj+"' id='timerT1_"+obj+"' value='T1' checked onclick='tmTypChg(this.value, this.id)'>&nbsp;&nbsp;依赖型定时&nbsp;&nbsp;";
		}else{
			tr4 = "</td><td><input type='radio' name='timerType_"+obj+"' id='timerT1_"+obj+"' value='T1'  onclick='tmTypChg(this.value, this.id)'>&nbsp;&nbsp;依赖型定时&nbsp;&nbsp;";
		}
		
		var tr5 = "";
		if('T2' == tp){
			tr5 = "</td><td><input type='radio' name='timerType_"+obj+"' id='timerT2_"+obj+"' value='T2' checked onclick='tmTypChg(this.value, this.id)'>&nbsp;&nbsp;非依赖型定时&nbsp;&nbsp;";
		}else{
			tr5 = "</td><td><input type='radio' name='timerType_"+obj+"' id='timerT2_"+obj+"' value='T2' onclick='tmTypChg(this.value, this.id)'>&nbsp;&nbsp;非依赖型定时&nbsp;&nbsp;";
		}
		
		var trEnd = "<input type='hidden' name='execTimeType' id='execTimerType_"+obj+"' value='"+tp+"'/></td></tr>";
		
		if('DAY' == tp){
			tr5 = "</td><td><input type='radio' name='timerType_"+obj+"' id='timerT2_"+obj+"' value='T2' checked onclick='tmTypChg(this.value, this.id)'>&nbsp;&nbsp;非依赖型定时&nbsp;&nbsp;";
			trEnd = "<input type='hidden' name='execTimeType' id='execTimerType_"+obj+"' value='T2'/></td></tr>";
		}
		
		var tr = trTop+tr1+tr2+tr3+tr4+tr5+trEnd;
		if(obj != 1){
			tr = trTop+tr0+tr2+tr4+tr5+trEnd;
		}
		$("#before").before(tr); 
		$("#rowsCount").val(obj);
	});
	var fnl = Number($("#rowsCount").val()) + 1;
	$("#rowsCount").val(fnl);
}
