package com.gm.javaeaseframe.core.context.model;

public class BaseEntityLong extends BaseEntity<Long> {

    private static final long serialVersionUID = 3645807639982979084L;

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
    public boolean newEntity() {
        return getId() == null || getId().longValue() == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass().isInstance(obj)) {
            BaseEntityLong tmp = (BaseEntityLong) obj;
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
