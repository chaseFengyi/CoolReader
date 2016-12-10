package com.xiyou.mygradutiondesign.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fengyi on 16/3/13.
 */
public class TrafficSignBean implements Parcelable {
    private Bitmap icon;
    //图片名称
    private String title;
    private String signExplain;
    //图片存储的时候存储名（11.jpg）
    private String address;
    //该交通标志所属类别
    private String belongTypes;

    public TrafficSignBean() {
    }
    private TrafficSignBean(Parcel in) {
        icon = in.readParcelable(null);
        title = in.readString();
        signExplain = in.readString();
        address = in.readString();
        belongTypes = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(icon,flags);
        dest.writeString(title);
        dest.writeString(signExplain);
        dest.writeString(address);
        dest.writeString(belongTypes);
    }

    public static final Parcelable.Creator<TrafficSignBean> CREATOR = new Parcelable.Creator<TrafficSignBean>(){

        @Override
        public TrafficSignBean createFromParcel(Parcel source) {
            return new TrafficSignBean(source);
        }

        @Override
        public TrafficSignBean[] newArray(int size) {
            return new TrafficSignBean[size];
        }
    };

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSignExplain() {
        return signExplain;
    }

    public void setSignExplain(String signExplain) {
        this.signExplain = signExplain;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBelongTypes() {
        return belongTypes;
    }

    public void setBelongTypes(String belongTypes) {
        this.belongTypes = belongTypes;
    }

    @Override
    public String toString() {
        return "TrafficSignBean{" +
                "icon=" + icon +
                ", title='" + title + '\'' +
                ", signExplain='" + signExplain + '\'' +
                ", address='" + address + '\'' +
                ", belongTypes='" + belongTypes + '\'' +
                '}';
    }
}
