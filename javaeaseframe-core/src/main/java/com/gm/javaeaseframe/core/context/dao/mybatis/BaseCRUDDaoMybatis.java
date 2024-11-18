package com.gm.javaeaseframe.core.context.dao.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.util.DataUtil;
import com.gm.javaeaseframe.common.util.EntityUtil;
import com.gm.javaeaseframe.core.context.dao.ICRUDDao;
import com.gm.javaeaseframe.core.context.model.BaseEntity;
import com.gm.javaeaseframe.core.context.model.DbMatchMode;
import com.gm.javaeaseframe.core.context.model.PageInfo;
import com.gm.javaeaseframe.core.context.model.ParamDto;
import com.gm.javaeaseframe.core.context.model.Result;

public class BaseCRUDDaoMybatis<T extends BaseEntity<PK>, PK extends Serializable> extends SqlSessionDaoSupport implements ICRUDDao<T, PK> {

    protected static final String SPLIT_CHAR = ".";
    /** 命名空间 */
    protected String namespace = this.getClass().getName();
    /** 插入语句sqlid */
    protected static final String SQLID_INSERT = "insert";
    /** 批量插入语句sqlid */
    protected static final String SQLID_INSERT_BATCH = "insertBatch";
    /** 更新语句sqlid */
    protected static final String SQLID_UPDATE = "update";
    /** 更新语句sqlid */
    protected static final String SQLID_UPDATE_BATCH = "updateBatch";
    /** 获取一个对象sqlid */
    protected static final String SQLID_GET = "getByKey";
    /** 根据主健删除一个对象sqlid */
    protected static final String SQLID_DELETE = "deleteByKey";
    /** 删除一批数据 */
    protected static final String SQLID_DELETE_PKS = "deleteByKeys";
    /** 删除一批数据 */
    protected static final String SQLID_DELETE_MAP = "deleteByMap";
    /** 根据实体对象查询一批数据 */
    protected static final String SQLID_LIST = "getList";
    /** 统计数量,分页查询统计数量id=list(id) + COUNT_SUFFIX */
    protected static final String SQLID_COUNT = "getListCount";
    /** 假删除，改变状态 */
    protected static final String SQL_DELETE_BY_STATUS = "changeStatusByKeys";
    
    /** 根据实体对象查询一批统计数据 */
    protected static final String SQLID_STAT_LIST = "getStatList";
    /** 根据实体对象查询一批统计数据 */
    protected static final String SQLID_STAT_LIST_COUNT = "getStatListCount";
    /** 根据实体对象查询一批统计数据 */
    protected static final String SQLID_STAT_EXT_LIST = "getStatExtList";
    /** 根据实体对象查询一批统计数据 */
    protected static final String SQLID_STAT_EXT_LIST_COUNT = "getStatExtListCount";
    
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    /** 获取包含命名空间的mybatis sql id */
    protected String getSqlId(String id) {
        return namespace + SPLIT_CHAR + id;
    }

    @Override
    public int insert(T entity) {
        return getSqlSession().insert(getSqlId(SQLID_INSERT), entity);
    }
    
	@Override
	public int insertBatch(List<T> list) {
		if (list == null || list.isEmpty()) {
			return 0;
		}
		ParamDto param = new ParamDto();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("dataList", list);
		param.setData(data);
		return getSqlSession().insert(getSqlId(SQLID_INSERT_BATCH), param);
	}

	@SuppressWarnings("unchecked")
    @Override
    public int update(T entity) {
        Map data = new HashMap();
        EntityUtil.entityToMap(entity, data);
        Map condition = new HashMap();
        condition.put("id", entity.getId());
        return update(data, condition);
    }
	
    @Override
    public int updateBatch(List<T> list) {
        if(list == null || list.isEmpty()){
            return 0;
        }
         ParamDto param = new ParamDto();
         Map<String, Object> data = new HashMap<String, Object>();
         List<Map> dataList = new ArrayList<>();
         for (T entity : list) {
             Map dataMap = new HashMap();
             EntityUtil.entityToMap(entity, dataMap);
             dataList.add(dataMap);
         }
         data.put("dataList", dataList);
         param.setData(data);
        return getSqlSession().update(getSqlId(SQLID_UPDATE_BATCH), param);
    }

