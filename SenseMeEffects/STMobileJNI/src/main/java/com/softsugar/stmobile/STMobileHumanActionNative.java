package com.softsugar.stmobile;

import android.content.res.AssetManager;

import com.softsugar.stmobile.model.STBodyAvatar;
import com.softsugar.stmobile.model.STFaceMeshList;
import com.softsugar.stmobile.model.STHumanAction;
import com.softsugar.stmobile.model.STImage;
import com.softsugar.stmobile.model.STMobileFaceInfo;

/**
 * \~Chinese 人脸关键点、人脸行为、手势、肢体等识别 \~English Human action detect
 */
public class STMobileHumanActionNative {

    public final static long ST_MOBILE_FACE_DETECT = 0x00000001;          ///< \~Chinese 人脸检测 \~English face detect

    public final static long ST_MOBILE_EYE_BLINK = 0x00000002;            ///< \~Chinese 眨眼 \~English eye blink
    public final static long ST_MOBILE_MOUTH_AH = 0x00000004;             ///< \~Chinese 嘴巴大张 \~English mouth ah
    public final static long ST_MOBILE_HEAD_YAW = 0x00000008;             ///< \~Chinese 摇头 \~English head yaw
    public final static long ST_MOBILE_HEAD_PITCH = 0x00000010;           ///< \~Chinese 点头 \~English head pitch
    public final static long ST_MOBILE_BROW_JUMP = 0x00000020;            ///< \~Chinese 眉毛挑动 \~English brow jump
    public final static long ST_MOBILE_FACE_LIPS_UPWARD = 0x00000040;     ///< \~Chinese 嘴角上扬 \~English lips upward
    public final static long ST_MOBILE_FACE_LIPS_POUTED = 0x00000080;     ///< \~Chinese 嘟嘴 \~English lips pouted

    //手势动作
    public final static long ST_MOBILE_HAND_DETECT = 0x00000100;    ///<  检测手
    public final static long ST_MOBILE_DETECT_HAND_GESTURE = 0x00000200;  ///< \~chinese 检测手势类别 \~english Detect hand gesture

    public final static long ST_MOBILE_SEG_BACKGROUND = 0x00010000;       ///< \~Chinese 检测前景背景分割 65536 \~English segment background

    public final static long ST_MOBILE_FACE_240_DETECT = 0x01000000;           ///< \~Chinese 检测人脸240关键点 (deprecated) \~English face 240 detect
    public final static long ST_MOBILE_DETECT_EXTRA_FACE_POINTS = 0x01000000;  ///< \~Chinese 检测人脸240关键点 \~English face extra point detect
    public final static long ST_MOBILE_DETECT_EYEBALL_CENTER = 0x02000000;     ///< \~Chinese 检测眼球中心点 \~English eyeball ceter
    public final static long ST_MOBILE_DETECT_EYEBALL_CONTOUR = 0x04000000;    ///< \~Chinese 检测眼球轮廓点 \~English eyeball contour
    public final static long ST_MOBILE_BODY_KEYPOINTS = 0x08000000;            ///< \~Chinese 检测肢体关键点 \~English body key points
    public final static long ST_MOBILE_BODY_CONTOUR = 0x10000000;              ///< \~Chinese 检测肢体轮廓点 \~English body contour

    public final static long ST_MOBILE_SEG_HAIR = 0x20000000;              ///< \~Chinese 检测头发分割 \~English seg hair
    public final static int ST_MOBILE_DETECT_TONGUE = 0x40000000;          ///< \~Chinese 检测舌头关键点 \~English tongue
    public final static long ST_MOBILE_SEG_HEAD = 0x0100000000L;           /// \~Chinese 检测头部分割 \~English seg head
    public final static long ST_MOBILE_SEG_SKIN = 0x0200000000L;           /// \~Chinese 检测皮肤分割 \~English seg skin
    public final static long ST_MOBILE_SEG_FACE_OCCLUSION = 0x0400000000L; /// \~Chinese 检测面部遮挡分割 \~English seg fce occlusion
    public final static long ST_MOBILE_DETECT_FOOT = 0x0800000000L;        /// \~Chinese 脚部关键点检测 \~English foot
    public final static long ST_MOBILE_BODY_ACTION5 = 0x1000000000L;       /// \~Chinese 动感超人 暂时不支持 \~English body actions
    public final static long ST_MOBILE_SEG_TROUSER_LEG = 0x1000000000L;    /// \~Chinese 检测裤腿分割 \~English seg tourser leg
    public final static long ST_MOBILE_DETECT_FINGER = 0x4000000000L;      /// \~Chinese 检测手指关键点 \~English finger keypoints
    public final static long ST_MOBILE_DETECT_HAND_SKELETON_KEYPOINTS = 0x20000000000L;     ///< \~Chinese 检测单手关键点, 最多支持两只手的关键点检测 \~English hand skeleton key points
    public final static long ST_MOBILE_DETECT_HAND_SKELETON_KEYPOINTS_3D = 0x40000000000L;  ///< \~Chinese 检测单手3d关键点 \~English hand skeleton key points 3d

