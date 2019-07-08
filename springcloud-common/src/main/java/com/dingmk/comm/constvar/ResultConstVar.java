package com.dingmk.comm.constvar;

/**
 * Created by lizhiming on 2017/3/16.
 */
public class ResultConstVar {
    /**
     * 未定义原因的错误
     */
    public static final int ERROR = 0; // 通用失败
    /**
     * 成功(1 - 999)
     */
    public static final int OK = 1; // 成功
    public static final int OK_WITH_VALUE = OK + 1; // 成功，且有数据返回
    public static final int OK_NO_VALUE = OK + 2; // 成功，但无数据返回
    public static final int OK_DO_NOTHING = OK + 3; // 成功，但没什么需要处理

    /**
     * 自定义异常(1000 - 1999)
     */
    public static final int EXCEPTION = 1000; // 一般异常
    public static final int EXCEPTION_FILE_NOTFOUND = EXCEPTION + 1; // 文件不存在异常
    public static final int EXCEPTION_IO = EXCEPTION + 2; // IO异常
    public static final int EXCEPTION_NETWORK = EXCEPTION + 3; // 网络异常
    public static final int EXCEPTION_DB = EXCEPTION + 4; // 数据库异常
    public static final int EXCEPTION_CONFIG = EXCEPTION + 5; // 配置错误
    public static final int EXCEPTION_UPLOAD_FILE = EXCEPTION + 6; // 上传文件异常
    public static final int EXCEPTION_UPLOAD_PIC = EXCEPTION + 7; // 必须上传图片

    /**
     * 通用错误码(2000 - 2999)
     */
    public static final int COMMON_ERROR = 2000;
    public static final int NOT_IMPLEMENT = COMMON_ERROR + 1; // 功能还没有实现
    public static final int SYSTEM_BUSY = COMMON_ERROR + 2; // 系统繁忙，请稍候再试
    public static final int NOT_SUPPORT = COMMON_ERROR + 3; // 合法，但由于限制原因不支持

    /**
     * 访问层错误(3000 - 3999)
     */
    public static final int ACCESS_ERROR = 3000;
    public static final int NO_PRIVILEGE = ACCESS_ERROR + 1; // 无权限
    public static final int BAD_REQUEST = ACCESS_ERROR + 2; // 非法请求
    public static final int BAD_RESPONSE = ACCESS_ERROR + 3; // 无法理解的响应消息
    public static final int BAD_SIGN = ACCESS_ERROR + 4; // 无效签名
    public static final int INVALID_SESSION = ACCESS_ERROR + 5; // 无效的Session
    public static final int INVALID_YZM = ACCESS_ERROR + 6; // 验证码错误
    public static final int NOT_LOGIN = ACCESS_ERROR + 7; // 未登录
    public static final int REQUEST_EXPIRED = ACCESS_ERROR + 8; // 请求已过期
    public static final int WEIXIN_NO_OPENID = ACCESS_ERROR + 9; // 无用户OpenID信息
    public static final int XYSDK_NO_USERID = ACCESS_ERROR + 10; // SDK端没有携带userid
    public static final int REQUEST_TOO_FAST = ACCESS_ERROR + 11; // 客户端请求太频繁，而拒绝服务
    public static final int SDKTOKEN_EXPIRED = ACCESS_ERROR + 12; // SDK请求token过期
    public static final int SDKTOKEN_INVALID = ACCESS_ERROR + 13; // SDK请求token无效
    public static final int PROTOCOL_NOT_SUPPORT = ACCESS_ERROR + 14;// 不支持的协议
    public static final int IP_WHITE_LIMIT = ACCESS_ERROR + 15; // 不在IP白名单内
    public static final int ACTIVE_LINK_LIMIT = ACCESS_ERROR + 16; // 并发链接数过多限制
    public static final int QPS_LIMIT = ACCESS_ERROR + 17; // QPS限制
    public static final int CHANNEL_UNAUTHORIZED = ACCESS_ERROR + 18;// 渠道未授权

