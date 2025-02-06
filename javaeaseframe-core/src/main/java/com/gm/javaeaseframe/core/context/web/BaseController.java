package com.gm.javaeaseframe.core.context.web;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.exception.ExceptionCodeEnum;
import com.gm.javaeaseframe.common.util.HttpUtil;
import com.gm.javaeaseframe.core.constains.FormState;
import com.gm.javaeaseframe.core.constains.GlobalSysInfo;
import com.gm.javaeaseframe.core.constains.SysConstains;
import com.gm.javaeaseframe.core.context.model.Context;
import com.gm.javaeaseframe.core.context.model.CookieInfo;
import com.gm.javaeaseframe.core.context.service.IOperLogService;
import com.gm.javaeaseframe.core.context.service.ITokenService;
import com.gm.javaeaseframe.core.context.service.IUser;
import com.gm.javaeaseframe.core.context.service.impl.CookieService;
import com.gm.javaeaseframe.core.context.service.impl.User;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;
import com.gm.javaeaseframe.core.thirty.spring.CustomDateEditor;
import com.gm.javaeaseframe.core.thirty.spring.CustomDoubleDefEditor;
import com.gm.javaeaseframe.core.thirty.spring.CustomDoubleEditor;
import com.gm.javaeaseframe.core.thirty.spring.CustomFloatDefEditor;
import com.gm.javaeaseframe.core.thirty.spring.CustomFloatEditor;
import com.gm.javaeaseframe.core.thirty.spring.CustomIntegerDefEditor;
import com.gm.javaeaseframe.core.thirty.spring.CustomIntegerEditor;
import com.gm.javaeaseframe.core.thirty.spring.CustomLongDefEditor;
import com.gm.javaeaseframe.core.thirty.spring.CustomLongEditor;

public abstract class BaseController {

    private static TokenProcessor token = TokenProcessor.getInstance();
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    
    /** cookie有效期，7*24小时 */
	private static final int COOKIE_MAX_AGE = 604800;
	
	public static final String KEY_RESULT_CODE = "code";
    public static final String KEY_RESULT_MSG = "msg";
    public static final String KEY_RESULT_DATA = "data";
    public static final String KEY_RESULT_QUERY = "query";
    public static final String KEY_RESULT_ENTITY = "entity";
    public static final String KEY_RESULT_LIST = "list";
    public static final String KEY_RESULT_DICT = "dict";
    public static final String KEY_RESULT_PAGE = "page";
    public static final String KEY_RESULT_CASCADE_LABEL = "label";
    public static final String KEY_RESULT_CASCADE_VALUE = "value";
    public static final String KEY_RESULT_CASCADE_EXTERNAL = "external";
    public static final int VALUE_RESULT_SUCCESS = 0;
    public static final int VALUE_RESULT_FAILURE = -1;
    @Autowired(required=false)
    private ITokenService tokenService;
    @Autowired(required=false)
    protected IOperLogService operLogService;