    public final static long ST_MOBILE_SEG_MULTI = 0x80000000000L;               ///< \~Chinese 检测多类分割 \~English seg multi
    public final static long ST_MOBILE_DETECT_GAZE = 0x100000000000L;            ///< \~Chinese 检测视线方向 \~English gaze
    public final static long ST_MOBILE_DETECT_DYNAMIC_GESTURE = 0x200000000000L; ///< \~Chinese 检测动态手势 \~English dynamic gesture
    public final static long ST_MOBILE_DETECT_AVATAR_HELPINFO = 0x800000000000L; ///< \~Chinese 检测avatar辅助信息 \~English avatar help info
    public final static long ST_MOBILE_DETECT_FACE_S_COLOR = 0x1000000000000L;   ///< \~Chinese 依赖106关键点检测 \~English face s color
    public final static long ST_MOBILE_DETECT_HAIR_COLOR = 0x100000000000000L;   ///< \~Chinese avatar发色检测，依赖106关键点和头发分割，目前只支持单人发色 \~English hair color
    public final static long ST_MOBILE_BODY_KEYPOINTS_3D = 0x2000000000000L;     ///< \~Chinese 检测肢体3d关键点 \~English body key points 3d
    public final static long ST_MOBILE_DETECT_EAR = 0x4000000000000L;            ///< \~Chinese 检测耳朵关键点 \~English ear
    public final static long ST_MOBILE_DETECT_FOREHEAD = 0x8000000000000L;       ///< \~Chinese 检测额头关键点 \~English forehead
    public final static long ST_MOBILE_DETECT_FACE_MESH = 0x40000000000000L;     ///< \~Chinese 检测3dmesh关键点 \~English ace mesh
    public final static long ST_MOBILE_DETECT_MOUTH_PARSE = 0x80000000000000L;   ///< \~Chinese 检测嘴部遮挡 \~English mouth parse
    public final static long ST_MOBILE_DETECT_HEAD = 0x200000000000000L;         ///< \~Chinese 检测head关键点 \~English head
    public final static long ST_MOBILE_DETECT_HEAD_MESH = 0x400000000000000L;    ///< \~Chinese 检测head mesh关键点 \~English head mesh
    public final static long ST_MOBILE_DETECT_UPBODY_AVATAR = 0x800000000000000L;///< \~Chinese 检测半身avatar \~English up body avatar
    public final static long ST_MOBILE_SEG_SKY = 0x1000000000000000L;            ///< \~Chinese 检测天空分割 \~English seg sky
    public final static long ST_MOBILE_DEPTH_ESTIMATION = 0X2000000000000000L;   ///< \~Chinese 检测深度估计 \~English depth estimation
    public final static long ST_MOBILE_NAIL_DETECT = 0x4000000000000000L;        ///< \~Chinese 指甲关键点检测 \~English nail
    public final static long ST_MOBILE_SEG_CLOTH = 0x000080000;                  ///< \~Chinese 衣物检测 \~English seg cloth
    public final static long ST_MOBILE_SEG_GREEN = 0x80000000;                   ///< \~Chinese 绿幕分割 \~English seg green
    public final static long ST_MOBILE_WRIST_DETECT = 0x8000000000000000L;       ///< \~Chinese 手腕关键点检测 \~English wrist

    public final static int ST_MOBILE_FACE_DETECT_FULL = 0x000000FF;            ///< \~Chinese 检测所有脸部动作 \~English face detect full
    public final static long ST_MOBILE_HAND_DETECT_FULL = 0x410000FEFF00L;      ///< \~Chinese 检测所有手势, 如果手势分类和手部骨骼点(2d/3d)的config同时打开时, 对于恭贺（抱拳)/双手合十/手势ILoveYou等组合手势只能检测出一个组合手势． \~English hand detect fulll
    public final static int ST_MOBILE_BODY_DETECT_FULL = 0x018000000;           ///< \~Chinese 检测肢体关键点和肢体轮廓点 \~English body detect full

