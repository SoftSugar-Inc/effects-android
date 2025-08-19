package softsugar.senseme.com.effects.view.widget

import com.blankj.utilcode.util.LogUtils
import com.softsugar.stmobile.params.STEffectBeautyType.*
import com.softsugar.stmobile.params.STFaceShape
import softsugar.senseme.com.effects.utils.Constants
import softsugar.senseme.com.effects.utils.ContextHolder
import java.util.*

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/23/21 12:52 PM
 */
enum class EffectType() {
    NATURAL_FACE(1, "自然脸"),
    ROUND_FACE(2, "圆脸"),
    SQUARE_FACE(3, "方脸"),
    LONG_FACE(4, "长脸"),
    CHF_FACE(5, "长方脸"),

    GROUP_EFFECT("基础美颜"),
    GROUP_STYLE("风格"),

    // TryOn
    TYPE_TRY_ON_BASIC("Tryon美颜"),
    TYPE_TRY_ON_TRY_ON("Tryon试妆"),
    TYPE_TRY_ON_NAIL("Tryon美甲", "114"),
    TYPE_TRY_ON_SHOES("Tryon试鞋", "73"),
    TYPE_TRY_ON_WATCH("Tryon试表", "74"),
    TYPE_TRY_ON_BRACELET("Tryon手链", "75"),
    TYPE_TRY_ON_EARRING("Tryon耳环", "76"),
    TYPE_TRY_ON_GLASS("Tryon眼镜", "77"),
    TYPE_TRY_ON_CAP("Tryon帽子", "78"),

    TYPE_TRY_ON_LIP(EFFECT_BEAUTY_TRYON_LIPSTICK, "TryOn口红", "29", Constants.TRY_ON_LIP),
    TYPE_TRY_ON_HAIR(EFFECT_BEAUTY_TRYON_HAIR_COLOR, "TryOn染发", "30", Constants.TRY_ON_HAIR),
    TYPE_TRY_ON_CHUNXIAN(EFFECT_BEAUTY_TRYON_LIPLINE, "TryOn唇线", "31", Constants.TRY_ON_LIP_LINE),
    TYPE_TRY_ON_YANYING(EFFECT_BEAUTY_TRYON_EYESHADOW, "TryOn眼影", "32", Constants.TRY_ON_EYE_SHADOW),
    TYPE_TRY_ON_YANXIAN(EFFECT_BEAUTY_TRYON_EYELINER, "TryOn眼线", "33", Constants.TRY_ON_EYELINER),

    TYPE_TRY_ON_YANYIN(EFFECT_BEAUTY_TRYON_STAMPLINER, "TryOn眼印", "34", Constants.TRY_ON_EYE_PRINT),
    TYPE_TRY_ON_YANJIEMAO(EFFECT_BEAUTY_TRYON_EYELASH, "TryOn眼睫毛", "35", Constants.TRY_ON_EYE_LASH),
    TYPE_TRY_ON_MEIMAO(EFFECT_BEAUTY_TRYON_BROW, "TryOn眉毛", "36", Constants.TRY_ON_EYE_BROW),
    TYPE_TRY_ON_SAIHONG(EFFECT_BEAUTY_TRYON_BLUSH, "TryOn腮红", "37", Constants.TRY_ON_BLUSH),
    TYPE_TRY_ON_XIURONG(EFFECT_BEAUTY_TRYON_CONTOUR, "TryOn修容", "38", Constants.TRY_ON_TRIMMING),

    TYPE_TRY_ON_FENDI(EFFECT_BEAUTY_TRYON_FOUNDATION, "TryOn粉底", "39", Constants.TRY_ON_FOUNDATION),

    // 美颜
    TYPE_BASE(1, "基础美颜"),
    TYPE_RESHAPE(2, "美形"),
    TYPE_PLASTIC(3, "微整形"),
    TYPE_3D_PLASTIC("3D微整形"),
    TYPE_TONE(6, "调整"),
    TYPE_BOKEH(7, "背景虚化"),
    TYPE_BODY("美体"),

