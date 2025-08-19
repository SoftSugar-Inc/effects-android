package com.softsugar.stmobile.params;

public class STHumanActionParamsType {

    /// \~Chinese 设置检测到的最大人脸数目N(默认值32, 最大值32),持续track已检测到的N个人脸直到人脸数小于N再继续做detect.值越大,检测到的人脸数目越多,但相应耗时越长. 如果当前人脸数目达到上限，检测线程将休息 \~english Set the maximum number of detected faces N (default value: 32, maximum value: 32). Continue tracking the detected N faces until the number of faces becomes less than N before continuing detection. The larger the value, the more faces will be detected, but the longer the processing time. If the current number of faces reaches the limit, the detection thread will rest
    public final static int ST_HUMAN_ACTION_PARAM_FACELIMIT = 0;
    /// \~Chinese 设置tracker每多少帧进行一次detect(默认值有人脸时24,无人脸时24/3=8). 值越大,cpu占用率越低, 但检测出新人脸的时间越长. \~english Set the number of frames between face detections (default value: 24 when a face is present, 8 when no face is present). The larger the value, the lower the CPU usage, but it takes longer to detect new faces
    public final static int ST_HUMAN_ACTION_PARAM_FACE_DETECT_INTERVAL = 1;
    /// \~Chinese 设置106点平滑的阈值[0.0,1.0](默认值0.5), 值越大, 点越稳定,但相应点会有滞后.\~english Set whether to smooth the 106-point or 240-point facial landmarks (default value: 0 for no smoothing). Non-smoothed landmarks may have some jitter, while smoothed landmarks may be slightly delayed
    public final static int ST_HUMAN_ACTION_PARAM_SMOOTH_THRESHOLD = 2;
    /// \~chinese 设置head_pose去抖动的阈值[0.0,1.0](默认值0.5),值越大, pose信息的值越稳定,但相应值会有滞后. \~english Set the threshold for head pose smoothing [0.0,1.0] (default value: 0.5). A higher value makes the pose information more stable, but introduces some delay
    public final static int ST_HUMAN_ACTION_PARAM_HEADPOSE_THRESHOLD = 3;
    /// \~Chinese 设置脸部隔帧检测（对上一帧结果做拷贝），目的是减少耗时。默认每帧检测一次. 最多每10帧检测一次. 开启隔帧检测后, 只能对拷贝出来的检测结果做后处理.\~english Set the interval for face processing (copying the results from the previous frame) to reduce processing time. The default is to process every frame. The maximum interval is every 10 frames. When enabled, only the copied detection results can be post-processed
    public final static int ST_HUMAN_ACTION_PARAM_FACE_PROCESS_INTERVAL = 5;
    /// \~Chinese 设置人脸106点检测的阈值[0.0,1.0] \~english Set the threshold for face detection [0.0,1.0]
    public final static int ST_HUMAN_ACTION_PARAM_FACE_THRESHOLD = 6;

    /// \~Chinese 设置mesh渲染模式, mesh分为人脸，眼睛，嘴巴，后脑勺，耳朵，脖子，眉毛七个部位，2106模型只包含人脸，眼睛，嘴巴三个部位，3060/2396模型只包含人脸，眼睛，嘴巴，后脑勺，耳朵，脖子六个部位
    /// \~Chinese 参数值类型为st_mobile_mesh_part，默认只渲染人脸st_mobile_mesh_part::ST_MOBILE_MESH_PART_FACE，
    /// \~Chinese 可以设置多个部位，例如：渲染人脸和嘴巴，st_mobile_mesh_part::ST_MOBILE_MESH_PART_FACE + st_mobile_mesh_part::ST_MOBILE_MESH_PART_MOUTH
    ////\~english Set the mesh rendering mode. The mesh consists of seven parts: face, eyes, mouth, back of the head, ears, neck, and eyebrows. The 2106 model only includes the face, eyes, and mouth parts, while the 3060/2396 model includes the face, eyes, mouth, back of the head, ears, and neck parts. The parameter value type is st_mobile_mesh_part. By default, only the face part (st_mobile_mesh_part::ST_MOBILE_MESH_PART_FACE) is rendered. Multiple parts can be set, for example: render the face and mouth (st_mobile_mesh_part::ST_MOBILE_MESH_PART_FACE + st_mobile_mesh_part::ST_MOBILE_MESH_PART_MOUTH)
    public final static int ST_HUMAN_ACTION_PARAM_MESH_MODE = 20;
    // \~Chinese 设置face mesh额头点扩展scale范围起始值（小于终止值，默认是2） \~english Set the starting scale value for expanding the forehead mesh points (default value: 2)
    public final static int ST_HUMAN_ACTION_PARAM_MESH_START_SCALE = 21;
    /// \~Chinese 设置mesh额头点扩展scale范围终止值（大于起始值，默认是3）\~english Set the ending scale value for expanding the forehead mesh points (default value: 3)
    public final static int ST_HUMAN_ACTION_PARAM_MESH_END_SCALE = 22;
    // \~Chinese 设置mesh结果输出坐标系,(0: 屏幕坐标系， 1：世界坐标系）\~english Set the coordinate system for mesh output (0: screen coordinates, 1: world coordinates)
    public final static int ST_HUMAN_ACTION_PARAM_MESH_OUTPUT_FORMAT = 23;
    /// \~Chinese 获取mesh模型支持的关键点的数量（2106/3060/2396） \~english Get the number of keypoints supported by the mesh model (2106/3060/2396)
    public final static int ST_HUMAN_ACTION_PARAM_MESH_MODEL_VERTEX_NUM = 24;
    /// \~Chinese 设置face mesh是否需要内缩282关键点坐标(只对face mesh有效，360度mesh不需要，0：不需要内缩，1：需要内缩) \~english Set whether to narrow the landmark coordinates of the face mesh (valid only for the face mesh). 0: no narrowing, 1: narrowing
    public final static int ST_HUMAN_ACTION_PARAM_FACE_MESH_NARROW_LANDMARK = 25;

