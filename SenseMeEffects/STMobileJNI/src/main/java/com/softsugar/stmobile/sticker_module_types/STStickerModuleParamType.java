package com.softsugar.stmobile.sticker_module_types;

public class STStickerModuleParamType {

    public final static int ST_STICKER_PARAM_MODULE_TYPE_INT = 100;                     ///< \~Chinese 获取贴纸模块的类型
    public final static int ST_STICKER_PARAM_MODULE_PACKAGE_ID_INT = 101;               ///< \~Chinese 设置/获取贴纸模块的package id
    public final static int ST_STICKER_PARAM_MODULE_ENABLED_BOOL = 102;                 ///< \~Chinese 设置贴纸模块的enable属性
    public final static int ST_STICKER_PARAM_MODULE_NAME_STR = 103;                     ///< \~Chinese 设置/获取贴纸模块的名称：字符串以 '\0' 结尾
    public final static int ST_STICKER_PARAM_MUDULE_POSITION_ULL = 104;                 ///< \~Chinese 设置/获取贴纸模块的位置 (unsigned long long)
    public final static int ST_STICKER_PARAM_MODULE_RENDER_ORDER_INDEX_INT = 105;       ///< \~Chinese 设置/获取贴纸模块的渲染序号, 序号较小的先渲染???
    public final static int ST_STICKER_PARAM_MODULE_ANIMATION_EVENT_CALLBACK_PTR = 106; ///< \~Chinese 设置贴纸模块的动画事件回调函数

    public final static int ST_STICKER_PARAM_MODULE_SCALE_FLOAT = 107;                  ///< \~Chinese 设置贴纸模块显示的缩放比例

    public final static int ST_STICKER_PARAM_MODULE_OFFSET_LEFT_INT = 108;              ///< \~Chinese 设置贴纸模块向左偏移的像素个数
    public final static int ST_STICKER_PARAM_MODULE_OFFSET_RIGHT_INT = 109;             ///< \~Chinese 设置贴纸模块向右偏移的像素个数
    public final static int ST_STICKER_PARAM_MODULE_OFFSET_TOP_INT = 110;               ///< \~Chinese 设置贴纸模块向上偏移的像素个数
    public final static int ST_STICKER_PARAM_MODULE_OFFSET_BOTTOM_INT = 111;             ///< \~Chinese 设置贴纸模块向下偏移的像素个数
    public final static int ST_STICKER_PARAM_MODULE_KEY_FRAME_EVENT_CALLBACK_PTR = 112;  ///< \~Chinese 设置贴纸模块的关键帧事件回调函数
}
