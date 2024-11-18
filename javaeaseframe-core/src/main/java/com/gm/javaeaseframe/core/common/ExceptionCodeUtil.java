package com.gm.javaeaseframe.core.common;

import org.apache.commons.lang3.StringUtils;

import com.gm.javaeaseframe.core.constains.GlobalSysInfo;

/**
 * 异常码处理工具
 * 
 * @author GM
 * @date 2023年10月16日
 */
public class ExceptionCodeUtil {
	/**
	 * 封装异常码
	 * 异常码规则：共5位：前2位表示系统编码，后3位表示业务编码
	 * 超过5位编码表示已经包含系统编码，小于等于0表示使用自定义编码
	 * @param code
	 * @return
	 */
	public static int encodeCode(int code) {
		if (code >= 10000 || code <= 0) {//5位编码表示已经包含系统编码，小于等于0表示使用自定义编码
			return code;
		} else {
			try {
				String newCode = GlobalSysInfo.platformCode  + "" + code;
				return Integer.parseInt(newCode);
			} catch (Exception e) {
				return code;
			}
		}
	}
	/**
	 * 解析异常码
	 * 异常码规则：共5位：前2位表示系统编码，后3位表示业务编码
	 * 如果code长度小于5位，则系统码为0，code作为错误码
	 * @param code
	 * @return	[0]:系统编码，[1]:业务编码
	 */
	public static int[] decodeCode(int code) {
		if (code < 10000) {
			return new int[] {0, code};
		} else {
			String codeStr = String.valueOf(code);
			String sysCode = codeStr.substring(0, 2);
			String busiCode = codeStr.substring(2);
			return new int[] {Integer.parseInt(sysCode), Integer.parseInt(busiCode)};
		}
	}
	/**
	 * 封装异常消息（e为空或e.getMessage为空，或e.getMessage的长度超过50，则使用defaultMsg，否则使用e.getMessage）
	 * @param e
	 * @param defaultMsg
	 * @return
	 */
	public static String encodeMsg(Exception e, String defaultMsg) {
		if (e == null) {
			return defaultMsg;
		}
		String msg = e.getMessage();
		if (StringUtils.isBlank(msg)) {
			return defaultMsg;
		} else if (msg.length() > 50) {
			return defaultMsg;
		} else {
			return msg;
		}
	}
}
