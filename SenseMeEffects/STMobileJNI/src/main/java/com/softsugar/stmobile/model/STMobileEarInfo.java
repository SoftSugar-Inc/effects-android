package com.softsugar.stmobile.model;

public class STMobileEarInfo {

    public STPoint[] earPoints;     ///< \~Chinese 耳朵关键点. 没有检测到时为NULL.耳朵左右各有18个关键点，共36个关键点，1-5为左耳靠近内耳区域的一条线，6-18为左耳外耳廓，19-23为右耳靠近内耳区域的一条线，24-36为右耳外耳廓 \~English era key points
    public int earPointsCount;      ///< \~Chinese 耳朵关键点个数. 检测到时为ST_MOBILE_EAR_POINTS_COUNT, 没有检测到时为0 \~English ear key points count
    public float leftEarScore;      ///< \~Chinese 左耳检测结果置信度: [0.0, 1.0] \~English left ear score
    public float rightEarScore;     ///< \~Chinese 右耳检测结果置信度: [0.0, 1.0] \~English right ear score

}
