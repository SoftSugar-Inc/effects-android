package com.softsugar.stmobile.model;

public class STGanRequest {

    private byte[] category;
    private byte[] function;
    private STFileBuffer[] zips;
    private STImage inImage;
    private STHumanAction humanAction;

    public byte[] getCategory() {
        return category;
    }

    public STFileBuffer[] getZips() {
        return zips;
    }

    public STImage getInImage() {
        return inImage;
    }

    public STHumanAction getHumanAction() {
        return humanAction;
    }

    public void setHumanAction(STHumanAction humanAction) {
        this.humanAction = humanAction;
    }

    public void setCategory(byte[] category) {
        this.category = category;
    }

    public byte[] getFunction() {
        return function;
    }

    public void setFunction(byte[] function) {
        this.function = function;
    }

    public void setZips(STFileBuffer[] zips) {
        this.zips = zips;
    }

    public void setInImage(STImage inImage) {
        this.inImage = inImage;
    }

    @Override
    public String toString() {
        return "STGanRequest{" +
                "name=" + new String(category) +
                ", inImage=" + inImage +
                '}';
    }
}