    //  \~Chinese 设置face mesh结果输出坐标系,值使用st_3d_coordinate_type \~english Set the coordinate system for face mesh output, value uses st_3d_coordinate_type
    public final static int ST_HUMAN_ACTION_PARAM_FACE_MESH_OUTPUT_FORMAT = 26;
    /// \~Chinese 设置head mesh结果输出坐标系,值使用st_3d_coordinate_type\~english Set the coordinate system for head mesh output, value uses st_3d_coordinate_type
    public final static int ST_HUMAN_ACTION_PARAM_HEAD_MESH_OUTPUT_FORMAT = 27;

    // \~Chinese 手部参数 \~english Hand parameters
    /// \~Chinese 设置检测到的最大手数目N(默认值2, 最大值32),持续track已检测到的N个hand直到人脸数小于N再继续做detect.值越大,检测到的hand数目越多,但相应耗时越长. 如果当前手数目达到上限，检测线程将休息 \~english Set the maximum number of hands detected N (default value 2, maximum value 32). Continuously track the detected N hands until the number of hands is less than N, then continue to detect. The larger the value, the more hands detected, but the longer the corresponding time. If the current number of hands reaches the upper limit, the detection thread will rest
    public final static int ST_HUMAN_ACTION_PARAM_HAND_LIMIT = 101;
    /// \~Chinese 设置手势检测每多少帧进行一次 detect (默认有手时30帧detect一次, 无手时10(30/3)帧detect一次). 值越大,cpu占用率越低, 但检测出新人脸的时间越长. \~english Set the interval for hand gesture detection (default is 30 frames for detecting hands and 10 (30/3) frames for detecting no hands). The larger the value, the lower the CPU usage, but the longer it takes to detect new hands
    public final static int ST_HUMAN_ACTION_PARAM_HAND_DETECT_INTERVAL = 102;
    /// \~Chinese 设置手势隔帧检测（对上一帧结果做拷贝），目的是减少耗时。默认每帧检测一次. 最多每10帧检测一次. 开启隔帧检测后, 只能对拷贝出来的检测结果做后处理. \~english Set the interval for hand gesture detection using the previous frame's result as a copy, to reduce processing time. By default, detect every frame. Can detect every 10 frames at most. After enabling interval detection, only post-processing can be done on the copied detection result
    public final static int ST_HUMAN_ACTION_PARAM_HAND_PROCESS_INTERVAL = 103;
    /// \~Chinese 设置手检测的阈值[0.0,1.0] \~english Set the threshold for hand detection [0.0,1.0]
    public final static int ST_HUMAN_ACTION_PARAM_HAND_THRESHOLD = 104;

    /// \~chinese 设置手骨架检测的阈值[0.0,1.0] \~english Set the threshold for hand skeleton detection [0.0,1.0]
    public final static int ST_HUMAN_ACTION_PARAM_HAND_SKELETON_THRESHOLD = 110;

    // \~Chinese 肢体参数 \~english Body parameters
    /// \~Chinese 设置检测到的最大肢体数目N(默认值1),持续track已检测到的N个肢体直到肢体数小于N再继续做detect.值越大,检测到的body数目越多,但相应耗时越长. 如果当前肢体数目达到上限，检测线程将休息 \~english Set the maximum number of bodies detected N (default value 1). Continuously track the detected N bodies until the number of bodies is less than N, then continue to detect. The larger the value, the more bodies detected, but the longer the corresponding time. If the current number of bodies reaches the upper limit, the detection thread will rest
    public final static int ST_HUMAN_ACTION_PARAM_BODY_LIMIT = 200;
    /// \~Chinese 设置肢体关键点检测每多少帧进行一次 detect (默认有肢体时30帧detect一次, 无body时10(30/3)帧detect一次). 值越大,cpu占用率越低, 但检测出新body的时间越长. \~english Set the interval for body keypoint detection (default is 30 frames for detecting bodies and 10 (30/3) frames for detecting no bodies). The larger the value, the lower the CPU usage, but the longer it takes to detect new bodies
    //    ST_HUMAN_ACTION_PARAM_BODY_DETECT_INTERVAL = 201,
    public final static int ST_HUMAN_ACTION_PARAM_BODY_DETECT_INTERVAL = 201;
    /// \~Chinese 设置肢体隔帧检测（对上一帧结果做拷贝），目的是减少耗时。默认每帧检测一次. 最多每10帧检测一次. 开启隔帧检测后, 只能对拷贝出来的检测结果做后处理. \~english Set the parameters for frame skipping in body detection, which copies the results from the previous frame to reduce processing time
    public final static int ST_HUMAN_ACTION_PARAM_BODY_PROCESS_INTERVAL = 202;
    //\~Chinese 设置身体检测的阈值[0.0，1.0] \~english Set the threshold for body detection [0.0,1.0]
    public final static int ST_HUMAN_ACTION_PARAM_BODY_THRESHOLD = 203;
    public final static int ST_HUMAN_ACTION_PARAM_BODY_STATURE = 210;   // \~Chinese 身高，单位为米，3D骨架乘以身高（整体缩放），得到真实的物理尺度 \~english Set the input real height for child body detection

