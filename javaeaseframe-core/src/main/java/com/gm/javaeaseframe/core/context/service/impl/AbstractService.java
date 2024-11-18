package com.gm.javaeaseframe.core.context.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.core.context.service.IService;

public abstract class AbstractService implements IService {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
    public String convertException(Throwable e) {
        if (e == null) {
            return "";
        }
        if (e instanceof BusinessException && e.getCause() instanceof BusinessException) {
            return e.getMessage();
        } else if (e instanceof java.net.SocketTimeoutException || e.getCause() instanceof java.net.SocketTimeoutException) {
            return "连接服务端超时";
        } else if (e instanceof java.net.ConnectException || e.getCause() instanceof java.net.ConnectException) {
            return "服务端拒绝连接";
        } else if (e.getCause() instanceof java.net.NoRouteToHostException) {
            return "远程主机不可到达";
        } else {
            String message = e.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = "未知的异常";
            } else if (this.isMatch(".*Could not get JDBC Connection.*", message)) {
                message = "数据库连接异常";
            } else if (this.isMatch(".*No such file or directory.*", message)) {
                message = "文件路径不存在";
            } else if (this.isMatch(".*Table.*doesn't exist.*", message)) {
                message = "表不存在";
            }
            message = message.replaceAll("'", "‘");
            message = message.replaceAll("\"", "‘");
            message = message.replaceAll("\n", "<br/>");
            if (message.length() > 150) {
                message = message.substring(0, 150);
            }
            return message;
        }
    }
    
    /**
	 * 检测是否是表不存在异常，如果是表不存在，则不做任务处理，否则不是表不存在，则直接抛出异常
	 * @param e
	 * @throws AppException
	 * @return true：表不存在
	 */
	protected boolean checkTableNotExistsException(Throwable e) throws BusinessException
	{
		if(e == null)
		{
			return false;
		}
		String message = e.getMessage();  
		if(StringUtils.isNotEmpty(message) && this.isMatch(".*Table.*doesn't exist.*", message))//表不存在，则返回无数据，不返回异常
		{
			log.warn(message);
			return true;
		}
		else
		{
			throw new BusinessException(e);
		}
	}
	/**
	 * 检测是否是表不存在异常，如果是表不存在，则不做任务处理，否则不是表不存在，则直接抛出异常
	 * @param e
	 * @throws AppException
	 * @return true：表不存在
	 */
	protected boolean checkTableNotExistsExceptionWithoutLog(Throwable e) throws BusinessException
	{
		if(e == null)
		{
			return false;
		}
		String message = e.getMessage();
		if(StringUtils.isNotEmpty(message) && this.isMatch(".*Table.*doesn't exist.*", message))//表不存在，则返回无数据，不返回异常
		{
			return true;
		}
		else
		{
			throw new BusinessException(e);
		}
	}
	/**
	 * 检测是否主键冲突
	 * @param e
	 * @return
	 * @throws AppException
	 */
	protected boolean checkPrimaryExistsExceptionWithoutLog(Throwable e) throws BusinessException
	{
		if(e == null)
		{
			return false;
		}
		String message = e.getMessage();
		if(StringUtils.isEmpty(message))
		{
			message = e.getCause() == null ? "" : e.getCause().getMessage();
		}
		if(StringUtils.isNotEmpty(message) && this.isMatch(".*Duplicate entry.*for key 'PRIMARY'.*", message))//主键已经存在，则返回无数据，不返回异常
		{
			return true;
		}
		else
		{
			throw new BusinessException(e);
		}
	}
	/**
	 * 判断字符是否正则匹配(忽略英文大小写)
	 * @param regex
	 * @param message
	 * @return
	 */
	private boolean isMatch(String regex, String message)
	{
		return isMatch(regex, message, true);
	}
	
	/**
	 * 判断数据是否匹配
	 * @param source
	 * @param regex
	 * @param case
	 * @return
	 */
	private boolean isMatch(String regex, String message, boolean ignoreCase)
	{
		Pattern pattern = Pattern.compile(regex, ignoreCase ? Pattern.CASE_INSENSITIVE : 0); 
		Matcher matcher = pattern.matcher(message);
		return matcher.find();
	}
}
