package com.gm.javaeaseframe.core.context.model;

public class BaseEntityStr extends BaseEntity<String> {

    private static final long serialVersionUID = -3504753140465778233L;

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void resetId(String id) {
        this.id = id;
    }

    public boolean newEntity() {
        return getId() == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof BaseEntityInt) {
            BaseEntityStr tmp = (BaseEntityStr) obj;
            if (this.getId() == null || tmp.getId() == null) {
                return false;
            }
            if (this.getId().equals(tmp.getId())) {
                return true;
            }
        }
        return false;
    }
}
