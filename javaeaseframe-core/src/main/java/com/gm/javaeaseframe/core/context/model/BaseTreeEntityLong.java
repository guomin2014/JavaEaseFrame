package com.gm.javaeaseframe.core.context.model;

public class BaseTreeEntityLong extends BaseTreeEntity<Long> {

	private static final long serialVersionUID = 7303203500074913251L;

	@Override
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public void resetId(Long id) {
        this.id = id;
    }
    
    @Override
    public Long getParentId()
    {
        return this.parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    @Override
    public void resetParentId(Long parentId) {
        this.parentId = parentId;
    }

	@Override
    public boolean newEntity() {
        return getId() == null || getId().longValue() == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass().isInstance(obj)) {
            BaseTreeEntityLong tmp = (BaseTreeEntityLong) obj;
            if (this.newEntity() || tmp.newEntity()) {
                return false;
            }
            if (this.getId().longValue() == tmp.getId().longValue()) {
                return true;
            }
        }
        return false;
    }
}