    public final static int ST_DYNAMIC_GESTURE_TYPE_INVALID = -1;                          ///< \~Chinese 无效的动态手势 \~English gesture type invallid
    public final static int ST_DYNAMIC_GESTURE_TYPE_HOLD_ON = 0;                           ///< \~Chinese 静止 \~English gesture type hold on
    public final static int ST_DYNAMIC_GESTURE_TYPE_FOREFINGER_CLICK = 1;                  ///< \~Chinese 食指点击 \~English gesture type
    public final static int ST_DYNAMIC_GESTURE_TYPE_FOREFINGER_ROTATION_CLOCKWISE = 2;     ///< \~Chinese 食指顺时针旋转 \~English gesture type
    public final static int ST_DYNAMIC_GESTURE_TYPE_FOREFINGER_ROTATION_ANTICLOCKWISE = 3; ///< \~Chinese 食指逆时针旋转 \~English gesture type
    public final static int ST_DYNAMIC_GESTURE_TYPE_PALM_FAN = 4;                          ///< \~Chinese 手掌扇风（废弃） \~English gesture type
    public final static int ST_DYNAMIC_GESTURE_TYPE_PALM_MOVING_LEFT_AND_RIGHT = 5;        ///< \~Chinese 手掌左右平移 \~English gesture type
    public final static int ST_DYNAMIC_GESTURE_TYPE_PALM_MOVING_UP_AND_DOWN = 6;           ///< \~Chinese 手掌上下平移 \~English gesture type
    public final static int ST_DYNAMIC_GESTURE_TYPE_MAX_NUM = 7;                           ///< \~Chinese 目前支持的动态手势个数 \~English gesture type

    public enum STMobileExpression {
        ST_MOBILE_EXPRESSION_EYE_BLINK (1),  ///< \~Chinese 眨眼 \~English eye blink
        ST_MOBILE_EXPRESSION_MOUTH_AH (2), ///< \~Chinese 嘴巴大张 \~English mouth ah
        ST_MOBILE_EXPRESSION_HEAD_YAW (3), ///< \~Chinese 摇头 \~English head yaw
        ST_MOBILE_EXPRESSION_HEAD_PITCH (4),  ///< \~Chinese 点头 \~English head pitch
        ST_MOBILE_EXPRESSION_BROW_JUMP (5),  ///< \~Chinese 挑眉 \~English brow jump

        ST_MOBILE_EXPRESSION_HAND_OK (9),  ///< \~Chinese OK手势 \~English hand ok
        ST_MOBILE_EXPRESSION_HAND_SCISSOR (10),  ///< \~Chinese 剪刀手 \~English hand scissor
        ST_MOBILE_EXPRESSION_HAND_GOOD (11),  ///< \~Chinese 大拇哥 \~Englishn hand good
        ST_MOBILE_EXPRESSION_HAND_PALM (12),  ///< \~Chinese 手掌 \~English hand palm
        ST_MOBILE_EXPRESSION_HAND_PISTOL (13),  ///< \~Chinese 手枪手势 \~English hand pistol
        ST_MOBILE_EXPRESSION_HAND_LOVE (14),  ///< \~Chinese 爱心手势 \~English hand love
        ST_MOBILE_EXPRESSION_HAND_HOLDUP (15),  ///< \~Chinese 托手手势 \~English hand holdup
        ST_MOBILE_EXPRESSION_HAND_CONGRATULATE (17),  ///< \~Chinese 恭贺（抱拳） \~English hand congratulate
        ST_MOBILE_EXPRESSION_HAND_FINGER_HEART (18),  ///< \~Chinese 单手比爱心 \~English hand finger heart
        ST_MOBILE_EXPRESSION_HAND_FINGER_INDEX (20),  ///< \~Chinese 食指指尖 \~English hand finger index

        ST_MOBILE_EXPRESSION_HEAD_NORMAL (65), ///< \~Chinese 头正向 \~English head normal
        ST_MOBILE_EXPRESSION_SIDE_FACE_LEFT (66), ///< \~Chinese 头向左侧偏 \~English side face left
        ST_MOBILE_EXPRESSION_SIDE_FACE_RIGHT (67), ///< \~Chinese 头向右侧偏 \~English side face right
        ST_MOBILE_EXPRESSION_TILTED_FACE_LEFT (68), ///< \~Chinese 头向左侧倾斜 \~English tilted face left
        ST_MOBILE_EXPRESSION_TILTED_FACE_RIGHT (69), ///< \~Chinese 头向右侧倾斜 \~English tilted face right
        ST_MOBILE_EXPRESSION_HEAD_RISE (70), ///< \~Chinese 抬头 \~English head rise
        ST_MOBILE_EXPRESSION_HEAD_LOWER (71), ///< \~Chinese 低头 \~English head lower

        ST_MOBILE_EXPRESSION_TWO_EYE_CLOSE (85), ///< \~Chinese 两眼都闭 \~English two eye close
        ST_MOBILE_EXPRESSION_TWO_EYE_OPEN (86), ///< \~Chinese 两眼都睁 \~English two eye open
        ST_MOBILE_EXPRESSION_LEFTEYE_OPEN_RIGHTEYE_CLOSE (87), ///< \~Chinese 左眼睁右眼闭 \~English left eye open and right eye close
        ST_MOBILE_EXPRESSION_LEFTEYE_CLOSE_RIGHTEYE_OPEN (88), ///< \~Chinese 左眼闭右眼睁 \~English left eye close and right eye open