    // 滤镜
    TYPE_TEXTURE("质感"),
    TYPE_FILM("胶片"),
    TYPE_VINTAGE("复古"),
    TYPE_PEOPLE("人物"),
    TYPE_SCENERY("风景"),
    TYPE_STILL_LIFE("静物"),
    TYPE_FOOD("美食"),

    // 美妆
    TYPE_HAIR,       // 染发
    TYPE_LIP,        // 口红
    TYPE_BLUSH,      // 腮红
    TYPE_XR,         // 修容
    TYPE_EYE_BROW,   // 眉毛
    TYPE_EYE_SHADOW, // 眼影
    TYPE_EYE_LINER,  // 眼线
    TYPE_EYELASH,    // 眼睫毛
    TYPE_EYEBALL,    // 美瞳

    // 特效
    TYPE_STICKER_NEW,        // 最新
    TYPE_STICKER_2D,         // 2D贴纸
    TYPE_STICKER_3D,         // 3D贴纸
    TYPE_STICKER_HANDLE,     // 手势贴纸
    TYPE_STICKER_BG,         // 背景分割
    TYPE_STICKER_FACE,       // 脸部变形
    TYPE_STICKER_AVATAR,     // Avatar
    TYPE_STICKER_TRY_ON,     // TryOn
    TYPE_STICKER_GAN,        // GAN
    TYPE_STICKER_BEAUTY,     // 美妆贴纸
    TYPE_STICKER_PARTICLE,   // 粒子贴纸
    TYPE_STICKER_TRACK,      // 通用物体追踪
    TYPE_STICKER_LOCAL,      // 本地贴纸
    TYPE_STICKER_ADD,        // 叠加贴纸
    TYPE_STICKER_SYNC,       // 同步
    TYPE_STICKER_CAT,        // 猫脸
    TYPE_STICKER_CARTOON,    // GAN
    TYPE_STICKER_BUCKLE,     // 抠脸
    TYPE_STICKER_PLAY,       // 特效玩法
    TYPE_STICKER_SHADOW,     // 影分身
    TYPE_STICKER_BIG_HEAD,   // 大头特效

    // 风格
    TYPE_ZIRAN(0, "自然", "26"),      // style_nature
    TYPE_QINGZHUANG(0, "轻妆", "27"), // style_lightly
    TYPE_FASHION(0, "流行", "28"),    // style_fashion

    // Avatar
    TYPE_ANIMAL("动物类"),
    TYPE_ERCIYUAN("二次元"),
    TYPE_CARTOON("卡通类"),

    // 漫画脸
    TYPE_CARTOON_FACE("xx风格"),

    // 基础美颜
    TYPE_BASIC_WHITEN_1,    // 美白1
    TYPE_BASIC_2,           // 美白2
    TYPE_WHITENING_BACK,    // 美白2（返回按钮）
    TYPE_BASIC_2_NATURE,    // 美白2-自然
    TYPE_BASIC_2_PINK,      // 美白2-粉嫩
    TYPE_BASIC_2_BLACK,     // 美白2-美黑
    TYPE_BASIC_WHITEN_3,    // 美白3
    TYPE_BASIC_WHITEN_4,    // 美白4
    TYPE_BASIC_4,           // 红润
    TYPE_BASIC_SMOOTH_1,    // 磨皮1
    TYPE_BASIC_SMOOTH_2,    // 磨皮2
    TYPE_BASIC_SMOOTH_3,    // 磨皮3
    TYPE_BASIC_SMOOTH_4,    // 磨皮4
    TYPE_BASIC_SMOOTH_GAN,  // GAN肤质
    TYPE_BASE_SKIN_SMOOTH(EFFECT_BEAUTY_BASE_SKIN_SMOOTH, "全身去褶皱", 0f),  // 全身去褶皱

    // 美形
    TYPE_MX_1,     // 瘦脸
    TYPE_MX_2,     // 大眼
    TYPE_MX_3,     // 小脸
    TYPE_MX_4,     // 窄脸
    TYPE_MX_5,     // 圆眼

