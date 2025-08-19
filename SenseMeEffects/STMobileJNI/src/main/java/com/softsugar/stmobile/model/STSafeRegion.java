package com.softsugar.stmobile.model;

public class STSafeRegion {
    public class STSafeRegionType{
        public static final int EFFECT_REGION_RECT = 0;
        public static final int EFFECT_REGION_ELLIPSE = 1;
    }

    int type;    // \~chinese 支持矩形、椭圆两种类型 \~english Supports rectangle and ellipse types
    STPoint param1;    // \~chinese 根据类型不同，语义不同，矩形指左上角坐标，椭圆指中心点坐标 \~english Different semantics based on type, rectangle refers to the top left corner coordinate, ellipse refers to the center point coordinate
    STPoint param2;    // \~chinese 根据类型不同，语义不同，矩形指右下角坐标，椭圆指[x: 横轴半径，y: 竖轴半径] \~english Different semantics based on type, rectangle refers to the bottom right corner coordinate, ellipse refers to [x: horizontal axis radius, y: vertical axis radius]

    public STSafeRegion(int type, STPoint param1, STPoint param2){
        this.param1 = param1;
        this.param2 = param2;
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public STPoint getParam1() {
        return param1;
    }

    public STPoint getParam2() {
        return param2;
    }

    public void setParam1(STPoint param1) {
        this.param1 = param1;
    }

    public void setParam2(STPoint param2) {
        this.param2 = param2;
    }
}
