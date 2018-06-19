	function dateChange(name, timeStart, timeEnd){
		var beginTimeObject = document.getElementById(timeStart);
		var endTimeObject = document.getElementById(timeEnd);
		var beginTime;
		var endTime;
		var now = new Date();
		var month = now.getMonth() + 1;
		var year = now.getFullYear();
		var day = now.getDate();
		var wday = now.getDay();
		switch(name){
			case "tweek": //本周
				var day  = now.getDay();
				now.setDate(now.getDate() - (day -1));
				beginTime = formatDate(now);
				now.setDate(now.getDate() + 6);
				endTime = formatDate(now);
			break;
			case "tmonth":  //本月
				now.setDate(1);
				beginTime = formatDate(now);
			    var nowDate = new Date(now.getFullYear(), now.getMonth() + 1, 0);
				endTime = formatDate(nowDate);
			break;
			case "tquarter":   //本季
				var m = now.getMonth() + 1;
				var q = parseInt((m + 2 ) / 3 ); //得到第几季
				m = q * 3 - 2;  //得到季的首月份
				now.setMonth(m-1);
				now.setDate(1);
				beginTime = formatDate(now);
				now.setMonth(now.getMonth() + 3);
				now.setDate(0);
				endTime = formatDate(now);
			break;
			case "tyear":    //本年
				now.setMonth(0);
				now.setDate(1);
				beginTime = formatDate(now);
				
				now.setMonth(11);
				now.setDate(31);
				endTime = formatDate(now);        
			break;
			case "today":    //今天
				beginTime = formatDate(now);
				endTime = beginTime;
			break;
			case "yesterday":  //昨日
			    var day  = now.getDay();
				now.setDate(now.getDate()-1);
				beginTime = formatDate(now);
				endTime = beginTime;
			break;	
			case "pweek":    //上周
				var day  = now.getDay();
				now.setDate(now.getDate() - (day -1) - 7 );
				beginTime = formatDate(now);
				now.setDate(now.getDate() + 6);
				endTime = formatDate(now);
			break;
			case "pmonth":    //上月
				now.setDate(1);
				now.setMonth(now.getMonth() -1 );
				beginTime = formatDate(now);
				//得到本月最后一天
				    var nowDate = new Date(now.getFullYear(), now.getMonth() + 1, 0);
				  
				endTime = formatDate(nowDate);
			break;
			case "pquarter":    //上季
				var m = now.getMonth() + 1;
				var q = parseInt((m + 2 ) / 3 ); //得到第几季
				m = q * 3 - 2;  //得到季的首月份
				m = m-3 ; //上季
				now.setMonth(m-1);
				now.setDate(1);
				beginTime = formatDate(now);
				now.setMonth(now.getMonth() + 3);
				now.setDate(0);
				endTime = formatDate(now);        
			break;
			case "pyear":    //去年
				now.setFullYear(now.getFullYear() -1 );
				now.setMonth(0);
				now.setDate(1);
				beginTime = formatDate(now);
				now.setMonth(11);
				now.setDate(31);
				endTime = formatDate(now);            
			break;
			case "p2week":    //上二周
				var day  = now.getDay();
				now.setDate(now.getDate() - (day -1) - 7 *2);
				beginTime = formatDate(now);
				now.setDate(now.getDate() + 6 + 7);
				endTime = formatDate(now);            
			break;
			case "p2month":    //上二月
				now.setDate(1);
				now.setMonth(now.getMonth() -1*2 );
				beginTime = formatDate(now);
				
				now.setMonth(now.getMonth() + 2);
				now.setDate(0);
				endTime = formatDate(now);
			break;
			case "customize":    //自定义
				beginTime = "";
				endTime = "";
			break;
		}
		beginTimeObject.value = beginTime;
		endTimeObject.value = endTime;
	}
		
	function formatDate(now){
		var month = now.getMonth()+1;
		var day = now.getDate();
		var year = now.getFullYear();
		var wday = now.getDay();
		return year+"-"+month+"-"+day ; 
	}	
	
	/**  
	*功能:dwr 时间显示挺无奈的，思考一下之后吗，写了格式化时间  
	*示例:DateUtil.Convert("yyyy/MM/dd","Thu Nov 9 20:30:37 UTC+0800 2006 ");  
	*返回:2010/01/26 QQ:962589149  
	*/  
	//定义一个类 
	function DateUtils(){};//空的构造函数 
	
	DateUtils.prototype.Convert=function(fmt,date){ 
		var result,d,arr_d; 
	    var patrn_now_1=/^y{4}-M{2}-d{2}\sh{2}:m{2}:s{2}$/;   
	    var patrn_now_11=/^y{4}-M{1,2}-d{1,2}\sh{1,2}:m{1,2}:s{1,2}$/; 
	    var patrn_now_2=/^y{4}\/M{2}\/d{2}\sh{2}:m{2}:s{2}$/;   
	    var patrn_now_22=/^y{4}\/M{1,2}\/d{1,2}\sh{1,2}:m{1,2}:s{1,2}$/; 
	    var patrn_now_3=/^y{4}年M{2}月d{2}日\sh{2}时m{2}分s{2}秒$/;   
	    var patrn_now_33=/^y{4}年M{1,2}月d{1,2}日\sh{1,2}时m{1,2}分s{1,2}秒$/; 
	    var patrn_date_1=/^y{4}-M{2}-d{2}$/;   
	    var patrn_date_11=/^y{4}-M{1,2}-d{1,2}$/; 
	    var patrn_date_2=/^y{4}\/M{2}\/d{2}$/; 
	    var patrn_date_22=/^y{4}\/M{1,2}\/d{1,2}$/; 
	    var patrn_date_3=/^y{4}年M{2}月d{2}日$/;   
	    var patrn_date_33=/^y{4}年M{1,2}月d{1,2}日$/; 
	    var patrn_time_1=/^h{2}:m{2}:s{2}$/;   
	    var patrn_time_11=/^h{1,2}:m{1,2}:s{1,2}$/;   
	    var patrn_time_2=/^h{2}时m{2}分s{2}秒$/;   
	    var patrn_time_22=/^h{1,2}时m{1,2}分s{1,2}秒$/;  
	    
	    if(!fmt){fmt="yyyy/MM/dd hh:mm:ss";}   
	    if(date){   
	        d=new Date(date);   
	        if(isNaN(d)){   
	            alert("参数非法!"); 
	            return;}   
	    }else{   
	        d=new Date();   
	    } 
	    if(patrn_now_1.test(fmt))   
	    {   
	        arr_d=splitDate(d,true);   
	        result=arr_d.yyyy+"-"+arr_d.MM+"-"+arr_d.dd+" "+arr_d.hh+":"+arr_d.mm+":"+arr_d.ss;   
	    }   
	    else if(patrn_now_11.test(fmt))   
	    {   
	        arr_d=splitDate(d);   
	        result=arr_d.yyyy+"-"+arr_d.MM+"-"+arr_d.dd+" "+arr_d.hh+":"+arr_d.mm+":"+arr_d.ss;   
	    }   
	    else if(patrn_now_2.test(fmt))   
	    {   
	        arr_d=splitDate(d,true);   
	        result=arr_d.yyyy+"/"+arr_d.MM+"/"+arr_d.dd+" "+arr_d.hh+":"+arr_d.mm+":"+arr_d.ss;   
	    }   
	    else if(patrn_now_22.test(fmt))   
	    {   
	        arr_d=splitDate(d);   
	        result=arr_d.yyyy+"/"+arr_d.MM+"/"+arr_d.dd+" "+arr_d.hh+":"+arr_d.mm+":"+arr_d.ss;   
	    }   
	    else if(patrn_now_3.test(fmt))   
	    {   
	        arr_d=splitDate(d,true);   
	        result=arr_d.yyyy+"年"+arr_d.MM+"月"+arr_d.dd+"日"+" "+arr_d.hh+"时"+arr_d.mm+"分"+arr_d.ss+"秒";   
	    }   
	    else if(patrn_now_33.test(fmt))   
	    {   
	        arr_d=splitDate(d);   
	        result=arr_d.yyyy+"年"+arr_d.MM+"月"+arr_d.dd+"日"+" "+arr_d.hh+"时"+arr_d.mm+"分"+arr_d.ss+"秒";   
	    }   
	       
	    else if(patrn_date_1.test(fmt))   
	    {   
	        arr_d=splitDate(d,true);   
	        result=arr_d.yyyy+"-"+arr_d.MM+"-"+arr_d.dd;   
	    }   
	    else if(patrn_date_11.test(fmt))   
	    {   
	        arr_d=splitDate(d);   
	        result=arr_d.yyyy+"-"+arr_d.MM+"-"+arr_d.dd;   
	    }   
	    else if(patrn_date_2.test(fmt))   
	    {   
	        arr_d=splitDate(d,true);   
	        result=arr_d.yyyy+"/"+arr_d.MM+"/"+arr_d.dd;   
	    }   
	    else if(patrn_date_22.test(fmt))   
	    {   
	        arr_d=splitDate(d);   
	        result=arr_d.yyyy+"/"+arr_d.MM+"/"+arr_d.dd;   
	    }   
	    else if(patrn_date_3.test(fmt))   
	    {   
	        arr_d=splitDate(d,true);   
	        result=arr_d.yyyy+"年"+arr_d.MM+"月"+arr_d.dd+"日";   
	    }   
	    else if(patrn_date_33.test(fmt))   
	    {   
	        arr_d=splitDate(d);   
	        result=arr_d.yyyy+"年"+arr_d.MM+"月"+arr_d.dd+"日";   
	    }   
	    else if(patrn_time_1.test(fmt)){   
	        arr_d=splitDate(d,true);   
	        result=arr_d.hh+":"+arr_d.mm+":"+arr_d.ss;   
	    }   
	    else if(patrn_time_11.test(fmt)){   
	        arr_d=splitDate(d);   
	        result=arr_d.hh+":"+arr_d.mm+":"+arr_d.ss;   
	    }   
	    else if(patrn_time_2.test(fmt)){   
	        arr_d=splitDate(d,true);   
	        result=arr_d.hh+"时"+arr_d.mm+"分"+arr_d.ss+"秒";   
	    }   
	    else if(patrn_time_22.test(fmt)){
	        arr_d=splitDate(d);   
	        result=arr_d.hh+"时"+arr_d.mm+"分"+arr_d.ss+"秒";   
	    } 
	    else{   
	        alert("没有匹配的时间格式!");   
	        return;   
	    } 
		//alert(result); 
	   	return result;
	} 
	
	function splitDate(d,isZero){   
	    var yyyy,MM,dd,hh,mm,ss;  
	    
	    if(isZero){   
	         yyyy=d.getYear();   
	         MM=(d.getMonth()+1)<10?"0"+(d.getMonth()+1):d.getMonth()+1;   
	         dd=d.getDate()<10?"0"+d.getDate():d.getDate();   
	         hh=d.getHours()<10?"0"+d.getHours():d.getHours();   
	         mm=d.getMinutes()<10?"0"+d.getMinutes():d.getMinutes();   
	         ss=d.getSeconds()<10?"0"+d.getSeconds():d.getSeconds();   
	    }else{   
	         yyyy=d.getYear();   
	         MM=d.getMonth()+1;   
	         dd=d.getDate();   
	         hh=d.getHours();   
	         mm=d.getMinutes();   
	         ss=d.getSeconds();     
	    } 
	    
	    return {"yyyy":yyyy,"MM":MM,"dd":dd,"hh":hh,"mm":mm,"ss":ss};     
	} 