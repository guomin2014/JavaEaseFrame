package com.gm.javaeaseframe.common.code;

/**
 * 平台编码
 * 
 * @author GM
 * @date 2023年9月19日
 */
public enum PlatformCodeEnum {

	/** 鉴权系统 */
	AUTH(10,"鉴权系统"),
	/** 代理商系统 */
	AGENT(11,"代理商系统"),
	/** 客户资源管理系统 */
	CMDB(12,"客户资源管理系统"),
	/** 财务系统 */
	FINANCIAL(13,"财务系统"),
	/** API中台 */
	OPENAPI(14,"API中台"),
	/** 中间件系统 */
	MIDDLEWARE(15,"中间件系统"),
	/** 订单系统 */
	ORDER(16,"订单系统"),
	/** 支付系统 */
	PAYMENT(17,"支付系统"),
	/** 多云管理系统 */
	MULTI_CLOUD(20, "多云管理系统"),
	/** 多云IaaS系统 */
	MULTI_IAAS(21, "多云IaaS系统"),
	;
	
	private int code;
    private String desc;
    
    PlatformCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
        
    }
    
    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return desc;
    }

    public static PlatformCodeEnum getByValue(int code) {
        for (PlatformCodeEnum custBalanceAlert : PlatformCodeEnum.values()) {
            if (custBalanceAlert.getCode() == code) {
                return custBalanceAlert;
            }
        }
        return null;
    }
}
