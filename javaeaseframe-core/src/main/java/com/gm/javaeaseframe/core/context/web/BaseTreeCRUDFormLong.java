package com.gm.javaeaseframe.core.context.web;

import com.gm.javaeaseframe.core.context.model.BaseTreeEntityLong;

public abstract class BaseTreeCRUDFormLong<T extends BaseTreeEntityLong> extends BaseCRUDForm<T, Long> {

    private Long[] id;

    @Override
    public Long[] getId() {
        return id;
    }

    @Override
    public void setId(Long[] id) {
        this.id = id;
    }

}
