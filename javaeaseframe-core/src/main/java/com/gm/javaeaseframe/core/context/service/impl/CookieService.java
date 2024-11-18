package com.gm.javaeaseframe.core.context.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.gm.javaeaseframe.common.util.AESUtil;
import com.gm.javaeaseframe.common.util.DataUtil;
import com.gm.javaeaseframe.common.util.HttpUtil;
import com.gm.javaeaseframe.common.util.MD5Util;
import com.gm.javaeaseframe.core.constains.GlobalSysInfo;
import com.gm.javaeaseframe.core.constains.SysConstains;
import com.gm.javaeaseframe.core.context.model.CookieInfo;
import com.gm.javaeaseframe.core.context.service.IUser;

public class CookieService
{
	private static Log logger = LogFactory.getLog(CookieService.class);
	/** coolie值分隔符 */
	public static final String COOKIE_STORE_SPLIT = "|";
	/** 当前cookie存储协议版本号 */
	public static final String COOKIE_STORE_VERSION = "1.0.0";
	/** cookie有效期，7*24小时 */
	private static final int COOKIE_MAX_AGE = 604800;
	
	/**
	 * 设置用户登录Cookie（有效期默认7天）
	 * @param response
	 * @param user			当前登录用户
	 * @param securityKey	安全密钥
	 * @param cookieName	cookie名称
	 * @param cookieDomain	cookie有效域名
	 */
	public static void setLoginCookie(HttpServletResponse response, IUser user, String securityKey, String cookieDomain)
	{
		setLoginCookie(response, user, securityKey, cookieDomain, COOKIE_MAX_AGE);
	}
	/**
	 * 设置用户登录Cookie
	 * @param response
	 * @param user			当前登录用户
	 * @param securityKey	安全密钥
	 * @param cookieName	cookie名称
	 * @param cookieDomain	cookie有效域名
	 * @param cookieMaxAge	cookie的有效期，单位：秒，大于等于0：则使用时间有效期，否则与session有效期相同
	 * 
	 */
	public static CookieInfo setLoginCookie(HttpServletResponse response, IUser user, String securityKey, String cookieDomain, int cookieMaxAge)
	{
		try
		{
			//设置cookie有效串，防篡改   LTPA版本号+创建时间+过期时间+用户名+LTPA密钥
//			SHA-1=LTPA版本号+创建时间+过期时间+用户名+Domino LTPA 密钥
//			LTPA Cookie= Base64(LTPA版本号+创建时间+过期时间+用户名+SHA-1)
			Calendar cal = Calendar.getInstance();
			long currTime = cal.getTime().getTime() / 1000;
			cal.add(Calendar.DAY_OF_YEAR, 1);
			long expiresTime = cal.getTime().getTime() / 1000;
			Long userId = user.getId();
			int userType = DataUtil.conver2Int(user.getUserType());
			int userRole = DataUtil.conver2Int(user.getUserRole());
			int maintain = user.isSystemUser() ? 1 : 0;
			String loginName = user.getLoginName();
			String userName = user.getRealName();
			Long customerId = user.getCustomerId();
			String customerNum = "";
			String customerJson = "";
			Long deptId = null;
			String deptName = null;
			int deptManager = user.isManager() ? 1 : 0;
			Long areaId = null;
			if(customerId != null || StringUtils.isNotEmpty(customerNum) || deptId != null || areaId != null)
			{
				try
				{
					customerJson = DataUtil.conver2Long(customerId)
							 + COOKIE_STORE_SPLIT + StringUtils.trim(customerNum) + COOKIE_STORE_SPLIT + DataUtil.conver2Long(deptId)
							 + COOKIE_STORE_SPLIT + StringUtils.trim(deptName) + COOKIE_STORE_SPLIT + deptManager + COOKIE_STORE_SPLIT + DataUtil.conver2Long(areaId);
					customerJson = AESUtil.encrypt(customerJson, securityKey);
				}
				catch(Exception e){
					logger.debug("用户的关联信息加密异常-->" + e.getMessage());
				}
			}
			customerJson = StringUtils.trim(customerJson);
			String cookieStroe = COOKIE_STORE_VERSION + COOKIE_STORE_SPLIT + currTime + COOKIE_STORE_SPLIT + expiresTime;
			String pinId = AESUtil.encrypt(userId.toString() + COOKIE_STORE_SPLIT + userType + COOKIE_STORE_SPLIT + userRole, securityKey);
			String trackId = MD5Util.getSignAndMD5(cookieStroe, pinId, loginName, userName, customerJson, securityKey);
			HttpUtil.setCookieValue(response, SysConstains.COOKIE_PIN, loginName, cookieDomain, cookieMaxAge);
			HttpUtil.setCookieValue(response, SysConstains.COOKIE_PIN_ID, pinId, cookieDomain, cookieMaxAge);
			HttpUtil.setCookieValue(response, SysConstains.COOKIE_UNICK, userName, cookieDomain, cookieMaxAge);
			HttpUtil.setCookieValue(response, SysConstains.COOKIE_STORE, cookieStroe, cookieDomain, cookieMaxAge);
			HttpUtil.setCookieValue(response, SysConstains.COOKIE_STORE_TOKEN, trackId, cookieDomain, cookieMaxAge);
			HttpUtil.setCookieValue(response, SysConstains.COOKIE_PAGE_UNICK, userName, cookieDomain, cookieMaxAge, false);
			HttpUtil.setCookieValue(response, SysConstains.COOKIE_PAGE_MAINTAIN, maintain + "", cookieDomain, cookieMaxAge, false);
			if(StringUtils.isNotEmpty(customerJson))
			{
				HttpUtil.setCookieValue(response, SysConstains.COOKIE_UNION, customerJson, cookieDomain, cookieMaxAge);
			}
			CookieInfo info = new CookieInfo(trackId, new Date(currTime), new Date(expiresTime), user);
			return info;
		}
		catch(Throwable e){
			logger.warn("设置用户的cookie异常-->" + e.getMessage());
			return null;
		}
	}
	
