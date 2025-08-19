package com.softsugar.stmobile.model;

public class STMobileHandInfo {
    int handId;
    STRect handRect;
    STPoint[] keyPoints;
    int keyPointsCount;
    long handAction;
    float handActionScore;

    int left_right;
    STPoint[] extra2dKeyPoints;   ///< \~Chinese 手部2d关键点
    STPoint3f[] extra3dKeyPoints;   ///< \~Chinese 手部3d关键点
    int extra2dKeyPointsCount;    ///< \~Chinese 手部2d关键点个数
    int extra3dKeyPointsCount;    ///< \~Chinese 手部3d关键点个数
    STHandDynamicGesture dynamicGesture; ///\~Chinese <动态手势
    STPoint[] gestureKeyPoints;   ///<\~Chinese  动态手部关键点
    int gestureKeyPointsCount;   ///< \~Chinese 动态手部关键点

    public class STMobileHandActionType {
        public static final long ST_HAND_ACTION_TYPE_OK         	= 0x00000200;               ///< \~chinese OK手势 \~english OK hand gesture
        public static final long ST_HAND_ACTION_TYPE_SCISSOR		= 0x00000400;          ///< \~chinese 剪刀手 \~english Scissor hand gesture
        public static final long ST_HAND_ACTION_TYPE_GOOD			= 0x00000800;              ///< \~chinese 大拇哥 \~english Thumbs up hand gesture
        public static final long ST_HAND_ACTION_TYPE_PALM			= 0x00001000;              ///< \~chinese 手掌 \~english Palm gesture
        public static final long ST_HAND_ACTION_TYPE_PISTOL			= 0x00002000;           ///< \~chinese 手枪手势 \~english Pistol hand gesture
        public static final long ST_HAND_ACTION_TYPE_LOVE			= 0x00004000;             ///< \~chinese 爱心手势 \~english Heart hand gesture
        public static final long ST_HAND_ACTION_TYPE_HOLDUP			= 0x00008000;           ///< \~chinese 托手手势 \~english Hold up hand gesture
        public static final long ST_HAND_ACTION_TYPE_FIST			= 0x00200000;             ///< \~chinese 拳头手势 \~english Fist hand gesture
        public static final long ST_HAND_ACTION_TYPE_CONGRATULATE	= 0x00020000;     ///< \~chinese 恭贺（抱拳）\~english Fist-palm salute
        public static final long ST_HAND_ACTION_TYPE_FINGER_HEART	= 0x00040000;     ///< \~chinese 单手比爱心 \~english Finger heart gesture
        public static final long ST_HAND_ACTION_TYPE_FINGER_INDEX	= 0x00100000;     ///< \~chinese 食指指尖 \~english Finger point gesture
        public static final long ST_HAND_ACTION_TYPE_666			= 0x00400000;              ///< \~chinese 666 \~english 666 hand gesture
        public static final long ST_HAND_ACTION_TYPE_BLESS			= 0x00800000;            ///< \~chinese 双手合十 \~english Bless hand gesture
        public static final long ST_HAND_ACTION_TYPE_ILOVEYOU		= 0x010000000000L;     ///< \~chinese 手势ILoveYou \~english I love you hand gesture
        public static final long ST_HAND_ACTION_TYPE_THREE			= 0x10000000000000L;      ///< \~chinese 三根手指 \~english Three fingers
        public static final long ST_HAND_ACTION_TYPE_FOUR			= 0x20000000000000L;       ///< \~chinese 四根手指 \~english Four fingers
        public static final long ST_HAND_ACTION_TYPE_THUMBS_DOWN	= 0x01;            ///< \~chinese 差评 \~english Thumbs down gesture
        public static final long ST_HAND_ACTION_TYPE_LOVEB          = 0x02;                  ///< \~chinese 比心A \~english Heart hand gesture
        public static final long ST_HAND_ACTION_TYPE_LOVEC			= 0x04;                  ///< \~chinese 比心C \~english Heart hand gesture
        public static final long ST_HAND_ACTION_TYPE_SEVEN			= 0x08;                  ///< \~chinese 七（卷心菜） \~english Seven gesture
        public static final long ST_HAND_ACTION_TYPE_FACE_PALMING	= 0x10;           ///< \~chinese 捧脸 \~english Face plaming gesture
        public static final long ST_HAND_ACTION_TYPE_PRAY			= 0x20;                   ///< \~chinese 祈祷 \~english Pray gesture
        public static final long ST_HAND_ACTION_TYPE_NINE			= 0x40;                   ///< \~chinese 九 \~english Nine gesture
        public static final long ST_HAND_ACTION_TYPE_CONGRATULATE_B = 0x80;                    ///< \~chinese 恭贺B（抱拳）\~english Fist-palm B salute
        public static final long ST_HAND_ACTION_TYPE_SSH			= 0x400000000000L;          ///< \~chinese 手势嘘,依赖于手势检测和106点检测 \~english Shush hand. This detection relies on gesture detection and face 106-point detection
        public static final long ST_HAND_ACTION_TYPE_OTHER			= 0;                  ///< \~chinese 其他手势 \~english Other gesture
    };