    // 微整形-高阶瘦脸
    TYPE_MX_HIGH_THIN_FACE(2011, "高阶瘦脸", 0f),
    TYPE_HIGH_BACK(0, "高阶瘦脸（返回按钮）", 0f),
    TYPE_HIGH_1(EFFECT_BEAUTY_PLASTIC_SHRINK_NATURAL_FACE, "自然", 0f),
    TYPE_HIGH_2(EFFECT_BEAUTY_PLASTIC_SHRINK_GODDESS_FACE, "女神", 0f),
    TYPE_HIGH_3(EFFECT_BEAUTY_PLASTIC_SHRINK_LONG_FACE, "长脸", 0f),
    TYPE_HIGH_4(EFFECT_BEAUTY_PLASTIC_SHRINK_ROUND_FACE, "圆脸", 0f),

    // 微整形新
    TYPE_THINNER_HEAD_1,
    TYPE_THINNER_HEAD_2,
    TYPE_WZH_2,
    TYPE_WZH_3,
    TYPE_WZH_4,
    TYPE_WZH_JAW,
    TYPE_WZH_5,
    TYPE_WZH_6,
    TYPE_WZH_7,
    TYPE_WZH_8,
    TYPE_WZH_9,
    TYPE_WZH_10,
    TYPE_WZH_11,
    TYPE_WZH_12,
    TYPE_WZH_13,
    TYPE_WZH_14,
    TYPE_WZH_15,
    TYPE_WZH_16,
    TYPE_WZH_17,
    TYPE_WZH_18,
    TYPE_WZH_19,
    TYPE_WZH_MOUTH_CORNER,
    TYPE_WZH_HAIR_LINE,
    TYPE_WZH_EYE_HEIGH,
    TYPE_PLASTIC_FULLER_LIPS,       // 丰唇
    TYPE_PLASTIC_MOUTH_WIDTH,       // 嘴巴宽度
    TYPE_PLASTIC_BROW_HEIGHT,       // 眉毛高度 眉毛上下
    TYPE_PLASTIC_BROW_THICKNESS,    // 眉毛粗细
    TYPE_PLASTIC_BROW_DISTANCE,     // 眉毛间距
    TYPE_PLASTIC_FACE_V_SHAPE,      // V脸，从下颌角到下巴的V脸效果
    TYPE_PLASTIC_FACE_FULL_V_SHAPE, // V脸，整体
    TYPE_PLASTIC_NOSE_TIP,          // 瘦鼻头
    TYPE_PLASTIC_NOSE_BRIDGE,       // 鼻梁调整
    TYPE_PLASTIC_ENLARGE_PUPIL,     // 放大瞳孔

    // 3D微整形-眼睛(80100)
    TYPE_WZH_3D_EYE_4,            // 眼睛-外眼角
    TYPE_WZH_3D_EYE_5,            // 眼睛-眼睛深浅
    TYPE_WZH_3D_EYE_6,            // 眼睛-卧蚕深浅
    TYPE_WZH_3D_EYE_7,            // 眼睛-眼睛角度
    TYPE_WZH_3D_EYE_OUTEREYETAIL, // 外眼尾
    TYPE_WZH_3D_EYE_INNERCORNER,  // 内眼角尖

    // 3D微整形-鼻子(80200)
    TYPE_WZH_3D_NOSE_4,     // 10 鼻高
    TYPE_WZH_3D_NOSE_5,     // 11 鼻根
    TYPE_WZH_3D_NOSE_6,     // 12 鼻子驼峰
    TYPE_WZH_3D_NOSE_8,     // 14 鼻翼

    // 3D微整形-嘴巴(80300)
    TYPE_WZH_3D_MOUTH_1,    // 15 嘴巴比例
    TYPE_WZH_3D_MOUTH_2,    // 16 嘴巴高度
    TYPE_WZH_3D_MOUTH_3,    // 17 嘴巴宽度
    TYPE_WZH_3D_MOUTH_4,    // 18 嘴巴深度
    TYPE_WZH_3D_MOUTH_5,    // 19 嘴巴厚度
    TYPE_WZH_3D_NEW_1,      // 20 嘟嘟唇
    TYPE_WZH_3D_NEW_2,      // 21 微笑唇

