package com.softsugar.stmobile.model;

public class STEffectRenderOutParam {

    //\~Chinese 输出纹理信息，需上层初始化  \~English output texture
    private STEffectTexture texture;

    //\~Chinese 输出图像数据，用于推流等场景，不需要可传null \~English output image buffer
    private STImage image;

    //\~Chinese 输出脸部变形后的人脸检测结果，不需要可传null \~English output human action info
    private STHumanAction humanAction;

    /**
     * \~Chinese 引擎渲染接口输出参数
     *
     * @param texture     \~Chinese 输出纹理信息，需上层初始化
     * @param humanAction \~Chinese 输出脸部变形后的人脸检测结果，不需要可传null
     * @param image       \~Chinese 输出图像数据，用于推流等场景，不需要可传null
     *
     * \~English Effects render output params
     *
     * @param texture     \~English output texture
     * @param humanAction \~English output human action info
     * @param image       \~English output image buffer
     */
    public STEffectRenderOutParam(STEffectTexture texture, STImage image, STHumanAction humanAction){
        this.texture = texture;
        this.image = image;
        this.humanAction = humanAction;
    }

    public STImage getImage() {
        return image;
    }

    public STEffectTexture getTexture() {
        return texture;
    }

    public STHumanAction getHumanAction() {
        return humanAction;
    }

    public void setImage(STImage image) {
        this.image = image;
    }

    public void setTexture(STEffectTexture texture) {
        this.texture = texture;
    }

    public void setHumanAction(STHumanAction humanAction) {
        this.humanAction = humanAction;
    }
}