        ST_MOBILE_EXPRESSION_MOUTH_OPEN (105), ///< \~Chinese 闭嘴 \~English mouth open
        ST_MOBILE_EXPRESSION_MOUTH_CLOSE (106), ///< \~Chinese 张嘴 \~English mouth close
        ST_MOBILE_EXPRESSION_FACE_LIPS_UPWARD (107), ///< \~Chinese 嘴角上扬 \~English lips upward
        ST_MOBILE_EXPRESSION_FACE_LIPS_POUTED (108), ///< \~Chinese 嘟嘴 \~English lips pouted
        ST_MOBILE_EXPRESSION_FACE_LIPS_CURL_LEFT (109),   ///< \~Chinese 左撇嘴 \~English lips curl left
        ST_MOBILE_EXPRESSION_FACE_LIPS_CURL_RIGHT (110),   ///< \~Chinese 右撇嘴 \~English lips curl right

        ST_MOBILE_EXPRESSION_COUNT (128),

        ST_MOBILE_EXPRESSION_FACE_ALL (257),   ///< \~Chinese 所有脸部动作（set_expression） \~English face all （set_expression）
        ST_MOBILE_EXPRESSION_HAND_ALL (258);   ///< \~Chinese 所有手部动作（set_expression） \~English  hand all （set_expression）

        private final int expressionCode;

        STMobileExpression(int expressionCode) {
            this.expressionCode = expressionCode;
        }

        public int getExpressionCode() {
            return expressionCode;
        }
    };

    /**
     * \~Chinese 加载libs \~English Load library
     */
    static {
        System.loadLibrary("st_mobile");
        System.loadLibrary("stmobile_jni");
    }

    /**
     * \~Chinese 供JNI使用，应用不需要关注 \~English JNI Using
     */
    private long nativeHumanActionHandle;

