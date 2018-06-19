<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType= "text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="vlh" uri="/WEB-INF/tld/valuelist.tld" %>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
<%@ taglib prefix="dict" uri="/dictionary" %>
<%@ taglib prefix="boss" uri="/WEB-INF/tld/boss.tld" %>
<%@ page import="com.pay.common.util.*"%>
<%@ page import="com.pay.boss.entity.*"%>
<%@ page import="java.util.*"%>

<link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/js/jquery/UI/jquery-ui-1.8.5.custom.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/js/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/js/jquery/UI/block.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/js/formValidator.1.7/css/validationEngine.jquery.css" type="text/css" media="screen" title="no title" charset="utf-8" rel="stylesheet"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery.idTabs.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/UI/jquery-ui-1.8.5.custom.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/UI/jquery.blockUI.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/datetime.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/formValidator.1.7/js/jquery.validationEngine-cn-encoded.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/formValidator.1.7/js/jquery.validationEngine.js" ></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/report/JSClass/FusionCharts.js'></script>

<!-- tree -->
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery/EasyUI/themes/gray/easyui.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery/EasyUI/themes/gray/tree.css"/>
		--%>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/EasyUI/plugins/jquery.tree.js"></script> --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/EasyUI/plugins/jquery.draggable.js"></script>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/orgTree.js" charset="utf-8"></script> --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/process.js" charset="utf-8"></script>
<script type="text/javascript">
	Array.prototype.indexOf = function(val) {
		for (var i = 0; i < this.length; i++) {
			if (this[i] == val) return i;
		}
		return -1;
	};
	Array.prototype.remove = function(val) {
		var index = this.indexOf(val);
		if (index > -1) {
			this.splice(index, 1);
		}
	};
	function inArray(needle,array){
			if(typeof needle=="string"||typeof needle=="number"){
				var len=array.length;
				for(var i=0;i<len;i++){
					if(needle===array[i]){
						return true;
					}
				}
				return false;
			}
	};

	$(function(){
		$("form").submit(function(){
			$(":input").each(function(){
				$(this).val($.trim($(this).val()));
			});
		});
	});
</script>

<%@ include file="dialog.jsp" %>