    // \~Chinese 人头分割参数 \~english Head segment parameters
    /// \~Chinese 设置头部分割检测结果灰度图的方向是否需要旋转（0: 不旋转, 保持竖直; 1: 旋转, 方向和输入图片一致. 默认不旋转) \~english Set whether to rotate the head segment detection result image
    public final static int ST_HUMAN_ACTION_PARAM_HEAD_SEGMENT_RESULT_ROTATE = 300;
    /// \~Chinese 设置人头分割边界区域上限阈值.  \~english Set the upper threshold for head segment detection result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_HEAD_SEGMENT_MAX_THRESHOLD = 301;
    /// \~Chinese 设置人头分割边界区域下限阈值 \~english Set the lower threshold for head segment detection result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_HEAD_SEGMENT_MIN_THRESHOLD = 302;
    // \~Chinese 头部分割后处理长边的长度[10,长边长度](默认长边240,短边=长边/原始图像长边*原始图像短边).值越大,头部分割后处理耗时越长,边缘部分效果越好.  \~english Set the length of the longer side for post-processing the head segment result. The value should be in the range [10, length of the longer side]
    public final static int ST_HUMAN_ACTION_PARAM_HEAD_SEGMENT_MAX_SIZE = 303;
    /// \~Chinese 设置最大人头分割个数，默认支持一个人头分割，face_id为人脸id; 若支持多个人头分割，则face id为-1.  \~english Sets the maximum number of head segmentations. By default, only one head segmentation is supported, and the face ID is used as the ID for the head segmentation. If multiple head segmentations are supported, the face ID should be set to -1
    public final static int ST_HUMAN_ACTION_PARAM_HEAD_SEGMENT_MAX_COUNT = 304;
    /// \~Chinese 设置头部分割检测结果边缘模糊程度 取值范围0-1 视频版默认0.5, 图片版默认是1  \~english Set the degree of edge blur for the head segment result. The value should be in the range [0, 1]. The default value is 0.5 for video and 1 for images
    public final static int ST_HUMAN_ACTION_PARAM_HEAD_SEGMENT_RESULT_BLUR = 305;
    /// \~Chinese 设置头部分割检测结果边缘模糊程度需保证开启温度系数，大于0.5为开启，小于0.5为关闭，关闭状态下使用卡阈值模式得到边界，默认状态为开启 \~english Set whether to use the temperature coefficient for the head segment result. The value should be greater than 0.5 to enable it and less than 0.5 to disable it. When disabled, the edge boundaries should be processed by thresholds
    public final static int ST_HUMAN_ACTION_PARAM_HEAD_SEGMENT_USE_TEMPERATURE = 306;
    /// \~chinese 边缘移动参数，取值范围再[-2,2],默认值是1 \~english Set the edge shift parameter for the head segment. The value should be in the range [-2, 2], and the default value is 1
    public final static int ST_HUMAN_ACTION_PARAM_HEAD_SEGMENT_EDGESHIFT = 307;
    /// \~Chinese 设置人头实例分割，人头实例分割依赖人脸检测，且只有加载人头实例分割模型设置该参数有效．默认不开启，出单个mask; 开启则多人多mask，数量跟人脸个数对应．
    public final static int ST_HUMAN_ACTION_PARAM_HEAD_SEGMENT_INSTANCE = 308;


    // \~chinese 背景分割/人像分割参数 \~english Background/figure segmentation parameters
    /// \~chinese 输出的background结果中长边的长度[10,长边长度](默认长边为模型内部处理的长边，若设置会做resize处理输出).值越大,背景分割的耗时越长,边缘部分效果越好.值为0还原为默认值. \~english Set the length of the longer side for the background segmentation result. The value should be in the range [10, length of the longer side]. By default, the longer side is determined by the internal processing of the model. If set, the output will be resized accordingly. A larger value will result in longer processing time and better edge effect on the segmented background. Setting the value to 0 will restore the default value
    public final static int ST_HUMAN_ACTION_PARAM_BACKGROUND_MAX_SIZE = 400;
    /// \~chinese 背景分割羽化程度[0,1](默认值0.35),0 完全不羽化,1羽化程度最高,在strenth较小时,羽化程度基本不变.值越大,前景与背景之间的过度边缘部分越宽. \~english Set the degree of feathering for the background segmentation result. The value should be in the range [0, 1], where 0 means no feathering and 1 means the highest degree of feathering. When the strength is low, the degree of feathering remains almost unchanged. A higher value widens the transition edge between the foreground and background
    /// \~chinese 备注：只对1.5.0 人像分割模型有效 \~english Note: This parameter is only effective for the figure segmentation model v1.5.0
    public final static int ST_HUMAN_ACTION_PARAM_BACKGROUND_BLUR_STRENGTH = 401;
    /// \~chinese 设置前后背景检测结果灰度图的方向是否需要旋转（0: 不旋转, 保持竖直; 1: 旋转, 方向和输入图片一致. 默认不旋转) \~english Set whether to rotate the grayscale background result image. The options are 0 (do not rotate, keep vertical) and 1 (rotate, align with the input image). By default, rotation is disabled
    public final static int ST_HUMAN_ACTION_PARAM_BACKGROUND_RESULT_ROTATE = 402;
    /// \~chinese 设置背景分割边界区域上限阈值. 开启温度系数时设置无效 \~english Set the upper threshold for the boundary region of the background segmentation result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_SEGMENT_MAX_THRESHOLD = 403;
    /// \~chinese 设置背景分割边界区域下限阈值 开启温度系数时设置无效 \~english Set the lower threshold for the boundary region of the background segmentation result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_SEGMENT_MIN_THRESHOLD = 404;
    //	ST_HUMAN_ACTION_PARAM_SEGMENT_KERNAL_TYPE = 406, \~chinese 已废弃 \~english (deprecated)
    /// \~chinese 设置背景分割检测结果边缘模糊程度 取值范围0-1, 视频版默认是0.5 图片版默认是1 \~english Set the degree of edge blur for the background segmentation result. The value should be in the range [0, 1]. The default value is 0.5 for video and 1 for images
    public final static int ST_HUMAN_ACTION_PARAM_BACKGROUND_SEGMENT_RESULT_BLUR = 407;
    /// \~chinese 设置背景分割检测结果边缘模糊程度需保证开启温度系数，大于0.5为开启，小于0.5为关闭，关闭状态下使用卡阈值模式得到边界，默认状态为开启 \~english Set whether to use the temperature coefficient for the background segmentation result. The value should be greater than 0.5 to enable it and less than 0.5 to disable it. When disabled, min max threshold should be used for mask postprocess. By default, this parameter is enabled
    public final static int ST_HUMAN_ACTION_PARAM_BACKGROUND_SEGMENT_USE_TEMPERATURE = 408;
    /// \~chinese 边缘移动参数，取值范围再[-2,2],默认值是1 \~english Set the edge shift parameter for the background segmentation. The value should be in the range [-2, 2], and the default value is 1
    public final static int ST_HUMAN_ACTION_PARAM_BACKGROUND_SEGMENT_EDGESHIFT=399;