    /**
     * \~Chinese 创建实例
     *
     * @param modelpath \~Chinese 模型文件的,例如models/face_track_2.x.x.model
     * @param config    \~Chinese 配置选项，建议输入ST_MOBILE_HUMAN_ACTION_DEFAULT_CONFIG_VIDEO
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Create handle
     *
     * @param modelpath \~English model path
     * @param config    \~English create config, for example ST_MOBILE_HUMAN_ACTION_DEFAULT_CONFIG_VIDEO
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstance(String modelpath, int config);

    /**
     * \~Chinese 从资源文件夹创建实例
     *
     * @param assetModelpath \~Chinese 模型文件的路径
     * @param config         \~Chinese 配置选项，比如STCommon.ST_MOBILE_TRACKING_RESIZE_IMG_320W。建议输入ST_MOBILE_HUMAN_ACTION_DEFAULT_CONFIG_VIDEO
     * @param assetManager   \~Chinese 资源文件管理器
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Create hanle from asset file
     *
     * @param assetModelpath \~English asset model file
     * @param config         \~English create config, for example ST_MOBILE_HUMAN_ACTION_DEFAULT_CONFIG_VIDEO
     * @param assetManager   \~English asset manager
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstanceFromAssetFile(String assetModelpath, int config, AssetManager assetManager);

    /**
     * \~Chinese 从buffer创建实例
     *
     * @param buffer \~Chinese 模型文件的buffer
     * @param len    \~Chinese buffer长度
     * @param config \~Chinese 配置选项，比如STCommon.ST_MOBILE_TRACKING_RESIZE_IMG_320W。建议输入ST_MOBILE_HUMAN_ACTION_DEFAULT_CONFIG_VIDEO
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Create handle fom buffer
     *
     * @param buffer \~English model buffer
     * @param len    \~English buffer length
     * @param config \~English create config, for example ST_MOBILE_HUMAN_ACTION_DEFAULT_CONFIG_VIDEO
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstanceFromBuffer(byte[] buffer, int len, int config);
    
    /**
     * \~Chinese 通过子模型创建人体行为检测句柄, st_mobile_human_action_create和st_mobile_human_action_create_with_sub_models只能调一个
     *
     * @param modelPaths \~Chinese 模型文件路径指针数组. 根据加载的子模型确定支持检测的类型. 如果包含相同的子模型, 后面的会覆盖前面的
     * @param modelCount \~Chinese 模型文件数目
     * @param config     \~Chinese detect_mode 设置检测模式. 检测视频时设置为ST_MOBILE_DETECT_MODE_VIDEO, 检测图片时设置为ST_MOBILE_DETECT_MODE_IMAGE
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Create human action handle with sub model
     *
     * @param modelPaths \~English models path
     * @param modelCount \~English model count
     * @param config     \~English create config, for example ST_MOBILE_HUMAN_ACTION_DEFAULT_CONFIG_VIDEO
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int createInstanceWithSubModels(String[] modelPaths, int modelCount, int config);

    /**
     * \~Chinese 添加子模型
     *
     * @param modelPath \~Chinese 模型文件的路径. 后添加的会覆盖之前添加的同类子模型。加载模型耗时较长, 建议在初始化创建句柄时就加载模型
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Add sun model
     *
     * @param modelPath \~English sub model path
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int addSubModel(String modelPath);

    /**
     * \~Chinese 从资源文件夹添加子模型
     *
     * @param assetModelpath \~Chinese 模型文件的路径. 后添加的会覆盖之前添加的同类子模型。加载模型耗时较长, 建议在初始化创建句柄时就加载模型
     * @param assetManager   \~Chinese 资源文件管理器
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Add sub model from asset file
     *
     * @param assetModelpath  \~English asset model path
     * @param assetManager    \~English asset manager
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int addSubModelFromAssetFile(String assetModelpath, AssetManager assetManager);

    /**
     * \~Chinese 从buffer添加子模型
     *
     * @param buffer \~Chinese 模型文件的buffer
     * @param len    \~Chinese buffer长度
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Add sub model from buffer
     *
     * @param buffer \~English model buffer
     * @param len    \~English buffer length
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int addSubModelFromBuffer(byte[] buffer, int len);

    /**
     * \~Chinese 删除已添加的子模型
     *
     * @param config \~Chinese 要删除的子模型对应的config，config为创建人体行为检测句柄配置选
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Remove sub model by config
     *
     * @param config \~English sub model config
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int removeSubModelByConfig(int config);

    /**
     * \~Chinese 设置参数
     *
     * @param type  \~Chinese 要设置Human Action参数的类型
     * @param value \~Chinese 设置的值
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English set param
     *
     * @param type  \~English type
     * @param value \~English value
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setParam(int type, float value);

//    /**
//     * \~Chinese 检测人脸106点及人脸行为 \~English
//     *
//     * @param imgData       \~Chinese 用于检测的图像数据 \~English
//     * @param imageFormat   \~Chinese 用于检测的图像数据的像素格式,比如STCommon.ST_PIX_FMT_NV12。能够独立提取灰度通道的图像格式处理速度较快,比如ST_PIX_FMT_GRAY8，ST_PIX_FMT_YUV420P，ST_PIX_FMT_NV12，ST_PIX_FMT_NV21 \~English
//     * @param detect_config \~Chinese 检测选项，代表当前需要检测哪些动作，例如ST_MOBILE_EYE_BLINK|ST_MOBILE_MOUTH_AH表示当前帧只检测眨眼和张嘴 \~English
//     * @param orientation   \~Chinese 图像中人脸的方向,例如STRotateType.ST_CLOCKWISE_ROTATE_0 \~English
//     * @param imageWidth    \~Chinese 用于检测的图像的宽度(以像素为单位) \~English
//     * @param imageHeight   \~Chinese 用于检测的图像的高度(以像素为单位) \~English
//     * @return 人脸信息
//     */
//    public native STHumanAction humanActionDetect(byte[] imgData, int imageFormat, long detect_config,
//                                                  int orientation, int imageWidth, int imageHeight);

    /**
     * \~Chinese 重置，清除所有缓存信息. 视频模式下会在handle中保存一些状态，当切换分辨率、切换前后摄像头、切换后台、两帧图像差别较大时需要调用reset
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Human action detect reset
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public synchronized native int reset();

    /**
     * \~Chinese 释放instance \~English Release handle
     */
    public synchronized native void destroyInstance();

    /**
     * \~Chinese 根据human_action结果获取expression动作信息. 在st_mobile_human_action_detect之后调用. 默认各动作阈值为0.5
     *
     * @param humanAction  \~Chinese 计算表情的STHumanAction对象
     * @param orientation  \~Chinese 图像中人脸的方向,例如STRotateType.ST_CLOCKWISE_ROTATE_90（人脸方向为90度）
     * @param needMirror   \~Chinese 是否需要镜像expression结果
     * @return \~Chinese 返回expressions_result检测动作结果数组,动作有效true，无效false
     *
     * \~English Get expression information from human action results, The default threshold for each action is 0.5
     *
     * @param humanAction  \~English human action detect result
     * @param orientation  \~English orientation, for example STRotateType.ST_CLOCKWISE_ROTATE_90
     * @param needMirror   \~English if need mirror
     * @return \~English expressions result list
     */
    public static native boolean[] getExpression(STHumanAction humanAction, int orientation, boolean needMirror);

