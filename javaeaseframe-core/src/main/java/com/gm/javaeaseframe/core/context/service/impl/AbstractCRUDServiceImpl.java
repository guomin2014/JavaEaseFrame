package com.gm.javaeaseframe.core.context.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.exception.ExceptionCodeEnum;
import com.gm.javaeaseframe.common.util.ExcelAgent;
import com.gm.javaeaseframe.common.util.ExcelExportData;
import com.gm.javaeaseframe.core.constains.SysConstains;
import com.gm.javaeaseframe.core.context.dao.ICRUDDao;
import com.gm.javaeaseframe.core.context.model.BaseEntity;
import com.gm.javaeaseframe.core.context.model.Context;
import com.gm.javaeaseframe.core.context.model.PageInfo;
import com.gm.javaeaseframe.core.context.model.Result;
import com.gm.javaeaseframe.core.context.service.ICRUDService;

public abstract class AbstractCRUDServiceImpl<Dao extends ICRUDDao<T, PK>, T extends BaseEntity<PK>, PK extends Serializable> extends AbstractService implements ICRUDService<T, PK> {

    protected Dao dao;
    
    @Autowired
    public void setDao(Dao dao) {
        this.dao = dao;
    }
    
    /**
     * 设置dto
     * @return
     */
    public Dao getDao() {
        return dao;
    }

    public AbstractCRUDServiceImpl(){}
    
    @Override
    public T save(T entity, Context context) throws BusinessException {
        saveBefore(entity, context);
        int iRet;
        iRet = dao.insert(entity);
        if (iRet == 0) {
            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
        }
        saveAfter(entity, context);
        return entity;
    }
    
    protected void saveBefore(T entity, Context context) throws BusinessException {
        validData(entity, context);
    }

    protected void saveAfter(T entity, Context context) throws BusinessException {

    }

    protected void validData(T entity, Context context) throws BusinessException {

    }
    
    @Override
    public int save(List<T> list, Context context) throws BusinessException
    {
        saveBefore(list, context);
        int iRet = dao.insertBatch(list);
        if (iRet == 0) {
            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
        }
        saveAfter(list, context);
        return iRet;
    }
    
    protected void saveBefore(List<T> list, Context context) throws BusinessException {
        if (list != null) {
            for (T t : list) {
                this.saveBefore(t, context);
            }
        }
    }

    protected void saveAfter(List<T> list, Context context) throws BusinessException {
        if (list != null) {
            for (T t : list) {
                this.saveAfter(t, context);
            }
        }
    }

    /**
     * 检查是新增还是修改，对于联合主健，需要重载
     * @param entity
     * @param context
     * @return
     */
    protected boolean isNew(T entity, Context context) {
        boolean isNew = true;
        if (entity.newEntity()) {
            isNew = true;
        } else {
            isNew = false;
        }
        return isNew;
    }

    @Override
    public T update(T entity, Context context) throws BusinessException {
        updateBefore(entity, context);
        int iRet = dao.update(entity);
        if (iRet == 0) {
            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
        }
        updateAfter(entity, context);

        return entity;
    }
    
    @Override
    public int update(List<T> list, Context context) throws BusinessException {
        updateBefore(list, context);
        int iRet = dao.updateBatch(list);
//        if (iRet == 0) {
//            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
//        }
        updateAfter(list, context);
        return iRet;
    }
    
    @Override
	public int update(T entity, T condition, Context context) throws BusinessException {
    	updateBefore(entity, condition, context);
    	int iRet = dao.update(entity, condition);
//        if (iRet == 0) {
//            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
//        }
        updateAfter(entity, condition, context);
		return iRet;
	}

	@Override
	public int update(T entity, Map condition, Context context) throws BusinessException {
		updateBefore(entity, condition, context);
    	int iRet = dao.update(entity, condition);
//        if (iRet == 0) {
//            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
//        }
        updateAfter(entity, condition, context);
		return iRet;
	}

	@Override
	public int update(Map data, Map condition, Context context) throws BusinessException {
		updateBefore(data, condition, context);
    	int iRet = dao.update(data, condition);
//        if (iRet == 0) {
//            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
//        }
        updateAfter(data, condition, context);
		return iRet;
	}

	protected void updateBefore(T entity, Context context) throws BusinessException {
        validData(entity, context);
    }

    protected void updateAfter(T entity, Context context) throws BusinessException {

    }
    protected void updateBefore(T entity, T condition, Context context) throws BusinessException {
    	validData(entity, context);
    }
    
    protected void updateAfter(T entity, T condition, Context context) throws BusinessException {
    	
    }
    protected void updateBefore(T entity, Map condition, Context context) throws BusinessException {
    	validData(entity, context);
    }
    
    protected void updateAfter(T entity, Map condition, Context context) throws BusinessException {
    	
    }
    protected void updateBefore(Map entity, Map condition, Context context) throws BusinessException {
    }
    
    protected void updateAfter(Map entity, Map condition, Context context) throws BusinessException {
    	
    }
    
    protected void updateBefore(List<T> list, Context context) throws BusinessException {
        if (list != null) {
            for (T t : list) {
                this.updateBefore(t, context);
            }
        }
    }

    protected void updateAfter(List<T> list, Context context) throws BusinessException {
        if (list != null) {
            for (T t : list) {
                this.updateAfter(t, context);
            }
        }
    }

    @Override
    public T get(PK key, Context context) throws BusinessException {
        return dao.get(key);
    }

    @Override
    public T get(PK key, boolean exception) throws BusinessException {
        T t = dao.get(key);
        if (t == null && exception) {
            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
        }
        return t;
    }

    @Override
    public T get(PK key, Context context, boolean exception) throws BusinessException {
        T entity = dao.get(key);
        if (entity == null && exception) {
            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
        }
        return entity;
    }

