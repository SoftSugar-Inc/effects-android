package com.softsugar.stmobile.params;

/**
 * Created by softsugar on 18-8-1.
 */

public class STAnimalFaceParamsType {
    /// \~Chinese 设置检测到的最大猫脸数目N,持续track已检测到的N个猫脸直到猫脸数小于N再继续做detect.默认32
    public final static int ST_MOBILE_PARAM_CAT_LIMIT = 1;
    /// \~Chinese 设置tracker每多少帧进行一次猫脸detect.
    public final static int ST_MOBILE_PARAM_CAT_DETECT_INTERVAL_LIMIT = 2;
    /// \~Chinese 设置猫脸跟踪的阈值
    public final static int ST_MOBILE_PARAM_CAT_THRESHOLD = 3;

    /// \~Chinese 设置检测到的最大猫脸数目N,持续track已检测到的N个狗脸直到狗脸数小于N再继续做detect.默认32
    public final static int ST_MOBILE_PARAM_DOG_LIMIT = 101;
    /// \~Chinese 设置tracker每多少帧进行一次狗脸detect.
    public final static int ST_MOBILE_PARAM_DOG_DETECT_INTERVAL_LIMIT = 102;
    /// \~Chinese 设置狗脸跟踪的阈值
    public final static int ST_MOBILE_PARAM_DOG_THRESHOLD = 103;

    /// \~Chinese 设置预处理图像的最长边，最小640， 默认值1500。 值越大，耗时越长，检测到的数目会多一些
    public final static int ST_MOBILE_PARAM_ANIMAL_PREPROCESS_MAX_SIZE = 500;

    public class STMobileAnimalType{
        public final static int ST_MOBILE_ANIMAL_CAT_FACE = 0;          ///< \~Chinese 猫脸
        public final static int ST_MOBILE_ANIMAL_DOG_FACE = 10;         ///< \~Chinese 狗脸
    }

    public class STMobileAnimalModelType{
        public final static int ST_MOBILE_ANIMAL_MODEL_CAT_FACE = 0;          ///< \~Chinese 猫脸
        public final static int ST_MOBILE_ANIMAL_MODEL_DOG_FACE = 10;         ///< \~Chinese 狗脸
    }

}