    /**
     * \~Chinese 设置expression动作阈值
     *
     * @param detectExpression \~Chinese 需要设置阈值的检测动作. 目前仅支持face相关的阈值，可以配置为ST_MOBILE_EXPRESSION_FACE_LIPS_POUTE等
     * @param threshold        \~Chinese 阈值数值[0,1]，默认阈值为0.5, 阈值越大，误检越少，漏检越多
     */
    /**
     *\~English Set expression action threshold
     *
     * @param detectExpression \~English expression acton to set
     * @param threshold        \~English The default threshold for each action is 0.5, range [0, 1], The higher the threshold, the less false positives and the more missed detections
     */
    public static native void setExpressionThreshold(int detectExpression, float threshold);

    /**
     * \~Chinese 测试人脸距离
     *
     * @param faceInfo      \~Chinese 输入人脸信息
     * @param orientation   \~Chinese 人脸方向
     * @param width         \~Chinese 检测图片宽度
     * @param height        \~Chinese 检测图片高度
     * @param fov           \~Chinese 相机fov信息
     *
     * \~English Get face distance
     *
     * @param faceInfo      \~English input face info
     * @param orientation   \~English orientation
     * @param width         \~English width
     * @param height        \~English height
     * @param fov           \~English camera fov
     */
    public native float getFaceDistance(STMobileFaceInfo faceInfo, int orientation, int width, int height, float fov);

    /**
     * \~Chinese 设置faceAction动作阈值
     *
     * @param faceAction \~Chinese 需要设置阈值的检测动作. 目前仅支持face相关的阈值
     * @param threshold \~Chinese 阈值数值[0,1]，默认阈值为0.5, 阈值越大，误检越少，漏检越多
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Set face action threshold
     *
     * @param faceAction \~English face action to set
     * @param threshold  \~English The default threshold for each action is 0.5, range [0, 1], The higher the threshold, the less false positives and the more missed detections
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int setFaceActionThreshold(long faceAction, float threshold);

    /**
     * \~Chinese 获取faceAction动作阈值
     * @param config \~Chinese 需要获取阈值的检测动作. 目前仅支持face相关的阈值
     *
     * @return 成功返回阈值数值
     *
     * \~English Get face action threshold
     * @param config \~English face action config
     *
     * @retur \~English return threshold value
     */
    public native float getFaceActionThreshold(long config);

    /**
     * \~Chinese HumanAction检测结果对应指针，detect检测结果会保存到其对应的结构体 \~English human action result handle
     */
    private long nativeHumanActionResultPtr;
    private long nativeHumanActionResultPtrCopy;

    public synchronized native void nativeHumanActionPtrCopy();

    public native void nativeResizeHumanActionPtr(float scale);

    public long getNativeHumanActionPtrCopy(){
        return nativeHumanActionResultPtrCopy;
    }

    /**
     * \~Chinese 获取HumanAction检测结果对应指针
     * @return \~Chinese 检测结果对应指针
     *
     * \~English Get native human action handle
     * @return \~English native human action pointer
     */
    public long getNativeHumanActionResultPtr() {
        return nativeHumanActionResultPtr;
    }

    private long nativeHumanActionCache1;
    private long nativeHumanActionCache2;

    /**
     * \~Chinese 更新jni层humanAction缓存
     * @param index \~Chinese 缓存索引
     *
     * \~English Update native human action result cache by index
     * @param index \~English cache index
     */
    public native void updateNativeHumanActionCache(int index);

    /**
     * \~Chinese 根据索引获取HumanAction检测结果对应指针
     * @param index \~Chinese 缓存索引
     *
     * @return \~Chinese 检测结果对应指针
     *
     * \~English Get native human action result pointer by index
     * @param index \~English cache index
     *
     * @return \~English native human action pointer
     */
    public long getNativeHumanActionResultCache(int index) {
        if(index == 0){
            return nativeHumanActionCache1;
        }else if(index == 1){
            return nativeHumanActionCache2;
        }

        return 0;
    }

    /**
     * \~Chinese 使用Native方式检测，并将检测结果保存到nativeHumanActionResultPtr指针对应地址
     *
     * @param imgData       \~Chinese 用于检测的图像数据
     * @param imageFormat   \~Chinese 用于检测的图像数据的像素格式,比如STCommon.ST_PIX_FMT_NV12。能够独立提取灰度通道的图像格式处理速度较快, 比如ST_PIX_FMT_GRAY8，ST_PIX_FMT_YUV420P，ST_PIX_FMT_NV12，ST_PIX_FMT_NV21
     * @param detect_config \~Chinese 检测选项，代表当前需要检测哪些动作，例如ST_MOBILE_EYE_BLINK|ST_MOBILE_MOUTH_AH表示当前帧只检测眨眼和张嘴
     * @param orientation   \~Chinese 图像中人脸的方向,例如STRotateType.ST_CLOCKWISE_ROTATE_0
     * @param imageWidth    \~Chinese 用于检测的图像的宽度(以像素为单位)
     * @param imageHeight   \~Chinese 用于检测的图像的高度(以像素为单位)
     *
     * \~English Human action detect
     *
     * @param imgData       \~English image data
     * @param imageFormat   \~English image format
     * @param detect_config \~English detect config
     * @param orientation   \~English orientation, for example STRotateType.ST_CLOCKWISE_ROTATE_0
     * @param imageWidth    \~English image width
     * @param imageHeight   \~English image height
     */
    public native int nativeHumanActionDetectPtr(byte[] imgData, int imageFormat, long detect_config,
                                                  int orientation, int imageWidth, int imageHeight);

