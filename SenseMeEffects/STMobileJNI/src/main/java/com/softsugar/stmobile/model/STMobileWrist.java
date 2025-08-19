package com.softsugar.stmobile.model;

/**
 * \~Chinese 手腕的信息
 */
public class STMobileWrist {
    // \~Chinese 手腕ID
    private int id;

    // \~Chinese 手腕的置信度
    private float score;

    // \~Chinese 矩形框
    private STRect rect;

    // \~Chinese 手腕部位关键点
    private STPoint[] keyPoints;

    // \~Chinese 手腕部关键点个数
    private int keyPointsCount;

    // \~Chinese 左右手分类
    private STLeftOrRight label;

    // \~Chinese pose手腕3d位置信息
    private STMobileTransform pose;

    // \~Chinese delay模式 pose是否有效，暂不支持后处理
    private boolean isvalid;

    // \~Chinese 点位是否进行镜像，主要用于调整pose
    private boolean ismirror;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public STRect getRect() {
        return rect;
    }

    public void setRect(STRect rect) {
        this.rect = rect;
    }

    public STPoint[] getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(STPoint[] keyPoints) {
        this.keyPoints = keyPoints;
    }

    public int getKeyPointsCount() {
        return keyPointsCount;
    }

    public void setKeyPointsCount(int keyPointsCount) {
        this.keyPointsCount = keyPointsCount;
    }

    public STLeftOrRight getLabel() {
        return label;
    }

    public void setLabel(STLeftOrRight label) {
        this.label = label;
    }

    public STMobileTransform getPose() {
        return pose;
    }

    public void setPose(STMobileTransform pose) {
        this.pose = pose;
    }

    public boolean isIsvalid() {
        return isvalid;
    }

    public void setIsvalid(boolean isvalid) {
        this.isvalid = isvalid;
    }

    public boolean isIsmirror() {
        return ismirror;
    }

    public void setIsmirror(boolean ismirror) {
        this.ismirror = ismirror;
    }
}
