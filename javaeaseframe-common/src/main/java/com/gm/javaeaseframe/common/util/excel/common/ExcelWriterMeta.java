package com.gm.javaeaseframe.common.util.excel.common;

public class ExcelWriterMeta
{
	/** 主标题 */
	private String title;
	/** 列标题 */
	private String[] columnName;
	/** 每个sheet最大多少行，-1：表示不限制，2003: 65535行，256列, 2007: 1048576行，16384列 */
	private int maxRowPreSheet = -1;
	/** sheet的名称 */
	private String sheetName;
	/** 创建用户 */
	private String createUser;
	/** 文件存储路径 */
	private String filePath;
	/** 模板文件路径 */
	private String templateFilePath;
	/** 模板文件是否包含主标题 */
	private boolean templateIncloudTitle = false;
	/** 模板文件是否包含列标题 */
	private boolean templateIncloudColumn = false;
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String[] getColumnName()
	{
		return columnName;
	}
	public void setColumnName(String[] columnName)
	{
		this.columnName = columnName;
	}
	public int getMaxRowPreSheet()
	{
		return maxRowPreSheet;
	}
	public void setMaxRowPreSheet(int maxRowPreSheet)
	{
		this.maxRowPreSheet = maxRowPreSheet;
	}
	public String getSheetName()
	{
		return sheetName;
	}
	public void setSheetName(String sheetName)
	{
		this.sheetName = sheetName;
	}
	public String getCreateUser()
	{
		return createUser;
	}
	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}
	public String getFilePath()
	{
		return filePath;
	}
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	public String getTemplateFilePath()
	{
		return templateFilePath;
	}
	public void setTemplateFilePath(String templateFilePath)
	{
		this.templateFilePath = templateFilePath;
	}
	public boolean getTemplateIncloudTitle()
	{
		return templateIncloudTitle;
	}
	public void setTemplateIncloudTitle(boolean templateIncloudTitle)
	{
		this.templateIncloudTitle = templateIncloudTitle;
	}
	public boolean getTemplateIncloudColumn()
	{
		return templateIncloudColumn;
	}
	public void setTemplateIncloudColumn(boolean templateIncloudColumn)
	{
		this.templateIncloudColumn = templateIncloudColumn;
	}
	
}