    @Override
    public List<T> get(PK[] ids, Context context) throws BusinessException {
        return dao.get(ids);
    }

    @Override
    public List<T> get(PK[] ids, boolean exception) throws BusinessException {
        List<T> list = dao.get(ids);
        if (exception && list.size() != ids.length) {
            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
        }
        return list;
    }

    @Override
    public List<T> get(PK[] ids, Context context, boolean exception) throws BusinessException {
        List<T> list = dao.get(ids);
        if (exception && list.size() != ids.length) {
            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
        }
        return list;
    }

    @Override
	public int remove(PK id, Context context) throws BusinessException {
    	removeBefore(id, context);
        int iRet = dao.delete(id);
        removeAfter(id, context, iRet);
        return iRet;
	}

	@Override
	public int remove(T condition, Context context) throws BusinessException {
		removeBefore(condition, context);
        int iRet = dao.delete(condition);
        removeAfter(condition, context, iRet);
        return iRet;
	}

	@Override
	public int remove(Map condition, Context context) throws BusinessException {
		removeBefore(condition, context);
        int iRet = dao.delete(condition);
        removeAfter(condition, context, iRet);
        return iRet;
	}

	@Override
    public int remove(PK[] ids, Context context) throws BusinessException {
        removeBefore(ids, context);
        int iRet = dao.delete(ids);
        removeAfter(ids, context, iRet);
        return iRet;
    }

	protected void removeBefore(PK id, Context context) throws BusinessException {
		
	}
	
	protected void removeAfter(PK id, Context context, int result) throws BusinessException {
		
	}
    protected void removeBefore(PK[] ids, Context context) throws BusinessException {

    }

    protected void removeAfter(PK[] ids, Context context, int result) throws BusinessException {

    }
    protected void removeBefore(T condition, Context context) throws BusinessException {
    	
    }
    
    protected void removeAfter(T condition, Context context, int result) throws BusinessException {
    	
    }
    protected void removeBefore(Map condition, Context context) throws BusinessException {
    	
    }
    
    protected void removeAfter(Map condition, Context context, int result) throws BusinessException {
    	
    }

    @Override
    public int count(T params, Context context) throws BusinessException {
        return dao.getCount(params);
    }

    public List<T> find(T params, Context context) throws BusinessException {
        T newParams = findBefore(params, context);
        List<T> list = dao.getList(newParams);
        findAfter(params, context, list);
        return list;
    }

    /**
     * 查询前处理
     * @param params
     * @param context
     * @return
     * @throws BusinessException
     */
    protected T findBefore(T params, Context context) throws BusinessException {
        return params;
    }

    /**
     * 查询后处理
     * @param params
     * @param context
     * @param list
     * @throws BusinessException
     */
    protected void findAfter(T params, Context context, List<T> list) throws BusinessException {

    }

    public Result<T> find(T params, PageInfo pageInfo, Context context) throws BusinessException {
        T newParams = findBefore(params, pageInfo, context);
        Result<T> result = dao.getList(newParams, pageInfo);
        findAfter(params, pageInfo, context, result.getList());
        return result;
    }

    /**
     * 查询前处理
     * @param params
     * @param pageInfo
     * @param context
     * @return
     * @throws BusinessException
     */
    protected T findBefore(T params, PageInfo pageInfo, Context context) throws BusinessException {
        return params;
    }

    /**
     * 查询后处理
     * @param params
     * @param pageInfo
     * @param context
     * @param list
     * @throws BusinessException
     */
    protected void findAfter(T params, PageInfo pageInfo, Context context, List<T> list) throws BusinessException {

    }

    @Override
	public List<T> findStat(T params, Context context) throws BusinessException {
		return this.dao.getStatList(params, 0, 0);
	}

	@Override
	public Result<T> findStat(T params, PageInfo pageInfo, Context context) throws BusinessException {
		return this.dao.getStatList(params, pageInfo);
	}

	@Override
    public boolean existFieldVal(PK id, String fieldName, Object fieldVal) throws BusinessException {
        Map<String, Object> condition = new HashMap<String, Object>();
        return existFieldVal(id, fieldName, fieldVal, condition);
    }

    public boolean existFieldVal(PK id, String fieldName, Object fieldVal, Map<String, Object> condition) throws BusinessException {
        condition.put(fieldName, fieldVal);
        List<T> list = this.dao.getList(condition);
        if (list.size() > 1) {
            return true;
        } else if (list.size() == 1) {
            T entity = list.get(0);
            if (id == null) {
                return true;
            } else {
                return !id.equals(entity.getId());
            }
        } else {
            return false;
        }
    }

    @Override
    public byte[] export(T params, Context context) throws BusinessException {
        this.exportBefore(params, context);
        List<T> list = this.find(params, context);
        if (list == null || list.isEmpty()) {
            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
        }
        this.exportAfter(params, context, list);
        ExcelExportData data = (ExcelExportData) context.getAttribute(SysConstains.KEY_EXPORT_DATA);
        if (data == null) {
            throw new BusinessException(ExceptionCodeEnum.PARAM_IS_EMPTY);
        }
        if (data.getDataList() == null || data.getDataList().isEmpty()) {
            throw new BusinessException(ExceptionCodeEnum.STATUS_REQUEST_DATA_NOT_EXISTS);
        }
        try {
            return ExcelAgent.export2ByteArray(data);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 导出数据前处理
     * @param params
     * @param context
     * @return
     * @throws BusinessException
     */
    protected T exportBefore(T params, Context context) throws BusinessException {
        return params;
    }

    /**
     * 导出数据后处理
     * @param params
     * @param context
     * @param list
     * @throws BusinessException
     */
    protected void exportAfter(T params, Context context, List<T> list) throws BusinessException {

    }
}
