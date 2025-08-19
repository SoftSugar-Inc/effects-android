package com.softsugar.stmobile.params;

public class STEffectParam {
    // \~Chinese 贴纸前后两个序列帧切换的最小时间间隔，单位为毫秒。当两个相机帧处理的间隔小于这个值的时候， \~English Effects param min frame interval
    public final static int EFFECT_PARAM_MIN_FRAME_INTERVAL = 0;

    // \~Chinese 设置贴纸素材资源所占用的最大内存（MB），当估算内存超过这个值时，将不能再加载新的素材包 \~English Effects param max memory budget frame
    public final static int EFFECT_PARAM_MAX_MEMORY_BUDGET_MB = 1;

    // \~Chinese 设置相机姿态平滑参数，表示平滑多少帧, 越大延迟越高，抖动越微弱 \~English Effects param quaternion smooth frame
    public final static int EFFECT_PARAM_QUATERNION_SMOOTH_FRAME = 2;

    // \~Chinese 设置贴纸是否使用外部时间戳更新 \~English Effects param use input timestamp
    public final static int EFFECT_PARAM_USE_INPUT_TIMESTAMP = 3;

    // \~Chinese 倾向于空间换时间，传0的话，则尽可能清理内部缓存，保持内存最小。目前主要影响3D共享资源 \~English Effects param prefer memory cache
    public final static int EFFECT_PARAM_PREFER_MEMORY_CACHE = 4;

    // \~Chinese 传入大于0的值，禁用美颜Overlap逻辑（贴纸中的美颜会覆盖前面通过API或者贴纸生效的美颜效果，贴纸成组覆盖，API单个覆盖），默认启用Overlap \~English Effects param disable beauty overlap
    public final static int EFFECT_PARAM_DISABLE_BEAUTY_OVERLAP = 5;

    //\~Chinese  传入大于0的值，禁用对于v3.1之前的素材包重新排序module的渲染顺序，该选项只会影响设置之后添加的素材。重新排序是为了在与美妆、风格素材包叠加时达到最佳效果，默认启用ReOrder \~English Effects param disable module reorder
    public final static int EFFECT_PARAM_DISABLE_MODULE_REORDER = 6;

    // \~Chinese 3DPose计算方案，传入0使用106旧模型方案，传1使用基于282模型优化的Pose方案，默认值为1 \~English Effects param 3d pose solution
    public final static int EFFECT_PARAM_3D_POSE_SOLUTION = 7;

    // \~Chinese 设置未来帧帧数，默认值是0, 需要是大于等于0的值，0表示不开未来帧 \~English Effects param render delay frame
    public final static int EFFECT_PARAM_RENDER_DELAY_FRAME = 8;

    // \~Chinese 设置去绿程度，0表示不去绿，1表示最大程度去绿，默认值为1
    public final static int EFFECT_PARAM_GREEN_COLOR_BALANCE = 9;

    public final static int EFFECT_PARAM_GREEN_SPILL_BY_ALPHA = 10;   ///< \~chinese 设置去绿色彩平衡, 和去绿程度配套使用, 平衡因去绿导致的主体颜色变化, 范围[0.0, 1.0], 默认0.5(不进行平衡) \~english Set green removal color balance, used in conjunction with green removal intensity to balance the color change caused by green removal. Range [0.0, 1.0], default 0.5 (no balance).

    // \~chinese 微整形效果遮挡，目前支持白牙、亮眼，默认值为0, 表示没有效果遮挡，1表示开启效果遮挡 \~english MicroPlastic effect occlusion, and currently supports teeth whitening and eye brightening. default value is 0, means no effect occlusion, 1 means open effect occlusion.
    public final static int EFFECT_PARAM_PLASTIC_FACE_OCCLUSION = 11;

}