    // \~chinese 头发分割参数 \~english Hair segmentation parameters
    /// \~chinese 头发结果中长边的长度[10,长边长度](默认长边240,短边=长边/原始图像长边*原始图像短边).值越大,头发分割的耗时越长,边缘部分效果越好. \~english Set the length of the longer side for the hair segmentation result. The value should be in the range [10, length of the longer side]. A larger value will result in longer processing time but better edge effect on the segmented hair
    public final static int ST_HUMAN_ACTION_PARAM_HAIR_MAX_SIZE = 410;
    /// \~chinese 头发分割羽化程度[0,1](默认值0.35),0 完全不羽化,1羽化程度最高,在strenth较小时,羽化程度基本不变.值越大,过度边缘部分越宽. \~english Set the hair segmentation feathering degree [0,1] (default value 0.35), 0 is no feathering at all, 1 is the highest feathering degree, when the strenth is small, the feathering degree is basically unchanged. The larger the value, the wider the excessive edge part
    public final static int ST_HUMAN_ACTION_PARAM_HAIR_BLUR_STRENGTH = 411;  // \~chinese 无效,可删除 \~english (deprecated)
    /// \~chinese 设置头发灰度图的方向是否需要旋转（0: 不旋转, 保持竖直; 1: 旋转, 方向和输入图片一致. 默认0不旋转) \~english Set whether the direction of the hair grayscale image needs to be rotated (0: no rotation, keep it vertical; 1: rotation, the direction is consistent with the input image. By default, rotation is disabled
    public final static int ST_HUMAN_ACTION_PARAM_HAIR_RESULT_ROTATE = 412;
    /// \~chinese 设置头发分割边界区域上限阈值.开启温度系数时设置无效 \~english Set the upper limit threshold of the hair segmentation boundary area. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_HAIR_SEGMENT_MAX_THRESHOLD = 414;
    /// \~chinese 设置头发分割边界区域下限阈值 开启温度系数时设置无效 \~english Set the lower limit threshold of the hair segmentation boundary area. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_HAIR_SEGMENT_MIN_THRESHOLD = 415;
    /// \~chinese 设置头发分割检测结果边缘模糊程度 取值范围0-1 视频版默认是0.5，图片版默认是1 \~english Set the edge blurring degree of the hair segmentation detection result. The value range is 0-1. The video mode defaults to 0.5, and the image mode defaults to 1
    public final static int ST_HUMAN_ACTION_PARAM_HAIR_SEGMENT_RESULT_BLUR = 416;
    /// \~chinese 设置头发分割检测结果边缘模糊程度需保证开启温度系数，大于0.5为开启，小于0.5为关闭，关闭状态下使用卡阈值模式得到边界，默认状态为开启 \~english Set whether to use the temperature coefficient for the hair segmentation result. The value should be greater than 0.5 to enable it and less than 0.5 to disable it. When disabled, min_threshold, max_threshold should be used for mask postprocess. By default, this parameter is enabled
    public final static int ST_HUMAN_ACTION_PARAM_HAIR_SEGMENT_USE_TEMPERATURE = 417;
    /// \~chinese 边缘移动参数，取值范围在[-2,2],默认值是1 \~english Set the edge shift parameter for the hair segmentation. The value should be in the range [-2, 2], and the default value is 1
    public final static int ST_HUMAN_ACTION_PARAM_HAIR_SEGMENT_EDGESHIFT=418;

    // \~chinese 多类分割参数 \~english Multi-class segmentation parameters
    /// \~chinese 输出的multisegment结果中长边的长度. \~english Set the length of the longer side for the multi-class segmentation result
    public final static int ST_HUMAN_ACTION_PARAM_MULTI_SEGMENT_MAX_SIZE = 420;
    /// \~chinese 设置多类分割检测结果灰度图的方向是否需要旋转（0: 不旋转, 保持竖直; 1: 旋转, 方向和输入图片一致. 默认不旋转) \~english Sets whether to rotate the grayscale multi-class segmentation result image. The options are 0 (do not rotate, keep vertical) and 1 (rotate, align with the input image). By default, rotation is disabled
    public final static int ST_HUMAN_ACTION_PARAM_MULTI_SEGMENT_RESULT_ROTATE = 421;