    @SuppressWarnings("rawtypes")
    public int update(T entity, T condition) {
        Map data = new HashMap();
        EntityUtil.entityToMap(entity, data);
        Map conditionMap = new HashMap();
        EntityUtil.entityToMap(condition, conditionMap);
        return update(data, conditionMap);
    }

    public int update(T entity, Map condition) {
        Map data = new HashMap();
        EntityUtil.entityToMap(entity, data);
        return update(data, condition);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int update(Map data, Map condition) {
        ParamDto param = new ParamDto();
        param.setData(data);
        param.setCondition(condition);
        return getSqlSession().update(getSqlId(SQLID_UPDATE), param);
    }

    @Override
    public int delete(PK key) {
        ParamDto param = new ParamDto();
        param.getCondition().put("id", key);
        return getSqlSession().delete(getSqlId(SQLID_DELETE), key);
    }

    @Override
    public int delete(PK[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return getSqlSession().delete(getSqlId(SQLID_DELETE_PKS), ids);
    }

    @Override
    public int delete(T condition) {
        if (condition == null) {
            return 0;
        }
        Map data = new HashMap();
        EntityUtil.entityToMap(condition, data);
        return delete(data);
    }

    @Override
    public int delete(Map condition) {
        if (condition == null || condition.isEmpty()) {
            return 0;
        }
        ParamDto param = new ParamDto();
        param.setCondition(condition);
        return getSqlSession().delete(getSqlId(SQLID_DELETE_MAP), param);
    }

    @Override
    public T get(PK id) {
        ParamDto paramDto = new ParamDto();
        paramDto.getCondition().put("id", id);
        return getSqlSession().selectOne(getSqlId(SQLID_GET), paramDto);
    }

    @Override
    public List<T> get(PK[] ids) {
        if (ids == null || ids.length == 0) {
            return new ArrayList<>();
        }
        ParamDto param = new ParamDto();
        param.getCondition().put("idList", ids);
        return getList(param);
    }

    @Override
    public List<T> getList(T params) {
        return getList(getQueryParam(params));
    }

    @Override
	public List<T> getList(T params, String sqlId) {
		return getList(getQueryParam(params), sqlId);
	}

	@Override
    public int getCount(T params) {
        return getCount(getQueryParam(params));
    }

    @Override
    public Result<T> getList(T params, PageInfo pageInfo) {
        return getList(getQueryParam(params), pageInfo);
    }

    @Override
    public List<T> getList(T params, int beginIndex, int prePageResult) {
        return getList(getQueryParam(params), beginIndex, prePageResult);
    }

    protected ParamDto getQueryParam(T params) {
        ParamDto paramDto = new ParamDto();
        Map<String, Object> map = new HashMap<>();
        EntityUtil.entityToMap(params, map);
        paramDto.setCondition(map);
        paramDto.setOrderColList(params.getOrderColList());
        paramDto.setData(params.getTabColMap());
        paramDto.setMatchMode(params.getMatchMode());
        paramDto.setColPickMode(params.getColPickMode());
        paramDto.setGroupList(params.getGroupList());
        return paramDto;
    }

    @Override
    public List<T> getList(Map<String, Object> condition) {
        ParamDto paramDto = new ParamDto();
        paramDto.setCondition(condition);
        return getSqlSession().selectList(getSqlId(SQLID_LIST), paramDto);
    }

    @Override
    public int getCount(ParamDto paramDto) {
        return getSqlSession().selectOne(getSqlId(SQLID_COUNT), cpyQueryParamDto(paramDto));
    }
    
    public int getCount(ParamDto paramDto, String sqlId) {
        return getSqlSession().selectOne(getSqlId(sqlId), cpyQueryParamDto(paramDto));
    }

    @Override
    public Result<T> getList(Map<String, Object> condition, PageInfo pageInfo) {
        ParamDto paramDto = new ParamDto();
        paramDto.setCondition(condition);
        return getList(paramDto, pageInfo);
    }

    @Override
    public List<T> getList(Map<String, Object> condition, int beginIndex, int prePageResult) {
        ParamDto paramDto = new ParamDto();
        paramDto.setCondition(condition);
        return getList(paramDto, beginIndex, prePageResult);
    }

    @Override
    public List<T> getList(ParamDto paramDto) {
        return this.getList(paramDto, SQLID_LIST);
    }
    public List<T> getList(ParamDto paramDto, String sqlId) {
    	return getSqlSession().selectList(getSqlId(sqlId), paramDto);
    }

    @Override
    public Result<T> getList(ParamDto paramDto, PageInfo pageInfo) {
        Result<T> result = new Result<T>();
        List<T> list = null;
        int count = getCount(paramDto);

        if (count == 0) {
            list = new ArrayList<>();
        } else {
            if (pageInfo.getPrePageResult() == -1) {// 查询所有记录
                list = getSqlSession().selectList(getSqlId(SQLID_LIST), paramDto);
            } else {
                if (pageInfo.getBeginIndex() > count) {
//                    pageInfo.setCurrPage(1);
                    list = new ArrayList<>();
                } else {
                    RowBounds rowBounds = new RowBounds(pageInfo.getBeginIndex(), pageInfo.getPrePageResult());
                    list = getSqlSession().selectList(getSqlId(SQLID_LIST), cpyQueryParamDto(paramDto), rowBounds);
                }
            }
        }
        pageInfo.setTotalResult(count);
        result.setPageInfo(pageInfo);
        result.setList(list);
        return result;
    }
    
    @Override
	public Result<T> getList(ParamDto paramDto, PageInfo pageInfo, String sqlId)
	{
		if (StringUtils.isEmpty(sqlId))
		{
			sqlId = SQLID_LIST;
		}
		Result<T> result = new Result<T>();
		List<T> list = null;
		int count = getCount(paramDto, sqlId + "Count");

		if (count == 0)
		{
			list = new ArrayList<T>();
		}
		else
		{
			if (pageInfo.getPrePageResult() == -1)
			{// 查询所有记录
				list = getSqlSession().selectList(getSqlId(sqlId), paramDto);
			}
			else
			{
				if (pageInfo.getBeginIndex() > count)
				{
//					pageInfo.setCurrPage(1);
				    list = new ArrayList<>();
				} else {
    				RowBounds rowBounds = new RowBounds(pageInfo.getBeginIndex(), pageInfo.getPrePageResult());
    				list = getSqlSession().selectList(getSqlId(sqlId), cpyQueryParamDto(paramDto), rowBounds);
				}
			}
		}
		pageInfo.setTotalResult(count);
		result.setPageInfo(pageInfo);
		result.setList(list);
		return result;
	}

	@Override
    public List<T> getList(ParamDto paramDto, int beginIndex, int prePageResult) {
        RowBounds rowBounds = new RowBounds(beginIndex, prePageResult);
        return getSqlSession().selectList(getSqlId(SQLID_LIST), cpyQueryParamDto(paramDto), rowBounds);
    }

    protected ParamDto cpyQueryParamDto(ParamDto paramDto) {
        if (paramDto.getMatchMode() == null || paramDto.getMatchMode() == DbMatchMode.WHOLE_WORD) {
            return paramDto;
        }
        ParamDto param = null;
        if (paramDto != null) {
            param = new ParamDto();
            if (paramDto.getCondition() != null) {
                Map<String, Object> condition = new HashMap<String, Object>();
                Iterator<Map.Entry<String, Object>> ite = paramDto.getCondition().entrySet().iterator();
                while (ite.hasNext()) {
                    Map.Entry<String, Object> entry = ite.next();
                    if (entry.getValue() != null && entry.getValue() instanceof String) {
                        String value = (String) entry.getValue();
                        if (StringUtils.isNotEmpty(value)) {
                            if (paramDto.getMatchMode() == DbMatchMode.LIKE_LEFT) {
                                value = "%" + value;
                            } else if (paramDto.getMatchMode() == DbMatchMode.LIKE_RIGHT) {
                                value = value + "%";
                            } else if (paramDto.getMatchMode() == DbMatchMode.LIKE_BOTH) {
                                value = "%" + value + "%";
                            }
                        }
                        condition.put(entry.getKey(), value);
                    } else {
                        condition.put(entry.getKey(), entry.getValue());
                    }
                }
                param.setCondition(condition);
            }
            param.setData(paramDto.getData());
            param.setGroupList(paramDto.getGroupList());
            param.setOrderCol(paramDto.getOrderCol());
            param.setOrderColList(paramDto.getOrderColList());
            param.setColPickMode(paramDto.getColPickMode());
            param.setTableName(paramDto.getTableName());
        }

        return param;
    }

    @Override
    public T get(PK id, T makeTableNameOfentity) {
        return this.get(id);
    }

    @Override
    public int deleteBy(PK[] ids, T makeTableNameOfentity) {
        return this.delete(ids);
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
		if(StringUtils.isNotEmpty(message) && isMatch(".*Table.*doesn't exist.*", message))//表不存在，则返回无数据，不返回异常
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

    @Override
    public int getStatCount(T entity)
    {
        return this.getStatCount(entity, SQLID_STAT_LIST_COUNT);
    }
    public int getStatCount(T entity, String sqlId)
    {
        ParamDto param = getQueryParam(entity);
        Integer count = getSqlSession().selectOne(getSqlId(sqlId), cpyQueryParamDto(param));
        return DataUtil.conver2Int(count);
    }

    @Override
    public List<T> getStatList(T entity, int beginIndex, int prePageResult)
    {
        ParamDto param = getQueryParam(entity);
        if(prePageResult <= 0)
        {
            return getSqlSession().selectList(getSqlId(SQLID_STAT_LIST), cpyQueryParamDto(param));
        }
        else
        {
            RowBounds rowBounds = new RowBounds(beginIndex, prePageResult);
            return getSqlSession().selectList(getSqlId(SQLID_STAT_LIST), cpyQueryParamDto(param), rowBounds);
        }
    }

    @Override
    public Result<T> getStatList(T entity, PageInfo pageInfo)
    {
        ParamDto paramDto = getQueryParam(entity);
        Result<T> result = new Result<T>();
        List<T> list = null;
        int count = getStatCount(entity);

        if (count == 0) {
            list = new ArrayList<T>();
        } else {
            if (pageInfo.getPrePageResult() == -1) {// 查询所有记录
                list = getSqlSession().selectList(getSqlId(SQLID_STAT_LIST), paramDto);
            } else {
                if (pageInfo.getBeginIndex() > count) {
//                    pageInfo.setCurrPage(1);
                    list = new ArrayList<>();
                } else {
                    RowBounds rowBounds = new RowBounds(pageInfo.getBeginIndex(), pageInfo.getPrePageResult());
                    list = getSqlSession().selectList(getSqlId(SQLID_STAT_LIST), cpyQueryParamDto(paramDto), rowBounds);
                }
            }
        }
        pageInfo.setTotalResult(count);
        result.setPageInfo(pageInfo);
        result.setList(list);
        return result;
    }

    @Override
    public <R> List<R> doStatTable(T params)
    {
        return getSqlSession().selectList(getSqlId(SQLID_STAT_EXT_LIST), getQueryParam(params));
    }

    @Override
    public <R> Result<R> doStatTable(T params, PageInfo pageInfo)
    {
        ParamDto paramDto = getQueryParam(params);
        Result<R> result = new Result<R>();
        List<R> list = null;
        int count = getStatCount(params, SQLID_STAT_EXT_LIST_COUNT);
        if (count == 0) 
        {
            list = new ArrayList<R>();
        }
        else
        {
            if(pageInfo == null || pageInfo.getPrePageResult() == -1)// 查询所有记录
            {
                list = getSqlSession().selectList(getSqlId(SQLID_STAT_EXT_LIST), paramDto);
            }
            else
            {
                if(pageInfo.getBeginIndex() > count)
                {
//                    pageInfo.setCurrPage(1);
                    list = new ArrayList<>();
                } else {
                    RowBounds rowBounds = new RowBounds(pageInfo.getBeginIndex(), pageInfo.getPrePageResult());
                    list = getSqlSession().selectList(getSqlId(SQLID_STAT_EXT_LIST), cpyQueryParamDto(paramDto), rowBounds);
                }
            }
        }
        if(pageInfo == null)
        {
            pageInfo = new PageInfo();
        }
        pageInfo.setTotalResult(count);
        result.setPageInfo(pageInfo);
        result.setList(list);
        return result;
    }
	
}
