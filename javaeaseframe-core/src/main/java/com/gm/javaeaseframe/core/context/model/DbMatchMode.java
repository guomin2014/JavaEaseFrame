package com.gm.javaeaseframe.core.context.model;

/**
 * <B>Description</B> 查询匹配模式 <br />
 * <B>Copyright</B> Copyright (c) 2016 www.diligrp.com All rights reserved.
 * <br />
 * 本软件源代码版权归哎媲媲网络,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 哎媲媲网络
 */
public enum DbMatchMode {
    /** 全匹配：xxxx */
    WHOLE_WORD(1),
    /** 右匹配：xxxxx% */
    LIKE_RIGHT(2),
    /** 左匹配：%xxxxx */
    LIKE_LEFT(3),
    /** 两侧匹配：%xxxxx% */
    LIKE_BOTH(4);

    private int value;

    private DbMatchMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