    // \~chinese 皮肤分割参数 \~english Skin segmentation parameters
    /// \~chinese 输出的皮肤分割结果中长边的长度. \~english Set the length of the longer side for the skin segmentation result
    public final static int ST_HUMAN_ACTION_PARAM_SKIN_SEGMENT_MAX_SIZE = 430;
    /// \~chinese 设置皮肤分割边界区域上限阈值.开启温度系数时设置无效 \~english Set the upper threshold for the boundary region of the skin segmentation result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_SKIN_SEGMENT_MAX_THRESHOLD = 431;
    /// \~chinese 设置皮肤分割边界区域下限阈值，开启温度系数时设置无效 \~english Set the lower threshold for the boundary region of the skin segmentation result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_SKIN_SEGMENT_MIN_THRESHOLD = 432;
    /// \~chinese 设置皮肤分割检测结果灰度图的方向是否需要旋转（0: 不旋转, 保持竖直; 1: 旋转, 方向和输入图片一致. 默认不旋转) \~english Set whether to rotate the grayscale skin segmentation result image. The options are 0 (do not rotate, keep vertical) and 1 (rotate, align with the input image). By default, rotation is disabled
    public final static int ST_HUMAN_ACTION_PARAM_SKIN_SEGMENT_RESULT_ROTATE = 433;
    /// \~chinese 设置皮肤分割检测结果边缘模糊程度 取值范围0-1 默认0.5 \~english Set the degree of edge blur for the skin segmentation result. The value should be in the range [0, 1], and the default value is 0.5
    public final static int ST_HUMAN_ACTION_PARAM_SKIN_SEGMENT_RESULT_BLUR = 434;
    /// \~chinese 设置皮肤分割检测结果边缘模糊程度需保证开启温度系数，大于0.5为开启，小于0.5为关闭，关闭状态下使用卡阈值模式得到边界，默认状态为开启 \~english Set whether to use the temperature coefficient for the skin segmentation result. The value should be greater than 0.5 to enable it and less than 0.5 to disable it. When disabled, min max threshould should be used for mask postprocess. By default, this parameter is enabled
    public final static int ST_HUMAN_ACTION_PARAM_SKIN_SEGMENT_USE_TEMPERATURE = 435;
    /// \~chinese 边缘移动参数，取值范围再[-2,2],默认值是1 \~english Set the edge shift parameter for the skin segmentation. The value should be in the range [-2, 2], and the default value is 1
    public final static int ST_HUMAN_ACTION_PARAM_SKIN_SEGMENT_EDGESHIFT=436;
    /// \~chinese 设置皮肤实例分割，皮肤实例分割依赖人脸检测，且只有加载皮肤实例模型设置该参数有效．默认不开启，只单个mask; 开启出多人多mask，数量跟人脸个数对应． \~english Set whether to enable skin instance segmentation. Skin instance segmentation relies on face detection, and this parameter is only effective when the skin instance model is loaded. By default, it is disabled, and only a single mask is generated. Enabling it will produce multiple masks corresponding to the number of detected faces
    public final static int ST_HUMAN_ACTION_PARAM_SKIN_SEGMENT_INSTANCE = 437;

// \~chinese 嘴唇分割 \~english Lip segmentation parameters
    /// \~chinese 设置嘴唇分割检测结果灰度图的方向是否需要旋转（0: 不旋转, 保持竖直; 1: 旋转, 方向和输入图片一致. 默认不旋转) \~english Set whether to rotate the grayscale lip segmentation result image. The options are 0 (do not rotate, keep vertical) and 1 (rotate, align with the input image). The default is not to rotate
public final static int ST_HUMAN_ACTION_PARAM_MOUTH_PARSE_RESULT_ROTATE = 450;
    /// \~chinese 设置是否使用嘴唇分割模型的点阈值，如果大于阈值，表示当前嘴无遮挡，不需要额外使用模型来分割，性能更优。 取值范围[0,1],默认值0.95 \~english Set whether to use threshold. If the value is greater than the threshold, it indicates that the lips are not occluded and there is no need to use the model for segmentation, which improves performance. The value range is [0, 1], with a default value of 0.95
    public final static int ST_HUMAN_ACTION_PARAM_MOUTH_PARSE_DETECT_THRESHOLD = 451;

    // \~chinese 面部遮挡分割参数 \~english Face occlusion segmentation parameters
    /// \~chinese 设置面部遮挡检测结果灰度图的方向是否需要旋转（0: 不旋转, 保持竖直; 1: 旋转, 方向和输入图片一致. 默认不旋转) \~english Set whether to rotate the grayscale face occlusion segmentation result image. The options are 0 (do not rotate, keep vertical) and 1 (rotate, align with the input image). By default, rotation is disabled
    public final static int ST_HUMAN_ACTION_PARAM_FACE_OCCLUSION_SEGMENT_RESULT_ROTATE = 460;
    /// \~chinese 设置面部遮挡分割边界区域上限阈值，开启温度系数时设置无效 \~english Set the upper threshold for the boundary region of the face occlusion segmentation result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_FACE_OCCLUSION_SEGMENT_MAX_THRESHOLD = 461;
    /// \~chinese 设置面部遮挡分割边界区域下限阈值，开启温度系数时设置无效 \~english Set the lower threshold for the boundary region of the face occlusion segmentation result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_FACE_OCCLUSION_SEGMENT_MIN_THRESHOLD = 462;
    /// \~chinese 面部遮挡分割后处理长边的长度[10,长边长度](默认长边240,短边=长边/原始图像长边*原始图像短边).值越大,面部遮挡分割后处理耗时越长,边缘部分效果越好. \~english Set the length of the longer side for post-processing after face occlusion segmentation. The value should be in the range [10, length of the longer side]. A larger value leads to longer processing time and better edge quality in the segmented region. By default, the longer side is set to 240
    public final static int ST_HUMAN_ACTION_PARAM_FACE_OCCLUSION_SEGMENT_MAX_SIZE = 463;
    /// \~chinese 边缘移动参数，取值范围再[-2,2],默认值是1 \~english Set the edge shift parameter for the face occlusion segmentation. The value should be in the range [-2, 2], and the default value is 1
    public final static int ST_HUMAN_ACTION_PARAM_FACE_OCCLUSION_SEGMENT_EDGESHIFT = 468;


    /// \~chinese 设置面部遮挡分割检测结果边缘模糊程度 默认参数0.5 \~english Set the degree of edge blur for the face occlusion segmentation result. The default value is 0.5
    public final static int ST_HUMAN_ACTION_PARAM_FACE_OCCLUSION_SEGMENT_RESULT_BLUR = 464;
    /// \~chinese 设置面部遮挡分割检测结果边缘模糊程度需保证开启温度系数，大于0.5为开启，小于0.5为关闭，关闭状态下使用卡阈值模式得到边界，默认状态为开启 \~english Set whether to use the temperature coefficient for the face occlusion segmentation result. The value should be greater than 0.5 to enable it and less than 0.5 to disable it. When disabled, min_threshold, max_threshold should be used for mask postprocess. By default, this parameter is enabled
    public final static int ST_HUMAN_ACTION_PARAM_FACE_OCCLUSION_SEGMENT_USE_TEMPERATURE = 467;
    /// \~chinese 设置人脸遮挡实例分割，人脸遮挡实例分割依赖人脸检测，默认不开启，出单个mask; 开启则多人多mask，数量跟人脸个数对应 \~english Set whether to enable face occlusion instance segmentation. Face occlusion instance segmentation relies on face detection. By default, it is disabled, and only a single mask is generated. When enabled, multi occlusion masks will be generated according to the number of detected faces
    public final static int ST_HUMAN_ACTION_PARAM_FACE_OCCLUSION_SEGMENT_INSTANCE = 469;

