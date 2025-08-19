package com.softsugar.stmobile.params;

public class STMobileModelType {
    public final static int ST_MOBILE_MODEL_TYPE_FACE_106 = 0;          ///< \~Chinese 人脸
    public final static int ST_MOBILE_MODEL_TYPE_FACE_EXTRA= 1;         ///< \~Chinese 人脸240点
    public final static int ST_MOBILE_MODEL_TYPE_FACE_EYEBALL= 2;       ///< \~Chinese 眼球中心点
    public final static int ST_MOBILE_MODEL_TYPE_FACE_TONGUE= 3;        ///< \~Chinese 舌头关键点
    public final static int ST_MOBILE_MODEL_TYPE_FACE_GAZE = 4;         ///< \~Chinese 视线方向
    public final static int ST_MOBILE_MODEL_TYPE_FACE_AVATAR_HELPER = 5;    ///< \~Chinese 人脸avatar辅助信息开关
    public final static int ST_MOBILE_MODEL_TYPE_FACE_EAR = 6;           ///< \~Chinese 耳朵关键点开关
    public final static int ST_MOBILE_MODEL_TYPE_FACE_MESH = 7; ///< \~Chinese 3dmesh关键点开关
    public final static int ST_MOBILE_MODEL_TYPE_AVARAE_HELPER = 8;    ///< \~Chinese avatar help 模型

    public final static int ST_MOBILE_MODEL_TYPE_HAND_DETECT =100;      ///< \~Chinese 手势
    public final static int ST_MOBILE_MODEL_TYPE_HAND_SKELETON_KEYPOINTS_2D3D = 101; ///<\~Chinese  手势关节点
    public final static int ST_MOBILE_MODEL_TYPE_HAND_DYNAMIC_GESTURE = 103;        ///<\~Chinese  动态手势
//	ST_MOBILE_MODEL_TYPE_HAND_FARDISTANCE = 104             ///\~Chinese 远距离手势

    public final static int ST_MOBILE_MODEL_TYPE_BODY_2D =200;   ///< \~Chinese 肢体2d模型
//	ST_MOBILE_MODEL_TYPE_UPBODY_KEYPOINTS = 201,        ///< \~Chinese 半身模型
//	ST_MOBILE_MODEL_TYPE_BODY_CONTOUR = 201,            ///< \~Chinese 肢体轮廓模型
    public final static int ST_MOBILE_MODEL_TYPE_BODY_3D = 202;                 ///< \~Chinese 肢体3d点
    public final static int ST_MOBILE_MODEL_TYPE_UPBODY_AVATAR = 203;           ///< \~Chinese 半身肢体avatar

    public final static int ST_MOBILE_MODEL_TYPE_HEAD_DETECT = 300; // \~Chinese 头部检测
    public final static int ST_MOBILE_MODEL_TYPE_HEAD_MESH = 301; // \~Chinese 头部mesh

    public final static int ST_MOBILE_MODEL_TYPE_SEGMENT_FIGURE = 400;         ///< \~Chinese 背景分割
    public final static int ST_MOBILE_MODEL_TYPE_SEGMENT_HAIR = 401;           ///< \~Chinese 头发分割
    public final static int ST_MOBILE_MODEL_TYPE_SEGMENT_MULTI = 402;           ///< \~Chinese 多类分割
    public final static int ST_MOBILE_MODEL_TYPE_SEGMENT_HEAD= 403;             ///< \~Chinese 头部分割
    public final static int ST_MOBILE_MODEL_TYPE_SEGMENT_SKIN = 404;            ///< \~Chinese 皮肤分割
    public final static int ST_MOBILE_MODEL_TYPE_SEGMENT_MOUTH_PARSE = 405;     ///< \~Chinese 嘴部遮挡信息分割
    public final static int ST_MOBILE_MODEL_TYPE_SEGMENT_FACE_OCCLUSION = 406;  ///< \~Chinese 面部遮挡信息分割
    public final static int ST_MOBILE_MODEL_TYPE_SEGMENT_SKY = 407;				///< \~Chinese 天空分割

    public final static int ST_MOBILE_MODEL_TYPE_DEPTH_ESTIMATION = 408;		///< \~Chinese 深度估计信息

}
