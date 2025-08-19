package com.softsugar.stmobile.params;

public class STSmoothMode {

    ///< \~chinese 只对人脸做磨皮处理 \~english smooth only on face
    public final static int EFFECT_SMOOTH_FACE_ONLY = 0;

    ///< \~chinese 对全图做磨皮处理 \~english whole image smooth
    public final static int EFFECT_SMOOTH_FULL_IMAGE = 1;

    ///< \~chinese 对脸部做精细化磨皮处理 \~english high quality smooth on face
    public final static int EFFECT_SMOOTH_FACE_DETAILED = 2;

    ///< \~chinese 对脸部皮肤细化和改善质感的磨皮处理 \~english refining and improving the texture of facial skin
    public final static int EFFECT_SMOOTH_FACE_REFINE = 3;

    ///< \~chinest 针对脸部皮肤平整的匀肤磨皮处理 \~english for the treatment of evening out and smoothing facial skin texture
    public final static int EFFECT_SMOOTH_FACE_EVEN = 4;

    ///< \~chinese gan模型对脸部皮肤进行磨皮处理，需要通过 st_mobile_effect_set_beauty 接口设置gan模型 \~english The gan model performs skin smoothing on facial skin and requires setting up the gan model through the st_mobile_effect_set_beauty interface.
    public final static int EFFECT_SMOOTH_FACE_GAN = 100;
}
