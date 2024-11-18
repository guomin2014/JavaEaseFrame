package com.gm.javaeaseframe.core.context.interceptor;

import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gm.javaeaseframe.common.util.ReflectionUtil;
import com.gm.javaeaseframe.core.constains.SysConstains;
import com.gm.javaeaseframe.core.context.service.IUser;
import com.gm.javaeaseframe.core.context.service.impl.User;

public class BaseInterceptor implements AsyncHandlerInterceptor {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private String[] pathPatterns = new String[] { "/**" };
//	private String[] excludePathPatterns;

	public String[] getPathPatterns() {
		return pathPatterns;
	}

	public int getOrder() {
		return 0;
	}
	/**
	 * 获取当前用户信息
	 * @param request
	 * @return
	 */
	protected IUser getCurrUser(HttpServletRequest request) {
    	String info = request.getHeader(SysConstains.LOGIN_USER_INFO);
    	if (StringUtils.isNotBlank(info)) {
    		try {
    			info = URLDecoder.decode(info, "UTF-8");
    		} catch (Exception e) {}
    		try {
    			User user = JSON.parseObject(info, User.class);
    			if (user != null && user.getId() == null) {//用户无ID，也表示为空
    				return null;
    			}
    			return user;
    		} catch (Exception e) {
    			return null;
    		}
    	}
    	return null;
    }
	/**
     * 获取当前请求ID
     * @return
     */
    protected String getRequestId() {
    	try {
	    	RequestAttributes requstAttr = RequestContextHolder.getRequestAttributes();
	    	HttpServletRequest request = ((ServletRequestAttributes) requstAttr).getRequest();
	    	return request.getHeader(SysConstains.REQUEST_ID);
    	} catch (Exception e) {
    		return null;
    	}
    }
    /**
     * 获取请求参数
     * @return
     */
    protected String getRequestParams(HttpServletRequest request) {
    	return this.getRequestParams(request, false);
    }
    /**
     * 获取请求参数
     * @param request
     * @param nativeRequestEable	是否从原生请求中获取
     * @return
     */
    protected String getRequestParams(HttpServletRequest request, boolean nativeRequestEable) {
    	try {
    		byte[] body = null;
    		ContentCachingRequestWrapper cacheRequest = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    		if (cacheRequest != null) {
	    		body = cacheRequest.getContentAsByteArray();
    		}
    		if (body == null || body.length == 0) {
    			if (nativeRequestEable) {
    				//如果未从缓存中获取到内容，则从原生请求中获取内容，并通过反射将读取操作重置（原生请求流只能读一次）
    				body = StreamUtils.copyToByteArray(request.getInputStream());
    				// 1.获取RequestFacade中的字段request
	    			Object req = ReflectionUtil.getFieldValue(request, "request");
	    			while(req != null && !req.getClass().getName().equalsIgnoreCase("org.apache.catalina.connector.Request")) {
	    				req = ReflectionUtil.getFieldValue(req, "request");
	    			}
					if (req != null) {
						// 2.获取RequestFacade中的字段usingInputStream并设置为false
						ReflectionUtil.setFieldValue(req, "usingInputStream", false);
						// 3.获取RequestFacade中的字段usingReader并设置为false
						ReflectionUtil.setFieldValue(req, "usingReader", false);
						// 4.获取RequestFacade中的字段inputStream
						Object inputStream = ReflectionUtil.getFieldValue(req, "inputStream");
//						// 4-1.获取RequestFacade中的字段inputStream的ib字段
//						Object ib = ReflectionUtil.getFieldValue(inputStream, "ib");
						// 4-2.新建InputBuffer并设置ByteBuffer和request(该request是RequestFacade中request字段的coyoteRequest字段值)
						Object inputBuffer = Class.forName("org.apache.catalina.connector.InputBuffer").newInstance();
						ReflectionUtil.setFieldValue(inputBuffer, "bb", ByteBuffer.wrap(body));
						Object coyoteRequest = ReflectionUtil.getFieldValue(req, "coyoteRequest");
						ReflectionUtil.setFieldValue(inputBuffer, "coyoteRequest", coyoteRequest);
						// 4-3.设置RequestFacade中的字段inputStream的ib字段为新建inputBuffer
						ReflectionUtil.setFieldValue(inputStream, "ib", inputBuffer);
						ReflectionUtil.setFieldValue(req, "inputBuffer", inputBuffer);
					}
    			}
    		}
    		if (body != null && body.length > 0) {
    			try {
    				String result = new String(body, "UTF-8")
							.replaceAll("\r\n", "")
							.replaceAll("\r", "")
							.replaceAll("\n", "")
							.replaceAll("\t", "")
							.replaceAll(",[\\s]*\"", ",\"")
							.replaceAll("\\{[\\s]*\"", "\\{\"")
							;
    				String contentType = request.getContentType();
    				//将表单提交转换成json格式
    				if (StringUtils.isNotBlank(contentType) && contentType.equalsIgnoreCase(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
    					result = URLDecoder.decode(result, "UTF-8");
    					JSONObject json = new JSONObject(true);
    					String[] params = result.split("&");
    					for (String param : params) {
    						if (StringUtils.isBlank(param)) {
    							continue;
    						}
    						String[] str = param.split("=");
    						if (str.length == 2) {
    							json.put(str[0], str[1]);
    						}
    					}
    					result = json.toJSONString();
    				}
					return result;
				} catch (Exception e) {}
    		} else {
    			Enumeration<String> parameters = request.getParameterNames();
				Map<String, String> params = new HashMap<>();
				while (parameters.hasMoreElements()) {
					String parameter = parameters.nextElement();
					String value = request.getParameter(parameter);
					params.put(parameter, value);
				}
				try {
					Collection<Part> parts = request.getParts();
					if (parts != null) {
						for (Part part : parts) {
							if (StringUtils.isNotBlank(part.getName()) && StringUtils.isNotBlank(part.getSubmittedFileName())) {
								params.put(part.getName(), part.getSubmittedFileName());
							}
						}
					}
				} catch (Exception e) {}
				if (!params.isEmpty()) {
					return JSON.toJSONString(params);
				}
    		}
    	} catch (Exception e) {}
		return null;
    }
    
    protected String getResponseBody(HttpServletResponse response) {
    	ContentCachingResponseWrapper cacheResponse =  WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    	if (cacheResponse != null) {
			try {
				return new String(cacheResponse.getContentAsByteArray(),"UTF-8")
						.replaceAll("\r\n", "")
						.replaceAll("\r", "")
						.replaceAll("\n", "")
						.replaceAll("\t", "")
						.replaceAll(",[\\s]*\"", ",\"")
						;
			} catch (Exception e) {
				
			}
    	}
    	return null;
    }

	protected boolean isAjaxSubmit(HttpServletRequest request) {
		try {
			String header = request.getHeader("X-Requested-With");
			if ("XMLHttpRequest".equalsIgnoreCase(header)) {
				return true;
			}
			String contentType = request.getHeader("content-type");
			if (contentType != null && contentType.toLowerCase().indexOf("application/json") != -1) {
				return true;
			}
			Enumeration<String> accepts = request.getHeaders("Accept");
			if (accepts != null) {
				while (accepts.hasMoreElements()) {
					String accept = accepts.nextElement();
					if (accept != null && accept.indexOf("application/json") != -1) {
						return true;
					}
				}
			}
//			String accept = request.getHeaders("Accept").nextElement().toString();
//			if(accept.indexOf("application/json") != -1)
//			{
//				return true;
//			}
		} catch (Exception e) {
			logger.debug("获取请求类型异常-->" + e.getMessage());
		}
		return false;
	}

	protected void writeJsonResponse(HttpServletResponse response, int status, String msg) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(status);
			response.getWriter().print(msg);
		} catch (Exception e) {
			logger.debug("写入响应信息异常", e);
		}
	}
}
