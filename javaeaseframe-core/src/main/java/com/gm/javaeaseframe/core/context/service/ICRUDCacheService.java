package com.gm.javaeaseframe.core.context.service;

import java.io.Serializable;
import java.util.List;

import com.gm.javaeaseframe.core.context.model.BaseEntity;

public interface ICRUDCacheService<T extends BaseEntity<PK>, PK extends Serializable> extends ICRUDService<T, PK>, IService {

	/**
	 * 是否是共享缓存
	 * @return
	 */
	boolean isShareCache();
    /**
     * 加载缓存数据
     */
    void loadCache();

    /**
     * 根据数据主键获取缓存对象
     * 
     * @param id
     * @return
     */
    T getCache(PK id);

    /**
     * 新增单个缓存数据
     * 
     * @param key
     * @param data
     */
    void putCache(PK key, T data);

    /**
     * 获取加载器中所有缓存数据
     * 
     * @param
     * @return
     */
    List<T> getCacheList();
    /**
     * 获取加载器中扩展缓存数据
     * @return
     */
    List<T> getExtCacheList();

    /**
     * 删除加载器总指定主键的对象
     * 
     * @param dataKey
     */
    void removeCache(PK key);

    /**
     * 删除所有缓存数据
     */
    void removeAllCache();

    /**
     * 根据自定义key获取缓存对象，具体扩展方式由实现类业务定义
     * 
     * @param key
     * @return
     */
    T getExtCache(String key);

}
