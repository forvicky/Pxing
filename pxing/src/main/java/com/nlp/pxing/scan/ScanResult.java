package com.nlp.pxing.scan;

import java.io.Serializable;

/**
 * 扫码结果类
 * Created by zdd on 2018/11/7
 */
public class ScanResult implements Serializable {
    private static final long serialVersionUID = -3908765691121463609L;
    private String lawsText;   //非隐藏区
    private String hiddenText; //隐藏区

    public ScanResult(String lawsText) {
        this.lawsText = lawsText;
        this.hiddenText = "null";
    }

    public ScanResult(String lawsText, String hiddenText) {
        this.lawsText = lawsText;
        if (hiddenText == null) {
            this.hiddenText = "null";
        } else {
            this.hiddenText = hiddenText;
        }

    }

    //是否是NLP的专属码
    public boolean isNLP(){
        return !"null".equals(hiddenText);
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
