package com.softsugar.stmobile.params;

import java.util.Arrays;

public class STAudio {

    /**
     * \~Chinese 语句ID, 独立音频参数无作用，同一语句tts分段音频不同音频段必须使用相同的id，用户自己分配id
     */
    private int sentenceId;

    /**
     * \~Chinese 音频数据
     */
    private byte[] pcmData;

    /**
     * \~Chinese 音频长度
     */
    private int pcmLength;

    public int getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
    }

    public byte[] getPcmData() {
        return pcmData;
    }

    public void setPcmData(byte[] pcmData) {
        this.pcmData = pcmData;
    }

    public int getPcmLength() {
        return pcmLength;
    }

    public void setPcmLength(int pcmLength) {
        this.pcmLength = pcmLength;
    }

    @Override
    public String toString() {
        return "STAudio{" +
                "sentenceId=" + sentenceId +
                ", pcmData=" + Arrays.toString(pcmData) +
                ", pcmLength=" + pcmLength +
                '}';
    }
}
