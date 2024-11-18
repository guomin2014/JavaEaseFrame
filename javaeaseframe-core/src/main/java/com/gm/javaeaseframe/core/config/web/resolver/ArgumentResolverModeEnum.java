package com.gm.javaeaseframe.core.config.web.resolver;

/**
 * 参数转换模式
 * @author	GM
 * @date	2023年9月18日
 */
public enum ArgumentResolverModeEnum {
    /** 全部 */
    ALL(0, "全部"),
    /** 黑名单 */
    BLACK(1, "黑名单"),
    /** 白名单 */
    WHITE(2, "白名单"),
    ;
    
    private int value;
    private String desc;
    

    ArgumentResolverModeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
        
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return desc;
    }

    public static ArgumentResolverModeEnum getByValue(int value) {
        for (ArgumentResolverModeEnum examStatus : ArgumentResolverModeEnum.values()) {
            if (examStatus.getValue() == value) {
                return examStatus;
            }
        }
        return null;
    }
}
