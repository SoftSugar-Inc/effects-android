package com.softsugar.stmobile.params;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class STEffectBeautyType {

    // \~Chinese 背景虚化
    public static final int BOKEH1_MODE = 0;//\~Chinese  虚化1
    public static final int BOKEH2_MODE = 1;// \~Chinese 虚化2

    // \~Chinese 美白
    public static final int WHITENING0_MODE = 101;  // 对应旧版6.11版本美白
    public static final int WHITENING1_MODE = 0;
    public static final int WHITENING2_MODE = 1;
    public static final int WHITENING3_MODE = 2;

    // \~Chinese 基础美颜 base
    public static final int EFFECT_BEAUTY_BASE_WHITEN                       = 101;  // \~Chinese 美白
    public static final int EFFECT_BEAUTY_BASE_REDDEN                       = 102;  // \~Chinese 红润
    public static final int EFFECT_BEAUTY_BASE_FACE_SMOOTH                  = 103;  // \~Chinese 磨皮
    public static final int EFFECT_BEAUTY_BASE_SKIN_SMOOTH                  = 104;  // \~Chinese 身体皮肤祛皱, [0,1.0],默认值0.0, 0.0不做身体皮肤祛皱


    // \~Chinese 美形 reshape
    public static final int EFFECT_BEAUTY_RESHAPE_SHRINK_FACE               = 201;  // \~Chinese 瘦脸
    public static final int EFFECT_BEAUTY_RESHAPE_ENLARGE_EYE               = 202;  // \~Chinese 大眼
    public static final int EFFECT_BEAUTY_RESHAPE_SHRINK_JAW                = 203;  // \~Chinese 小脸
    public static final int EFFECT_BEAUTY_RESHAPE_NARROW_FACE               = 204;  // \~Chinese 窄脸
    public static final int EFFECT_BEAUTY_RESHAPE_ROUND_EYE                 = 205;  // \~Chinese 圆眼

    // \~Chinese 微整形 plastic
    public static final int EFFECT_BEAUTY_PLASTIC_THINNER_HEAD              = 301;  // \~Chinese 小头
    public static final int EFFECT_BEAUTY_PLASTIC_THIN_FACE                 = 302;  // \~Chinese 瘦脸型
    public static final int EFFECT_BEAUTY_PLASTIC_CHIN_LENGTH               = 303;  // \~Chinese 下巴
    public static final int EFFECT_BEAUTY_PLASTIC_HAIRLINE_HEIGHT           = 304;  // \~Chinese 额头
    public static final int EFFECT_BEAUTY_PLASTIC_APPLE_MUSLE               = 305;  // \~Chinese 苹果肌
    public static final int EFFECT_BEAUTY_PLASTIC_NARROW_NOSE               = 306;  // \~Chinese 瘦鼻翼
    public static final int EFFECT_BEAUTY_PLASTIC_NOSE_LENGTH               = 307;  // \~Chinese 长鼻
    public static final int EFFECT_BEAUTY_PLASTIC_PROFILE_RHINOPLASTY       = 308;  // \~Chinese 侧脸隆鼻
    public static final int EFFECT_BEAUTY_PLASTIC_MOUTH_SIZE                = 309;  // \~Chinese 嘴型
    public static final int EFFECT_BEAUTY_PLASTIC_PHILTRUM_LENGTH           = 310;  // \~Chinese 缩人中
    public static final int EFFECT_BEAUTY_PLASTIC_EYE_DISTANCE              = 311;  // \~Chinese 眼距
    public static final int EFFECT_BEAUTY_PLASTIC_EYE_ANGLE                 = 312;  // \~Chinese 眼睛角度
    public static final int EFFECT_BEAUTY_PLASTIC_OPEN_CANTHUS              = 313;  // \~Chinese 开眼角
    public static final int EFFECT_BEAUTY_PLASTIC_BRIGHT_EYE                = 314;  // \~Chinese 亮眼
    public static final int EFFECT_BEAUTY_PLASTIC_REMOVE_DARK_CIRCLES       = 315;  // \~Chinese 祛黑眼圈
    public static final int EFFECT_BEAUTY_PLASTIC_REMOVE_NASOLABIAL_FOLDS   = 316;  // \~Chinese 祛法令纹
    public static final int EFFECT_BEAUTY_PLASTIC_WHITE_TEETH               = 317;  // \~Chinese 白牙
    public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_CHEEKBONE          = 318;  // \~Chinese 瘦颧骨
    public static final int EFFECT_BEAUTY_PLASTIC_OPEN_EXTERNAL_CANTHUS     = 319;  // \~Chinese 开外眼角比例
    public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_JAWBONE            = 320;  ///< \~Chinese 瘦下颔，【0, 1.0], 默认值0.0， 0.0不做瘦下颔
    public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_ROUND_FACE         = 321;  ///< \~Chinese 圆脸瘦脸，【0, 1.0], 默认值0.0， 0.0不做瘦脸
    public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_LONG_FACE          = 322;  ///< \~Chinese 长脸瘦脸，【0, 1.0], 默认值0.0， 0.0不做瘦脸
    public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_GODDESS_FACE       = 323;  ///< \~Chinese 女神瘦脸，【0, 1.0], 默认值0.0， 0.0不做瘦脸
    public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_NATURAL_FACE       = 324;  ///< \~Chinese 自然瘦脸，【0, 1.0], 默认值0.0， 0.0不做瘦脸
    public static final int EFFECT_BEAUTY_PLASTIC_SHRINK_WHOLE_HEAD         = 325;  ///\~Chinese 整体缩放小头
    public static final int EFFECT_BEAUTY_PLASTIC_EYE_HEIGHT                = 326;  ///< 眼睛位置比例，[-1, 1]，默认值0.0, [-1, 0]向下移动眼睛，[0, 1]向上移动眼睛
    public static final int EFFECT_BEAUTY_PLASTIC_MOUTH_CORNER              = 327;  ///< 嘴角上移比例，[0, 1.0]，默认值0.0, 0.0不做嘴角调整
    public static final int EFFECT_BEAUTY_PLASTIC_HAIRLINE                  = 328;  ///< 新发际线高低比例，[-1, 1]，默认值0.0, [-1, 0]为低发际线，[0, 1]为高发际线
    public static final int EFFECT_BEAUTY_PLASTIC_FULLER_LIPS               = 329;  ///< \~chinese 丰唇，[-1, 1]，默认值0.0，[-1, 0]为嘴唇变薄，[0, 1]为丰唇 \~english Lip fullness adjustment, [-1, 1], default value is 0.0; [-1, 0] reduces lip fullness, [0, 1] increases lip fullness
    public static final int EFFECT_BEAUTY_PLASTIC_MOUTH_WIDTH               = 330;  ///< \~chinese 嘴巴宽度，[-1, 1]，默认值0.0，[-1, 0]为嘴巴变宽，[0, 1]为嘴巴变窄 \~english Mouth width adjustment, [-1, 1], default value is 0.0; [-1, 0] increases width, [0, 1] decreases width
    public static final int EFFECT_BEAUTY_PLASTIC_BROW_HEIGHT               = 331;  ///< \~chinese 眉毛高度，[-1, 1]，默认值0.0，[-1, 0]为眉毛上移，[0, 1]为眉毛下移 \~english Eyebrow height adjustment, [-1, 1], default value is 0.0; [-1, 0] raises brows, [0, 1] lowers brows
    public static final int EFFECT_BEAUTY_PLASTIC_BROW_THICKNESS            = 332;  ///< \~chinese 眉毛粗细，[-1, 1]，默认值0.0，[-1, 0]为眉毛变细，[0, 1]为眉毛增粗 \~english Eyebrow thickness adjustment, [-1, 1], default value is 0.0; [-1, 0] makes brows thinner, [0, 1] makes brows thicker
    public static final int EFFECT_BEAUTY_PLASTIC_BROW_DISTANCE             = 333;  ///< \~chinese 眉毛间距，[-1, 1]，默认值0.0，[-1, 0]为眉毛间距变大，[0, 1]为眉毛间距变小 \~english Eyebrow distance adjustment, [-1, 1], default value is 0.0; [-1, 0] increases distance between brows, [0, 1] decreases it
    public static final int EFFECT_BEAUTY_PLASTIC_FACE_V_SHAPE              = 334;  ///< \~chinese V脸，从下颌角到下巴的V脸效果，[0, 1.0]，默认值0.0, 0.0不做V脸 \~english V-shaped face effect (jawline to chin), [0, 1.0], default value is 0.0; 0.0 applies no V-shape effect
    public static final int EFFECT_BEAUTY_PLASTIC_FACE_FULL_V_SHAPE         = 335;  ///< \~chinese V脸，整脸的V脸效果，[0, 1.0]，默认值0.0, 0.0不做V脸 \~english Full V-shaped face effect, [0, 1.0], default value is 0.0; 0.0 applies no V-shape effect
    public static final int EFFECT_BEAUTY_PLASTIC_NOSE_TIP                  = 336;  ///< \~chinese 瘦鼻头，[0, 1.0]，默认值0.0, 0.0不做瘦鼻头 \~english Nose tip refinement, [0, 1.0], default value is 0.0; 0.0 applies no nose tip refinement
    public static final int EFFECT_BEAUTY_PLASTIC_NOSE_BRIDGE               = 337;  ///< \~chinese 鼻梁调整，[0, 1.0]，默认值0.0, 0.0不做鼻梁调整 \~english Nose bridge adjustment, [0, 1.0], default value is 0.0; 0.0 applies no nose bridge adjustment
    public static final int EFFECT_BEAUTY_PLASTIC_ENLARGE_PUPIL             = 338;  ///< \~chinese 放大瞳孔，[0, 1.0]，默认值0.0, 0.0不做瞳孔放大 \~english Pupil enlargement, [0, 1.0], default value is 0.0; 0.0 applies no pupil enlargement

    // \~Chinese 调整 tone
    public static final int EFFECT_BEAUTY_TONE_CONTRAST                     = 601;  // \~Chinese 对比度
    public static final int EFFECT_BEAUTY_TONE_SATURATION                   = 602;  // \~Chinese 饱和度
    public static final int EFFECT_BEAUTY_TONE_SHARPEN                      = 603;  // \~Chinese 锐化
    public static final int EFFECT_BEAUTY_TONE_CLEAR                        = 604;  // \~Chinese 清晰度
    public static final int EFFECT_BEAUTY_DENOISING                         = 606;  // \~Chinese 去噪
    public static final int EFFECT_BEAUTY_TONE_COLOR_TONE                   = 607;  // \~Chinese 色调，[-1.0, 1.0], 默认值0.0, 0.0不做色调处理
    public static final int EFFECT_BEAUTY_TONE_COLOR_TEMPERATURE            = 608;  // \~Chinese 色温，[-1.0, 1.0], 默认值0.0, 0.0不做色温处理

    // \~Chinese 美妆 makeup
    public static final int EFFECT_BEAUTY_HAIR_DYE                          = 401;  // \~Chinese 染发
    public static final int EFFECT_BEAUTY_MAKEUP_LIP                        = 402;  // \~Chinese 口红
    public static final int EFFECT_BEAUTY_MAKEUP_CHEEK                      = 403;  // \~Chinese 腮红
    public static final int EFFECT_BEAUTY_MAKEUP_NOSE                       = 404;  // \~Chinese 修容
    public static final int EFFECT_BEAUTY_MAKEUP_EYE_BROW                   = 405;  // \~Chinese 眉毛
    public static final int EFFECT_BEAUTY_MAKEUP_EYE_SHADOW                 = 406;  // \~Chinese 眼影
    public static final int EFFECT_BEAUTY_MAKEUP_EYE_LINE                   = 407;  // \~Chinese 眼线
    public static final int EFFECT_BEAUTY_MAKEUP_EYE_LASH                   = 408;  // \~Chinese 眼睫毛
    public static final int EFFECT_BEAUTY_MAKEUP_EYE_BALL                   = 409;  // \~Chinese 美瞳
    public static final int EFFECT_BEAUTY_MAKEUP_ALL                        = 410;  // \~Chinese 整妆

    public static final int EFFECT_BEAUTY_TONE_BOKEH                        = 605;  // \~Chinese 背景虚化

    // \~Chinese 滤镜 filter
    public static final int EFFECT_BEAUTY_FILTER                            = 501;  // \~Chinese 滤镜

    // \~Chinese 试妆 tryon ///< 参数通过 st_effect_tryon_info_t 结构体中对应的参数来设置
    public static final int EFFECT_BEAUTY_TRYON_HAIR_COLOR                  = 701;  ///< \~Chinese 染发，可设置的参数包括：颜色，强度，明暗度，高光
    public static final int EFFECT_BEAUTY_TRYON_LIPSTICK                    = 702;  ///< \~Chinese 口红，可设置的参数包括：颜色，强度，高光(特定材质：水润、闪烁、金属)，质地类型
    public static final int EFFECT_BEAUTY_TRYON_LIPLINE                     = 703;  ///< \~Chinese 唇线，可设置的参数包括：颜色，强度，唇线线宽
    public static final int EFFECT_BEAUTY_TRYON_BLUSH                       = 704;  ///< \~Chinese 腮红，可设置的参数包括：颜色，强度
    public static final int EFFECT_BEAUTY_TRYON_BROW                        = 705;  ///< \~Chinese 眉毛，可设置的参数包括：颜色，强度
    public static final int EFFECT_BEAUTY_TRYON_FOUNDATION                  = 706;  ///< \~Chinese 粉底，可设置的参数包括：颜色，强度
    public static final int EFFECT_BEAUTY_TRYON_CONTOUR                     = 707;  ///< \~Chinese 修容，可设置的参数包括：强度（整体），区域信息（区域id，颜色，强度）
    public static final int EFFECT_BEAUTY_TRYON_EYESHADOW                   = 708;  ///< \~Chinese 眼影，可设置的参数包括：强度（整体），区域信息（区域id，颜色，强度）
    public static final int EFFECT_BEAUTY_TRYON_EYELINER                    = 709;  ///< \~Chinese 眼线，可设置的参数包括：强度（整体），区域信息（区域id，颜色，强度）
    public static final int EFFECT_BEAUTY_TRYON_EYELASH                     = 710;  ///< \~Chinese 眼睫毛，可设置的参数包括：颜色，强度
    public static final int EFFECT_BEAUTY_TRYON_STAMPLINER                  = 711;  ///< \~Chinese 眼印，可设置的参数包括：颜色，强度

    // \~Chinese 3D 微整形
    public static final int EFFECT_BEAUTY_3D_MICRO_PLASTIC                  = 801;  // \~Chinese 3D 微整形

}