    /**
     * \~Chinese 将Native检测结果resize
     *
     * @param scale  \~Chinese resize比例
     *
     * \~English Resize native human action result
     *
     * @param scale \~English scale
     */
    public native void nativeHumanActionResizePtr(float scale);

    /**
     * \~Chinese 将Native检测结果镜像
     *
     * @param width \~Chinese  图像宽度
     *
     * \~English Mirror native human action result
     *
     * @param width \~English width
     */
    public native void nativeHumanActionMirrorPtr(int width);

    /**
     * \~Chinese 将Native检测结果旋转
     *
     * @param width            \~Chinese 用于检测的图像的宽度(以像素为单位)
     * @param height           \~Chinese 用于检测的图像的高度(以像素为单位)
     * @param rotateBackground \~Chinese 是否旋转前后背景分割的结果
     * @param rotation         \~Chinese 方向,例如STRotateType.ST_CLOCKWISE_ROTATE_0
     *
     * \~English Rotate native human action result
     *
     * @param width            \~English width
     * @param height           \~English height
     * @param rotateBackground \~English if need rotate background
     * @param rotation         \~English rotation, for example STRotateType.ST_CLOCKWISE_ROTATE_0
     */
    public native void nativeHumanActionRotatePtr(int width, int height, int rotation, boolean rotateBackground);

    /**
     * \~Chinese 获取native human action 检测数据到java结构体
     * @return \~Chinese 检测结果对应指针
     *
     * \~English Get native human action result
     * @return \~English native human action pointer
     */
    public native STHumanAction getNativeHumanAction();

    /**
     * \~Chinese 获取handle对应人脸的面片索引，注：检测之前需要先调一下
     * @return  \~Chinese 面片索引
     *
     * \~English Get face mesh list
     * @return \~English face mesh list
     */
    public native STFaceMeshList getFaceMeshList();

    /**
     * \~Chinese 获取handle对应人脸的面片索引，注：检测之前需要先调一下
     * @return  \~Chinese 面片索引
     *
     * \~English Get face mesh list
     * @return \~English face mesh list
     */
    public native int[] getFaceMeshList2();

//    public native void getFaceMeshListQua();

    /**
     * \~Chinese 获取face 3DMesh关键点数组
     *
     * @param index \~Chinese 人脸index
     * @return  \~Chinese Mesh关键点数组
     *
     * \~English Get array of face mesh point
     *
     * @param index \~English face id
     * @return \~English array of face mesh point
     */
    public native float[] getFaceMeshPoint(int index);

    /**
     * \~Chinese 获取face 3DMesh法线数组
     *
     * @param index \~Chinese 人脸index
     * @return  \~Chinese Mesh法线数组
     *
     * \~English Get array of face mesh normal
     *
     * @param index \~English face id
     * @return \~English array of face mesh normal
     */
    public native float[] getFaceMeshNormal(int index);

    /**
     * \~Chinese 获取handle对应人头的面片索引，注：检测之前需要先调一下
     * @return  \~Chinese 面片索引
     *
     * \~English Get head mesh list
     * @return \~English head mesh list
     */
    public native int[] getHeadMeshList2();

    /**
     * \~Chinese 获取人头 3DMesh关键点数组
     *
     * @param index \~Chinese 人头index
     * @return  \~Chinese Mesh关键点数组
     *
     * \~English get array of head mesh point
     *
     * @param index \~English head id
     * @return \~English array of head mesh point
     */
    public native float[] getHeadMeshPoint(int index);

    /**
     * \~Chinese 获取人头 3DMesh法线数组
     *
     * @param index \~Chinese 人头index
     * @return  \~Chinese Mesh法线数组
     *
     * \~English Get head mesh normal
     *
     * @param index \~English head id
     * @return \~English array of head mesh normal
     */
    public native float[] getHeadMeshNormal(int index);

    /**
     * \~Chinese 获取用于遮罩的嘴唇和眼睛索引 \~English Get face Occlude list
     */
    public native int[] getFaceOccluderList2();

    /**
     * \~Chinese 获取handle对应的人脸mesh纹理坐标信息
     *
     * @param index \~Chinese 人脸id
     * @return  \~Chinese 人脸mesh纹理坐标信息
     *
     *\~English Get face mesh texcoords info
     *
     * @param index \~English face id
     * @return \~English head mesh texcoords info
     */
    public native float[] getFaceMeshTexcoords(int index);

