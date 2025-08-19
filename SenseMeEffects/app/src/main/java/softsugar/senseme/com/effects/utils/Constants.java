package softsugar.senseme.com.effects.utils;

public class Constants {

    public static final boolean ENABLE_25FPS = false;

    // 属性识别
    public static final String MODEL_NAME_FACE_ATTRIBUTE = "models/M_SenseME_Attribute_p_1.2.8.1.model";// 性别识别

    // uid
    public static final String UID_TITLE_3D_PLASTIC = "base_3d_plastic";// 一级菜单，3D微整形

    public static final String UID_GAN_SKIN = "gan_skin";  // gan 磨皮

    // 进入Activity后模式
    public static final String MODE_IMG = "mode_img";
    public static final String MODE_VIDEO = "mode_video";
    public static final String MODE_CAMERA = "mode_camera";

    // 平均耗时
    public static final int MAX_RUN_COUNT = 200;

    public static String APP_ID = "e22024c218fd48638ca9d85514c36e66";
    public static String APP_KEY = "c930320e4806494a817e5bb80278defc";

    public static final String SP_FIRST = "sp_first";
    public static final String SP_FIRST_IDENTIFY = "sp_first_identify_gender";// 第一次识别到的性别

    // 滤镜本地素材文件夹
    public static final String ASSET_FILTER_TEXTURE = "filter_texture";        // 滤镜-质感
    public static final String ASSET_FILTER_FILM = "filter_film";              // 滤镜-胶片
    public static final String ASSET_FILTER_VINTAGE = "filter_vintage";                // 滤镜-复古
    public static final String ASSET_FILTER_FOOD = "filter_food";              // 滤镜-食物
    public static final String ASSET_FILTER_PORTRAIT = "filter_portrait";      // 滤镜-人像
    public static final String ASSET_FILTER_SCENERY = "filter_scenery";        // 滤镜-风景
    public static final String ASSET_FILTER_STILL_LIFE = "filter_still_life";  // 滤镜-静物

    // 美妆和风格妆本地素材文件夹
    public static final String ASSET_MAKEUP_EYESHADOW = "makeup_eyeshadow"; // 眼影
    public static final String ASSET_MAKEUP_BROW = "makeup_brow";           // 眉毛
    public static final String ASSET_MAKEUP_BLUSH = "makeup_blush";         // 腮红
    public static final String ASSET_MAKEUP_HIGHLIGHT = "makeup_highlight"; // 修容
    public static final String ASSET_MAKEUP_LIP = "makeup_lip";             // 口红
    public static final String ASSET_MAKEUP_EYELINER = "makeup_eyeliner";   // 眼线
    public static final String ASSET_MAKEUP_EYELASH = "makeup_eyelash";     // 睫毛
    public static final String ASSET_MAKEUP_EYEBALL = "makeup_eyeball";     // 美瞳
    public static final String ASSET_MAKEUP_HAIRDYE = "makeup_hairdye";     // 染发
    public static final String ASSET_STYLE_NATURE = "style_nature";         // 风格妆-自然
    public static final String ASSET_STYLE_LIGHTLY = "style_lightly";       // 风格妆-轻妆
    public static final String ASSET_STYLE_FASHION = "style_fashion";       // 风格妆-流行

    // 风格妆默认
    public static final float DEF_STYLE_STRENGTH = 0.8f;     // 默认
    public static final float DEF_STYLE_BOY_STRENGTH = 0.5f; // 男
    public static final float DEF_STYLE_GIRL_STRENGTH = 0.8f;// 女

    public static final boolean CFG_K_USECAMERA2 = true;
    public static final boolean CFG_K_LOG_SWITCH = true;

    public static final String ASSET_PATH_HTML = "html";
    public static final String ASSET_PATH_TERMS = "file:///android_asset/html/SenseME_Provisions_v1.0.html";

    // 美白2对应的素材
    public static final String WHITENING_PATH = "whiten_gif.zip";

    public static final String STICKER_SYNC = "stickerSync";
    public static final String STICKER_LOCAL = "newEngine";// 本地贴纸文件夹

    // TryOn本地素材包
    public static final String TRY_ON_ASSET_PATH = "tryOn";

