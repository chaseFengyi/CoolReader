package com.xiyou.mygradutiondesign.sqLite.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import java.util.Arrays;

/**
 * Created by fengyi on 16/5/3.
 */
@Table(name="destpicturetable")
public class DestPicInfoBean {
    @Id(column = "destPicId")
    private int destPicId;
    private String picName;
    private String picDescribe;
    private String picBelongType;
    private int picID;
    private double[] featureValue;//图片的特征向量

    public int getDestPicId() {
        return destPicId;
    }

    public void setDestPicId(int destPicId) {
        this.destPicId = destPicId;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicDescribe() {
        return picDescribe;
    }

    public void setPicDescribe(String picDescribe) {
        this.picDescribe = picDescribe;
    }

    public String getPicBelongType() {
        return picBelongType;
    }

    public void setPicBelongType(String picBelongType) {
        this.picBelongType = picBelongType;
    }

    public int getPicID() {
        return picID;
    }

    public void setPicID(int picID) {
        this.picID = picID;
    }

    public double[] getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(double[] featureValue) {
        this.featureValue = featureValue;
    }

    @Override
    public String toString() {
        return "DestPicInfoBean{" +
                "destPicId=" + destPicId +
                ", picName='" + picName + '\'' +
                ", picDescribe='" + picDescribe + '\'' +
                ", picBelongType='" + picBelongType + '\'' +
                ", picID=" + picID +
                ", featureValue=" + Arrays.toString(featureValue) +
                '}';
    }
}
