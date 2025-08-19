package com.softsugar.stmobile.model;

import android.hardware.Camera;

import com.softsugar.stmobile.STMobileHumanActionNative;
import com.softsugar.stmobile.params.STRotateType;

/**
 * \~Chinese 人脸、手势等信息定义 \~English faceInfo and handInfo and ...
 */
public class STHumanAction implements Cloneable {
    int bufIndex;             ///< \~Chinese 对应的图像数据缓冲区索引，用于双缓冲渲染时使用 \~English

    public STMobileFaceInfo[] faces; ///< \~Chinese 检测到的人脸信息 \~English face info array
    public int faceCount;            ///< \~Chinese 检测到的人脸数目 \~English face count

    STMobileHandInfo[] hands; ///< \~Chinese 检测到的手的信息 \~English  hand info array
    int handCount;            ///< \~Chinese 检测到的手的数目 (目前仅支持检测一只手) \~English hand count

    public STMobileHeadInfo[] heads; ///< \~Chinese 检测到的头的信息 \~English head info array
    public int headCount;            ///< \~Chinese 检测到的头的数目 \~English head count

    private STMobileFoot[] feets;    /// \~Chinese 检测到的脚的信息 \~English feet array
    private int footCount;           /// \~Chinese 检测到的脚的个数 \~English feet count

    private STMobileWrist[] wrists;  /// \~Chinese 检测到的手腕的信息 \~English wrist array
    private int wristCount;           /// \~Chinese 检测到的手腕的个数 \~English wrist count

    STHumanActionSegments humanActionSegments;//\~Chinese 分割类结果 \~English segment info

    public STMobileBodyInfo[] bodys; ///< \~Chinese 检测到的人体信息 \~English body info array
    public int bodyCount;            ///< \~Chinese 检测到的人体的数目 \~English body count

    public STMobile106[] getMobileFaces() {
        if (faceCount == 0) {
            return null;
        }

        STMobile106[] arrayFaces = new STMobile106[faceCount];
        for(int i = 0; i < faceCount; ++i) {
            arrayFaces[i] = faces[i].face106;
        }

        return arrayFaces;
    }

    public boolean replaceMobile106(STMobile106[] arrayFaces) {
        if (arrayFaces == null || arrayFaces.length == 0
                || faces == null || faceCount != arrayFaces.length) {
            return false;
        }

        for (int i = 0; i < arrayFaces.length; ++i) {
            faces[i].face106 = arrayFaces[i];
        }
        return true;
    }

    public STMobileFaceInfo[] getFaceInfos() {
        if (faceCount == 0) {
            return null;
        }

        return faces;
    }

    public STMobileHandInfo[] getHandInfos() {
        if (handCount == 0) {
            return null;
        }

        return hands;
    }

    public int getBodyCount() {
        return bodyCount;
    }

    public int getBufIndex() {
        return bufIndex;
    }

    public int getFaceCount() {
        return faceCount;
    }

    public int getHandCount() {
        return handCount;
    }

    public STMobileBodyInfo[] getBodys() {
        return bodys;
    }

    public STMobileFaceInfo[] getFaces() {
        return faces;
    }

    public STMobileHandInfo[] getHands() {
        return hands;
    }

    public void setBodyCount(int bodyCount) {
        this.bodyCount = bodyCount;
    }

    public void setBodys(STMobileBodyInfo[] bodys) {
        this.bodys = bodys;
    }

    public void setBufIndex(int bufIndex) {
        this.bufIndex = bufIndex;
    }

    public void setFaceCount(int faceCount) {
        this.faceCount = faceCount;
    }

    public void setFaces(STMobileFaceInfo[] faces) {
        this.faces = faces;
    }

    public void setHandCount(int handCount) {
        this.handCount = handCount;
    }

    public void setHands(STMobileHandInfo[] hands) {
        this.hands = hands;
    }

    public STHumanActionSegments getHumanActionSegments() {
        return humanActionSegments;
    }

    public void setHumanActionSegments(STHumanActionSegments humanActionSegments) {
        this.humanActionSegments = humanActionSegments;
    }

    public int getHeadCount() {
        return headCount;
    }

    public STMobileHeadInfo[] getHeads() {
        return heads;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }

    public void setHeads(STMobileHeadInfo[] heads) {
        this.heads = heads;
    }

    /**
     * 镜像human_action检测结果
     *
     * @param width        用于转换的图像的宽度(以像素为单位)
     * @param humanAction  需要镜像的STHumanAction对象
     * @return 成功返回0，错误返回其他，参考STUtils.ResultCode
     */
    public static native STHumanAction humanActionMirror(int width, STHumanAction humanAction);

    /**
     * 旋转human_action检测结果
     *
     * @param width        用于转换的图像的宽度(以像素为单位)
     * @param width        用于转换的图像的宽度(以像素为单位)
     * @param orientation  顺时针旋转的角度,例如STRotateType.ST_CLOCKWISE_ROTATE_90（旋转90度）
     * @param rotateBackGround 是否旋转前后背景分割结果
     * @param humanAction  需要镜像的STHumanAction对象
     * @return 成功返回0，错误返回其他，参考STUtils.ResultCode
     */
    public static native STHumanAction humanActionRotate(int width, int height, int orientation, boolean rotateBackGround, STHumanAction humanAction);

    /**
     * resize human_action检测结果
     *
     * @param scale        缩放的尺度
     * @param humanAction  需要resize的STHumanAction对象
     * @return 成功返回新humanActon，错误返回null
     */
    public static native STHumanAction humanActionResize(float scale, STHumanAction humanAction);

