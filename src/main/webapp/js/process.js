//业务流程统一跳转链接
function toCommonAuditAction(taskId,taskDefinitionKey,processInstanceId, lastIsAgree, forwardJsp){		
	var url = "workFlowTaskRedirectAction.action?" +
			 "runtimeBean.taskDefinitionKey="+taskDefinitionKey+
			 "&runtimeBean.processInstanceId="+processInstanceId+
			 "&runtimeBean.taskId="+taskId+
			 "&runtimeBean.lastIsAgree="+lastIsAgree+
			 "&runtimeBean.forwardJsp="+forwardJsp;
	if(forwardJsp == 'main'){
		window.parent.parent.location.href = url;
	}else{
		window.parent.location.href = url;
	}
}	

//show global process detail
function showProcessDetail(processInstanceId){
	var html = '<div id="processDetail" style="margin:5px 0px;width:100%;display:none">'+
	'<iframe id="process" name="processDetail" src="processDetail.action?processInstanceId='+processInstanceId+
	'" height="" width="100%" scrolling="yes" frameborder="0" >'+
	'</iframe></div>';
	$("#showProcessDetail").append(html);
	$("#processDetail").slideToggle();
}

//show global process image
function showProcessImg(processInstanceId){
	var html1 = '<div id="processImage" style="height:180px;width:100%;display:none">'+
	'<iframe id="processImg" name="processImg" src="processImg.action?processInstanceId='+processInstanceId+
	'"  width="100%" scrolling="no" frameborder="0" >'+
	'</iframe></div>';
	$("#showProcessDetail").append(html1);
	$("#processImage").slideToggle();
}

//task suspend
function suspendTask(taskId){
	
	var c = confirm("确定要挂起吗？");
	if(c == true){
		$.ajax({
			type:"post",
			url:"suspendTask.action",
			data:{taskId:taskId},
			success:function(){
				var path=location.protocol +'//'+location.hostname+':'+location.port;
				var url=path+"/boss/authorityProxy.action?url=/posprocess/welcomeQuery.action";
				window.location.href = url;
			}
		});
	}else{
		return;
	}
}

function reactiveProcInst(processInstanceId,userName){
	$.ajax({
		type:"post",
		url:"reactiveProcInst.action",
		data:{processInstanceId:processInstanceId,userName:userName},
		success:function(){
			alert("恢复成功！");
			window.location.href = window.location.href;
		}
	});
}