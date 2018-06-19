//验证黑名单规则的唯一约束
function mySubmit(){
	var message="";
	var businessNo = $("input[name=blackList.businessNo]").val();
	var status = $("select[name=blackList.status]").find("option:selected").val();
	var scope = $("select[name=blackList.scope]").find("option:selected").val();
	var businessType = $("select[name=blackList.businessType]").find("option:selected").val();
	var effectTime=$("input[name=blackList.effectTime]").val();
	var expireTime=$("input[name=blackList.expireTime]").val();
	var submitType=$("input[name=submitType]").val();
	if(businessNo==""){
		message +="业务编号不能为空<br/>";
	}
	if(businessNo.indexOf(" ")>-1){
		message +="业务编号不能有空格<br/>";
	}
	if(status==""){
		message+="状态不能为空<br/>";
	}
	if(scope==""){
		message+="范围不能为空<br/>";
	}
	if(businessType==""){
		message+="业务类型不能为空<br/>";
	}
	if(effectTime==""){
		message+="生效日期不能为空<br/>";
	}
	if(expireTime!=""){
		if(effectTime>expireTime){
			message+="生效日期不能大于失效日期<br/>";
		}
	}
	
	if(submitType=="add"){
		if(effectTime<new Date().format("yyyy-MM-dd")){
			message+="生效日期不能为当天之前的日期";
		}
	}
	if(message!=""){
		showMsgDiv(message);
	}else{
		var efTime=$("input[name=efTime]").val();
		var exTime=$("input[name=exTime]").val();
		if(submitType=="add"){
			isExistCustomer(businessNo,businessType,scope);
		}else if(new Date(effectTime).format("yyyy-MM-dd")==new Date(efTime).format("yyyy-MM-dd")&&new Date(expireTime).format("yyyy-MM-dd")==new Date(exTime).format("yyyy-MM-dd")){
			mySubmitConfirm();
		}else if(submitType=="update"){
			var id=$("input[name=blackList.id]").val();
			var scope1=$("input[name=blackList.scope]").val();
			var businessType1=$("input[name=blackList.businessType]").val();
			isRepeatDate(id,businessNo,businessType1,scope1,effectTime,expireTime);
		}
		
	}
}
function mySubmitConfirm(){
	$( "#dialog").dialog( "destroy" );	
	$( "#dialog-confirm-info").html("确认提交该信息吗?");
	$( "#dialog-confirm" ).dialog({	resizable: false,height:140,modal: true,					
		buttons: { "确定": function() { 
						$( this ).dialog( "close" );
				   		$("#blackListForm").submit();
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
function isExistCustomer(businessNo,businessType,scope){
	blackListService.isExistCustomer(businessNo,businessType,scope,function(data){
		if(data==true){
			showMsgDiv("该业务编号已加入黑名单");
		}else{
			blackListService.getCustomer(businessNo,function(data1){
				if(data1){
					showMsgDiv("该业务编号不存在");
				}else{
					mySubmitConfirm();
//					blackListService.isExistWhiteList(businessNo,function(data2){
//						if(data2){mySubmitConfirm();}else{showMsgDiv("该业务编号已加入白名单");}
//					});
				}
			});
		}
	});	
}
function isRepeatDate(id,businessNo,businessType,scope,effectTime,expireTime){
	blackListService.isRepeatDate(id,businessNo,businessType,scope,effectTime,expireTime,function(data){
		if(data=="false"){
			mySubmitConfirm();
		}else{
			showMsgDiv("该条黑名单记录与之前记录的生效日期、<br/>失效日期区间重合<br/>生效、失效日期应晚于"+data+"");
		}
	});
}
Date.prototype.format = function (formatStr) {  
	var date = this;  
	/*  
	函数：填充0字符  
	参数：value-需要填充的字符串, length-总长度  
	返回：填充后的字符串  
	*/ 
	var zeroize = function (value, length) {  
	if (!length) {  
	length = 2;  
	}  
	value = new String(value);  
	for (var i = 0, zeros = ''; i < (length - value.length); i++) {  
	zeros += '0';  
	}  
	return zeros + value;  
	};  
	return formatStr.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|M{1,4}|yy(?:yy)?|([hHmstT])\1?|[lLZ])\b/g, function($0) {  
	switch ($0) {  
	case 'd': return date.getDate();  
	case 'dd': return zeroize(date.getDate());  
	case 'ddd': return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][date.getDay()];  
	case 'dddd': return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][date.getDay()];  
	case 'M': return date.getMonth() + 1;  
	case 'MM': return zeroize(date.getMonth() + 1);  
	case 'MMM': return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][date.getMonth()];  
	case 'MMMM': return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][date.getMonth()];  
	case 'yy': return new String(date.getFullYear()).substr(2);  
	case 'yyyy': return date.getFullYear();  
	case 'h': return date.getHours() % 12 || 12;  
	case 'hh': return zeroize(date.getHours() % 12 || 12);  
	case 'H': return date.getHours();  
	case 'HH': return zeroize(date.getHours());  
	case 'm': return date.getMinutes();  
	case 'mm': return zeroize(date.getMinutes());  
	case 's': return date.getSeconds();  
	case 'ss': return zeroize(date.getSeconds());  
	case 'l': return date.getMilliseconds();  
	case 'll': return zeroize(date.getMilliseconds());  
	case 'tt': return date.getHours() < 12 ? 'am' : 'pm';  
	case 'TT': return date.getHours() < 12 ? 'AM' : 'PM';  
	}  
	});  
	} 