    /**
     * 根据摄像头ID和摄像头方向，重新计算HumanAction（双输入场景使用）
     * @param humanAction  输入HumanAction
     * @param width        图像宽度
     * @param height       图像高度
     * @param cameraId     摄像头ID
     * @param cameraOrientation  摄像头方向
     * @param deviceOrientation  设备方向
     * @return  旋转或镜像后的HumanAction
     */
    public static STHumanAction humanActionRotateAndMirror(STHumanAction humanAction, int width, int height, int cameraId, int cameraOrientation, int deviceOrientation){
        if(humanAction == null){
            return null;
        }
        if(cameraId != Camera.CameraInfo.CAMERA_FACING_FRONT && cameraId != Camera.CameraInfo.CAMERA_FACING_BACK){
            return humanAction;
        }

        //flip
        if(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && (deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_0 || deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_180)){
            if(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){
                humanAction = humanActionMirror(height, humanAction);
            }
        }

        //前置摄像头一般是270，后置是90
        switch (cameraOrientation){
            case 90:
                humanAction = humanActionRotate(height, width, STRotateType.ST_CLOCKWISE_ROTATE_90, false, humanAction);
                break;
            case 270:
                if(deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_0 || deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_180){
                    humanAction = humanActionRotate(height, width, STRotateType.ST_CLOCKWISE_ROTATE_90, false, humanAction);
                }else {
                    humanAction = humanActionRotate(height, width, STRotateType.ST_CLOCKWISE_ROTATE_270, false, humanAction);
                }
                break;
            default:
                return humanAction;
        }

        //mirror
        if(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && (deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_90 || deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_270)){
            humanAction = humanActionMirror(width, humanAction);
        }

        return humanAction;
    }

    @Override
    public Object clone() {
        STHumanAction humanAction = null;
        try{
            humanAction = (STHumanAction) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return humanAction;
    }

    public static void nativeHumanActionRotateAndMirror(STMobileHumanActionNative humanActionHandle, long humanAction, int width, int height, int cameraId, int cameraOrientation, int deviceOrientation){

        if(cameraId != Camera.CameraInfo.CAMERA_FACING_FRONT && cameraId != Camera.CameraInfo.CAMERA_FACING_BACK){
            return;
        }

        //flip
        if(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && (deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_0 || deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_180)){
            if(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){
                humanActionHandle.nativeHumanActionMirrorPtr(height);
            }
        }

        //前置摄像头一般是270，后置是90
        switch (cameraOrientation){
            case 90:
                humanActionHandle.nativeHumanActionRotatePtr(height, width, STRotateType.ST_CLOCKWISE_ROTATE_90, false);
                break;
            case 270:
                if(deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_0 || deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_180){
                    humanActionHandle.nativeHumanActionRotatePtr(height, width, STRotateType.ST_CLOCKWISE_ROTATE_90, false);
                }else {
                    humanActionHandle.nativeHumanActionRotatePtr(height, width, STRotateType.ST_CLOCKWISE_ROTATE_270, false);
                }
                break;
            default:
                return;
        }

        //mirror
        if(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && (deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_90 || deviceOrientation == STRotateType.ST_CLOCKWISE_ROTATE_270)){
            humanActionHandle.nativeHumanActionMirrorPtr(width);
        }
    }

    /**
     * 根据摄像头ID和摄像头方向，重新计算HumanAction（双输入场景使用）
     * @param humanAction  输入HumanAction
     * @param width        图像宽度
     * @param height       图像高度
     * @param cameraId     摄像头ID
     * @param cameraOrientation  摄像头方向
     * @return  旋转或镜像后的HumanAction
     */
    public static STHumanAction humanActionRotateAndMirror(STHumanAction humanAction, int width, int height, int cameraId, int cameraOrientation){
        if(humanAction == null){
            return null;
        }
        if(cameraId != Camera.CameraInfo.CAMERA_FACING_FRONT && cameraId != Camera.CameraInfo.CAMERA_FACING_BACK){
            return humanAction;
        }
        if(cameraOrientation != 90 && cameraOrientation != 270){
            return humanAction;
        }
        //humanAction rotate && mirror
        if(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && cameraOrientation == 90){
            humanAction = humanActionRotate(height, width, STRotateType.ST_CLOCKWISE_ROTATE_90, false, humanAction);
            humanAction = humanActionMirror(width, humanAction);
        }else if(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && cameraOrientation == 270){
            humanAction = humanActionRotate(height, width, STRotateType.ST_CLOCKWISE_ROTATE_270, false, humanAction);
            humanAction = humanActionMirror(width, humanAction);
        }else if(cameraId == Camera.CameraInfo.CAMERA_FACING_BACK && cameraOrientation == 270){
            humanAction = humanActionRotate(height, width, STRotateType.ST_CLOCKWISE_ROTATE_270, false, humanAction);
        }else if(cameraId == Camera.CameraInfo.CAMERA_FACING_BACK && cameraOrientation == 90){
            humanAction = humanActionRotate(height, width, STRotateType.ST_CLOCKWISE_ROTATE_90, false, humanAction);
        }

        return humanAction;
    }

    public STMobileFoot[] getFeets() {
        return feets;
    }

    public void setFeets(STMobileFoot[] feets) {
        this.feets = feets;
    }

    public int getFootCount() {
        return footCount;
    }

    public void setFootCount(int footCount) {
        this.footCount = footCount;
    }
}