    // 3D微整形-脸部(80500)
    TYPE_WZH_3D_CHEEKBONE,            // 28 苹果肌
    TYPE_WZH_3D_FOREHEAD,             // 额头
    TYPE_WZH_3D_NASOLABIAL,           // 法令纹
    TYPE_WZH_3D_TEARDITCH,            // 泪沟
    TYPE_WZH_3D_BROWBONE,             // 眉骨
    TYPE_WZH_3D_RAISEEYEBROWS,        // 挑眉
    TYPE_WZH_3D_TEMPLE,               // 太阳穴
    TYPE_WZH_3D_FOREHEADTWO,          // 侧额头

    // 调整
    TYPE_TZH_1(EFFECT_BEAUTY_TONE_CONTRAST, "对比度", 0f),
    TYPE_TZH_2(EFFECT_BEAUTY_TONE_SATURATION, "饱和度", 0f),
    TYPE_TZH_3(EFFECT_BEAUTY_TONE_SHARPEN, "锐化", 0.1f),
    TYPE_TZH_4(EFFECT_BEAUTY_TONE_CLEAR, "清晰度", 0.1f),
    TYPE_TZH_5(EFFECT_BEAUTY_DENOISING, "去噪", 0f),
    TYPE_TZH_6(EFFECT_BEAUTY_TONE_COLOR_TONE, "色调", 0f),
    TYPE_TZH_7(EFFECT_BEAUTY_TONE_COLOR_TEMPERATURE, "色温", 0f),

    // 背景虚化
    TYPE_BASIC_BOKEH1,
    TYPE_BASIC_BOKEH2,

    TYPE_BODY_1(0, "整体效果", 0f),
    TYPE_BODY_2(0, "瘦头", 0f),
    TYPE_BODY_3(0, "瘦肩", 0f),
    TYPE_BODY_4(0, "美臀", 0f),
    TYPE_BODY_5(0, "瘦腿", 0f);

    var code: Int = 0
    var desc: String? = null
    var startCenterSeekBar: Boolean = false
    var strength: Float = 0f
        get() {
            return field
        }

    var groupId: String = ""
    var assetPath: String = ""

    constructor(code: Int) : this() {
        this.code = code
    }

    constructor(desc: String) : this() {
        this.desc = desc
    }

    constructor(desc: String, groupId: String) : this() {
        this.desc = desc
        this.groupId = groupId
    }

    constructor(code: Int, desc: String) : this(code) {
        this.desc = desc
    }

    constructor(code: Int, desc: String, groupId: String) : this(code, desc) {
        this.groupId = groupId
    }

    constructor(code: Int, desc: String, groupId: String, assetPath: String) : this(code, desc) {
        this.groupId = groupId
        this.assetPath = assetPath
    }

    constructor(code: Int, desc: String, defStrength: Float) : this(code, desc) {
        this.strength = defStrength
    }

    constructor(code: Int, desc: String, startCenterSeekBar: Boolean, defStrength: Float) : this(
        code
    ) {
        this.desc = desc
        this.startCenterSeekBar = startCenterSeekBar
        strength = defStrength
    }

