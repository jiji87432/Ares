//验证认定处理措施的唯一约束
function mySubmit() {
	var message = "";
	var riskcategoryname = $(
			"select[name=identifyHandleMeasure.riskcategoryname]").find(
			"option:selected").val();
	var risklevel = $("select[name=identifyHandleMeasure.risklevel]").find(
			"option:selected").val();
	if (riskcategoryname == "") {
		message += "风险类别名称不能为空<br/>";
	}
	if (risklevel == "") {
		message += "风险级别不能为空<br/>";
	}
	if (message != "") {
		showMsgDiv(message);
	} else {
		mySubmitConfirm();
	}
}
function mySubmitConfirm() {
	$("#dialog").dialog("destroy");
	$("#dialog-confirm-info").html("确认提交该信息吗?");
	$("#dialog-confirm").dialog({
		resizable : false,
		height : 140,
		modal : true,
		buttons : {
			"确定" : function() {
				$(this).dialog("close");
				$("#identifyHandleMeasureForm").submit();
			},
			"取消" : function() {
				$(this).dialog("close");
			}
		}
	});
}
function showMsgDiv(msg) {
	$("#dialog").dialog("destroy");
	$("#dialog-message-info").html(msg);
	$("#dialog-message").dialog({
		resizable : false,
		height : 260,
		modal : true,
		buttons : {
			"取消" : function() {
				$(this).dialog("close");
			}
		}
	});
}
function convertStringText(str1,str2,divId){
	var inputId="handle";
	var s1=str1;
	var s2=str2;
	var str=/{*\{*\}*}/;
	var msg="";
		s1=s1.replace(/\{([^\}]+)\}/g,function($0,$1){return '<input type="text" class="'+s2+'" disabled="disabled" style="border-color: #878787; border-style: solid; border-top-width: 0px; border-right-width: 0px; border-bottom-width: 1px; border-left-width: 0px;text-align:center" size="10" maxlength="10" id="' + $1 + '" name="' + inputId + s2+'" value="" />';});
		msg='<input type="checkbox" onclick="chkText($(this).val())" id="' +s2+ '" name="' + inputId+ '" value="'+inputId+s2+'"/>';
    if(s1==""){
    	$("#"+divId).html($("#"+divId).html()+msg+str1+'<br/>');
	}else{
		$("#"+divId).html($("#"+divId).html()+msg+s1+'<br/>');
	} 
}
function convertExecParam() {
	var message = "";
	var riskcategoryname = $(
			"select[name=identifyHandleMeasure.riskCategoryName]").find(
			"option:selected").val();
	var risklevel = $("select[name=identifyHandleMeasure.riskLevel]").find(
			"option:selected").val();
	if (riskcategoryname == "") {
		message += "风险类别名称不能为空<br/>";
	}
	if (risklevel == "") {
		message += "风险级别不能为空<br/>";
	}
	if (message != "") {
		showMsgDiv(message);
		return false;
	}
	identifyHandleMeasureService
			.exist(
					riskcategoryname,
					risklevel,
					"",
					function(data) {
						if (data == "false") {
							showMsgDiv("风险类别加风险级别的组合不能重复");
							return false;
						} else {
							var handleCodes = "";
							var execParam = "";
							var execParams = "";
							$("input:checked")
									.each(
											function(index) {
												execParam = $(this).attr("id")
														+ ";" + "{";
												handleCodes += ""
														+ $(this).attr("id")
														+ ",";
												$(
														"input:[name='"
																+ $(this).val()
																+ "']")
														.each(
																function(index) {
																	if($(this).val()==""){message="参数不能为空";return false;
																	}else if($(this).val().indexOf(" ")>-1){message+="参数中不能有空格<br/>";return false;
															    	}else if(isNaN($(this).val())){message+="参数必须为数字<br/>";return false;
															    	}else if($(this).val().indexOf("-")>-1){return false;message+="参数必须不能为负数<br/>";
															    	}
																	execParam += "\""
																			+ $(
																					this)
																					.attr(
																							"id")
																			+ "\":\""
																			+ $(
																					this)
																					.val()
																			+ "\",";
																	inputName = $(
																			this)
																			.attr(
																					"name");

																});
												if (execParam != $(this).attr(
														"id")
														+ ";" + "{") {
													execParam = execParam
															.substring(
																	0,
																	execParam.length - 1);
													execParam += "};";
													execParams += execParam;

												}
											});
							if (execParams != "") {
								if (checkAdd(message, execParams.substring(0,
										execParams.length - 1))) {
									return;
								}
							}
							if (message != "") {
								showMsgDiv(message);
							} else {
								execParams = execParams.substring(0,
										execParams.length - 1);
								$("input[name=identifyHandleMeasure.execParam]")
										.val(execParams);
								handleCodes = handleCodes.substring(0,
										handleCodes.length - 1);
								$(
										"input[name=identifyHandleMeasure.handleMeasureCode]")
										.val(handleCodes);
								mySubmitConfirm();
							}
						}
					});
}

