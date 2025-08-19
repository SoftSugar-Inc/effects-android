package softsugar.senseme.com.effects.display;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import softsugar.senseme.com.effects.encoder.MediaVideoEncoder;
import softsugar.senseme.com.effects.entity.BasicBeautyTitleItem;
import softsugar.senseme.com.effects.view.BeautyItem;

/**
 * 相机显示接口，定义了相机预览、特效处理、滤镜、贴纸和美颜等功能
 * 提供了相机应用中所有视觉效果相关的操作接口
 * 
 * @author SenseMe Effects Team
 * @version 1.0
 */
public interface CameraDisplay {

    // ============== 生命周期管理 ==============
    
    /**
     * 恢复相机预览和特效处理
     * 通常在Activity/Fragment的onResume()中调用
     */
    void onResume();

    /**
     * 暂停相机预览和特效处理
     * 通常在Activity/Fragment的onPause()中调用
     */
    void onPause();

    /**
     * 销毁资源，释放内存
     * 通常在Activity/Fragment的onDestroy()中调用
     */
    void onDestroy();

    // ============== 相机控制 ==============
    
    /**
     * 开启相机预览
     * 初始化相机并开始预览显示
     */
    void startCamera();

    /**
     * 切换前后置摄像头
     * 在前置和后置摄像头之间切换
     */
    void switchCamera();

    /**
     * 修改相机预览分辨率
     * @param index 分辨率索引，对应预设的分辨率列表
     */
    void changePreviewSize(int index);

    // ============== 显示控制 ==============
    
    /**
     * 设置是否显示原图
     * @param isShow true-显示原图(无特效), false-显示特效处理后的图像
     */
    void setShowOriginal(boolean isShow);

    /**
     * 拍照
     */
    void setSaveImage();

    void setVideoEncoder(final MediaVideoEncoder encoder);
    int getPreviewWidth();

    int getPreviewHeight();

    /**
     * 设置图片保存监听器
     * @param saveImageListener 保存图片的回调监听器
     */
    void setOnSaveImageListener(SavePicListener saveImageListener);

    // ============== 滤镜效果 ==============

    /**
     * 设置滤镜样式
     * @param modelPath 滤镜模型文件路径
     */
    void setFilter(String modelPath);

    /**
     * 设置滤镜强度
     * @param strength 滤镜强度，范围通常为0.0-1.0
     */
    void setFilterStrength(float strength);

    // ============== 贴纸效果 ==============
    
    /**
     * 添加贴纸效果
     * @param stickerPath 贴纸资源路径
     */
    void addSticker(String stickerPath);

    /**
     * 移除指定贴纸
     * @param stickerPath 要移除的贴纸资源路径
     */
    void removeSticker(String stickerPath);

    /**
     * 清空所有贴纸效果
     * 移除当前应用的所有贴纸和通用物体追踪效果
     */
    void clearAllSticker();

    /**
     * 重播素材包效果
     * 重新播放当前的动态贴纸或特效素材
     */
    void replayPackage();

    // ============== 基础美颜 ==============
    
    /**
     * 重置基础美颜效果
     * 将指定分类的美颜效果重置为默认值
     * 
     * @param currentTitleData 当前美颜分类标题数据
     * @param dataList 该分类下的美颜项目列表
     */
    void resetBasicBeauty(@NotNull BasicBeautyTitleItem currentTitleData, @NotNull ArrayList<BeautyItem> dataList);

    /**
     * 设置基础美颜参数
     * 调整指定美颜项目的强度
     * 
     * @param titleData 美颜分类标题数据
     * @param beautyItem 具体的美颜项目
     * @param strength 美颜强度，范围通常为0.0-1.0
     */
    void setBasicBeauty(BasicBeautyTitleItem titleData, @NotNull BeautyItem beautyItem, float strength);
}
