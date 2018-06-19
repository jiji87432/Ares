package com.pay.risk.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pay.commons.web.WebConstants;
import com.pay.commons.web.springmvc.annotation.PermissionCheck;
import com.pay.commons.web.springmvc.annotation.PermissionCheckType;


/**
 * 权限拦截
 * @author qinji.xu
 */
public class LoginPermissionInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LoginPermissionInterceptor.class);

    private static final Integer REDIS_DB_INDEX = 1;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("===" + request.getSession().getAttribute(WebConstants.SESSION_OPERATOR));

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String className = handlerMethod.getMethod().getDeclaringClass().getName();
            String methodName = handlerMethod.getMethod().getName();
            PermissionCheck methodAnno = handlerMethod.getMethodAnnotation(PermissionCheck.class);
            PermissionCheck classAnno = handlerMethod.getMethod().getDeclaringClass().getAnnotation(PermissionCheck.class);

            String operator = (String) request.getSession().getAttribute(WebConstants.SESSION_OPERATOR);
            if ("".equals(operator) || null == operator) {
                Object object = request.getSession().getAttribute("_const_cas_assertion_");
                if (object == null) {
                    logger.error("Don't has operator");
                    return false;
                }
                Assertion assertion = (Assertion) object;
                operator = assertion.getPrincipal().getName();
                request.getSession().setAttribute(WebConstants.SESSION_OPERATOR, operator);
            }

            boolean hasPermission = false;
            if ((methodAnno == null || (Arrays.asList(methodAnno.checkType()).contains(PermissionCheckType.LOGIN) && methodAnno.value()))
                    || (classAnno == null || (Arrays.asList(classAnno.checkType()).contains(PermissionCheckType.LOGIN) && classAnno.value()))) {
                /*
                 * @SuppressWarnings("unchecked")
                 * Set<String> set =
                 * CacheUtils.get(REDIS_DB_INDEX,
                 * WebConstants.BOSS_OPERATOR_RESOURCE + "." + operator,
                 * Set.class,
                 * true);
                 */
                String servletPath = request.getServletPath();
                String resource = servletPath.replaceFirst("/", "");
                String requestUrl = request.getRequestURL().toString();
                String remoteAddress = requestUrl.substring(0, requestUrl.indexOf(servletPath));
                /*
                 * if (!set.contains(resource))
                 * {
                 * logger.info("noPermit! for operator {} action={}", operator, resource);
                 * String url = remoteAddress + "/jsp/noPermit.jsp?type=A";
                 * response.sendRedirect(url);
                 * return hasPermission;
                 * }
                 */
                hasPermission = true;
            }
            logger.info("className=[" + className + "],methodName=[" + methodName + "]");
            return hasPermission;
        }

        return true;
    }

}
