package com.gm.javaeaseframe.core.context.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.gm.javaeaseframe.core.context.model.BaseEntity;
import com.gm.javaeaseframe.core.context.model.PageInfo;
import com.gm.javaeaseframe.core.context.model.ParamDto;
import com.gm.javaeaseframe.core.context.model.Result;

public interface ICRUDDao<T extends BaseEntity<PK>, PK extends Serializable> extends IBaseDao {

    /**
     * 新增对象
     * @param entity
     * @return
     */
    public int insert(T entity);
    
    /**
     * 批量新增对象
     * @param list
     * @return
     */
    public int insertBatch(List<T> list);

    /**
     * 修改对象
     * @param entity
     * @return
     */
    public int update(T entity);
    
    /**
     * 批量新增对象
     * @param list
     * @return
     */
    public int updateBatch(List<T> list);

    /**
     * 修改对象
     * @param entity
     * @param condition
     * @return
     */
    public int update(T entity, T condition);
    /**
     * 修改对象
     * @param entity
     * @param condition
     * @return
     */
    @SuppressWarnings("rawtypes")
	public int update(T entity, Map condition);

    /**
     * 修改对象
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public int update(Map data, Map condition);

    /**
     * 根据主健删除对象
     * @param entity
     * @return
     */
    public int delete(PK key);

    /**
     * 删除多个对象
     * @param ids
     * @return
     */
    public int delete(PK[] keys);
    /**
     * 删除多个对象
     * @param condition
     * @return
     */
    public int delete(T condition);
    /**
     * 删除多个对象
     * @param condition
     * @return
     */
    public int delete(Map condition);

    /**
     * 根据主健获取对象
     * @param id
     * @return
     */
    public T get(PK key);
    /**
     * 根据主健获取对象
     * @param keys
     * @return
     */
    public List<T> get(PK[] keys);

    /**
     * 查询一批对象
     * @param params
     * @return
     */
    public List<T> getList(T params);
    /**
     * 查询一批对象
     * @param params
     * @return
     */
    public List<T> getList(T params, String sqlId);

    /**
     * 统计数量
     * @param params
     * @return
     */
    public int getCount(T params);

    /**
     * 查询一批对象（分页）
     * @param params
     * @param beginIndex
     * @param maxResult
     * @return
     */
    public Result<T> getList(T params, PageInfo pageInfo);

    /**
     * 查询一批对象，指定范围
     * @param params
     * @param beginIndex
     * @param prePageResult
     * @return
     */
    public List<T> getList(T params, int beginIndex, int prePageResult);

    /**
     * 查询一批数据
     * @param map
     * @return
     */
    public List<T> getList(Map<String, Object> map);

    /**
     * 统计数量
     * @param paramDto
     * @return
     */
    public int getCount(ParamDto paramDto);

    /**
     * 查询一批对象（分页）
     * @param map
     * @param beginIndex
     * @param maxResult
     * @return
     */
    public Result<T> getList(Map<String, Object> map, PageInfo pageInfo);

    /**
     * 查询一批对象，指定范围
     * @param map
     * @param beginIndex
     * @param prePageResult
     * @return
     */
    public List<T> getList(Map<String, Object> map, int beginIndex, int prePageResult);

    /**
     * 查询一批数据
     * @param paramDto
     * @return
     */
    public List<T> getList(ParamDto paramDto);

    /**
     * 查询一批数据（分页）
     * @param paramDto
     * @return
     */
    public Result<T> getList(ParamDto paramDto, PageInfo pageInfo);
    /**
     * 查询一批对象（分页）指定SQL
     * @param params
     * @param pageInfo
     * @param sqlId
     * @return
     */
    public Result<T> getList(ParamDto paramDto, PageInfo pageInfo, String sqlId);

    /**
     * 查询一批数据，指定范围
     * @param paramDto
     * @param beginIndex
     * @param prePageResult
     * @return
     */
    public List<T> getList(ParamDto paramDto, int beginIndex, int prePageResult);
    
    //===========================================================================
    public T get(PK id,T makeTableNameOfentity);
    public int deleteBy(PK[] ids,T makeTableNameOfentity);
    
    
    /**
     * 获取统计数据行数
     * @param entity
     * @return
     */
    public int getStatCount(T entity);
    /**
     * 获取统计数据
     * @param entity
     * @param beginIndex
     * @param prePageResult
     * @return
     */
    public List<T> getStatList(T entity, int beginIndex, int prePageResult);
    /**
     * 获取统计数据
     * @param entity
     * @param pageInfo
     * @return
     */
    public Result<T> getStatList(T entity, PageInfo pageInfo);
    /**
     * 对分表数据进行统计
     * @param params
     * @return
     */
    public <R> List<R> doStatTable(T params);
    /**
     * 对分表数据进行统计并分页显示
     * @param params
     * @param pageInfo
     * @return
     */
    public <R> Result<R> doStatTable(T params, PageInfo pageInfo);

}