	public static CookieInfo getLoginCookie(HttpServletRequest request, String securityKey)
	{
		try
		{
			String loginName = HttpUtil.getCookieValue(request, SysConstains.COOKIE_PIN);
			String pinId = HttpUtil.getCookieValue(request, SysConstains.COOKIE_PIN_ID);
			String userName = HttpUtil.getCookieValue(request, SysConstains.COOKIE_UNICK);
			String customerJson = HttpUtil.getCookieValue(request, SysConstains.COOKIE_UNION);
			String cookieStroe = HttpUtil.getCookieValue(request, SysConstains.COOKIE_STORE);
			String trackId = HttpUtil.getCookieValue(request, SysConstains.COOKIE_STORE_TOKEN);
			customerJson = StringUtils.trim(customerJson);
			String realTrackId = MD5Util.getSignAndMD5(cookieStroe, pinId, loginName, userName, customerJson, securityKey);
			if(realTrackId.equals(trackId))
			{
				String[] values = cookieStroe.split("\\" + COOKIE_STORE_SPLIT);
				long currTime = Long.parseLong(values[1]) * 1000;
				long expiresTime = Long.parseLong(values[2]) * 1000;
				pinId = AESUtil.decrypt(pinId, securityKey);
				String[] uss = pinId.split("\\" + COOKIE_STORE_SPLIT);
				Long userId = Long.parseLong(uss[0]);
				int userType = uss.length >= 2 ? Integer.parseInt(uss[1]) : 0;
				int userRole = uss.length >= 3 ? Integer.parseInt(uss[2]) : 0;
				Long customerId = null;
				Long customerJoinId = null;
				String customerNum = null;
				Long deptId = null;
				String deptName = null;
				int deptManager = 0;
				Long areaId = null;
				if(StringUtils.isNotEmpty(customerJson))
				{
					try
					{
						customerJson = AESUtil.decrypt(customerJson, securityKey);
						String[] css = customerJson.split("\\" + COOKIE_STORE_SPLIT);
						customerId = Long.parseLong(css[0]);
						customerJoinId = Long.parseLong(css[1]);
						customerNum = css[2];
						if(css.length >= 4)
							deptId = Long.parseLong(css[3]);
						if(css.length >= 5)
							deptName = css[4];
						if(css.length >= 6)
							deptManager = Integer.parseInt(css[5]);
						if(css.length >= 7)
						    areaId = Long.parseLong(css[6]);
					}catch(Exception e){
						logger.debug("用户的关联信息解密异常-->" + e.getMessage());
					}
				}
				IUser user = createUser(loginName, userId, userName, userType, userRole, customerId, customerJoinId, customerNum, deptId, deptName, deptManager, areaId);
				CookieInfo info = new CookieInfo(trackId, new Date(currTime), new Date(expiresTime), user);
				return info;
			}
		}
		catch(Throwable e){
			logger.warn("获取用户的cookie异常-->" + e.getMessage());
		}
		return null;
	}
	
