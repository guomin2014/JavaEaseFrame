package com.gm.javaeaseframe.core.context.service;

import com.gm.javaeaseframe.common.exception.BusinessException;

public interface ITaskExcuteService extends IService {

    public void excuteTask(ITask task) throws BusinessException;

    public void stopTask(ITask task) throws BusinessException;
}
