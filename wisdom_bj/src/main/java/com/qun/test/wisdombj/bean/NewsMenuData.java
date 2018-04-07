package com.qun.test.wisdombj.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/6.
 */

public class NewsMenuData implements Parcelable {
    private int id;
    private String title;
    private int type;
    public String url;
    private ArrayList<NewsTabData> children;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

    public ArrayList<NewsTabData> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<NewsTabData> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "NewsMenuData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", children=" + children +
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
        dest.writeTypedList(this.children);
    }

    public NewsMenuData() {
    }

    protected NewsMenuData(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.type = in.readInt();
        this.url = in.readString();
        this.children = in.createTypedArrayList(NewsTabData.CREATOR);
    }

    public static final Creator<NewsMenuData> CREATOR = new Creator<NewsMenuData>() {
        @Override
        public NewsMenuData createFromParcel(Parcel source) {
            return new NewsMenuData(source);
        }

        @Override
        public NewsMenuData[] newArray(int size) {
            return new NewsMenuData[size];
        }
    };
}
