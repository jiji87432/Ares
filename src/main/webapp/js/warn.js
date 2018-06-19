//验证过滤规则的唯一约束
function mySubmit(){
	var message="";
	var content = $("#content").val();
	var code = $("#warnCode").val();
	var description = $("#description").val();
	var type = $("select[name=warn.type]").find("option:selected").val();
	if(code==""){
		message +="警告编码不能为空<br/>";
	}
	if(content==""){
		message +="内容不能为空<br/>";
	}
	if(description==""){
		message+="描述不能为空<br/>";
	}
	if(type==""){
		message+="类型不能为空<br/>";
	}
	if(message!=""){
		showMsgDiv(message);
	}else{
		mySubmitConfirm();
	}
}
function mySubmitConfirm(){
	$( "#dialog").dialog( "destroy" );	
	$( "#dialog-confirm-info").html("确认提交该信息吗?");
	$( "#dialog-confirm" ).dialog({	resizable: false,height:140,modal: true,					
		buttons: { "确定": function() { 
						$( this ).dialog( "close" );
				   		$("#warnForm").submit();
				   },
				   "取消": function() {$( this ).dialog( "close" );}
				 }
	});
}
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