    public static final String TRY_ON_LIP = TRY_ON_ASSET_PATH + "/lip";//口红
    public static final String TRY_ON_HAIR = TRY_ON_ASSET_PATH + "/hair";//染发
    public static final String TRY_ON_LIP_LINE = TRY_ON_ASSET_PATH + "/lipline";// 唇线
    public static final String TRY_ON_EYE_SHADOW = TRY_ON_ASSET_PATH + "/eyeShadow";//眼影
    public static final String TRY_ON_EYELINER = TRY_ON_ASSET_PATH + "/eyeliner";//眼线

    public static final String TRY_ON_EYE_PRINT = TRY_ON_ASSET_PATH + "/eyePrint";//眼印(stampliner.zip)
    public static final String TRY_ON_EYE_LASH = TRY_ON_ASSET_PATH + "/eyeLash";//眼睫毛
    public static final String TRY_ON_EYE_BROW = TRY_ON_ASSET_PATH + "/eyeBrow";//眉毛
    public static final String TRY_ON_BLUSH = TRY_ON_ASSET_PATH + "/blush";//腮红
    public static final String TRY_ON_TRIMMING = TRY_ON_ASSET_PATH + "/trimming";//修容(contour)

    public static final String TRY_ON_FOUNDATION = TRY_ON_ASSET_PATH + "/foundation";//粉底

    // 换背景 固定素材ID
    public static final int CHOOSE_BG_STICKER_ID = 1030;
    public static final int CHOOSE_GREEN_ID = 2935;       // 绿幕
    public static final int CHOOSE_REED_ID = 2936;        // 红幕
    public static final int CHOOSE_BLUE_ID = 2937;        // 蓝幕
    public static final int CHOOSE_GREEN_VIDEO_ID = 3121; // 绿幕视频

    public static final int ATY_TYPE_CAMERA = 0;
    public static final int ATY_TYPE_TRY_ON = 1;
    public static final int ATY_TYPE_VIDEO = 2;
    public static final int ATY_TYPE_IMAGE = 3;
    public static final int ATY_TYPE_IMAGE_TRY_ON = 3;

    // 重启恢复效果用，标识
    public static final int SP_WHITEN_1 = 0;
    public static final int SP_WHITEN_2 = 1;
    public static final int SP_WHITEN_3 = 2;
    public static final int SP_WHITEN_4 = 3;

    // 基础美颜
    public static final float[] mNewBeautifyParamsTypeBase = {
            0.00f,  // 1.美白1
            0.00f,  // 2.美白2
            0.00f,  // 3.美白3
            0.00f,  // 4.红润
            0.00f,  // 5.磨皮1
            0.50f   // 6.磨皮2
    };
    // 美形
    public static final float[] mNewBeautifyParamsTypeProfessional = {
            0.34f,  // 1.瘦脸
            0.29f,  // 2.大眼
            0.10f,  // 3.小脸
            0.25f,  // 4.窄脸
            0.07f   // 5.圆眼
    };
    // 微整形
    public static final float[] mNewBeautifyParamsTypeMicro = {
            0.00f,  // 1.小头
            0.45f,  // 2.瘦脸型
            0.20f,  // 3.下巴
            0.00f,  // 4.额头
            0.30f,  // 5.苹果肌
            0.21f,   // 6.瘦鼻翼
            0.00f,  // 7.长鼻
            0.10f,  // 8.侧脸隆鼻
            0.51f,  // 9.嘴型
            0.00f,  // 10.缩人中
            -0.23f, // 11.眼距
            0.00f,  // 12.眼睛角度
            0.00f,  // 13.开眼角
            0.25f,  // 14.亮眼
            0.69f,  // 15.祛黑眼圈
            0.60f,  // 16.祛法令纹
            0.20f,  // 17.白牙
            0.36f,  // 18.瘦颧骨
            0.00f   // 19.开外眼角
    };
    // 调整
    public static final float[] mNewBeautifyParamsTypeAdjust = {
            0.00f,  // 1.对比度
            0.00f,  // 2.饱和度
            0.50f,  // 3.清晰度
            0.20f   // 4.锐化
    };

    public static boolean ACTIVITY_MODE_LANDSCAPE = false;
    public static boolean ACTIVITY_MODE_FOR_TV = false;

    public static final int MAKEUP_TYPE_COUNT = 11;

    public static final int ANDROID_MIN_SDK_VERSION = 21; // 5.0
    public static final int ANDROID_MIN_HARDWAREBUFFER_VERSION = 26;

    public static final float MAKEUP_HAIRDYE_STRENGTH_RATIO = 0.22f;

}