function getHandleMeasure() {
	handleMeasureService.getHandleMeasure("", "", function(data) {
		if (data != null) {
			$("#handleMeasureDiv").html("");
			var result = data.split('|_|');
			var handleMeasure = "";
			for ( var i = 0; i < result.length - 1; i++) {
				handleMeasure = result[i].split('!~!');
				convertStringText(handleMeasure[0], handleMeasure[2],
						"handleMeasureDiv");
			}
		} else {
			$("#handleMeasureDiv").text("暂无处理措施");
		}
	});
}
function getHandleMeasureQueryResult(riskcategoryname,risklevel,code,divId) {
	code+=";"+riskcategoryname+";"+risklevel;
	handleMeasureService.getHandleMeasure(code,"code", function(data) {
		if (data != null) {
			var result = data.split('|_|');
			var handleMeasure = "";
			for ( var i = 0; i < result.length - 1; i++) {
				handleMeasure = result[i].split('!~!');
				convertStringDetail(handleMeasure[0], handleMeasure[3], divId);
			}
		} else {
			$("#" + divId).text("暂无处理措施");
		}
	});
}
function convertStringDetail(str1, str2, divId) {
	var s1 = str1;
	var s2 = str2;
	var str = /{*\{*\}*}/;
	if (str.exec(s1) && str.exec(s2)) {
		var o = eval('(' + s2 + ')');
		s1 = s1.replace(/\{([^\}]+)\}/g, function($0, $1) {
			if (o[$1])
				return o[$1];
			return '';
		});
	}
	if (s1 == "") {
		$("#" + divId).html($("#" + divId).html() + str1 + '<br/>');
	} else {
		$("#" + divId).html($("#" + divId).html() + s1 + '<br/>');
	}
}

