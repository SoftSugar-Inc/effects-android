package com.softsugar.stmobile.params;

public enum STResultCode {
    ST_OK(0),                                  ///< \~chinese 正常运行 \~english Successful execution
    ST_E_INVALIDARG(-1),                       ///< \~chinese 无效参数 \~english Invalid argument
    ST_E_HANDLE(-2),                           ///< \~chinese 句柄错误 \~english Handle error
    ST_E_OUTOFMEMORY(-3),                      ///< \~chinese 内存不足 \~english Out of memory
    ST_E_FAIL(-4),                             ///< \~chinese 内部错误 \~english Internal error
    ST_E_DELNOTFOUND(-5),                      ///< \~chinese 定义缺失 \~english Definition missing
    ST_E_INVALID_PIXEL_FORMAT(-6),             ///< \~chinese 不支持的图像格式 \~english Unsupported image format
    ST_E_FILE_NOT_FOUND(-7),                   ///< \~chinese 文件不存在 \~english File not found
    ST_E_INVALID_FILE_FORMAT(-8),              ///< \~chinese 文件格式不正确导致加载失败 \~english Invalid file format causing loading failure
    ST_E_FILE_EXPIRE(-9),                      ///< \~chinese 文件过期 \~english File expired

    ST_E_INVALID_AUTH(-13),                    ///< \~chinese license不合法 \~english  Invalid license
    ST_E_INVALID_APPID(-14),                   ///< \~chinese 包名错误 \~english Incorrect package name
    ST_E_AUTH_EXPIRE(-15),                     ///< \~chinese license过期 \~english License expired
    ST_E_UUID_MISMATCH(-16),                   ///< \~chinese \~chinese UUID不匹配 \~english UUID mismatch
    ST_E_ONLINE_AUTH_CONNECT_FAIL(-17),        ///< \~chinese 在线验证连接失败 \~english UUID  Online verification connection failed
    ST_E_ONLINE_AUTH_TIMEOUT(-18),             ///< \~chinese 在线验证超时 \~english Online verification timeout
    ST_E_ONLINE_AUTH_INVALID(-19),             ///< \~chinese 在线签发服务器端返回错误 \~english Error returned by online signing server
    ST_E_LICENSE_IS_NOT_ACTIVABLE(-20),        ///< \~chinese license不可激活 \~english License cannot be activated
    ST_E_ACTIVE_FAIL(-21),                     ///< \~chinese license激活失败 \~english License activation failed
    ST_E_ACTIVE_CODE_INVALID(-22),             ///< \~chinese 激活码无效 \~english Invalid activation code

    ST_E_NO_CAPABILITY(-23),                   ///< \~chinese license文件没有提供这个能力 \~english The capability of this license is not provided
    ST_E_PLATFORM_NOTSUPPORTED(-24),           ///< \~chinese license不支持这个平台 \~english This platform is not supported by the license
    ST_E_SUBMODEL_NOT_EXIST(-26),              ///< \~chinese 子模型不存在 \~english Submodel does not exist
    ST_E_ONLINE_ACTIVATE_NO_NEED(-27),         ///< \~chinese 不需要在线激活 \~english Online activation not required
    ST_E_ONLINE_ACTIVATE_FAIL(-28),            ///< \~chinese 在线激活失败 \~english Online activation failed
    ST_E_ONLINE_ACTIVATE_CODE_INVALID(-29),    ///< \~chinese 在线激活码无效 \~english Invalid online activation code
    ST_E_ONLINE_ACTIVATE_CONNECT_FAIL(-30),    ///< \~chinese 在线激活连接失败 \~english Online activation connection failed

    ST_E_MODEL_NOT_IN_MEMORY(-31),             ///< \~chinese 模型不在内存中 \~english Model is not in memory
    ST_E_UNSUPPORTED_ZIP(-32),                 ///< \~chinese 当前sdk不支持的素材包 \~english Unsupported asset package for the current SDK
    ST_E_PACKAGE_EXIST_IN_MEMORY(-33),         ///< \~chinese 素材包已存在在内存中,不重复加载,或相同动画正在播放,不重复播放 \~english Asset package already exists in memory, no duplicate loading, or the same animation is already playing, no duplicate playback

    ST_E_NOT_CONNECT_TO_NETWORK(-34),          ///< \~chinese 设备没有联网 \~english Device not connected to the network
    ST_E_OTHER_LINK_ERRORS_IN_HTTPS(-35),      ///< \~chinese https中的其他链接错误 \~english  Other link errors in HTTPS
    ST_E_CERTIFICAT_NOT_BE_TRUSTED(-36),       ///< \~chinese windows系统有病毒或被黑导致证书不被信任 \~english The certificate is not trusted due to virus or hacking on the Windows system

    ST_E_LICENSE_LIMIT_EXCEEDED(-37),          ///< \~chinese license激活次数已用完 \~english License activation limit exceeded

    ST_E_NOFACE(-38),                     	 ///< \~chinese 没有检测到人脸 \~english No face detected

    T_E_API_UNSUPPORTED(-39),                 ///< \~chinese 该API暂不支持 \~english The API is not supported
    ST_E_API_DEPRECATED(-40),                  ///< \~chinese 该API已标记为废弃,应替换其他API或停止使用 \~english The API has been deprecated and should be replaced with other APIs or discontinued
    ST_E_ARG_UNSUPPORTED(-41),                 ///< \~chinese 该参数不支持 \~english The argument is not supported
    ST_E_PRECONDITION(-42),                    ///< \~chinese 前置条件不满足 \~english Preconditions are not met
    ST_E_SIGN_ACTIVATION_CODE_TOKEN_EXPIRE(-43),  ///< \~chinese 激活码token过期 \~english Activation code token expired
    ST_E_SIGN_ACTIVATION_CODE_EXPIRE(-44),     ///< \~chinese 激活码过期 \~english Activation code expired

    ST_E_INVALID_GL_CONTEXT(-100),              ///< \~chinese OpenGL Context错误,当前为空,或不一致 \~english OpenGL context error, currently empty or inconsistent
    ST_E_RENDER_DISABLED(-101),                 ///< \~chinese 创建句柄时候没有开启渲染 \~english Rendering not enabled when creating the handle


    ST_E_FORBID_PUSH_DATA_WHEN_STOPPING(-200),    ///< \~chinese 调用Stop接口过程中不再接收数据 \~english Data push is forbidden while stopping. This means that data should not be pushed when the Stop interface is called

    ST_E_OUT_OF_TIME_AREA(-300);                  /// \~chinese 输入的时间戳不在当前音素时间区域内 \~english The input timestamp is not within the current phoneme time area


    private final int resultCode;

    STResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }
}
