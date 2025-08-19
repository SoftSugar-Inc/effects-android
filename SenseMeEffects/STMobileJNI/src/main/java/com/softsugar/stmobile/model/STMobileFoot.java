package com.softsugar.stmobile.model;

/**
 * \~Chinese 脚部检测结果
 */
public class STMobileFoot {
    // \~Chinese 脚的ID
    private int id;

    // \~Chinese 脚的置信度
    private float score;

    // \~Chinese 矩形框
    private STRect rect;

    // \~Chinese 脚部关键点
    private STPoint[] keyPoints;

    // \~Chinese 脚部关键点个数
    private int keyPointsCount;

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

    public STPoint[] getpKeyPoints() {
        return keyPoints;
    }

    public void setpKeyPoints(STPoint[] pKeyPoints) {
        this.keyPoints = pKeyPoints;
    }

    public int getKeyPointsCount() {
        return keyPointsCount;
    }

    public void setKeyPointsCount(int keyPointsCount) {
        this.keyPointsCount = keyPointsCount;
    }
}
