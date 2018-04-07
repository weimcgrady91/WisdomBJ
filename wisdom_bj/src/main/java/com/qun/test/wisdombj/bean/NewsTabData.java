package com.qun.test.wisdombj.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/4/6.
 */

public class NewsTabData implements Parcelable {
    private int id;
    private String title;
    private int type;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NewsTabData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeString(this.url);
    }

    public NewsTabData() {
    }

    protected NewsTabData(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.type = in.readInt();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<NewsTabData> CREATOR = new Parcelable.Creator<NewsTabData>() {
        @Override
        public NewsTabData createFromParcel(Parcel source) {
            return new NewsTabData(source);
        }

        @Override
        public NewsTabData[] newArray(int size) {
            return new NewsTabData[size];
        }
    };
}
