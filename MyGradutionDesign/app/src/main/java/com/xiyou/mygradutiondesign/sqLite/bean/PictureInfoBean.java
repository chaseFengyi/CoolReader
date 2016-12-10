package com.xiyou.mygradutiondesign.sqLite.bean;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Id;

/**
 * Created by fengyi on 16/3/16.
 */
@Table(name="picturetable")
public class PictureInfoBean {

    @Id(column = "picId")
    private int picId;
    private String picName;
    private String picStorePosition;
    private String picJPEGName;
    private String describe;
    private String belongType;

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicStorePosition() {
        return picStorePosition;
    }

    public void setPicStorePosition(String picStorePosition) {
        this.picStorePosition = picStorePosition;
    }

    public String getPicJPEGName() {
        return picJPEGName;
    }

    public void setPicJPEGName(String picJPEGName) {
        this.picJPEGName = picJPEGName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getBelongType() {
        return belongType;
    }

    public void setBelongType(String belongType) {
        this.belongType = belongType;
    }

    @Override
    public String toString() {
        return "PictureInfoBean{" +
                "picId=" + picId +
                ", picName='" + picName + '\'' +
                ", picStorePosition='" + picStorePosition + '\'' +
                ", picJPEGName='" + picJPEGName + '\'' +
                ", describe='" + describe + '\'' +
                ", belongType='" + belongType + '\'' +
                '}';
    }
}