    public void setKeyPointsCount(int keyPointsCount) {
        this.keyPointsCount = keyPointsCount;
    }

    public void setKeyPoints(STPoint[] keyPoints) {
        this.keyPoints = keyPoints;
    }

    public int getKeyPointsCount() {
        return keyPointsCount;
    }

    public float getHandActionScore() {
        return handActionScore;
    }

    public int getHandId() {
        return handId;
    }

    public long getHandAction() {
        return handAction;
    }

    public STPoint[] getKeyPoints() {
        return keyPoints;
    }

    public STRect getHandRect() {
        return handRect;
    }

    public void setHandAction(long handAction) {
        this.handAction = handAction;
    }

    public void setHandActionScore(float handActionScore) {
        this.handActionScore = handActionScore;
    }

    public void setHandId(int handId) {
        this.handId = handId;
    }

    public void setHandRect(STRect handRect) {
        this.handRect = handRect;
    }

    public int getExtra2dKeyPointsCount() {
        return extra2dKeyPointsCount;
    }

    public int getExtra3dKeyPointsCount() {
        return extra3dKeyPointsCount;
    }

    public int getGestureKeyPointsCount() {
        return gestureKeyPointsCount;
    }

    public int getLeft_right() {
        return left_right;
    }

    public STHandDynamicGesture getDynamicGesture() {
        return dynamicGesture;
    }

    public STPoint3f[] getExtra3dKeyPoints() {
        return extra3dKeyPoints;
    }

    public STPoint[] getExtra2dKeyPoints() {
        return extra2dKeyPoints;
    }

    public STPoint[] getGestureKeyPoints() {
        return gestureKeyPoints;
    }

    public void setDynamicGesture(STHandDynamicGesture dynamicGesture) {
        this.dynamicGesture = dynamicGesture;
    }

    public void setExtra2dKeyPoints(STPoint[] extra2dKeyPoints) {
        this.extra2dKeyPoints = extra2dKeyPoints;
    }

    public void setExtra2dKeyPointsCount(int extra2dKeyPointsCount) {
        this.extra2dKeyPointsCount = extra2dKeyPointsCount;
    }

    public void setExtra3dKeyPoints(STPoint3f[] extra3dKeyPoints) {
        this.extra3dKeyPoints = extra3dKeyPoints;
    }

    public void setExtra3dKeyPointsCount(int extra3dKeyPointsCount) {
        this.extra3dKeyPointsCount = extra3dKeyPointsCount;
    }

    public void setGestureKeyPoints(STPoint[] gestureKeyPoints) {
        this.gestureKeyPoints = gestureKeyPoints;
    }

    public void setGestureKeyPointsCount(int gestureKeyPointsCount) {
        this.gestureKeyPointsCount = gestureKeyPointsCount;
    }

    public void setLeft_right(int left_right) {
        this.left_right = left_right;
    }
}