	public static CookieInfo getLoginCookie(HttpServletRequest request)
	{
		try
		{
			String securityKey = GlobalSysInfo.getPropertyValue(SysConstains.PROP_COOKIE_SECURITY_KEY);
			return getLoginCookie(request, securityKey);
		}
		catch(Throwable e){
			logger.warn("获取用户的cookie异常-->" + e.getMessage());
		}
		return null;
	}
	
	public static void deleteLoginCookie(HttpServletRequest request, HttpServletResponse response)
	{
		HttpUtil.deleteCookie(request, response, SysConstains.COOKIE_PIN);
		HttpUtil.deleteCookie(request, response, SysConstains.COOKIE_PIN_ID);
		HttpUtil.deleteCookie(request, response, SysConstains.COOKIE_UNICK);
		HttpUtil.deleteCookie(request, response, SysConstains.COOKIE_UNION);
		HttpUtil.deleteCookie(request, response, SysConstains.COOKIE_STORE);
		HttpUtil.deleteCookie(request, response, SysConstains.COOKIE_STORE_TOKEN);
	}
	
	public static void setCookieBySecurity(HttpServletResponse response, String token, String securityKey, String cookieDomain)
	{
		setCookieBySecurity(response, token, securityKey, cookieDomain, COOKIE_MAX_AGE);
	}
	public static void setCookieBySecurity(HttpServletResponse response, String token, String securityKey, String cookieDomain, int cookieMaxAge)
	{
		try
		{
			token = AESUtil.decrypt(token, securityKey);
			JSONObject json = JSONObject.parseObject(token);
			String loginName = json.getString("loginName");
			Long userId = json.getLong("userId");
			String userName = json.getString("userName");
			int userType = json.getIntValue("userType");
			int userRole = json.getIntValue("userRole");
			int deptManager = json.getIntValue("deptManager");
			Long customerId = json.getLong("customerId");
			Long customerJoinId = json.getLong("customerJoinId");
			String customerNum = json.getString("customerNum");
			Long deptId = json.getLong("deptId");
			String deptName = json.getString("deptName");
			Long areaId = json.getLong("areaId");
			IUser user = createUser(loginName, userId, userName, userType, userRole, customerId, customerJoinId, customerNum, deptId, deptName, deptManager, areaId);
			setLoginCookie(response, user, securityKey, cookieDomain, cookieMaxAge);
		}
		catch(Throwable e){
			logger.warn("设置用户的cookie异常-->" + e.getMessage());
		}
	}
	
