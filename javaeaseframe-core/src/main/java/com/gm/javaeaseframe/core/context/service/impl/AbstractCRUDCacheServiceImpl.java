package com.gm.javaeaseframe.core.context.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.core.context.dao.ICRUDDao;
import com.gm.javaeaseframe.core.context.model.BaseEntity;
import com.gm.javaeaseframe.core.context.model.Context;
import com.gm.javaeaseframe.core.context.service.ICRUDCacheService;
import com.gm.javaeaseframe.core.context.service.ICacheService;
/**
 * 本地永久缓存（在应用启动时就加入缓存，数据更新时同时更新缓存）
 * @author	GM
 * @date		2023-10-7
 */
public abstract class AbstractCRUDCacheServiceImpl<Dao extends ICRUDDao<T, PK>, T extends BaseEntity<PK>, PK extends Serializable> extends AbstractCRUDServiceImpl<Dao, T, PK>
        implements ICRUDCacheService<T, PK> {

    /** 如果未设置，默认使用本地缓存，即single版 */
    protected ICacheService cacheService = LocalCacheServiceImpl.getInstance();
    
    public AbstractCRUDCacheServiceImpl(){}
    
    @Autowired
    public void setDao(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    public void setCacheService(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 是否区分大小写，默认要区分
     * @return
     */
    protected boolean isCaseSensitive() {
        return true;
    }
    
    @Override
	public boolean isShareCache()
	{
		return this.cacheService.isShareCache();
	}

    protected String getCacheName() {
    	String cacheName = "";
    	cacheName = this.getRuntimesEntityClass().getName();
    	if(StringUtils.isEmpty(cacheName))
    	{
    		cacheName = this.getClass().getName();
    	}
    	if(!StringUtils.isEmpty(cacheName))
    	{
    		cacheName = cacheName.replace(".", ":");
    	}
        return cacheName;
    }

    /**
     * 获取扩展key值
     * @param data
     * @return
     */
    protected String getExtKey(T data) {
        return null;
    }

    @Override
	protected void removeBefore(PK[] ids, Context context) throws BusinessException
	{
    	removeCacheAtBefore(ids);
		super.removeBefore(ids, context);
	}

	@Override
    protected void removeAfter(PK[] ids, Context context, int result) throws BusinessException {
        removeCache(ids);
        super.removeAfter(ids, context, result);
    }

    protected void removeCache(PK[] ids) {
        for (PK id : ids) {
            removeCache(id);
        }
    }
    
    protected void removeCacheAtBefore(PK[] ids) {
        
    }

    @Override
    protected void saveAfter(T entity, Context context) throws BusinessException {
        putCache(entity.getId(), entity);
        super.saveAfter(entity, context);
    }

    @Override
    protected void updateAfter(T entity, Context context) throws BusinessException {
        entity = get((PK) entity.getId(), context);
        putCache(entity.getId(), entity);
        super.updateAfter(entity, context);
    }

    @Override
    public T getCache(PK id) {
        return cacheService.hget(getCacheName(), id.toString(), getRuntimesEntityClass());
    }

    @Override
    public List<T> getCacheList() {
    	Map<String, T> cacheMap = cacheService.hgetAll(getCacheName(), getRuntimesEntityClass());
    	if(cacheMap == null || cacheMap.isEmpty())
    	{
    		return new ArrayList<>();
    	}
    	List<T> retList = new ArrayList<>();
    	for(Map.Entry<String, T> entry : cacheMap.entrySet())
    	{
    		if(entry.getKey().equalsIgnoreCase(getExtKey(entry.getValue())))
    		{
    			continue;
    		}
    		retList.add(entry.getValue());
    	}
    	cacheMap.clear();
        return retList;
    }
    
    public List<T> getExtCacheList()
    {
    	Map<String, T> cacheMap = cacheService.hgetAll(getCacheName(), getRuntimesEntityClass());
    	if(cacheMap == null || cacheMap.isEmpty())
    	{
    		return new ArrayList<>();
    	}
    	List<T> retList = new ArrayList<>();
    	for(Map.Entry<String, T> entry : cacheMap.entrySet())
    	{
    		if(!entry.getKey().equalsIgnoreCase(getExtKey(entry.getValue())))
    		{
    			continue;
    		}
    		retList.add(entry.getValue());
    	}
    	cacheMap.clear();
        return retList;
    }

    @Override
    public void loadCache() {
    	log.info("loadCache..." + getCacheName());
        Map<String, Object> param = new HashMap<String, Object>();
        List<T> list = getDao().getList(param);
        for (T entity : list) {
//            cacheService.hset(getCacheName(), entity.getId().toString(), entity);
            putCache(entity.getId(), entity);
        }
    }

    @Override
    public void putCache(PK key, T data) {
        if (key != null) {
            cacheService.hset(getCacheName(), key.toString(), data);
        }
        String extKey = getExtKey(data);
        if (extKey != null && !isCaseSensitive()) {
            extKey = extKey.toUpperCase();
        }
        if (extKey != null) {
            cacheService.hset(getCacheName(), extKey, data);
        }
    }

    @Override
    public void removeCache(PK key) {
        cacheService.hdel(getCacheName(), key.toString());
    }

    @Override
    public T getExtCache(String extKey) {
        if (extKey != null && !isCaseSensitive()) {
            extKey = extKey.toUpperCase();
        }
        return cacheService.hget(getCacheName(), extKey, getRuntimesEntityClass());
    }

    @Override
    public void removeAllCache() {
        cacheService.del(getCacheName());
    }

    /**
     * 获取运行时当前Service所指向的真实T类型
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getRuntimesEntityClass() {
        // 获取当前实例的泛型的真实类类型
        Type ptype = this.getClass().getGenericSuperclass();
        if (ptype instanceof ParameterizedType) {
            // 获取本类泛型T的运行时实际类类型
            Type type = ((ParameterizedType) ptype).getActualTypeArguments()[1];// 1代表当前类的第二个泛型参数，即T的运行时类型
            if (type instanceof Class) {
                Class<T> reqClz = (Class<T>) type;
                return reqClz;
            }
        }
        return null;
    }

}
