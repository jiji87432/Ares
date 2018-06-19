//验证处理措施的唯一约束
function mySubmit(){
	var message="";
	var content = $("#content").val();
	var description = $("#description").val();
	var status = $("select[name=status]").find("option:selected").val();
//	if(content==""){
//		message +="处理措施内容不能为空<br/>";
//	}
	if(description==""){
		message+="处理措施描述不能为空<br/>";
	}
	if(description.indexOf(" ")>-1){
		message+="处理措施描述中不能有空格<br/>";
	}
	if(status==""){
		message+="处理措施状态不能为空<br/>";
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
				   		$("#handleMeasureForm").submit();
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
function convertString(str1,divId){
	var str=/{*\{*\}*}/;
	if(str.exec(str1)){
		var o="{系数}";
		str1=str1.replace(/\{([^\}]+)\}/g,function($0,$1){if(str1!="")return o;return '';});
	}
	$("#"+divId).html(str1);
}