    /**
     * 控制层错误(4000 - 4999)
     */
    public static final int CONTROL_ERROR = 4000; //
    public static final int INVALID_SIGN_ERROR = CONTROL_ERROR + 1; // 请求接口签名错误
    public static final int INVALID_FILE_NAME = CONTROL_ERROR + 2; // 文件格式不合法
    public static final int INVALID_FILE_SIZE = CONTROL_ERROR + 3; // 文件大小不合法
    public static final int INVALID_FILE_MEASURE = CONTROL_ERROR + 4; // 文件尺寸不合法
    public static final int INVALID_PASSWORD = CONTROL_ERROR + 5; // 用户密码错误

    /**
     * 数据层错误(5000 - 5999)
     */
    public static final int DATA_ERROR = 5000;
    public static final int KEY_DUPLICATE = DATA_ERROR + 1; // 键值重复
    public static final int NOT_FOUND = DATA_ERROR + 2; // 数据不存在
    public static final int DATA_DIRTY = DATA_ERROR + 3; // 脏数据
    public static final int DATA_TOO_MANY = DATA_ERROR + 4; // 数据太多

    /**
     * 逻辑层错误(6000 - 10999)
     */
    public static final int LOGIC_ERROR = 6000;
    public static final int INVALID_ARGUMENT = LOGIC_ERROR + 1; // 参数错误
    public static final int NULL_ARGUMENT = LOGIC_ERROR + 2; // 参数为空
    public static final int DATA_STATUS_ERROR = LOGIC_ERROR + 3; // 数据状态错误
    public static final int UNSUPPORT_CHANNEL = LOGIC_ERROR + 4; // 不支持的渠道
    public static final int THIRD_SERVER_ERROR = LOGIC_ERROR + 5; // 第三方服务器异常
    public static final int USER_LOGIC_ERROR = LOGIC_ERROR + 1000; // 用户领域逻辑错误起始值
    public static final int ITEM_LOGIC_ERROR = LOGIC_ERROR + 2000; // 商品领域逻辑错误起始值
    public static final int TRADE_LOGIC_ERROR = LOGIC_ERROR + 3000; // 资金领域逻辑错误起始值

    /**
     * V0协议常量
     */
    public static final int ERR_CODE_990000 = 990000;
    public static final int ERR_CODE_990001 = 990001; // 授权失败
    public static final int ERR_CODE_990002 = 990002;
    public static final int ERR_CODE_990003 = 990003; // 参数错误
    public static final int ERR_CODE_990004 = 990004;
    public static final int ERR_CODE_990006 = 990006;
    public static final int ERR_CODE_990007 = 990007;
    public static final int ERR_CODE_990008 = 990008; // 无效token
    public static final int ERR_CODE_999997 = 999997;

    /**
     * V1协议常量, 用于兼容，但建议使用V2的常量
     */
    public static final int V1_SUCCESS = 90001;
    public static final int V1_PARAM_ERROR = 90002;
    public static final int V1_OTHER_ERROR = 90003;
    public static final int V1_MORE_DATA = 90004;

    // 响应头
    @Deprecated
    public static final int V1_TOKEN_DENY = 90005;
    @Deprecated
    public static final int V1_PARAMETER_ERROR = 90006;
    @Deprecated
    public static final int V1_SERVER_ERROR = 90007;

    public static final int V1_PROTOCOL_TOKEN_DENY = 1;
    public static final int V1_PROTOCOL_PARAMETER_ERROR = 2;
    public static final int V1_PROTOCOL_SERVER_ERROR = 3;

    public static final String V1_SUCCESS_STR = "成功";
    public static final String V1_PARAM_ERROR_STR = "参数错误";
    public static final String V1_OTHER_ERROR_STR = "其他错误";

    /**
     * 静态方法，以判断结果码是否代表成功
     *
     * @param result
     *            结果码
     * @return
     */
    public static boolean isSuccess(int result) {
        if (result >= 1 && result <= 999) {
            return true;
        }
        return false;
    }
}
