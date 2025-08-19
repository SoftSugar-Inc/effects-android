package com.softsugar.stmobile.model;

public class STAnimalFace {

    int id;                 ///< \~Chinese  每个检测到的猫脸拥有唯一的ID.猫脸跟踪丢失以后重新被检测到,会有一个新的ID
    STRect rect;         ///<\~Chinese  代表面部的矩形区域
    float score;            ///< \~Chinese 置信度
    STPoint[] p_key_points;  ///< \~Chinese 关键点
    int key_points_count;       ///< \~Chinese 关键点个数
    float yaw;              ///< \~Chinese 水平转角,真实度量的左负右正
    float pitch;            ///< \~Chinese 俯仰角,真实度量的上负下正
    float roll;             ///< \~Chinese 旋转角,真实度量的左负右正

    int animalType;
    float[] earScore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public STRect getRect() {
        return rect;
    }

    public void setRect(STRect rect) {
        this.rect = rect;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public STPoint[] getP_key_points() {
        return p_key_points;
    }

    public void setP_key_points(STPoint[] p_key_points) {
        this.p_key_points = p_key_points;
    }

    public int getKey_points_count() {
        return key_points_count;
    }

    public void setKey_points_count(int key_points_count) {
        this.key_points_count = key_points_count;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public int getAnimalType() {
        return animalType;
    }

    public void setAnimalType(int animalType) {
        this.animalType = animalType;
    }

    public float[] getEarScore() {
        return earScore;
    }

    public void setEarScore(float[] earScore) {
        this.earScore = earScore;
    }
}