	public static String getCookieBySecurity(HttpServletRequest request, String securityKey, String newSecurityKey)
	{
		try
		{
			CookieInfo info = getLoginCookie(request, securityKey);
			if(info == null || info.getUser() == null)
			{
				return "";
			}
			IUser user = info.getUser();
			JSONObject json = new JSONObject();
			json.put("loginName", user.getLoginName());
			json.put("userId", user.getId());
			json.put("userName", user.getRealName());
			json.put("userType", DataUtil.conver2Int(user.getUserType()));
			json.put("userRole", DataUtil.conver2Int(user.getUserRole()));
			return AESUtil.encrypt(json.toJSONString(), newSecurityKey);
		}
		catch(Throwable e)
		{
			logger.warn("获取用户的cookie异常-->" + e.getMessage());
			return "";
		}
	}
	public static void setCookieForAuth(HttpServletRequest request, HttpServletResponse response, String securityKey, Set<String> urls) throws Exception
	{
//		String menuUrl = StringUtils.converArray2Str(urls.toArray(new String[urls.size()]));
		//remove by gm 20170228 该方式会占用很多内存，频繁访问将导致Java Heap Over
//		BitSet bit = new BitSet();
//		for(String url : urls)
//		{
//			int index = url.hashCode() & (Integer.MAX_VALUE-1);
//			bit.set(index);
//		}
//		//剔除重复数字后的元素个数
//		int bitLen = bit.cardinality();
//		//进行排序，即把bit为true的元素复制到另一个数组
//		int[] orderedArray = new int[bitLen];
//		int k = 0;
//		for (int i = bit.nextSetBit(0); i >= 0; i = bit.nextSetBit(i + 1)) {
//			orderedArray[k++] = i;
//		}
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ObjectOutputStream oos = new ObjectOutputStream(bos);
//		oos.writeObject(orderedArray);
//		oos.flush();
//		menuUrl = Base64.encode(bos.toByteArray());
//		menuUrl = AESUtil.encrypt(menuUrl, securityKey);
		StringBuilder sb = new StringBuilder();
		if(urls != null && urls.size() > 0)
		{
			for(String url : urls)
			{
				int index = url.hashCode() & (Integer.MAX_VALUE-1);
				sb.append(index).append(",");
			}
		}
		String menuUrl = sb.toString();
		menuUrl = AESUtil.encrypt(menuUrl, securityKey);
		HttpUtil.setCookieValue(request, response, SysConstains.COOKIE_MENU, menuUrl, -1);
	}
	
	public static void deleteCookieForAuth(HttpServletRequest request, HttpServletResponse response)
	{
		HttpUtil.deleteCookie(request, response, SysConstains.COOKIE_MENU);
	}
	
	public static boolean checkAuth(HttpServletRequest request, String requestUrl, String securityKey) throws Exception
	{
		int code = requestUrl.hashCode() & (Integer.MAX_VALUE-1);
		String menuUrl = HttpUtil.getCookieValue(request, SysConstains.COOKIE_MENU);
		if(StringUtils.isEmpty(menuUrl))
		{
			return false;
		}
		menuUrl = AESUtil.decrypt(menuUrl, securityKey);
//		byte[] bys = Base64.decode(menuUrl);
//		ByteArrayInputStream bais = new ByteArrayInputStream(bys);
//		ObjectInputStream bis = new ObjectInputStream(bais);
//		Object obj = bis.readObject();
//		if(obj == null)
//		{
//			return false;
//		}
//		int[] orderedArray = (int[])obj;
//		String codes = "," + StringUtils.converArr2Str(orderedArray) + ",";
		String codes = "," + menuUrl + ",";
		String codeKey = "," + code + ",";
		if(codes.indexOf(codeKey) != -1)
		{
			return true;
		}
		return false;
	}
	
	private static IUser createUser(final String loginName, final Long userId, final String userName, final int userType, final int userRole,
			final Long customerId, final Long customerJoinId, final String customerNum, 
			final Long deptId, final String deptName, final int deptManager, final Long areaId)
	{
		return new IUser(){
			@Override
			public Long getId()
			{
				return userId;
			}

			@Override
			public Long getCustomerId()
			{
				return customerId;
			}

			@Override
			public String getCustomerCode() {
				return customerNum;
			}

			@Override
			public String getLoginName()
			{
				return loginName;
			}

			@Override
			public String getRealName()
			{
				return userName;
			}

			@Override
			public boolean isAdmin()
			{
				return userType == 1 && userRole == 1;
			}

			@Override
			public boolean isSystemUser()
			{
				return userType == 1;
			}
			
			@Override
			public boolean isManager()
			{
				return userRole == 1;
			}

			@Override
			public Integer getUserType()
			{
				return userType;
			}

            @Override
            public Integer getUserRole() {
                return userRole;
            }

			@Override
			public Integer getStatus() {
				return null;
			}

			};
	}
}
