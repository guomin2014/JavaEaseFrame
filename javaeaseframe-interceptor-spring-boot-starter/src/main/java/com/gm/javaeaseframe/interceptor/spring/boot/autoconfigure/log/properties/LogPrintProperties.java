package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.log.properties;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class LogPrintProperties {

	private boolean enable = true;
	
	/** 是否从原生请求中获取请求Body */
	private boolean fetchFromNativeRequestEnable = false;
	/** 打印请求用户信息 */
	private boolean printRequestUser = true;
	/** 打印请求参数信息 */
	private boolean printRequestBody = false;
	/** 打印响应参数信息 */
	private boolean printResponseBody = false;
	
	/** 打印格式，如：statMOD=${MOD} statPV=${PV} statUV=${UV} statUserId=${UserId} statUserType=${UserType} statIP=${IP} statURL=${URL} statDuration=${Duration} ${Message} requestParams=${RequestParams} responseParams=${ResponseParams} */
	private String format;
	/** 打印格式中包含的变量名集合 */
	private Set<String> variableNames;
	/** 打印格式中变量与变量之间的分隔符 */
	private final String variableSplit = " ";
	
	public static enum VariableName {
		MOD, PV, UV, UserId, UserName, UserType, LoginName, IP, URL, RequestId, Duration, Message, RequestParams, ResponseParams;
		
		public String getKey() {
			return "${" + this.name() + "}";
		}
		
		public static String getKeyByName(String name) {
			return "${" + name + "}";
		}
		
		public static VariableName getByName(String name) {
			if (StringUtils.isBlank(name)) {
				return null;
			}
			for (VariableName vn : values()) {
				if (vn.name().equalsIgnoreCase(name)) {
					return vn;
				}
			}
			return null;
		}
	}
	
	public void init() {
		if (this.enable) {
			variableNames = new HashSet<>();
			if (StringUtils.isBlank(format)) {
				StringBuilder build = new StringBuilder();
				build.append("statMOD").append("=").append(VariableName.MOD.getKey());
				build.append(variableSplit).append("statPV").append("=").append(VariableName.PV.getKey());
				if (printRequestUser) {
					build.append(variableSplit).append("statUV").append("=").append(VariableName.UV.getKey());
					build.append(variableSplit).append("statUserId").append("=").append(VariableName.UserId.getKey());
				}
				build.append(variableSplit).append("statIP").append("=").append(VariableName.IP.getKey());
				build.append(variableSplit).append("statURL").append("=").append(VariableName.URL.getKey());
				build.append(variableSplit).append("statDuration").append("=").append(VariableName.Duration.getKey());
				build.append(variableSplit).append(VariableName.Message.getKey());
				if (printRequestBody) {
					build.append(variableSplit).append("requestParams").append("=").append(VariableName.RequestParams.getKey());
				}
				if (printResponseBody) {
					build.append(variableSplit).append("responseParams").append("=").append(VariableName.ResponseParams.getKey());
				}
				format = build.toString();
			}
			variableNames.addAll(this.extractVariables(format));
		}
	}
	
	private Set<String> extractVariables(String content) {
		Set<String> variables = new HashSet<>();
        // 定义正则表达式模式，用于匹配 ${} 包含的变量
        String regex = "\\$\\{([^}]+)}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        // 查找所有匹配的变量
        while (matcher.find()) {
            // 获取 ${} 内的内容
            variables.add(matcher.group(1));
        }

        return variables;
    }
	
	public boolean isEnable() {
		return enable;
	}

	public String getFormat() {
		return format;
	}

	public boolean isFetchFromNativeRequestEnable() {
		return fetchFromNativeRequestEnable;
	}

	public boolean isPrintRequestUser() {
		return printRequestUser;
	}

	public boolean isPrintRequestBody() {
		return printRequestBody;
	}

	public boolean isPrintResponseBody() {
		return printResponseBody;
	}

	public void setFetchFromNativeRequestEnable(boolean fetchFromNativeRequestEnable) {
		this.fetchFromNativeRequestEnable = fetchFromNativeRequestEnable;
	}

	public void setPrintRequestUser(boolean printRequestUser) {
		this.printRequestUser = printRequestUser;
	}

	public void setPrintRequestBody(boolean printRequestBody) {
		this.printRequestBody = printRequestBody;
	}

	public void setPrintResponseBody(boolean printResponseBody) {
		this.printResponseBody = printResponseBody;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Set<String> getVariableNames() {
		return variableNames;
	}
	
}
