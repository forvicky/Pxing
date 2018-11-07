package com.nlp.pxing.scan;

/**
 * Created by zdd on 2018/11/7
 */
public class ScanResult {
    private String lawsText;   //非隐藏区
    private String hiddenText; //隐藏区

    public ScanResult(String lawsText, String hiddenText) {
        this.lawsText = lawsText;
        this.hiddenText = hiddenText;
    }

    public String getLawsText() {
        return lawsText;
    }

    public void setLawsText(String lawsText) {
        this.lawsText = lawsText;
    }

    public String getHiddenText() {
        return hiddenText;
    }

    public void setHiddenText(String hiddenText) {
        this.hiddenText = hiddenText;
    }
}
