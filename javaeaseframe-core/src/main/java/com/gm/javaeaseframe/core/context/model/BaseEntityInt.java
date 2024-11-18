package com.gm.javaeaseframe.core.context.model;

public class BaseEntityInt extends BaseEntity<Integer> {

    private static final long serialVersionUID = -3259437254447779722L;

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    @Override
    public void resetId(Integer id) {
        this.id = id;
    }

    public boolean newEntity() {
        return getId() == null || getId().intValue() == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof BaseEntityInt) {
            BaseEntityInt tmp = (BaseEntityInt) obj;
            if (this.newEntity() || tmp.newEntity()) {
                return false;
            }
            if (this.getId().intValue() == tmp.getId().intValue()) {
                return true;
            }
        }
        return false;
    }
}
