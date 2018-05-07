package com.sweetmilkcake.beauty.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BeautyBean implements Parcelable {

    /**
     * _id : 59f9674c421aa90fe50c01c6
     * createdAt : 2017-11-01T14:18:52.937Z
     * desc : 11-1
     * publishedAt : 2017-11-01T14:20:59.209Z
     * source : chrome
     * type : 福利
     * url : http://7xi8d6.com1.z0.glb.clouddn.com/20171101141835_yQYTXc_enakorin_1_11_2017_14_16_45_351.jpeg
     * used : true
     * who : daimajia
     */

    private String _id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    // 存储图片的宽和高度
    private int width = 0;
    private int height = 0;

    public BeautyBean() {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.createdAt);
        dest.writeString(this.desc);
        dest.writeString(this.publishedAt);
        dest.writeString(this.source);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeByte((byte) (this.used ? 1 : 0)); // if used == true, byte = 1
        dest.writeString(this.who);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
    }
    
    protected BeautyBean(Parcel in) {
        this._id = in.readString();
        this.createdAt = in.readString();
        this.desc = in.readString();
        this.publishedAt = in.readString();
        this.source = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.used = in.readByte() != 0; // if byte = 1, used = true
        this.who = in.readString();
        // 存储图片的宽和高度
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Parcelable.Creator<BeautyBean> CREATOR = new Parcelable.Creator<BeautyBean>() {

        @Override
        public BeautyBean createFromParcel(Parcel source) {
            return new BeautyBean(source);
        }

        @Override
        public BeautyBean[] newArray(int size) {
            return new BeautyBean[size];
        }
    };
}