    /**
     * 数据绑定类型转换
     * @param request
     * @param binder
     * @throws Exception
     */
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(int.class, new CustomIntegerDefEditor());
        binder.registerCustomEditor(Integer.class, new CustomIntegerEditor());
        binder.registerCustomEditor(long.class, new CustomLongDefEditor());
        binder.registerCustomEditor(Long.class, new CustomLongEditor());
        binder.registerCustomEditor(double.class, new CustomDoubleDefEditor());
        binder.registerCustomEditor(Double.class, new CustomDoubleEditor());
        binder.registerCustomEditor(float.class, new CustomFloatDefEditor());
        binder.registerCustomEditor(Float.class, new CustomFloatEditor());
        binder.registerCustomEditor(Date.class, new CustomDateEditor());
    }
    
    protected IUser convertHeader2User(HttpServletRequest request) {
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
	 * 获取登录账号信息
	 * 
	 * @return
	 */
	protected IUser getCurUser() {
		RequestAttributes requstAttr = RequestContextHolder.getRequestAttributes();
		//从session中获取用户信息
		IUser user = (IUser) requstAttr.getAttribute(SysConstains.LOGIN_USER_INFO, RequestAttributes.SCOPE_SESSION);
		if (user != null) {
			return user;
		}
		try {
			HttpServletRequest request = ((ServletRequestAttributes) requstAttr).getRequest();
			//请header中获取用户信息
			user = convertHeader2User(request);
			if (user != null) {
				return user;
			} else {//从cookie中获取用户信息
				String securityKey = GlobalSysInfo.getPropertyValue(SysConstains.PROP_COOKIE_SECURITY_KEY);
				CookieInfo cookie = CookieService.getLoginCookie(request, securityKey);
				if (cookie == null) {
					return null;
				} else {
					return cookie.getUser();
				}
			}
		} catch (Throwable e) {
			log.debug("获取用户Cookie信息异常-->" + e.getMessage());
			return null;
		}
	}
    /**
     * 保存用户登录信息
     * @param request
     * @param response
     * @param user
     */
    protected void saveCurrUser(HttpServletRequest request, HttpServletResponse response, IUser user)
    {
		this.saveCurrUser(request, response, user, COOKIE_MAX_AGE);
    }
    /**
     * 保存用户登录信息（在当前session范围内有效）
     * @param request
     * @param response
     * @param user
     */
    protected void saveCurrUserForSession(HttpServletRequest request, HttpServletResponse response, IUser user)
    {
    	this.saveCurrUser(request, response, user, -1);
    }
    /**
     * 保存用户登录信息
     * @param request
     * @param response
     * @param user
     * @param maxAge	最大有效期，单位：秒，大于等于0：则使用时间有效期，否则与session有效期相同
     */
    protected void saveCurrUser(HttpServletRequest request, HttpServletResponse response, IUser user, int maxAge)
    {
    	String securityKey = GlobalSysInfo.getPropertyValue(SysConstains.PROP_COOKIE_SECURITY_KEY);
    	String cookieDomain = GlobalSysInfo.getPropertyValue(SysConstains.PROP_COOKIE_DOMAIN);
    	String currDoman = request.getServerName();
    	CookieInfo cookie = CookieService.setLoginCookie(response, user, securityKey, currDoman, maxAge);
    	if(StringUtils.isNotEmpty(cookieDomain) && !currDoman.equals(cookieDomain))
    	{
    		CookieService.setLoginCookie(response, user, securityKey, cookieDomain, maxAge);
    	}
    	if(cookie != null && tokenService != null)
    	{
    		try
    		{
    			tokenService.saveToken(cookie);
    		}
    		catch(Throwable e)
    		{
    			log.error("存储cookie信息到服务器异常", e);
    		}
    	}
    }
    /**
     * 保存用户登录信息
     * @param request
     * @param response
     * @param user
     * @param domains
     */
    protected void saveCurrUser(HttpServletRequest request, HttpServletResponse response, IUser user, List<String> domains)
    {
    	this.saveCurrUser(request, response, user, domains, COOKIE_MAX_AGE);
    }
    /**
     * 保存用户登录信息（在当前session范围内有效）
     * @param request
     * @param response
     * @param user
     * @param domains
     */
    protected void saveCurrUserForSession(HttpServletRequest request, HttpServletResponse response, IUser user, List<String> domains)
    {
    	this.saveCurrUser(request, response, user, domains, -1);
    }
    /**
     * 保存用户登录信息
     * @param request
     * @param response
     * @param user
     * @param domains
     * @param maxAge	最大有效期，单位：秒，大于等于0：则使用时间有效期，否则与session有效期相同
     */
    protected void saveCurrUser(HttpServletRequest request, HttpServletResponse response, IUser user, List<String> domains, int maxAge)
    {
    	String securityKey = GlobalSysInfo.getPropertyValue(SysConstains.PROP_COOKIE_SECURITY_KEY);
    	String cookieDomain = GlobalSysInfo.getPropertyValue(SysConstains.PROP_COOKIE_DOMAIN);
    	CookieInfo cookie = CookieService.setLoginCookie(response, user, securityKey, cookieDomain, maxAge);
    	if(domains != null && domains.size() > 0)
    	{
    		for(String domain : domains)
    		{
    			if(StringUtils.isNotEmpty(domain))
    			{
    				CookieService.setLoginCookie(response, user, securityKey, domain, maxAge);
    			}
    		}
    	}
    	if(cookie != null && tokenService != null)
    	{
    		try
    		{
    			tokenService.saveToken(cookie);
    		}
    		catch(Throwable e)
    		{
    			log.error("存储cookie信息到服务器异常", e);
    		}
    	}
    }
    
    protected void removeCurrUser(HttpServletRequest request, HttpServletResponse response)
    {
    	HttpSession session = request.getSession();
//		session.removeAttribute(SysConstains.LOGIN_USER_INFO);
		session.invalidate();
		if(tokenService != null)
		{
			try
			{
				CookieInfo cookie = CookieService.getLoginCookie(request);
				if(cookie != null)
				{
					tokenService.expireToken(cookie);
				}
			}
			catch(Exception e){
				log.error("设置服务器的cookie过期异常", e);
			}
		}
		CookieService.deleteLoginCookie(request, response);
    }
    /**
     * 移除当前用户的所有登录信息
     * @param request
     * @param response
     */
    protected void removeAllCurrUser(HttpServletRequest request, HttpServletResponse response)
    {
    	HttpSession session = request.getSession();
//		session.removeAttribute(SysConstains.LOGIN_USER_INFO);
		session.invalidate();
		if(tokenService != null)
		{
			try
			{
				CookieInfo cookie = CookieService.getLoginCookie(request);
				if(cookie != null)
				{
					tokenService.expireUser(cookie);
				}
			}
			catch(Exception e){
				log.error("设置服务器的用户所有cookie过期异常", e);
			}
		}
		CookieService.deleteLoginCookie(request, response);
    }
    
    public void setCookie(HttpServletRequest request, HttpServletResponse response)
    {
    	this.setCookie(request, response, COOKIE_MAX_AGE);
    }
    public void setCookieForSession(HttpServletRequest request, HttpServletResponse response)
    {
    	this.setCookie(request, response, -1);
    }
    public void setCookie(HttpServletRequest request, HttpServletResponse response, int maxAge)
    {
    	try
    	{
    		String c = request.getParameter("c");
    		String securityKey = GlobalSysInfo.getPropertyValue(SysConstains.PROP_COOKIE_SECURITY_KEY);
    		String cookieDomain = GlobalSysInfo.getPropertyValue(SysConstains.PROP_COOKIE_DOMAIN);
    		String currDoman = request.getServerName();
    		CookieService.setCookieBySecurity(response, c, securityKey, currDoman, maxAge);
    		if(!currDoman.equals(cookieDomain))
    		{
    			CookieService.setCookieBySecurity(response, c, securityKey, cookieDomain, maxAge);
    		}
    	}
    	catch(Throwable e){
    		log.warn("设置用户的Cookie信息异常-->" + e.getMessage());
    	}
    }
    
    public void exit(HttpServletRequest request, HttpServletResponse response)
    {
    	if(tokenService != null)
		{
			try
			{
				CookieInfo cookie = CookieService.getLoginCookie(request);
				if(cookie != null)
				{
					tokenService.expireToken(cookie);
				}
			}
			catch(Exception e){
				log.error("设置服务器的cookie过期异常", e);
			}
		}
    	try
    	{
    		CookieService.deleteLoginCookie(request, response);
    	}
    	catch(Throwable e){
    		log.debug("删除用户的Cookie信息异常-->" + e.getMessage());
    	}
    }

    /**
     * 包装上下文信息
     * @return
     */
    protected Context getContext() {
        Context context = new Context();
        context.setUser(getCurUser());
        context.setRequestId(getRequestId());
        return context;
    }

    /**
     * 处理异常
     * @param model
     * @param e
     */
    protected final void doException(HttpServletRequest request, String moduleDesc, Map<String, Object> model, Throwable e) {
        // 处理异常，标识处理出错
        String message = convertException(e);
        model.put(KEY_RESULT_MSG, StringUtils.isBlank(message) ? moduleDesc + " 【失败】 " : message);
        recordSysLog(request, getCurUser(), moduleDesc + " 【失败】 " + (message.length() > 50 ? message.substring(0, 50) : message));
        log.error(moduleDesc + "出错!原因:", e);
    }
    protected final void doException(HttpServletRequest request, IUser user, String moduleDesc, Map<String, Object> model, Throwable e) {
    	// 处理异常，标识处理出错
    	String message = convertException(e);
    	model.put(KEY_RESULT_MSG, StringUtils.isBlank(message) ? moduleDesc + " 【失败】 " : message);
    	recordSysLog(request, user, moduleDesc + " 【失败】 " + (message.length() > 50 ? message.substring(0, 50) : message));
    	log.error(moduleDesc + "出错!原因:", e);
    }
    /**
     * 记录并转换异常
     * @param request
     * @param moduleDesc
     * @param model
     * @param e
     */
    protected final <R> CommonResult<R> doRecordAndConvertException(HttpServletRequest request, String moduleDesc, Throwable e) {
    	CommonResult<R> result = convertException2Entity(e);
    	String message = result.getMsg();
    	recordSysLog(request, getCurUser(), moduleDesc + " 【失败】 " + (message.length() > 50 ? message.substring(0, 50) : message));
    	log.error(moduleDesc + "出错!原因:", e);
    	return result;
    }
    /**
     * 打印异常信息
     * @param e
     */
    protected void doPrintException(String moduleDesc, Throwable e) {
        if (e instanceof BusinessException) {
            log.error(moduleDesc + "【异常】-->" + e.getMessage());
        } else if (e.getCause() instanceof BusinessException) {
            log.error(moduleDesc + "【异常】-->" + e.getCause().getMessage());
        } else {
            log.error(moduleDesc + "【异常】", e);
        }
    }

    /**
     * 异常信息转换
     * @param e
     * @return
     */
    protected String convertException(Throwable e) {
    	CommonResult<?> result = this.convertException2Entity(e);
        return result.getMsg();
    }
    protected <R> CommonResult<R> convertException2Entity(Throwable e) {
    	int code = VALUE_RESULT_FAILURE;
    	String message = "";
        if (e instanceof org.springframework.jdbc.CannotGetJdbcConnectionException) {
        	code = ExceptionCodeEnum.SYSTEM_SERVICE_UNAVAILABLE.getCode();
            message = "不能连接数据库";
        } else if (e.getCause() instanceof BusinessException) {
        	code = ((BusinessException)e.getCause()).getCode();
            message = e.getCause().getMessage();
        } else if (e instanceof BusinessException) {
        	code = ((BusinessException)e).getCode();
            message = e.getMessage();
        } else {
        	code = ExceptionCodeEnum.SYSTEM_ERROR.getCode();
            message = e.getMessage();
            if (message == null || message.equals("")) {
                message = "系统发生错误，请联系管理员！";
            } else if (Pattern.compile("Table '[^']*' doesn't exist", Pattern.CASE_INSENSITIVE).matcher(message).find()) {
                message = "数据不存在";
            } else if (message.length() > 50){
                message = "系统发生错误，请联系管理员！";
            }
        }
        // ********去掉一些特殊字符以便页面展现***********
        // 去除换行符
        message = StringUtils.trim(message).replaceAll("\\n", "");
        message = StringUtils.trim(message).replaceAll("\\r", "");
        if (message.length() > 50) {
            message = "操作异常，请稍后再试！";
        }
        CommonResult<R> result = new CommonResult<>();
        result.setCode(code);
        result.setMsg(message);
    	return result;
    }
    /**
     * 文字消息响应
     * @param response
     * @param msg
     */
    protected void writeResponse(HttpServletResponse response, String msg) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(msg);
            out.flush();
        } catch (Exception e) {
        	log.debug("响应消息异常-->" + e.getMessage());
        }
    }
    protected void writeResponse(HttpServletResponse response, int code, String msg) {
        PrintWriter out = null;
        try {
            response.setStatus(code);
            out = response.getWriter();
            out.write(msg);
            out.flush();
        } catch (Exception e) {
            log.debug("响应消息异常-->" + e.getMessage());
        }
    }
    /**
     * 图片消息响应
     * @param response
     * @param msg
     */
    protected void doResponseImage(HttpServletResponse response, java.awt.image.RenderedImage image) {
        try {
        	 //禁止缓存
  		  response.setHeader("Pragma", "No-cache");
  		  response.setHeader("Cache-Control", "No-cache");
  		  response.setDateHeader("Expires", 0);
  		  // 指定生成的响应是图片
  		  response.setContentType("image/jpeg");
  		  ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (Exception e) {
        	log.debug("响应图片消息异常-->" + e.getMessage());
        }
    }

    /**
     * 生成token信息
     * @param request
     * @return
     */
    protected String generateToken(HttpServletRequest request) {
        return token.generateToken(request);
    }

    /**
     * 检查token是否有效
     * @param request
     * @return
     */
    protected boolean isTokenValid(HttpServletRequest request) {
        return token.isTokenValid(request, false);
    }

    /**
     * 检查token是否有效
     * @param request
     * @param reset
     * @return
     */
    protected boolean isTokenValid(HttpServletRequest request, boolean reset) {
        return token.isTokenValid(request, reset);
    }

    /**
     * 重置token
     * @param request
     */
    protected void resetToken(HttpServletRequest request) {
        token.resetToken(request);
    }

    /**
     * 保存token信息
     * @param request
     */
    protected void saveToken(HttpServletRequest request) {
        token.saveToken(request);
    }
    /**
     * 检查token是否有效
     * @param request
     * @return
     */
    protected boolean isTokenValid(HttpServletRequest request, HttpServletResponse response) {
    	return token.isTokenValid(request, response, false);
    }
    
    /**
     * 检查token是否有效
     * @param request
     * @param reset
     * @return
     */
    protected boolean isTokenValid(HttpServletRequest request, HttpServletResponse response, boolean reset) {
    	return token.isTokenValid(request, response, reset);
    }
    
    /**
     * 重置token
     * @param request
     */
    protected void resetToken(HttpServletRequest request, HttpServletResponse response) {
    	token.resetToken(request, response);
    }
    
    /**
     * 保存token信息
     * @param request
     */
    protected void saveToken(HttpServletRequest request, HttpServletResponse response) {
    	token.saveToken(request, response);
    }

    /**
     * 设定界面新增
     * @param request
     */
    protected void setFormAdd(HttpServletRequest request) {
        request.setAttribute(SysConstains.FORM_STATE, FormState.ADD);
    }

    /**
     * 设定界面修改状态
     * @param request
     */
    protected void setFormEdit(HttpServletRequest request) {
        request.setAttribute(SysConstains.FORM_STATE, FormState.EDIT);
    }

    /**
     * 设定页面查看状态
     * @param request
     */
    protected void setFormView(HttpServletRequest request) {
        request.setAttribute(SysConstains.FORM_STATE, FormState.VIEW);
    }

    /**
     * 设定页面审核状态
     * @param request
     */
    protected void setFormExam(HttpServletRequest request) {
        request.setAttribute(SysConstains.FORM_STATE, FormState.EXAM);
    }

    /**
     * 设定页面列表记录状态
     * @param request
     */
    protected void setFormList(HttpServletRequest request) {
        request.setAttribute(SysConstains.FORM_STATE, FormState.LIST);
    }
    /**
     * 设定页面详情记录状态
     * @param request
     */
    protected void setFormRecorde(HttpServletRequest request) {
        request.setAttribute(SysConstains.FORM_STATE, FormState.RECORD);
    }
    /**
     * 设定页面删除状态
     * @param request
     */
    protected void setFormDelete(HttpServletRequest request) {
        request.setAttribute(SysConstains.FORM_STATE, FormState.DELETE);
    }
    /**
     * 设定页面导出状态
     * @param request
     */
    protected void setFormExport(HttpServletRequest request) {
        request.setAttribute(SysConstains.FORM_STATE, FormState.EXPORT);
    }

    /**
     * 设定页面状态
     * @param request
     * @param formState
     */
    protected void setFormState(HttpServletRequest request, FormState formState) {
        request.setAttribute(SysConstains.FORM_STATE, formState);
    }
    /**
     * 获取页面状态
     * @param request
     * @return
     */
    protected FormState getFormState(HttpServletRequest request) {
        Object obj = request.getAttribute(SysConstains.FORM_STATE);
        if (obj != null && obj instanceof FormState) {
            return (FormState)obj;
        }
        return null;
    }
    /**
     * 是否是列表页面
     * @param request
     * @return
     */
    protected boolean isListForm(HttpServletRequest request) {
        FormState state = this.getFormState(request);
        return state == FormState.LIST;
    }
    /**
     * 是否是明细页面
     * @param request
     * @return
     */
    protected boolean isViewForm(HttpServletRequest request) {
        FormState state = this.getFormState(request);
        return state == FormState.VIEW;
    }
    /**
     * 是否是新增页面
     * @param request
     * @return
     */
    protected boolean isAddForm(HttpServletRequest request) {
        FormState state = this.getFormState(request);
        return state == FormState.ADD;
    }
    /**
     * 是否是编辑页面
     * @param request
     * @return
     */
    protected boolean isEditForm(HttpServletRequest request) {
        FormState state = this.getFormState(request);
        return state == FormState.EDIT;
    }

    protected void recordSysLog(HttpServletRequest request, String message) {
        recordSysLog(request, getCurUser(), message);
    }
    
    protected void recordSysLog(HttpServletRequest request, IUser sysUser, String message) {
		if (operLogService != null) {
			String requestUrl = request.getRequestURI();
			String requestIp = this.getRequestIP(request);
	    	String requestId = this.getRequestId();
			try {
				operLogService.doHandlerLog(requestId, requestUrl, requestIp, sysUser, message);
			} catch (Exception e) {
				log.debug("记录系统操作日志异常-->" + e.getMessage());
			}
		}
    }

    protected String getWebBasePath(HttpServletRequest request) {
        String path = request.getContextPath();
        int port = request.getServerPort();
        String basePath = request.getScheme() + "://" + request.getServerName();
        if (port == 80) {
            basePath += path;
        } else {
            basePath += ":" + request.getServerPort() + path;
        }
        if (!basePath.endsWith("/")) {
            basePath = basePath + "/";
        }
        return basePath;
    }
    
    /**
	 * 获取发送请求的源IP地址
	 * @param request
	 * @return
	 */
	public String getRequestIP(HttpServletRequest request) 
	{
		return HttpUtil.getRequestIP(request);
	}
	
	protected String createFailJsonResp(String msg)
	{
		JSONObject ret = new JSONObject();
		 ret.put(KEY_RESULT_CODE, VALUE_RESULT_FAILURE);
         ret.put(KEY_RESULT_MSG, msg);
         return ret.toJSONString();
	}
	protected String createSuccJsonResp(String msg)
	{
		JSONObject ret = new JSONObject();
		ret.put(KEY_RESULT_CODE, VALUE_RESULT_SUCCESS);
		ret.put(KEY_RESULT_MSG, msg);
		return ret.toJSONString();
	}
	/**
	 * 添加字典值
	 * @param model
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
    protected void addDict(Map<String, Object> model, String key, Object value)
	{
	    Map<String,Object> dictMap = null;
        Object dict = model.get(KEY_RESULT_DICT);
        if (dict != null) {
            dictMap = (Map<String,Object>) dict;
        } else {
            dictMap = new HashMap<String,Object>();
            model.put(KEY_RESULT_DICT, dictMap);
        }
        dictMap.put(key, value);
	}
	/**
	 * 判断二个对象是否相等
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean isEqual(Object source, Object target)
	{
		if(source == null && target == null)
		{
			return true;
		}
		if(source == null || target == null)
		{
			return false;
		}
		return source.toString().equals(target.toString());
	}
}
