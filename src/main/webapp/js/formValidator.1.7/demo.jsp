<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
  <head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<title>验证框架的标准使用方法介绍</title>
	<link rel="stylesheet" type="text/css"  href="<%=request.getContextPath()%>/js/formValidator.1.7/css/validationEngine.jquery.css"/>
	<script src="<%=request.getContextPath()%>/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/formValidator.1.7/js/jquery.validationEngine-cn-encoded.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/formValidator.1.7/js/jquery.validationEngine.js" type="text/javascript"></script>
	<script>	
		/**验证框架使用的标准写法*/		
		$(document).ready(function() {
			
			/**添加对应的验证规则*/
			$("#firstname").addClass( "validate[optional,custom[onlyLetter],length[0,100]] text-input" ) ;
			$("#lastname").addClass( "validate[required,custom[onlyLetter],length[0,100]] text-input" ) ;
			
			$("#radio1").addClass("validate[required] radio") ;
			$("#radio2").addClass("validate[required] radio") ;
			$("#radio3").addClass("validate[required] radio") ;
			
			$("#date").addClass("validate[required,custom[date]] text-input") ;
			$("#sport").addClass("validate[required]") ;
			$("#age").addClass("validate[required,custom[onlyNumber],length[0,3]] text-input") ;
			$("#telephone").addClass("validate[required,custom[telephone]] text-input") ;
			$("#email").addClass("validate[required,custom[email]] text-input") ;
			$("#password").addClass("validate[required,length[6,11]] text-input") ;
			$("#password2").addClass("validate[required,confirm[password]] text-input") ;
		    
		    /**调用表单验证的方法（要注意这个应该在添加过验证规则之后再进行表单验证）*/
			$("#formID").validationEngine() ;
			
		});
	</script>	
  </head>
  <body>
	<form id="formID" class="formular" method="post" action="">
	   <fieldset>
		  <legend>Demo Validator1.7</legend>
			<label>
			  <span>demo input 1 :</span>
				<input  id="firstname" value="cedric" type="text" name="firstname"/>
			</label>
			<label>
			  <span>demo input 2 :</span>
				<input id="lastname" value="" type="text" name="lastname"  />
			</label>
			<div>
			    <span>单选框 : <br /></span>
				<span>radio 1: </span>
				<input type="radio" name="data"  id="radio1"  value="5">
				<span>radio 2: </span>
				<input type="radio" name="data"  id="radio2"  value="3"/>
				<span>radio 3: </span>
				<input radio" type="radio" name="data"  id="radio3"  value="9"/>
			 </div>
			 <label>
				<span>日期格式 : (YYYY-MM-DD)</span>
				<input id="date" type="text" name="date"/>
			 </label>
			 <label>
			    <span>下拉表单 :</span>
				<select name="sport" id="sport" id="sport"  >
					<option value="">Choose a sport</option>
					<option value="option1">Tennis</option>
					<option value="option2">Football</option>
					<option value="option3">Golf</option>
				</select>
			  </label>
			  <label>
				 <span>数字大小 : </span>
				 <input id="age" type="text" name="age"/>
			  </label>
			  <label>
				 <span>电话号码 : </span>
				 <input id="telephone" type="text" name="telephone"/>
			  </label>
			  <label>
					<span>邮箱地址 : </span>
					<input type="text" name="email" id="email"/>
				</label>
			</fieldset>
			<fieldset>
				<legend>密码一致验证</legend>
				<label>
					<span>Password : </span>
					<input id="password" type="password" name="password"/>
				</label>
				<label>
					<span>Confirm password : </span>
					<input id="password2" type="password" name="password2"/>
				</label>
			</fieldset>
			<input class="submit" type="submit" value="提交"/>
			<hr/>
		</form>
		
	</body>
</html>








<style type="text/css">
body{	background:#ececec;}

form.formular {
	font-family: tahoma, verdana, "sans-serif";
	font-size: 12px;
	padding: 20px;
	border: 1px solid #A5A8B8;

	width:300px;
	margin-left:300px;
}

.formular fieldset {
	margin-top: 20px;
	padding : 15px;
	border: 1px solid #B5B8C8;
	
}

.formular legend {
	font-size: 12px;
	color: #15428B;
	font-weight: 900;
}

.formular fieldset label {
	float: none;
	text-align: inherit;
	width: auto;
}

.formular label span {
	color: #000;
}

.formular input, .formular select, .formular textarea {
	display : block;
	margin-bottom: 5px;
}

.formular .text-input {
	width: 250px;
	color: #555;
	padding: 4px;
	border: 1px solid #B5B8C8;
	font-size: 14px;
	margin-top: 4px;
	background: #FFF url('/img/form/text-bg.gif') repeat-x;
	
}
.formular textarea {
	width: 250px;
	height:70px;
	color: #555;
	padding: 4px;
	border: 1px solid #B5B8C8;
	font-size: 14px;
	margin-top: 4px;
	background: #FFF url('/img/form/text-bg.gif') repeat-x;
	
}
.formular .infos {
	background: #FFF;
	color: #333;
	font-size: 12px;
	padding: 10px;
	margin-bottom: 10px;
}

.formular span.checkbox, .formular .checkbox {
	display: inline;
}

.formular .submit {
	background: url('/img/form/button-bg.png') repeat-x;
	border: 1px solid #AAA;
	padding: 4px;
	margin-top: 20px;
	float: right;
	text-decoration: none;
	cursor:pointer;
}

.formular hr {
	clear: both;
	visibility: hidden;
}

.formular .fc-error {
	width: 350px;
	color: 555;
	padding: 4px;
	border: 1px solid #B5B8C8;
	font-size: 12px;
	margin-bottom: 15px;
	background: #FFEAEA;
}
</style>