    // \~chinese 通用参数 \~english General parameters
    /// \~chinese 设置预处理图像大小 \~english Set the maximum size of the preprocessed image
    public final static int ST_HUMAN_ACTION_PARAM_PREPROCESS_MAX_SIZE = 500; //
    /// \~chinese 摄像头x方向上的视场角，单位为度，3d点会需要 \~english Set the field of view angle in the x-direction of the camera in degrees. This parameter is required for 3D point calculations
    public final static int ST_HUMAN_ACTION_PARAM_CAM_FOVX = 211;
    /// \~chinese pose3d结果是否需要保持人头朝上(1: detect输出的结果永远保持人头朝上;  0: detect输出的pos方向和输入图片一致) 默认人头朝上 \~english Set whether the pose3D result should keep the head upright. The options are 1 (the pose3D result always keeps the head upright) and 0 (the pose3D result follows the same direction as the input image). By default, the head is kept upright
    public final static int ST_HUMAN_ACTION_PARAM_POS3D_UP = 502;
    /// \~chinese 设置检测结果延迟帧数[0,2],检测接口输出结果为(当前-N)帧的结果, 默认0不延迟.  注意：在该参数修改过程中,当前检测结果会清空重新开始检测 \~english Set the number of frames to delay the detection results. The detection interface outputs results from the (current - N) frame. The default value is 0, which means no delay. Note that modifying this parameter will clear the current results and start the detection process again
    public final static int ST_HUMAN_ACTION_PARAM_DELAY_FRAME = 503;
    /// \~chinese 使用GPU做后处理 \~english Set whether to use the GPU for post-processing
    public final static int ST_HUMAN_ACTION_PARAM_SEGMENT_POST_PROCESS_USE_GPU=504;
    //  \~chinese 天空分割参数 \~english Sky segmentation parameters
    /// \~chinese 设置目标特征图像 目前已废弃 \~english (Deprecated) Set the target feature image for sky segmentation
    public final static int ST_HUMAN_ACTION_PARAM_SKY_TARGET_IMAGE= 509;
    /// \~chinese 输出的sky结果中长边的长度 \~english Set the length of the longer side for the output sky segmentation result
    public final static int ST_HUMAN_ACTION_PARAM_SKY_MAX_SIZE = 510;
    /// \~chinese 天空分割检测结果灰度图的方向是否需要旋转 \~english Set whether to rotate the grayscale sky segmentation result image. The options are 0 (do not rotate, keep vertical) and 1 (rotate, align with the input image)
    public final static int ST_HUMAN_ACTION_PARAM_SKY_RESULT_ROTATE = 511;
    /// \~chinese 设置天空分割边界区域上限阈值，开启温度系数时设置无效 \~english Set the upper threshold for the boundary region of the sky segmentation result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_SKY_SEGMENT_MAX_THRESHOLD = 512;
    /// \~chinese 设置天空分割边界区域下限阈值，开启温度系数时设置无效 \~english Set the lower threshold for the boundary region of the sky segmentation result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_SKY_SEGMENT_MIN_THRESHOLD = 513;
    /// \~chinese 设置天空分割检测结果边缘模糊程度 取值范围0-1，视频版默认参数0.5，图片版默认参数1 \~english Set the degree of edge blur for the sky segmentation result. The value should be in the range [0, 1]. For the video mode, the default value is 0.5, and for the image mode, the default value is 1
    public final static int ST_HUMAN_ACTION_PARAM_SKY_SEGMENT_RESULT_BLUR = 508;
    /// \~chinese 设置天空分割检测结果边缘模糊程度需保证开启温度系数，大于0.5为开启，小于0.5为关闭，关闭状态下使用卡阈值模式得到边界，默认状态为开启 \~english Set whether to use the temperature coefficient for the sky segmentation result. The value should be greater than 0.5 to enable it and less than 0.5 to disable it. When disabled, min max threshold should be used for mask postprocess. By default, this parameter is enabled
    public final static int ST_HUMAN_ACTION_PARAM_SKY_SEGMENT_USE_TEMPERATURE = 507;
    /// \~chinese 使用CPU进行refine操作,默认是使用（＞0.5），当输入参数小于等于0.5时不使用 \~english Set whether to use CPU for sky segmentation refinement. The default behavior is to use CPU when the input parameter is greater than 0.5 and not use CPU when it is less than or equal to 0.5
    public final static int ST_HUMAN_ACTION_PARAM_SKY_SEGMENT_REFINE_CPU_PROCESS = 514;
    /// \~chinese 边缘移动参数，取值范围再[-2,2],默认值是1 \~english Set the edge shift parameter for sky segmentation. The value should be in the range [-2, 2], and the default value is 1
    public final static int ST_HUMAN_ACTION_PARAM_SKY_SEGMENT_EDGESHIFT = 516;
    //  \~chinese 深度估计参数 \~english Depth estimation parameters
    /// \~chinese 输出的深度估计结果中长边的长度 \~english Set the length of the longer side for the output depth estimation result
    public final static int ST_HUMAN_ACTION_PARAM_DEPTH_ESTIMATION_MAX_SIZE = 515;

