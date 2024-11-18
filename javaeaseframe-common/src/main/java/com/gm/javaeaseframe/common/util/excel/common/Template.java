package com.gm.javaeaseframe.common.util.excel.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Template
{
	/** 属性列表，key：属性名，value：属性值 */
	private Map<String, String> attributes = new LinkedHashMap<>();
	/** 头部默认子元素集合，元素的XML值，不使用Map的原因是要求元素顺序，如果顺序错误，Excel将不能识别 */
	private List<String> headerDefaultElements = new ArrayList<>();
	/** 底部默认子元素集合，元素的XML值，不使用Map的原因是要求元素顺序，如果顺序错误，Excel将不能识别 */
	private List<String> footerDefaultElements = new ArrayList<>();
	
	public Map<String, String> getAttributes()
	{
		return attributes;
	}
	public void setAttributes(Map<String, String> attributes)
	{
		this.attributes = attributes;
	}
	public List<String> getHeaderDefaultElements()
	{
		return headerDefaultElements;
	}
	public void setHeaderDefaultElements(List<String> headerDefaultElements)
	{
		this.headerDefaultElements = headerDefaultElements;
	}
	public List<String> getFooterDefaultElements()
	{
		return footerDefaultElements;
	}
	public void setFooterDefaultElements(List<String> footerDefaultElements)
	{
		this.footerDefaultElements = footerDefaultElements;
	}
	
	public String attrsToStr()
	{
		StringBuffer attrs = new StringBuffer();
		if(this.attributes != null)
		{
			for(Map.Entry<String, String> entry : this.attributes.entrySet())
			{
				String name = entry.getKey();
				String value = entry.getValue();
				if(StringUtils.isNotEmpty(name))
				{
					attrs.append(" ").append(name).append("=\"").append(value).append("\"");
				}
			}
			attrs.append(" ");
		}
		return attrs.toString();
	}
}
