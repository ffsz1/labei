/**
 * 
 */
package com.tongdaxing.xchat_framework.coremanager;

/**
 * @author daixiang
 */
public class CoreError {
	
	// error code 定义
	public static final int NETWORK_ERROR = 1000;
	public static final int DB_ERROR = 1001;
	public static final int TIMEOUT_ERROR = 1002;
	public static final int SERVER_ERROR = 1003;
	public static final int UNKNOWN_ERROR = 1004;
	public static final int TRAFFIC_FORBIDDEN_ERROR = 1005;
	public static final int ILLEGAL_ACCESS_ERROR = 1006;//非法访问
	public static final int ILLEGAL_SMS_CODE_ERROR = 1007;//非法短信验证码
	public static final int DUPLICATE_ERROR = 1008; // 存在重复的记录
	public static final int NEWTORK_ERROR_ONLY_WIFI = 1009; // 仅wifi下可以访问网络
    public static final int SERVER_ERROR_USER_FORBIDDEN = 1010; // 黑名单用户
    public static final int SERVER_ERROR_ILLEGAL_CONTENT = 1011; // 内容不合法
    public static final int DATA_NOT_FOUND = 1404; // 数据找不到

	//JX 错误吗定义
	public static final int ACCESS_DENIED = 100 ;//拒绝访问

	public static final int INVALID_REQUEST = 101;//请求不合法

	public static final int INVALID_REQUEST_SCHEME = 102;//错误的请求协议

	public static final int INVALID_REQUEST_METHOD = 103;//错误的请求方法

	public static final int INVALID_CLIENT_ID = 104; //client id不存在或已删除

	public static final int CLIENT_ID_IS_BLOCKED = 105;//client id已被禁用

	public static final int UNAUTHORIZED_CLIENT_ID = 106; //client id未授权

	public static final int USERNAME_PASSWORD_MISMATCH = 107; //用户名密码不匹配

	public static final int INVALID_REQUEST_SCOPE = 108; //访问的scope不合法，开发者不用太关注，一般不会出现该错误

	public static final int INVALID_USER = 109; //用户不存在或已删除

	public static final int USER_HAS_BLOCKED = 110; //用户已被屏蔽

	public static final int INVALID_TOKEN = 111;//token不存在或已被用户删除，或者用户修改了密码

	public static final int ACCESS_TOKEN_IS_MISSING = 112; //未找到access_token

	public static final int ACCESS_TOKEN_HAS_EXPIRED = 113;//access_token已过期

	public static final int INVALID_REQUEST_URI = 114; //请求地址未注册

	public static final int INVALID_CREDENTIAL_1 = 115; //用户未授权访问此数据

	public static final int INVALID_CREDENTIAL_2 = 116; //client id未申请此权限

	public static final int NOT_TRIAL_USER = 117; //未注册的测试用户

	public static final int REQUIRED_PARAMETER_IS_MISSING = 118; //缺少参数

	public static final int INVALID_GRANT = 119;//invalid grant type"

	public static final int UNSUPPORTED_GRANT_TYPE = 120;//错误的grant_type

	public static final int UNSUPPORTED_RESPONSE_TYPE = 121; //错误的response_type

	public static final int CLIENT_SECRET_MISMATCH = 122; //client_secret不匹配

	public static final int REDIRECT_URI_MISMATCH = 123; //redirect_uri不匹配

	public static final int INVALID_AUTHORIZATION_CODE = 124;//authorization_code不存在或已过期

	public static final int ACCESS_TOKEN_HAS_EXPIRED_SINCE_PASSWORD_CHANGED = 125;//因用户修改密码而导致access_token过期

	public static final int ACCESS_TOKEN_HAS_NOT_EXPIRED = 126; //access_token未过期;

	public static final int UNSUPPORTED_TICKET_ISSUE_TYPE = 127;//unsupported ticket issue type"

	public static final int INVALID_TICKET = 128;//ticket不存在或已过期

	public static final int TICKET_IS_MISSING = 129; //未找到ticket

	public static final int TICKET_HAS_EXPIRED = 130; //ticket过期

	public static final int TICKET_HAS_NOT_EXPIRED = 131; //ticket未过期

	public static final int TICKET_HAS_EXPIRED_SINCE_PASSWORD_CHANGED = 132; //因为用户修改密码而ticket过期

	public static final int INVALID_SCOPE = 133;

	public static final int RATE_LIMIT_EXCEEDED1 = 134;//用户访问速度限制

	public static final int RATE_LIMIT_EXCEEDED2 = 135;//IP访问速度限制

	public static final int INVALID_IDENTIFYING_CODE = 150; //不可用的验证码

	public static final int INVALID_USERNAME = 151; //用户名不合法