    // \~chinese 指甲检测 \~english Nail detection parameters
    /// \~chinese 设置检测到的最大目标数目N,有效范围为[1, 32], 返回的值可能比输入的值要小. 默认值为10. \~english Set the maximum number of detected nail targets. The valid range is [1, 32], but the returned value may be smaller than the input value. The default value is 10
    public final static int ST_HUMAN_ACTION_PARAM_NAIL_LIMIT = 602;
    /// \~chinese 设置指甲检测每多少帧进行一次 detect (默认指甲时30帧detect一次, 无指甲时10(30/3)帧detect一次). 值越大,cpu占用率越低, 但检测出新对象的时间越长. \~english Set the interval between nail detection in frames. When there are nails, the default interval is 30 frames, and when there are no nails, the default interval is 10 frames (30/3). A larger value reduces CPU usage but increases the time to detect new objects
    public final static int ST_HUMAN_ACTION_PARAM_NAIL_DETECT_INTERVAL = 603;
    /// \~chinese 设置指甲检测的阈值[0.0,1.0] \~english Set the threshold for nail detection. The value should be in the range [0.0, 1.0]
    public final static int ST_HUMAN_ACTION_PARAM_NAIL_THRESHOLD = 604;
    /// \~chinese 指甲平滑参数，取值范围[0,-) 默认参数是0.1 目前以废弃 \~english (Deprecated) Set the smooth parameter for nail detection. The valid range is [0, -). The default value is 0.1
    public final static int ST_HUMAN_ACTION_PARAM_NAIL_SMOOTH = 605;

    //	\~chinese 脚部参数 \~english Foot detection parameters
    /// \~chinese 设置检测到的最大脚的个数 \~english Set the maximum number of detected feet
    public final static int ST_HUMAN_ACTION_PARAM_FOOT_MAX_LIMIT = 700;
    /// \~chinese 设置脚部检测每多少帧进行一次 detect (默认有脚时30帧detect一次, 无脚时10(30/3)帧detect一次). 值越大,cpu占用率越低, 但检测出新对象的时间越长. \~english Set the interval between foot detections in frames. When feet are present, the default interval is 30 frames, and when no feet are present, the default interval is 10 frames (30/3). A larger value reduces CPU usage but increases the time to detect new objects
    public final static int ST_HUMAN_ACTION_PARAM_FOOT_DETECT_INTERVAL = 701;
    /// \~chinese 设置检测阈值[0.0,1.0] \~english Set the threshold for foot detection. The value should be in the range [0.0, 1.0]
    public final static int ST_HUMAN_ACTION_PARAM_FOOT_THRESHOLD = 702;
    /// \~chinese 设置裤腿分割检测结果边缘模糊程度，取值范围0-1，默认参数0.5 \~english Set the degree of edge blur for trouser leg segmentation results. The value should be in the range [0, 1] and the default parameter is 0.5
    public final static int ST_HUMAN_ACTION_PARAM_TROUSER_LEG_SEGMENT_BLUR = 750;
    /// \~chinese 设置分割检测结果边缘模糊程度需保证开启温度系数，大于0.5为开启，小于0.5为关闭，关闭状态下使用卡阈值模式得到边界，默认状态为开启 \~english Set whether to use the temperature coefficient for trouser leg segmentation results. The value should be greater than 0.5 to enable it and less than 0.5 to disable it. When disabled, min max threshold should be used for mask postprocess. By default, this parameter is enabled
    public final static int ST_HUMAN_ACTION_PARAM_TROUSER_LEG_SEGMENT_USE_TEMPERATURE = 751;
    /// \~chinese 边缘移动参数，取值范围再[-2,2],默认值是1 \~english Set the edge shift parameter for trouser leg segmentation. The value should be in the range [-2, 2], and the default value is 1
    public final static int ST_HUMAN_ACTION_PARAM_TROUSER_LEG_SEGMENT_EDGESHIFT = 451;

    // \~chinese 衣物分割 \~english Clothing segmentation parameters
    /// \~chinese 设置衣物分割检测结果灰度图的方向是否需要旋转（0: 不旋转, 保持竖直; 1: 旋转, 方向和输入图片一致. 默认不旋转) \~english Set whether to rotate the grayscale clothing segmentation result image. The options are 0 (do not rotate, keep vertical) and 1 (rotate, align with the input image). The default is not to rotate
    public final static int ST_HUMAN_ACTION_PARAM_CLOTH_SEGMENT_RESULT_ROTATE = 800;
    /// \~chinese 设置衣物分割长边的长度 \~english Set the length of the longer side for the output clothing segmentation result
    public final static int ST_HUMAN_ACTION_PARAM_CLOTH_SEGMENT_MAX_SIZE = 801;
    /// \~chinese 设置衣物分割检测结果边缘模糊程度 取值范围0-1，视频版默认参数0.5，图片版默认参数1 \~english Set the degree of edge blur for the clothing segmentation result. The value should be in the range [0, 1]. For the video mode, the default value is 0.5, and for the image mode, the default value is 1
    public final static int ST_HUMAN_ACTION_PARAM_CLOTH_SEGMENT_RESULT_BLUR = 802;
    /// \~chinese 设置衣物分割检测结果边缘模糊程度需保证开启温度系数，大于0.5为开启，小于0.5为关闭，关闭状态下使用卡阈值模式得到边界，默认状态为开启 \~english Set whether to use the temperature coefficient for the clothing segmentation result. The value should be greater than 0.5 to enable it and less than 0.5 to disable it. When disabled, min max threshold should be used for mask postprocess. By default, this parameter is enabled
    public final static int ST_HUMAN_ACTION_PARAM_CLOTH_SEGMENT_USE_TEMPERATURE = 803;
    /// \~chinese 设置衣物分割边界区域上限阈值，开启温度系数时设置无效 \~english Set the upper threshold for the boundary region of the clothing segmentation result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_CLOTH_SEGMENT_MAX_THRESHOLD = 804;
    /// \~chinese 设置衣物分割边界区域下限阈值，开启温度系数时设置无效 \~english Set the lower threshold for the boundary region of the clothing segmentation result. This setting is invalid when the temperature coefficient is enabled
    public final static int ST_HUMAN_ACTION_PARAM_CLOTH_SEGMENT_MIN_THRESHOLD = 805;
    /// \~chinese 边缘移动参数，取值范围再[-2,2],默认值是1 \~english Set the edge shift parameter for clothing segmentation. The value should be in the range [-2, 2], and the default value is 1
    public final static int ST_HUMAN_ACTION_PARAM_CLOTH_SEGMENT_EDGESHIFT = 806;

