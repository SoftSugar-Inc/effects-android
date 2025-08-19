package com.softsugar.stmobile.model;

public class STHumanActionSegments {

    private STSegment image;            ///< \~Chinese 前后背景分割图片信息,前景为0,背景为255,边缘部分有模糊(0-255之间),输出图像大小可以调节 \~English figure segment result
    private STSegment hair;             ///< \~Chinese 头发分割图片信息,前景为0,背景为255,边缘部分有模糊(0-255之间),输出图像大小可以调节 \~English hair segment result
    private STSegment skin;             ///< \~Chinese 皮肤分割图片信息,前景为0,背景为255,边缘部分有模糊(0-255之间),输出图像大小可以调节 \~English skin segment result
    private STSegment head;             ///< \~Chinese 头部分割图片信息,前景为0,背景为255,边缘部分有模糊(0-255之间),输出图像大小可以调节 \~English head segment result
    private STSegment[] mouthParses;    ///< \~Chinese 嘴唇遮挡信息 \~English mouth parse array
    private int mouthParseCount;        ///< \~Chinese 嘴唇遮挡信息个数 \~English mouth parse count
    private STSegment sky;             ///<  \~Chinese 天空分割图片信息,前景为0,背景为255,边缘部分有模糊(0-255之间),输出图像大小可以调节 \~English sky segment result
    private STSegment depth;             ///<  \~Chinese 深度估计信息 \~English  depth info

    private STSegment[] faceOcclusions; ///<  \~Chinese 深度估计信息 \~English face occlusion array
    private int faceOcclusionCount;///<  \~Chinese 深度估计信息 \~English  face occlusion count

    private int headCount; ///<  \~Chinese 人头个数 \~English head count
    private STSegment multiSegment; ///\~Chinese 多类分割结果图片信息,背景0，手1，头发2，眼镜3，躯干4，上臂5，下臂6，大腿7，小腿8，脚9，帽子10，随身物品11，脸12，上衣13，下装14，输出图像大小可以调节 \~English multi segment ifo


    public STSegment getFigureSegment(){
        return image;
    }

    public STSegment getHair(){
        return hair;
    }
    public STSegment getSkin(){
        return skin;
    }
    public STSegment getHead(){
        return head;
    }

    public STSegment[] getMouthParses(){
        return mouthParses;
    }

    public int getMouthParseCount(){
        return mouthParseCount;
    }

    public STSegment getMultiSegment(){
        return multiSegment;
    }

    public int getHeadCount(){
        return headCount;
    }

    public int getFaceOcclusionCount() {
        return faceOcclusionCount;
    }

    public STSegment[] getFaceOcclusions() {
        return faceOcclusions;
    }

    public STSegment getDepth() {
        return depth;
    }

    public STSegment getSky() {
        return sky;
    }

    public STSegment getImage() {
        return image;
    }
}
