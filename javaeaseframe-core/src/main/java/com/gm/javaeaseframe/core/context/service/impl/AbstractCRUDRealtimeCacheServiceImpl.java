package com.gm.javaeaseframe.core.context.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.core.context.dao.ICRUDDao;
import com.gm.javaeaseframe.core.context.model.BaseEntity;
import com.gm.javaeaseframe.core.context.model.Context;
import com.gm.javaeaseframe.core.context.service.ICRUDCacheService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
/**
 * 本地实时缓存（数据在使用时加入缓存，过期从缓存移除）
 * @author	GM
 * @date		2023-10-7
 */
public abstract class AbstractCRUDRealtimeCacheServiceImpl<Dao extends ICRUDDao<T, PK>, T extends BaseEntity<PK>, PK extends Serializable> extends AbstractCRUDServiceImpl<Dao, T, PK>
        implements ICRUDCacheService<T, PK> {

    protected LoadingCache<PK, T> cache;
    
    /**
     * 
     * @param maxCacheSize			最大缓存数量
     * @param maxCacheDuration	最大缓存时间
     * @param timeUnit					最大缓存时间单位，默认:秒
     */
    public AbstractCRUDRealtimeCacheServiceImpl(long maxCacheSize, long maxCacheDuration, TimeUnit timeUnit){
    	if (maxCacheSize <= 0) {
    		maxCacheSize = 1;
    	}
    	if (timeUnit == null) {
    		timeUnit = TimeUnit.SECONDS;
    	}
    	cache =  CacheBuilder.newBuilder()
                            // 初始化容量
                            .initialCapacity(4)
                            // 缓存池大小，在缓存数量到达该大小时， Guava开始回收旧的数据
                            .maximumSize(maxCacheSize)
//                            // 设置时间对象没有被读/写访问则对象从内存中删除
//                            .expireAfterAccess(maxCacheDuration, timeUnit)
                            // 设置缓存在写入之后 设定时间 后失效
                            .expireAfterWrite(maxCacheDuration, timeUnit)
//                            // 数据被移除时的监听器, 缓存项被移除时会触发执行
//                            .removalListener((RemovalListener<String, String>) rn -> {
//                                System.out.println(String.format("数据key:%s value:%s 因为%s被移除了", rn.getKey(), rn.getValue(),
//                                        rn.getCause().name()));
//                            })
//                            // 开启Guava Cache的统计功能
//                            .recordStats()
//                            // 数据写入后被多久刷新一次
//                            .refreshAfterWrite(5, TimeUnit.SECONDS)
//                            // 数据并发级别
//                            .concurrencyLevel(16)
                            // 当缓存中没有数据时的数据加载器
                            .build(new CacheLoader<PK, T>() {
                                @Override
                                public T load(PK key) throws Exception {
                                    return getDao().get(key);
                                }
                            });
    }
    
    @Autowired
    public void setDao(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 是否区分大小写，默认要区分
     * @return
     */
    protected boolean isCaseSensitive() {
        return true;
    }
    
    @Override
	public boolean isShareCache() {
		return false;
	}

	protected String getCacheName() {
		String cacheName = "";
		cacheName = this.getRuntimesEntityClass().getName();
		if (StringUtils.isBlank(cacheName)) {
			cacheName = this.getClass().getName();
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
    protected void removeAfter(PK[] ids, Context context, int result) throws BusinessException {
        removeCache(ids);
        super.removeAfter(ids, context, result);
    }

    protected void removeCache(PK[] ids) {
        for (PK id : ids) {
            removeCache(id);
        }
    }
    
    @Override
    protected void saveAfter(T entity, Context context) throws BusinessException {
        this.removeCache(entity.getId());//让缓存失效
        super.saveAfter(entity, context);
    }

    @Override
    protected void updateAfter(T entity, Context context) throws BusinessException {
    	this.removeCache(entity.getId());//让缓存失效
        super.updateAfter(entity, context);
    }

    @Override
    public T getCache(PK id) {
        try {
			return cache.get(id);
		} catch (ExecutionException e) {
			throw new BusinessException(e);
		}
    }

    @Override
    public List<T> getCacheList() {
    	throw new BusinessException("Unsupported method");
    }
    
    public List<T> getExtCacheList() {
    	throw new BusinessException("Unsupported method");
    }
    @Override
    public T getExtCache(String extKey) {
    	throw new BusinessException("Unsupported method");
    }

    @Override
    public void loadCache() {
    	log.info("loadCache..." + getCacheName());
    }

    @Override
    public void putCache(PK key, T data) {
        cache.put(key, data);
    }

    @Override
    public void removeCache(PK key) {
    	if (key == null) {
    		return;
    	}
    	cache.invalidate(key);
    }

    @Override
    public void removeAllCache() {
    	cache.invalidateAll();
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