    // \~chinese 手腕检测 \~english Wrist detection parameters
    /// \~chinese 设置检测到的最大目标数目N,有效范围为[1, 32], 返回的值可能比输入的值要小. 默认值为10. \~english Set the maximum number of detected wrists. The valid range is [1, 32], and the returned value may be smaller than the input value. The default value is 10
    public final static int ST_HUMAN_ACTION_PARAM_WRIST_LIMIT = 900;
    /// \~chinese 设置手腕检测每多少帧进行一次 detect (默认30帧detect一次, 无手腕10(30/3)帧detect一次). 值越大,cpu占用率越低, 但检测出新对象的时间越长. \~english Set the interval between wrist detections in frames. The default interval is 30 frames for wrist detection and 10 frames (30/3) for no wrist detection. A larger value reduces CPU usage but increases the time to detect new objects
    public final static int ST_HUMAN_ACTION_PARAM_WRIST_DETECT_INTERVAL = 901;
    /// \~chinese 设置手腕检测的roi参数，默认全0，设置检测roi区域，需enable roi需调用st_mobile_human_action_set_roi，默认全0，不开启 \~english Set the ROI (Region of Interest) for wrist detection. This parameter enables setting a specific region for detection. To enable ROI, you need to call st_mobile_human_action_set_roi and provide the ROI parameters. The default value is all zeros, indicating no ROI
    public final static int ST_HUMAN_ACTION_PARAM_WRIST_ROI=906;
    /// \~chinese 开启手腕检测roi设置，默认不打开 \~english Enable wrist detection ROI. By default, this parameter is disabled
    public final static int ST_HUMAN_ACTION_PARAM_WRIST_ENABLE_ROI=907;
    // /// \~chinese 打开roi设置之后，resize的ratio比例，(0,1] \~english Set the resize ratio in the range (0, 1] when ROI is enabled
    //ST_HUMAN_ACTION_PARAM_WRIST_ROI_RATIO=909,
    /// \~chinese 手表佩戴位置相比手腕宽度的比例 ,默认值是1.0 \~english Set the ratio between the position of the watch and the wrist width. The default value is 1.0
    public final static int ST_HUMAN_ACTION_PARAM_WRIST_WRIST_RATIO=911;
    /// \~chinese 手表渲染窗口的roi位置,单位是像素,调用st_mobile_human_action_set_roi进行设置，默认是原始图像，无需提前设置enable roi \~english Sets the ROI for the wrist rendering window in pixels. This parameter is used to specify a specific region for rendering. By default, the ROI is set to the original image, and there is no need to set the ROI in advance
    public final static int ST_HUMAN_ACTION_PARAM_WRIST_RENDER_ROI = 914;
    /// \~chinese 手表渲染窗口的尺度变化,(0，-] \~english Set the scale factor for the wrist rendering window. The value should be in the range (0, -)
    public final static int ST_HUMAN_ACTION_PARAM_WRIST_RENDER_ROI_SCALE = 915;
    /// \~chinese 设置绿幕分割颜色RGB值,默认为绿色,将颜色值（16进制数0xFFFFFF,按R、G、B排列）按float类型传入 \~english Set the RGB color value for green screen segmentation. The default color is green (hexadecimal value 0xFFFFFF, arranged as R, G, B). The color value should be passed as a float type
    public final static int ST_HUMAN_ACTION_PARAM_GREEN_SEGMENT_COLOR = 1000;
    // \~chinese 相似度，范围220-140， 默认140，调大可以小相似度 \~english Similarity, ranging from 220 to 140. The default value is 140. Increasing it can decrease the similarity threshold
    public final static int ST_HUMAN_ACTION_PARAM_GREEN_SEGMENT_SIMILAR = 1002;
    /// \~chinese 设置绿幕分割使用CPU处理，默认为GPU \~english Set whether to use CPU processing for green screen segmentation. The default is to use GPU processing
    public final static int ST_HUMAN_ACTION_PARAM_GREEN_SEGMENT_POST_PROCESS_USE_CPU = 1001;
    /// \~chinese 平滑度,建议范围 1-3, 默认3, 越大越平滑 \~english Smoothness, recommended range 1-3, default 3. The higher the value, the smoother the result
    public final static int ST_HUMAN_ACTION_PARAM_GREEN_SEGMENT_SMOOTH = 1003;
    // 平滑度二,范围0-10， 默认1，越大越平滑， 对边缘也有影响,若边缘出现黑边异色可以将该值升高，若边缘出现内蚀可以该值适当降低
    public final static int ST_HUMAN_ACTION_PARAM_GREEN_SEGMENT_SMOOTH2 = 1004;
    /// \~chinese 边界效果,范围10-60， 默认60，效果为若边缘出现黑边、异色，可以将该值降低，若边缘出现内蚀，可以适当增大 \~english Range 10-60, default 60. This parameter affects the appearance of the edges. If there are black borders or color artifacts along the edges, you can decrease this value. If the edges appear eroded, you can increase it slightly
    public final static int ST_HUMAN_ACTION_PARAM_GREEN_SEGMENT_EDGE = 1005;

    public class STMeshType{
        public static final int ST_MOBILE_FACE_MESH = 1;  ///< \~Chinese face mesh 类型 \~english  face mesh type
        public static final int ST_MOBILE_HEAD_MESH = 2;   ///<\~Chinese  360度mesh 类型 \~english  head mesh type
    }
}
