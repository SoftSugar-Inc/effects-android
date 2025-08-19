package com.softsugar.stmobile.params;

/**
 * \~Chinese 图像旋转方向
 */
public class STRotateType {
    //\~Chinese 因为只有正向的人脸能够被识别，所以需要输入图像中人脸需要旋转的角度，
    public final static int ST_CLOCKWISE_ROTATE_0 = 0;   ///< \~Chinese 图像不需要旋转,图像中的人脸为正脸
    public final static int ST_CLOCKWISE_ROTATE_90 = 1;  ///< \~Chinese 图像需要顺时针旋转90度,使图像中的人脸为正
    public final static int ST_CLOCKWISE_ROTATE_180 = 2; ///< \~Chinese 图像需要顺时针旋转180度,使图像中的人脸为正
    public final static int ST_CLOCKWISE_ROTATE_270 = 3; ///< \~Chinese 图像需要顺时针旋转270度,使图像中的人脸为正
}
