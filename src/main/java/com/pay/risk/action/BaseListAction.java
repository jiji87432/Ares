package com.pay.risk.action;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.mlw.vlh.ValueList;
import net.mlw.vlh.ValueListInfo;
import net.mlw.vlh.web.mvc.ValueListHandlerHelper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pay.common.util.PreventAttackUtil;
import com.riskrule.runmvel.RunRule;

/**
 * 公共的ValueList查询，菜单链接转向
 * @author qinji.xu
 *
 */
@Controller
public class BaseListAction
{
	@Resource
	public ValueListHandlerHelper valueListHelper;
	private Logger log=Logger.getLogger(BaseListAction.class);
	/**
	 * 多个queryId的查询
	 */
	protected String[] queryIds;

	/**
	 *
	 * 通用的，返回 target的页面
	 *
	 * @param target
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(value = "simpleJump.action")
	public String initQrycondition(String target)
	{
		return target;
	}

	/**
	 *
	 * 获取列表结果，返回 responseView的页面
	 *
	 * @param request
	 * @param queryId
	 * @param responseView 返回视图名
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "result")
	public String getResult(HttpServletRequest request, String queryId, String responseView)
	{
		Map requestParam = retrieveParams(request.getParameterMap());
		/*mysql数据库的话，需要计算查询起始行*/
		if(requestParam.get("pagingNumberPer")!=null&&requestParam.get("pagingPage")!=null){
			int pagingNumberPer = Integer.parseInt(requestParam.get("pagingNumberPer").toString());
			int pagingPage = Integer.parseInt(requestParam.get("pagingPage").toString());
			int StartIndex = pagingNumberPer*(pagingPage-1);
			requestParam.put("StartIndex", StartIndex);
		}else{
			requestParam.put("StartIndex", 0);
		}

		queryIds = queryId.split(",");
		for (int i = 0; i < queryIds.length; i++)
		{
			ValueList valueList = this.getValueList(queryIds[i], requestParam);
			request.setAttribute(queryIds[i], valueList);
			log.info("第"+i+"页");
		}
		return responseView;
	}

	/**
	 *
	 * 导出功能 页面名称与 queryIds[0]一致
	 *
	 * @param request
	 * @param queryId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "export")
	public String getExport(HttpServletRequest request, String queryId)
	{
		Map requestParam = retrieveParams(request.getParameterMap());
		queryIds = queryId.split(",");
		for (int i = 0; i < queryIds.length; i++)
		{
			ValueList valueList = this.getValueList(queryIds[i], requestParam);
			request.setAttribute(queryIds[i], valueList);
		}
		return queryIds[0];
	}

	/**
	 *
	 * 小计功能 页面名称与 queryIds[0]一致
	 *
	 * @param request
	 * @param queryId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "total")
	public String getTotal(HttpServletRequest request, String queryId)
	{
		Map requestParam = retrieveParams(request.getParameterMap());
		queryIds = queryId.split(",");
		for (int i = 0; i < queryIds.length; i++)
		{
			ValueList valueList = this.getValueList(queryIds[i], requestParam);
			request.setAttribute(queryIds[i], valueList);
		}
		return queryIds[0];
	}

	/**
	 *
	 * 调用valuelist查询
	 *
	 * @param queryId
	 * @param params
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("rawtypes")
	protected ValueList getValueList(String queryId, Map params)
	{
		ValueListInfo info = new ValueListInfo(params);
		/**
		 * 将页码设为1的情况： 1、页码为空 2、查看合计，只有一条数据
		 */
		if (params.get("pagingPage") == null)
		{
			info.setPagingPage(1);
		}
		ValueList valueList = valueListHelper.getValueList(queryId, info);
		return valueList;
	}

	/**
	 *
	 * 原始请求参数转换
	 *
	 * @param requestMap
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected static Map retrieveParams(Map requestMap)
	{
		Map resultMap = new HashMap();
		for (Object key : requestMap.keySet())
		{
			Object value = requestMap.get(key);
			if (value != null)
			{
				if (value.getClass().isArray())
				{
					int length = Array.getLength(value);
					if (length == 1)
					{
						boolean includedInSQLInjectionWhitelist = false;
						boolean includedInXSSWhitelist = false;
						if (!includedInSQLInjectionWhitelist)
						{
							resultMap.put(key, PreventAttackUtil.filterSQLInjection(Array.get(value, 0).toString())
								.trim());
						}
						if (!includedInXSSWhitelist)
						{
							if (resultMap.containsKey(key))
							{
								resultMap.put(key, PreventAttackUtil.filterXSS(resultMap.get(key).toString()).trim());
							}
							else
							{
								resultMap.put(key, PreventAttackUtil.filterXSS(Array.get(value, 0).toString()).trim());
							}
						}
						if (includedInSQLInjectionWhitelist && includedInXSSWhitelist)
						{
							resultMap.put(key, Array.get(value, 0).toString().trim());
						}
					}
					if (length > 1)
					{
						resultMap.put(key, value);
					}
				}
				else
				{
					boolean includedInSQLInjectionWhitelist = false;
					boolean includedInXSSWhitelist = false;

					if (!includedInSQLInjectionWhitelist)
					{
						resultMap.put(key, PreventAttackUtil.filterSQLInjection(value.toString()).trim());
					}
					if (!includedInXSSWhitelist)
					{
						if (resultMap.containsKey(key))
						{
							resultMap.put(key, PreventAttackUtil.filterXSS(resultMap.get(key).toString()).trim());
						}
						else
						{
							resultMap.put(key, PreventAttackUtil.filterXSS(value.toString()).trim());
						}
					}
					if (includedInSQLInjectionWhitelist && includedInXSSWhitelist)
					{
						resultMap.put(key, value.toString().trim());
					}
				}
			}
		}
		return resultMap;
	}

	/**
	 * 获取 valueListHelper
	 *
	 * @return 返回 valueListHelper
	 */
	public ValueListHandlerHelper getValueListHelper()
	{
		return valueListHelper;
	}

	/**
	 * 设置 valueListHelper
	 *
	 * @param 对valueListHelper进行赋值
	 */
	public void setValueListHelper(ValueListHandlerHelper valueListHelper)
	{
		this.valueListHelper = valueListHelper;
	}

	/**
	 * 获取 queryIds
	 *
	 * @return 返回 queryIds
	 */
	public String[] getQueryIds()
	{
		return queryIds;
	}

	/**
	 * 设置 queryIds
	 *
	 * @param 对queryIds进行赋值
	 */
	public void setQueryIds(String[] queryIds)
	{
		this.queryIds = queryIds;
	}

}