    /**
     * \~Chinese 获取用于遮罩的嘴唇和眼睛索引 \~English Get head Occlude list
     */
    public native int[] getHeadOccluderList2();

    /**
     * \~Chinese 获取handle对应的head mesh纹理坐标信息
     *
     * @param index \~Chinese head id
     * @return  \~Chinese head mesh纹理坐标信息
     *
     * \~English Get head mesh texcoords info
     *
     * @param index \~English head id
     * @return \~English head mesh texcoords info
     */
    public native float[] getHeadMeshTexcoords(int index);

    /**
     * \~Chinese 从资源文件夹添加obj
     *
     * @param assetFilepath \~Chinese obj文件的路径
     * @param meshType \~Chinese mesh类型
     * @param assetManager \~Chinese 资源文件管理器
     * @return \~Chinese 成功返回0，错误返回其他，参考STCommon.ResultCode
     *
     * \~Chinese 从资源文件夹添加obj \~English Load standard mesh obj from asset file
     *
     * @param assetFilepath \~English file path
     * @param meshType      \~English mesh type
     * @param assetManager  \~English asset manager
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int loadStandardMeshObjFromAssetFile(String assetFilepath, int meshType, AssetManager assetManager);

    /**
     * \~Chinese 添加obj
     *
     * @param filePath \~Chinese obj文件的路径
     * @param meshType \~Chinese mesh类型
     * @return \~Chinese 成功返回0，错误返回其他，参考STUtils.ResultCode
     *
     * \~English Load standard mesh obj
     *
     * @param filePath \~English file path
     * @param meshType \~English mesh type
     * @return \~English return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int loadStandardMeshObj(String filePath, int meshType);

    /**
     * \~Chinese 对分割后的图像进行描边
     *
     * @param imageInput \~Chinese 分割结果二值图，前景像素为0，背景像素为255
     * @param edgeWidth  \~Chinese 需要描边的边缘宽度
     * @param edgeBlur   \~Chinese 对edge进行滤波与否
     * @return  \~Chinese 描边后的灰度图,边缘值处于0-255之间,其余为0, 用户分配图像内存，大小与segment图像内存相同
     *
     * \~Chinese 对分割后的图像进行描边 \~English Retrieve human action segment edge
     *
     * @param imageInput \~English segment image
     * @param edgeWidth  \~English edge width
     * @param edgeBlur   \~English edge height
     * @return \~English result image
     */
    public native STImage retrieveHumanEdge(STImage imageInput, int edgeWidth, boolean edgeBlur);

    /**
     * \~Chinese 对分割后的图像进行描边
     *
     * @param config \~Chinese 分割config
     * @return  \~Chinese 获取分割matting图像
     *
     * \~English Get figure matting foreground image
     *
     * @param config \~English segment config
     * @return \~English return figure matting foreground image
     */
    public native STImage getFigureMattingForeground(long config);

//    /** \~Chinese 获取半身肢体四元数信息 \~English
//     * @param humanAction \~Chinese 检测到的body结果 \~English
//     * @return  \~Chinese 半身肢体四元数信息 \~English
//     */
//    public native int bodyAvatarGetQuaternion(STHumanAction humanAction, int imageWidth, int imageHeight, int rotate, STBodyAvatar[] bodyAvatar, int bodyAvatarCount);
//
//    public native STBodyAvatar[] calcUpbodyAvatarQuaternion(STHumanAction humanAction, int imageWidth, int imageHeight, int rotate);

    /** \~Chinese 获取脸型 \~English Get face shape
     * @param faceInfo \~Chinese 检测到的人脸信息
     * @param faceShapes \~Chinese 返回的脸型数据，上层分配内存，int[] faceShapes = new int[1]; int faceShape = faceShapes[0] \~English face shapes info
     * @return \~Chinese 成功返回0，错误返回其它，参考STResultCode
     *
     * \~English Get face shape
     * @param faceInfo   \~English detect face info
     * @param faceShapes \~English face shapes info, int[] faceShapes = new int[1]; int faceShape = faceShapes[0]
     * @return \~English  return ST_OK or 0 is success, error refer to STResultCode
     */
    public native int getFaceShape(STMobileFaceInfo faceInfo, int[] faceShapes);

    /**
     * \~Chinese 获取mesh三角拓扑面片索引信息，在加载模型后调用一次来获取索引信息，或者在每次设置mesh模式后调用一次来更新索引信息
     *
     * @param meshType \~Chinese mesh类型,face mesh 或者 head mesh
     * @return \~Chinese 返回 获取mesh三角拓扑面片索引信息
     *
     *  \~English Get mesh triangle topology patch index information
     *
     * @param meshType \~English mesh type, face mesh or head mesh
     * @return \~English return mesh triangle topology patch index information
     */
    public native STFaceMeshList getMeshList(int meshType);

}