    companion object {
        val basicList =
            arrayListOf(TYPE_BASE, TYPE_RESHAPE, TYPE_PLASTIC, TYPE_3D_PLASTIC, TYPE_TONE, TYPE_BODY, TYPE_BOKEH)

        val filterList = arrayListOf(TYPE_TEXTURE, TYPE_FILM,TYPE_VINTAGE, TYPE_PEOPLE, TYPE_SCENERY, TYPE_STILL_LIFE, TYPE_FOOD)

        val makeupList = arrayListOf(
            TYPE_HAIR, TYPE_LIP, TYPE_BLUSH, TYPE_XR, TYPE_EYE_BROW,
            TYPE_EYE_SHADOW, TYPE_EYE_LINER, TYPE_EYELASH, TYPE_EYEBALL
        )

        // 试妆列表
        val tryOnList = arrayListOf(
            TYPE_TRY_ON_LIP, TYPE_TRY_ON_HAIR,
            TYPE_TRY_ON_CHUNXIAN, TYPE_TRY_ON_YANYING,
            TYPE_TRY_ON_YANXIAN, TYPE_TRY_ON_YANYIN,
            TYPE_TRY_ON_YANJIEMAO, TYPE_TRY_ON_MEIMAO,
            TYPE_TRY_ON_SAIHONG, TYPE_TRY_ON_XIURONG,
            TYPE_TRY_ON_FENDI
        )

        val styleList = arrayListOf(TYPE_ZIRAN, TYPE_QINGZHUANG, TYPE_FASHION)

        // 基础美颜
        val basicEffectList = arrayListOf(
            TYPE_BASIC_WHITEN_1,
            TYPE_BASIC_2,
            TYPE_BASIC_2_NATURE,
            TYPE_BASIC_2_PINK,
            TYPE_BASIC_2_BLACK,
            TYPE_BASIC_WHITEN_3,
            TYPE_BASIC_WHITEN_4,
            TYPE_BASIC_4,
            TYPE_BASIC_SMOOTH_1,
            TYPE_BASIC_SMOOTH_2,
            TYPE_BASIC_SMOOTH_3,
            TYPE_BASIC_SMOOTH_4,
            TYPE_BASIC_SMOOTH_GAN,
            TYPE_BASE_SKIN_SMOOTH
        )
        val mxEffectList = arrayListOf(TYPE_MX_1, TYPE_MX_2, TYPE_MX_3, TYPE_MX_4, TYPE_MX_5)
        val wzhEffectList = arrayListOf(
            TYPE_WZH_MOUTH_CORNER,
            TYPE_WZH_EYE_HEIGH,
            TYPE_WZH_HAIR_LINE,

            TYPE_THINNER_HEAD_1,
            TYPE_THINNER_HEAD_2,
            TYPE_WZH_2,
            TYPE_WZH_3,
            TYPE_WZH_4,
            TYPE_WZH_JAW,
            TYPE_WZH_5,
            TYPE_WZH_6,
            TYPE_WZH_7,
            TYPE_WZH_8,
            TYPE_WZH_9,
            TYPE_WZH_10,
            TYPE_WZH_11,
            TYPE_WZH_12,
            TYPE_WZH_13,
            TYPE_WZH_14,
            TYPE_WZH_15,
            TYPE_WZH_16,
            TYPE_WZH_17,
            TYPE_WZH_18,
            TYPE_WZH_19,
            TYPE_PLASTIC_FULLER_LIPS,       // 丰唇
            TYPE_PLASTIC_MOUTH_WIDTH,       // 嘴巴宽度
            TYPE_PLASTIC_BROW_HEIGHT,       // 眉毛高度 眉毛上下
            TYPE_PLASTIC_BROW_THICKNESS,    // 眉毛粗细
            TYPE_PLASTIC_BROW_DISTANCE,     // 眉毛间距
            TYPE_PLASTIC_FACE_V_SHAPE,      // V脸，从下颌角到下巴的V脸效果
            TYPE_PLASTIC_FACE_FULL_V_SHAPE, // V脸，整体
            TYPE_PLASTIC_NOSE_TIP,          // 瘦鼻头
            TYPE_PLASTIC_NOSE_BRIDGE,       // 鼻梁调整
            TYPE_PLASTIC_ENLARGE_PUPIL,     // 放大瞳孔
        )
        val adjustList = arrayListOf(TYPE_TZH_1, TYPE_TZH_2, TYPE_TZH_3, TYPE_TZH_4, TYPE_TZH_5, TYPE_TZH_6, TYPE_TZH_7)
        val bokehList = arrayListOf(TYPE_BASIC_BOKEH1, TYPE_BASIC_BOKEH2)
        val highThinFaceList =
            arrayListOf(TYPE_HIGH_BACK, TYPE_HIGH_1, TYPE_HIGH_2, TYPE_HIGH_3, TYPE_HIGH_4)
        val wzh3d = arrayListOf(
            TYPE_WZH_3D_MOUTH_1, TYPE_WZH_3D_MOUTH_2,
            TYPE_WZH_3D_MOUTH_3, TYPE_WZH_3D_MOUTH_4,
            TYPE_WZH_3D_MOUTH_5,

            TYPE_WZH_3D_NEW_1, TYPE_WZH_3D_NEW_2,
            TYPE_WZH_3D_NOSE_4,
            TYPE_WZH_3D_NOSE_5, TYPE_WZH_3D_NOSE_6,
            TYPE_WZH_3D_NOSE_8,

            TYPE_WZH_3D_EYE_4,
            TYPE_WZH_3D_EYE_5, TYPE_WZH_3D_EYE_6,
            TYPE_WZH_3D_EYE_7, TYPE_WZH_3D_EYE_OUTEREYETAIL,
            TYPE_WZH_3D_EYE_INNERCORNER,
            TYPE_WZH_3D_CHEEKBONE,
            TYPE_WZH_3D_FOREHEAD, TYPE_WZH_3D_NASOLABIAL,
            TYPE_WZH_3D_TEARDITCH, TYPE_WZH_3D_BROWBONE,
            TYPE_WZH_3D_RAISEEYEBROWS, TYPE_WZH_3D_TEMPLE,
            TYPE_WZH_3D_FOREHEADTWO
        )

        val mutualHighFaceList = arrayListOf(TYPE_HIGH_1, TYPE_HIGH_2, TYPE_HIGH_3, TYPE_HIGH_4)
        val mutualSmoothList = arrayListOf(TYPE_BASIC_SMOOTH_1, TYPE_BASIC_SMOOTH_2, TYPE_BASIC_SMOOTH_3, TYPE_BASIC_SMOOTH_4, TYPE_BASIC_SMOOTH_GAN)
        val mutualBokehList = arrayListOf(TYPE_BASIC_BOKEH1, TYPE_BASIC_BOKEH2)
        val mutualWhiteList = arrayListOf(
            TYPE_BASIC_WHITEN_1,
            TYPE_BASIC_2_NATURE,
            TYPE_BASIC_2_PINK,
            TYPE_BASIC_2_BLACK,
            TYPE_BASIC_WHITEN_3,
            TYPE_BASIC_WHITEN_4
        )
        //val mutualThinHeadList = arrayListOf(TYPE_THINNER_HEAD_1, TYPE_THINNER_HEAD_2)

        fun getAllBasicType(): ArrayList<EffectType> {
            val total = arrayListOf<EffectType>()
            total.addAll(basicEffectList)
            total.addAll(mxEffectList)
            total.addAll(wzhEffectList)
            total.addAll(adjustList)
            total.addAll(highThinFaceList)
            total.addAll(wzh3d)
            total.addAll(bokehList)
            total.add(EffectType.TYPE_BASE_SKIN_SMOOTH)
            return total
        }

        //基础美颜
        fun getStrengthMap(type: EffectType): EnumMap<EffectType, Float> {
            val strengthsMap = EnumMap<EffectType, Float>(EffectType::class.java)
            when (type) {
                TYPE_3D_PLASTIC -> {
                    for (i in wzh3d.indices) {
                        strengthsMap[wzh3d[i]] = wzh3d[i].strength
                    }
                }

                TYPE_BASE -> {
                    for (i in basicEffectList.indices) {
                        strengthsMap[basicEffectList[i]] = basicEffectList[i].strength
                    }
                }

                TYPE_RESHAPE -> {// meixing
                    for (i in mxEffectList.indices) {
                        strengthsMap[mxEffectList[i]] = mxEffectList[i].strength
                    }
                }

                TYPE_PLASTIC -> {// 微整形
                    for (i in wzhEffectList.indices) {
                        strengthsMap[wzhEffectList[i]] = wzhEffectList[i].strength
                    }
                    for (i in highThinFaceList.indices) {
                        strengthsMap[highThinFaceList[i]] = highThinFaceList[i].strength
                    }
                }

                TYPE_TONE -> {
                    for (i in adjustList.indices) {
                        strengthsMap[adjustList[i]] = adjustList[i].strength
                    }
                }

                TYPE_BOKEH -> {
                    for (i in bokehList.indices) {
                        strengthsMap[bokehList[i]] = bokehList[i].strength
                    }
                }

                TYPE_BODY -> {

                }

                GROUP_EFFECT -> {//一整组
                    for (i in basicEffectList.indices) {
                        strengthsMap[basicEffectList[i]] = basicEffectList[i].strength
                    }
                    for (i in mxEffectList.indices) {
                        strengthsMap[mxEffectList[i]] = mxEffectList[i].strength
                    }
                    for (i in wzhEffectList.indices) {
                        strengthsMap[wzhEffectList[i]] = wzhEffectList[i].strength
                    }
                    for (i in adjustList.indices) {
                        strengthsMap[adjustList[i]] = adjustList[i].strength
                    }
                }

                else -> {
                }
            }
            return strengthsMap
        }

        val wzh3dEyeList = arrayListOf(
            TYPE_WZH_3D_EYE_4,
            TYPE_WZH_3D_EYE_5, TYPE_WZH_3D_EYE_6,
            TYPE_WZH_3D_EYE_7, TYPE_WZH_3D_EYE_OUTEREYETAIL,
            TYPE_WZH_3D_EYE_INNERCORNER
        )

        // 鼻子一组
        val wzh3dNoseList = arrayListOf(
            TYPE_WZH_3D_NOSE_4,
            TYPE_WZH_3D_NOSE_5, TYPE_WZH_3D_NOSE_6,
            TYPE_WZH_3D_NOSE_8
        )

        // 嘴巴一组
        val wzh3dMouthList = arrayListOf(
            TYPE_WZH_3D_MOUTH_1, TYPE_WZH_3D_MOUTH_2,
            TYPE_WZH_3D_MOUTH_3, TYPE_WZH_3D_MOUTH_4,
            TYPE_WZH_3D_MOUTH_5, TYPE_WZH_3D_NEW_1,
            TYPE_WZH_3D_NEW_2
        )

        val wzh3dFaceList = arrayListOf(
            TYPE_WZH_3D_CHEEKBONE, TYPE_WZH_3D_FOREHEAD,
            TYPE_WZH_3D_NASOLABIAL, TYPE_WZH_3D_TEARDITCH,
            TYPE_WZH_3D_BROWBONE, TYPE_WZH_3D_RAISEEYEBROWS,
            TYPE_WZH_3D_TEMPLE, TYPE_WZH_3D_FOREHEADTWO
        )

        val stickerList = arrayListOf(
            TYPE_STICKER_NEW, TYPE_STICKER_2D,
            TYPE_STICKER_3D, TYPE_STICKER_HANDLE,
            TYPE_STICKER_BG, TYPE_STICKER_FACE,
            TYPE_STICKER_AVATAR, TYPE_STICKER_BEAUTY,
            TYPE_STICKER_PARTICLE, TYPE_STICKER_TRACK,
            TYPE_STICKER_LOCAL, TYPE_STICKER_ADD,
            TYPE_STICKER_SYNC, TYPE_STICKER_CAT,
            TYPE_STICKER_BUCKLE, TYPE_STICKER_PLAY,
            TYPE_STICKER_SHADOW, TYPE_STICKER_BIG_HEAD,
            TYPE_STICKER_TRY_ON, TYPE_STICKER_GAN,
            TYPE_STICKER_CARTOON
        )

        fun getTypeByCode(code: Int): EffectType {
            for (item in values()) {
                if (code == TYPE_BASIC_SMOOTH_1.code)
                    return TYPE_BASIC_SMOOTH_1
                if (code == TYPE_BASIC_WHITEN_1.code)
                    return TYPE_BASIC_WHITEN_1
                if (item.code == code) {
                    return item
                }
            }
            return TYPE_HAIR
        }

        fun getTypeByGroupId(groupId: String): EffectType {
            for (item in values()) {
                if (item.groupId == groupId) {
                    return item
                }
            }
            return TYPE_HAIR
        }

        // 根据名称获取枚举对象
        fun getTypeByName(name: String?): EffectType? {
            for (item in EffectType.values()) {
                if (item.name.equals(name)) {
                    return item
                }
            }
            return null
        }

        var mCurrFace: EffectType = NATURAL_FACE
        fun setCurrFace(type: EffectType) {
            mCurrFace = type
        }
    }
}