	public static final int USER_HAS_SIGNED_UP = 152;//用户名已被注册

	public static final int INVALID_RESET_CODE = 153;//重置码无效

	public static final int INVALID_NICK = 161;  //昵称不合法

	public static final int INVALID_THIRD_TOKEN = 162; //第三方token不合法

	public static final int THIRD_ACCOUNT_HAVE_BIND = 163; //第三方账户已经绑定或之前已使用该账户登陆过系统

	public static final int UNBIND_OPENID_NOT_MATCH = 164; //账户解绑失败

	public static final int UNBIND_MAIN_ACCOUNT = 165;//解绑主账户错误

	public static final int SUCCESS = 200; //成功

	public static final int INVALID_SERVICE = 199;//服务不可用

	public static final int UNKNOWN = 999;//未知错误


	// 登录错误码
	public static final int AUTH_USER_NOT_EXIST = 2000;
	public static final int AUTH_PASSWORD_ERROR = 2001;
	public static final int AUTH_USER_BANNED = 2002;
	public static final int NO_LOGIN_ERROR = 2003;//没有登录
	public static final int NEED_RELOGIN_ERROR = 2004;//需要重新登录
	public static final int LOGIN_EXPIRED_ERROR = 2005;//登录信息过期
	public static final int REGISTER_ERROR = 2006;//注册失败
	public static final int THIRD_PARTY_LOGIN_ERROR = 2007;//第三方登录失败
	public static final int THIRD_PARTY_BIND_TOKEN_INVALID = 2008;//第三方绑定失败, token 无效
	public static final int THIRD_PARTY_BIND_ACCOUNT_HAVE_BIND = 2009;//第三方绑定失败, 第三方账户已经绑定或之前已使用该账户登陆过系统
	public static final int THIRD_PARTY_UNBIND_ERROR = 2010;//第三方解绑失败

    // 文件错误码
    public static final int FILE_ERROR = 3000;//文件通用错误
    public static final int FILE_NO_ENOUGH_SPACE = 3001;//磁盘剩余空间不够

	public int code;
	public String message;
	public Throwable throwable;

	public static CoreError empty() {
		return new CoreError(-1, null);
	}

	public CoreError(int code, String message) {
		this.message = message;
		this.code = code;
	}

	public CoreError(int code) {
		this.code = code;
	}

	public CoreError(int code, String message, Throwable throwable) {
		this.code = code;
		this.message = message;
		this.throwable = throwable;
	}

	public static CoreError serverError() {
		CoreError error = empty();
		error.code = SERVER_ERROR;
		return error;
	}

	public static CoreError timeoutError() {
		CoreError error = empty();
		error.code = TIMEOUT_ERROR;
		return error;
	}

	public static CoreError networkError() {
		CoreError error = empty();
		error.code = NETWORK_ERROR;
		return error;
	}

	public static CoreError dbError() {
		CoreError error = empty();
		error.code = DB_ERROR;
		return error;
	}

	public static CoreError unkonwnError() {
		CoreError error = empty();
		error.code = UNKNOWN_ERROR;
		return error;
	}

	public static CoreError noLoginError() {
		CoreError error = empty();
		error.code = NO_LOGIN_ERROR;
		return error;
	}

	public static CoreError needReloginError() {
		CoreError error = empty();
		error.code = NEED_RELOGIN_ERROR;
		return error;
	}

	public static CoreError duplicateError() {
		CoreError error = empty();
		error.code = DUPLICATE_ERROR;
		return error;
	}

	public static CoreError onlyWifiAccessError() {
		CoreError error = empty();
		error.code = NEWTORK_ERROR_ONLY_WIFI;
		return error;
	}

    public static CoreError userForbiddenError() {
        CoreError error = empty();
        error.code = SERVER_ERROR_USER_FORBIDDEN;
        return error;
    }

    public static CoreError illegalContentError() {
        CoreError error = empty();
        error.code = SERVER_ERROR_ILLEGAL_CONTENT;
        return error;
    }

	public static CoreError thirdpartyLoginError() {
		CoreError error = empty();
		error.code = THIRD_PARTY_LOGIN_ERROR;
		return error;
	}

	public static CoreError thirdpartyBindError() {
		CoreError error = empty();
		error.code = THIRD_PARTY_BIND_TOKEN_INVALID;
		return error;
	}

	public static CoreError thirdpartyUnbindError() {
		CoreError error = empty();
		error.code = THIRD_PARTY_UNBIND_ERROR;
		return error;
	}

	public static CoreError dataNotFoundError() {
		CoreError error = empty();
		error.code = DATA_NOT_FOUND;
		return error;
	}

	public boolean isDuplicateError() {
		return code == DUPLICATE_ERROR;
	}
}