function getHandleMeasureUpdate(code) {
	var s1 = "", s2 = "", divId = "handleMeasureDiv", inputId = "handle", str = /{*\{*\}*}/;
	var f1 = "", f2 = "";
	var execParam = $("#handleMeasureExecParam").val();
	identifyHandleMeasureService
			.getHandleMeasure(
					code,
					execParam,
					function(data) {
						$("#handleMeasureDiv").html("");
						if (data == null || data == "") {
							getHandleMeasure("", "");
							return;
						}
						var result = data.split('|_|');
						var handleMeasure = "";
						handleMeasureService
								.getHandleMeasure(
										"",
										"",
										function(data1) {
											if (data1 != null && data1 != "") {
												$("#handleMeasureDiv").html("");
												var result1 = data1
														.split('|_|');
												var handleMeasure = "";
												var flag = "";
												for ( var i = 0; i < result.length; i++) {
													handleMeasure = result[i]
															.split('!~!');
													s1 = handleMeasure[0];
													s2 = handleMeasure[3];
													for ( var s = 0; s < result1.length - 1; s++) {
														handleMeasure1 = result1[s]
																.split('!~!');
														f1 = handleMeasure1[0];
														f2 = handleMeasure1[3];
														if (handleMeasure[1] == handleMeasure1[1]) {
															flag += handleMeasure[1]
																	+ ",";
															if (str.exec(s1)
																	&& str
																			.exec(s2)) {
																var o = eval('('
																		+ s2
																		+ ')');
																s1 = s1
																		.replace(
																				/\{([^\}]+)\}/g,
																				function(
																						$0,
																						$1) {
																					if (o[$1])
																						return '<input type="text" class="myClass"  style="border-color: #878787; border-style: solid; border-top-width: 0px; border-right-width: 0px; border-bottom-width: 1px; border-left-width: 0px;text-align:center" size="10" maxlength="10" id="'
																								+ $1
																								+ '" name="'
																								+ inputId
																								+ handleMeasure[2]
																								+ '" value="'
																								+ o[$1]
																								+ '" />';
																					return '';
																				});
															} else {
																s1 = s1
																		.replace(
																				/\{([^\}]+)\}/g,
																				function(
																						$0,
																						$1) {
																					if (o[$1])
																						return o[$1];
																					return '';
																				});
															}
															var msg = '<input type="checkbox" onclick="chkText($(this).val())" checked="checked" id="'
																	+ handleMeasure[2]
																	+ '" name="'
																	+ inputId
																	+ '" value="'
																	+ inputId
																	+ handleMeasure[2]
																	+ '"/>';
															if (s1 == "") {
																$("#" + divId)
																		.html(
																				$(
																						"#"
																								+ divId)
																						.html()
																						+ msg
																						+ handleMeasure1[0]
																						+ '<br/>');
															} else {
																$("#" + divId)
																		.html(
																				$(
																						"#"
																								+ divId)
																						.html()
																						+ msg
																						+ s1
																						+ '<br/>');
															}
														}
													}
												}
												for ( var a = 0; a < result1.length - 1; a++) {
													handleMeasure1 = result1[a]
															.split('!~!');
													f1 = handleMeasure1[0];
													f2 = handleMeasure1[3];
													var v = 0;
													var flags = flag.split(',');
													for ( var f = 0; f < flags.length; f++) {
														if (handleMeasure1[1] != flags[f]) {
															v++;
														}
														if (v == flags.length) {
															convertStringUpdate(
																	f1,
																	f2,
																	divId,
																	inputId,
																	handleMeasure1[2]);
														}
													}
												}
											} else {
												$("#handleMeasureDiv").text(
														"暂无处理措施");
											}
										});
					});
}
function convertStringUpdate(str1, str2, divId, inputId, id) {
	var s1 = str1;
	var s2 = str2;
	var str = /{*\{*\}*}/;
	var msg = ""
	s1 = s1
			.replace(
					/\{([^\}]+)\}/g,
					function($0, $1) {
						return '<input type="text" disabled="disabled" class="'
								+ id
								+ '"  style="border-color: #878787; border-style: solid; border-top-width: 0px; border-right-width: 0px; border-bottom-width: 1px; border-left-width: 0px;text-align:center" size="10" maxlength="10" id="'
								+ $1 + '" name="' + inputId + id
								+ '" value="" />';
					});
	msg = '<input type="checkbox" onclick="chkText($(this).val())" id="' + id + '" name="' + inputId
			+ '" value="' + inputId + id + '"/>';
	if (s1 == "") {
		$("#" + divId).html($("#" + divId).html() + msg + str1 + '<br/>');
	} else {
		$("#" + divId).html($("#" + divId).html() + msg + s1 + '<br/>');
	}
}
function convertExecParamUpdate() {
	var message = "";
	var code = $("#handleCode").val();
	var riskcategoryname = $(
			"select[name=identifyHandleMeasure.riskCategoryName]").find(
			"option:selected").val();
	var risklevel = $("select[name=identifyHandleMeasure.riskLevel]").find(
			"option:selected").val();
	if (riskcategoryname == "") {
		message += "风险类别名称不能为空<br/>";
	}
	if (risklevel == "") {
		message += "风险级别不能为空<br/>";
	}
	if (message != "") {
		showMsgDiv(message);
		return false;
	}
	identifyHandleMeasureService
			.exist(
					riskcategoryname,
					risklevel,
					code,
					function(data) {
						var execParam = "", execParams = "", handleCodes = "";
						;
						if (data == "false") {
							showMsgDiv("风险类别加风险级别的组合不能重复");
							return false;
						}
						$("input:checked")
								.each(
										function(index) {
											execParam = $(this).attr("name")
													+ ";" + $(this).attr("id")
													+ ";" + "{";
											handleCodes += $(this).attr("id")
													+ ",";
											$(
													"input:[name='"
															+ $(this).val()
															+ "']")
													.each(
															function(index) {
																if($(this).val()==""){message="参数不能为空";return false;
																}else if($(this).val().indexOf(" ")>-1){message+="参数中不能有空格<br/>";return false;
														    	}else if(isNaN($(this).val())){message+="参数必须为数字<br/>";return false;
														    	}else if($(this).val().indexOf("-")>-1){return false;message+="参数必须不能为负数<br/>";
														    	}
																execParam += "\""
																		+ $(
																				this)
																				.attr(
																						"id")
																		+ "\":\""
																		+ $(
																				this)
																				.val()
																		+ "\",";
															});
											if (execParam != $(this).attr(
													"name")
													+ ";"
													+ $(this).attr("id")
													+ ";" + "{") {
												execParam = execParam
														.substring(
																0,
																execParam.length - 1);
												execParam += "};";
												execParams += execParam;
											}
										});
						if (execParams != "") {
							if (check(message, execParams.substring(0,
									execParams.length - 1))) {
								return;
							}
						}
						if (message != "") {
							showMsgDiv(message);
						} else {
							execParams = execParams.substring(0,
									execParams.length - 1);
							var params = execParams.split(";");
							var handle = "";
							for ( var i = 0; i < params.length; i++) {
								if (params[i] == "handle") {
									handle += params[i + 1] + ";"
											+ params[i + 2] + ";";
								}
								i++;
								i++;
							}
							handle = handle.substring(0, handle.length - 1)
									.toString();
							handleCode = handleCodes.substring(0,
									handleCodes.length - 1).toString();
							$("input[id=handle]").val(handle);
							$("input[id=handleCode]").val(handleCode + "");
							mySubmitConfirm();
						}
					});
}
function checkAdd(message, param) {
	var params = param;
	var execParam = "";
	var property = "";
	params = params.split(";");
	for ( var a = 0; a < params.length; a++) {
		a++;
		var p = params[a].substring(1, params[a].length - 1);
		p = p.split('",');
		if (p.length > 1) {
			for ( var b = 0; b < p.length; b++) {
				property = p[b].split(":")[0];
				if (b < p.length - 1) {
					execParam = p[b].substring(1, p[b].length).split(":")[1];
				} else {
					execParam = p[b].substring(1, p[b].length - 1).split(":")[1];
				}
				execParam = execParam.substring(1, execParam.length);
				message += checkString(message, property, execParam);
			}
		} else {
			property = params[a].substring(1, params[a].length - 1).split(":")[0];
			execParam = params[a].substring(1, params[a].length - 1).split(":")[1];
			execParam = execParam.substring(1, execParam.length - 1);
			message += checkString(message, property, execParam);
		}
		if (message != "") {
			showMsgDiv(message);
			return true;
		}
	}
}
function check(message, param) {
	var params = param;
	var execParam = "";
	var property = "";
	params = params.split(";");
	for ( var a = 0; a < params.length; a++) {
		a++;
		a++;
		var p = params[a].substring(1, params[a].length - 1);
		p = p.split('",');
		if (p.length > 1) {
			for ( var b = 0; b < p.length; b++) {
				property = p[b].split(":")[0];
				if (b < p.length - 1) {
					execParam = p[b].substring(1, p[b].length).split(":")[1];
				} else {
					execParam = p[b].substring(1, p[b].length - 1).split(":")[1];
				}
				execParam = execParam.substring(1, execParam.length);
				message += checkString(message, property, execParam);
			}
		} else {
			property = params[a].substring(1, params[a].length - 1).split(":")[0];
			execParam = params[a].substring(1, params[a].length - 1).split(":")[1];
			execParam = execParam.substring(1, execParam.length - 1);
			message += checkString(message, property, execParam);
		}
		if (message != "") {
			showMsgDiv(message);
			return true;
		}
	}
}
function checkString(msg, property, execParam) {
	// alert(msg+"-->"+property+"-->"+execParam);
	var message = "";
	if (property.substring(1, 6) == "money"
			|| property.substring(property.length - 6, property.length - 1) == "Money") {
		if(isNaN(execParam)){
			message+="MONEY参数格式必须为数字<br/>";
		}else if(execParam.indexOf(" ")>-1){
			message+="MONEY参数中不能有空格<br/>";
		}else if(execParam.indexOf("-")>-1){
			message+="MONEY参数不能为负数<br/>";
		}else{
			if(isNaN(execParam)){
				message+="MONEY参数格式必须为数字<br/>";
			}else{
				var s=execParam.split(".");
				if(s.length>1){
					if(s[1].length>2){
						message+="MONEY参数格式小数位不能超过两位<br/>";
					}
					if(s[0].length>10){
						message+="MONEY参数整数位不能超过十位<br/>";
					}
				}else{
					if(execParam.length>10){
						message+="MONEY参数整数位不能超过十位<br/>";
					}else if(execParam.substring(0,1)==0){
						message+="MONEY参数不能以0开头<br/>";
					}
				}
			}
		}
	}
	if(property.substring(1, 4)=="day"||property.substring(property.length-4, property.length-1)=="Day"){
		if(isNaN(execParam)){
			message+="DAY参数格式必须为整数格式<br/>";
		}else if(execParam<=0){
			message+="DAY参数不能为0或负数<br/>";
		}else if(execParam.length>3){
			message+="3DAY参数不能大于3位<br/>";
		}else if(execParam.substring(0,1)==0){
			message+="DAY参数不能以0开头<br/>";
		}else{
			if(execParam.indexOf(".")>-1){
				message+="DAY参数格式必须为整数格式<br/>";
			}
		}
	}
	if(property.substring(1, 6)=="scale"||property.substring(property.length-6, property.length-1)=="Scale"){
		if(isNaN(execParam)){
			message+="SCALE参数格式必须为数字<br/>";
		}else if(execParam<0||execParam>100){
			message+="SCALE参数不能小于0并且不能大于100<br/>";
		}else{
			var s=execParam.split(".");
			if(s.length>1){
				if(s[1].length>2){
					message+="SCALE参数格式小数位不能超过两位<br/>";
				}
			}else{
				if(execParam.length>3){
					message+="SCALE参数整数位不能超过三位<br/>";
				}else if(execParam.substring(0,1)==0){
					message+="SCALE参数不能以0开头<br/>";
				}
			}
		}
	}
	if(property.substring(1, 6)=="count"||property.substring(property.length-6, property.length-1)=="Count"){
		if(isNaN(execParam)){
			message+="COUNT参数格式必须为整数格式<br/>";
		}else if(execParam<=0){
			message+="COUNT参数不能小于等于0<br/>";
		}else{
			if(execParam.indexOf(".")>-1){
				message+="COUNT参数格式必须为整数格式<br/>";
			}else{
				if(execParam.length>5){
					message+="COUNT参数格式不能大于5位数<br/>";
				}
			}
		}
	}
	// if(property.substring(1, property.length-1)=="mcc"||property.substring(4,
	// property.length-1)=="Mcc"||property.substring(1,
	// property.length-1)=="MCC"){
	// if(isNaN(execParam)){
	// message+="MCC参数格式必须为整数格式<br/>";
	// }else{
	// if(execParam.indexOf(".")>-1){
	// message+="MCC参数格式必须为整数格式<br/>";
	// }
	// }
	// }
	msg = message;
	return msg;
}
function chkText(name){
	$("input:[name='" +name+ "']").each(function(index){
		if($("input:[name='" +name+ "']").attr("disabled")){$("input:[name='" +name+ "']").attr("disabled","");return false;}
		else{$("input:[name='" +name+ "']").attr("disabled","disabled");$("input:[name='" +name+ "']").val("");return false;}
	});
}