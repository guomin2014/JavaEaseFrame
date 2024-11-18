package com.gm.javaeaseframe.core.context.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.core.context.model.BaseEntity;
import com.gm.javaeaseframe.core.context.model.Context;
import com.gm.javaeaseframe.core.context.model.PageInfo;
import com.gm.javaeaseframe.core.context.model.Result;

public interface ICRUDService<T extends BaseEntity<PK>, PK extends Serializable> extends IService {

    /**
     * 保存或更新一个对像
     * 
     * @param t
     * @return
     */
    public T save(T entity, Context context) throws BusinessException;
    /**
     * 保存一批对象
     * @param list
     * @param context
     * @return
     * @throws BusinessException
     */
    public int save(List<T> list, Context context) throws BusinessException;

    /**
     * 更新一个对像
     * 
     * @param t
     * @return
     */
    public T update(T entity, Context context) throws BusinessException;
    /**
     * 更新一批对象
     * @param list
     * @param context
     * @return
     * @throws BusinessException
     */
    public int update(List<T> list, Context context) throws BusinessException;
    /**
     * 更新一批对象
     * @param entity	需要更新的属性
     * @param condition	更新的条件
     * @param context
     * @return
     * @throws BusinessException
     */
    public int update(T entity, T condition, Context context) throws BusinessException;
    /**
     * 更新一批对象
     * @param entity
     * @param condition
     * @return
     */
    public int update(T entity, Map condition, Context context) throws BusinessException;

    /**
     * 更新一批对象
     * @param map
     * @return
     */
    public int update(Map data, Map condition, Context context) throws BusinessException;

    /**
     * 取得一个对象，未取得对象时，抛出异常
     * 
     * @param id
     * @return
     */
    public T get(PK key, Context context) throws BusinessException;
    /**
     * 取得多个对象
     * @param ids
     * @param context
     * @return
     * @throws BusinessException
     */
    public List<T> get(PK[] ids, Context context) throws BusinessException;

    /**
     * 取提一个对象，
     * @param key
     * @param exception
     * @return
     * @throws BusinessException
     */
    public T get(PK key, boolean exception) throws BusinessException;
    /**
     * 取得多个对象
     * @param ids
     * @param exception true：抛出异常；false：不抛出异常，返回null值
     * @return
     * @throws BusinessException
     */
    public List<T> get(PK[] ids, boolean exception) throws BusinessException;
    /**
     * 取得一个对象
     * 
     * @param id
     * @param exception true：抛出异常；false：不抛出异常，返回null值
     * @return
     */
    public T get(PK key, Context context, boolean exception) throws BusinessException;
    /**
     * 取得多个对象
     * @param ids
     * @param context
     * @param exception   true：抛出异常；false：不抛出异常，返回null值
     * @return
     * @throws BusinessException
     */
    public List<T> get(PK[] ids, Context context, boolean exception) throws BusinessException;
    /**
     * 删除一个对象
     * @param id
     * @param context
     * @return
     * @throws BusinessException
     */
    public int remove(PK id, Context context) throws BusinessException;
    /**
     * 删除多个对象
     * @param ids 对象id集合，多个以,分隔
     */
    public int remove(PK[] ids, Context context) throws BusinessException;
    /**
     * 删除多个对象
     * @param condition
     * @return
     */
    public int remove(T condition, Context context) throws BusinessException;
    /**
     * 删除多个对象
     * @param condition
     * @return
     */
    public int remove(Map condition, Context context) throws BusinessException;

    /**
     * 查询记录条数
     * @param params
     * @param context
     * @return
     * @throws BusinessException
     */
    public int count(T params, Context context) throws BusinessException;

    /**
     * 查询列表
     * @param params
     * @param context
     * @return
     * @throws BusinessException
     */
    public List<T> find(T params, Context context) throws BusinessException;

    /**
     * 分页查询对象
     * @param params
     * @param pageInfo
     * @param context
     * @return
     * @throws BusinessException
     */
    public Result<T> find(T params, PageInfo pageInfo, Context context) throws BusinessException;
    
    /**
     * 统计查询列表
     * @param params
     * @param context
     * @return
     * @throws BusinessException
     */
    public List<T> findStat(T params, Context context) throws BusinessException;

    /**
     * 分页统计查询对象
     * @param params
     * @param pageInfo
     * @param context
     * @return
     * @throws BusinessException
     */
    public Result<T> findStat(T params, PageInfo pageInfo, Context context) throws BusinessException;

    /**
     * 检查某字段值是否重复
     * @param id
     * @param fieldVal
     * @return
     * @throws BusinessException
     */
    public boolean existFieldVal(PK id, String fieldName, Object fieldVal) throws BusinessException;

    /**
     * 检查某字段值在特定条件下是否重复(eg:同一个客户下是否重复)
     * @param id
     * @param fieldName
     * @param fieldVal
     * @param condition
     * @return
     * @throws BusinessException
     */
    public boolean existFieldVal(PK id, String fieldName, Object fieldVal, Map<String, Object> condition) throws BusinessException;

    /**
     * 数据导出
     * @param params
     * @param context
     * @return
     * @throws BusinessException
     */
    public byte[] export(T params, Context context) throws BusinessException;
    /**
     * 转换异常消息
     * @param e
     * @return
     */
    public String convertException(Throwable e);

}
