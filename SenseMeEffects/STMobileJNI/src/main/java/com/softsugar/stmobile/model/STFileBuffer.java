package com.softsugar.stmobile.model;

import java.util.Arrays;

public class STFileBuffer {
    byte[] fileName;
    byte[] fileBuffer;

    public byte[] getFileBuffer() {
        return fileBuffer;
    }

    public byte[] getFileName() {
        return fileName;
    }

    public void setFileName(byte[] fileName) {
        this.fileName = fileName;
    }

    public void setFileBuffer(byte[] fileBuffer) {
        this.fileBuffer = fileBuffer;
    }
}
