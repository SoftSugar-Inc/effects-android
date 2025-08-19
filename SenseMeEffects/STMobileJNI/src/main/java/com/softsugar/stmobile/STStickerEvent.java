package com.softsugar.stmobile;

public class STStickerEvent {
    private static String TAG = "STStickerEvent";
    private static STStickerEvent mInstance;
    private StickerEventListener mStickerEventDefaultListener;

    /**
     * \~Chinese STStickerEvent初始化
     * @return \~Chinese 回调事件
     *
     * \~Chinese create STStickerEvent instance
     * @return \~Chinese sticker event
     */
    public static STStickerEvent createInstance() {
        if (mInstance == null) {
            mInstance = new STStickerEvent();
        }
        return mInstance;
    }

    public static STStickerEvent getInstance() {
        return mInstance;
    }

    /**
     * \~Chinese 音频播放监听器 \~English sticker event listener
     */
    public interface StickerEventListener {
        /**
         * \~Chinese 贴纸package播放event回调
         *
         * @param packageName    \~Chinese 贴纸package名字
         * @param packageID      \~Chinese 贴纸package id
         * @param event          \~Chinese 贴纸package播放event, 见st_mobile_sticker_transition.h中的st_package_state_type
         * @param displayedFrame \~Chinese 当前package播放的帧数
         *
         * \~English package event callback
         *
         * @param packageName    \~English package name
         * @param packageID      \~English package id
         * @param event          \~English package event, refer to st_mobile_sticker_transition.h and st_package_state_type
         * @param displayedFrame \~English package displayed frame
         */
        void onPackageEvent(String packageName, int packageID, int event, int displayedFrame);

        /**
         * \~Chinese 贴纸part播放event回调
         *
         * @param moduleName     \~Chinese 贴纸part名字
         * @param moduleId       \~Chinese 贴纸part id
         * @param animationEvent \~Chinese 贴纸part播放event, 见st_mobile_sticker_transition.h中的st_animation_state_type
         * @param currentFrame   \~Chinese 当前播放的帧数
         * @param positionId     \~Chinese 贴纸对应的position id, 即st_mobile_human_action_t结果中不同类型结果中的id
         * @param positionType   \~Chinese 贴纸对应的position种类, 见st_mobile_human_action_t中的动作类型
         *
         * \~English Animation event callback
         *
         * @param moduleName     \~English module name
         * @param moduleId       \~English module id
         * @param animationEvent \~English animation event, refer to st_mobile_sticker_transition.h and st_animation_state_type
         * @param currentFrame   \~English current frame
         * @param positionId     \~English module position id,  refer to st_mobile_human_action_t id
         * @param positionType   \~English module position type, refer to st_mobile_human_action_t action type
         */
        void onAnimationEvent(String moduleName, int moduleId, int animationEvent, int currentFrame, int positionId, long positionType);

        /**
         * \~Chinese 贴纸关键帧event回调
         *
         * @param materialName  \~Chinese 贴纸part名字
         * @param frame         \~Chinese 触发的关键帧
         *
         * \~English key frame event callback
         *
         * @param materialName  \~English material part name
         * @param frame         \~English key frame
         */
        void onKeyFrameEvent(String materialName, int frame);
    }

    /**
     * \~Chinese 设置控制监听器
     * @param listener \~Chinese listener为null，SDK默认处理，若不为null，用户自行处理
     *
     * \~English 设置控制监听器
     * @param listener \~English if listener is null，use sdk default
     */
    public void setStickerEventListener(StickerEventListener listener) {
        if (listener != null) {
            mStickerEventDefaultListener = listener;
        }
    }

    //\~Chinese JNI调用，不做混淆 \~English no need code obfuscation
    private void onPackageEvent(String packageName, int packageID, int event, int displayedFrame) {
        if (mStickerEventDefaultListener != null) {
            mStickerEventDefaultListener.onPackageEvent(packageName, packageID, event, displayedFrame);
        }
    }

    //\~Chinese JNI调用，不做混淆 \~English no need code obfuscation
    private void onAnimationEvent(String moduleName, int moduleId, int animationEvent, int currentFrame, int positionId, long position_type) {
        if (mStickerEventDefaultListener != null) {
            mStickerEventDefaultListener.onAnimationEvent(moduleName, moduleId, animationEvent, currentFrame, positionId, position_type);
        }
    }

    //\~Chinese JNI调用，不做混淆 \~English no need code obfuscation
    private void onKeyFrameEvent(String materialName, int frame) {
        if (mStickerEventDefaultListener != null) {
            mStickerEventDefaultListener.onKeyFrameEvent(materialName, frame);
        }
    }
}
