package com.gm.javaeaseframe.core.constains;

/**
 * 系统常量类
 * 
 * @author
 * @createTime
 */
public interface SysConstains {

    /** 用户登录信息 */
    public static final String LOGIN_USER_INFO = "login-user-info";
    /** 请求ID */
    public static final String REQUEST_ID = "easyd-request-id";
    /** 用户登录来源 */
    public static final String LOGIN_USER_SOURCE = "login-user-source";

    /** 系统管理员ID */
    public static final long ADMIN_ID = 1L;

    /** 分隔符 */
    public static final String HEADER_KEY_SPLIT = ",";
    /** token键值 */
    public static final String TOKEN = "__normal_token__";
    /** 页面状态 */
    public static final String FORM_STATE = "__normal_form_state__";

    public static final String FORM_DISABLED_KEY = "_disabled_";
    /** 操作结果key */
    public static final String FORM_RESULT_KEY = "__normal_form_result__";
    /** 上传文件缓存key */
    public static final String FORM_UPLOAD_FILE_KEY = "__normal_form_upload_file__";

    /** 缓存多字段做为key时，分隔符 */
    public static final String EXT_KEY_SPLIT = "__";
    
    /** 用户登录名--cookie存储键 */
    public static final String COOKIE_PIN = "pin";
    /** 用户ID--cookie存储键 */
    public static final String COOKIE_PIN_ID = "pinId";
    /** 用户昵称--cookie存储键 */
    public static final String COOKIE_UNICK = "unick";
    /** 用户昵称(页面可读)--cookie存储键 */
    public static final String COOKIE_PAGE_UNICK = "__mtp_unick_";
    /** 用户是否是维护用户的标识(页面可读)--cookie存储键 */
    public static final String COOKIE_PAGE_MAINTAIN = "__mtp_maint_";
    /** 用户权限路径--cookie存储键 */
    public static final String COOKIE_MENU = "__mtm_";
    /** cookie相关信息【版本号，创建时间，过期时间】--cookie存储键 */
    public static final String COOKIE_STORE = "__mts_";
    /** 用户识别串--cookie存储键 */
    public static final String COOKIE_STORE_TOKEN = "__mt_token_";
    /** 用户关联信息--cookie存储键 */
    public static final String COOKIE_UNION = "__mt_union_";
    
    /** cookie的安全密钥--Property文件 */
    public static final String PROP_COOKIE_SECURITY_KEY = "cookie.key";
    /** cookie的有效域名--Property文件 */
    public static final String PROP_COOKIE_DOMAIN = "cookie.domain";
    /** 平台名称 */
    public static final String PROP_PLATFORM_MARK = "platform.mark";

    /** 创建表相关数据的传递key */
    public static final String KEY_TABLE_TEMPLETE = "key_table_templete";
    /** 导出数据的传递key */
    public static final String KEY_EXPORT_DATA = "key_export_data";
    

}
