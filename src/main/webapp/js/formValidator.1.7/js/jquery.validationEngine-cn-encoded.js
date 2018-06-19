
(function($) {
	$.fn.validationEngineLanguage = function() {};
	$.validationEngineLanguage = {
		newLang: function() {
			$.validationEngineLanguage.allRules = {
				// Add your regex rules here, you can take telephone as an example
					"required":{ 
						"regex":"none",
						"alertText":"* \u6b64\u9879\u5185\u5bb9\u5fc5\u987b\u586b\u5199\u6216\u9009\u62e9",
						"alertTextCheckboxMultiple":"* \u8bf7\u9009\u62e9\u4e00\u4e2a\u9009\u9879",
						"alertTextCheckboxe":"* \u8bf7\u81f3\u5c11\u9009\u62e9\u4e00\u4e2a\u9009\u9879"
					},
					"length":{
						"regex":"none",
						"alertText":"* \u60a8\u8f93\u5165\u7684\u5b57\u7b26\u4e32\u957f\u5ea6\u5e94\u8be5\u5728",
						"alertText2":" \u4e0e ",
						"alertText3": " \u4e4b\u95f4"
					},
					"maxCheckbox":{
						"regex":"none",
						"alertText":"* \u5bf9\u4e0d\u8d77\uff0c\u60a8\u9009\u62e9\u7684\u9009\u9879\u8fc7\u591a"
					},	
					"minCheckbox":{
						"regex":"none",
						"alertText":"* \u8bf7\u81f3\u5c11\u9009\u62e9 ",
						"alertText2":" \u9879"},	
					"confirm":{
						"regex":"none",
						"alertText":"* \u8bf7\u8f93\u5165\u4e0e\u4e4b\u524d\u503c\u76f8\u7b49\u7684\u503c"},			
					"email":{
						"regex":"/^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9]{2,4}$/",
						"alertText":"* \u8bf7\u8f93\u5165\u6b63\u786e\u7684\u90ae\u7bb1\u5730\u5740<span>\u4e3e\u4f8b: san.zhang@163.com </span>"},	
					"date":{
             			"regex":"/^[0-9]{4}\-\[0-9]{1,2}\-\[0-9]{1,2}$/",
             			"alertText":"* \u8bf7\u8f93\u5165\u6b63\u786e\u7684\u65e5\u671f <br /><span>\u4e3e\u4f8b: 2010-01-01 </span>"},
					"onlyNumber":{
						"regex":"/^\\d+(\\.\\d+)?$/",
						"alertText":"* \ufeff\u5bf9\u4e0d\u8d77 , \u6b64\u5904\u53ea\u80fd\u8f93\u5165\u6570\u5b57"},	
					"noSpecialCaracters":{
						"regex":"/^[0-9a-zA-Z]+$/",
						"alertText":"* \u8bf7\u4e0d\u8981\u8f93\u5165\u5305\u542b @ , # , $ \u7b49\u7279\u6b8a\u5b57\u7b26"},	
					"ajaxUser":{
						"file":"validateUser.php",
						"extraData":"name=eric",
						"alertTextOk":"* This user is available",	
						"alertTextLoad":"* Loading, please wait",
						"alertText":"* This user is already taken"},	
					"ajaxName":{
						"file":"validateUser.php",
						"alertText":"* This name is already taken",
						"alertTextOk":"* This name is available",	
						"alertTextLoad":"* Loading, please wait"},		
					"onlyLetter":{
						"regex":"/^[a-zA-Z\ \']+$/",
						"alertText":"* \u8bf7\u8f93\u5165\u53ea\u5305\u542b\u82f1\u6587\u5b57\u6bcd\u7684\u5b57\u7b26\u4e32"
					},
					"Letter":{ 
						"regex":"/^[a-zA-Z\\u4e00-\\u9fa5]+$/",
						"alertText":"* \u8bf7\u8f93\u5165\u5408\u6cd5\u7684\u540d\u5b57"
					},
					"ip":{ 
						"regex":"/^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])){3}$/", 
						"alertText":"\u8bf7\u8f93\u5165\u4e00\u4e2a\u5408\u6cd5\u7684IP\u5730\u5740<br /><span>\u4e3e\u4f8b: 126.125.147.146</span>"
					},
		  		    "china-citizen-id":{
						"regex":"/^(\\d{15}$)|(^\\d{17}([0-9]|X))$/", 
						"alertText":"\ufeff\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u8eab\u4efd\u8bc1\u53f7\u7801<br /><span>\u4e3e\u4f8b: 232332298702231087</span>"
					},
					"china-phone":{
						"regex":"/^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3}))?$/", 
								 
						"alertText":"<span>* \u8bf7\u8f93\u5165\u5408\u6cd5\u7684\u7535\u8bdd\u53f7\u7801<br />\u4e3e\u4f8b: (010)0-59017300-010 \u6216 (010)0-5901730-010</span>"	
					},
					"china-zipcode":{
						"regex":"/^[1-9]\\d{5}(?!\\d)$/", 
						"alertText":"* \u8bf7\u8f93\u5165\u6b63\u786e\u7684\u90ae\u7f16<br /><span>\u4e3e\u4f8b: 163700 </span>"	
					},
					"url":{
						"regex":"/^(http|https|ftp)\\:\\/\\/[a-z0-9\\-\\.]+\\.[a-z]{2,3}(:[a-z0-9]*)?\\/?([a-z0-9\\-\\._\\?\\,\\'\\/\\\\\+&amp;%\\$#\\=~])*$/", 
						"alertText":"* \ufeff\u8bf7\u8f93\u5165\u6b63\u786e\u7684URL\u5730\u5740<br /><span>\u4e3e\u4f8b: http://www.yeepay.com</span>"
					},
					"min-url":{
						"regex":"/^[a-z0-9\\-\\.]+\\.[a-z]{2,3}(:[a-z0-9]*)?\\/?([a-z0-9\\-\\._\\?\\,\\'\\/\\\\\+&amp;%\\$#\\=~])*$/", 
						"alertText":"* \ufeff\u8bf7\u8f93\u5165\u6b63\u786e\u7684URL\u5730\u5740<br /><span>\u4e3e\u4f8b: toRoleAdd.action</span>"
					},
					"telephone":{
						"regex":"/^1[3,5,8]\\d{9}$/",
						"alertText":"* \u8bf7\u8f93\u5165\u5408\u6cd5\u7684\u624b\u673a\u53f7\u7801"
					},
					"excel":{
						"regex":"/^.*?\\.(xls|xlsx)$/",
						"alertText":"* \ufeff\u8bf7\u9009\u62e9\u6b63\u786e\u7684Excel\u6587\u4ef6"
					}
					
			}	
		}
	}
})(jQuery);

$(document).ready(function() {	
	$.validationEngineLanguage.newLang()
});