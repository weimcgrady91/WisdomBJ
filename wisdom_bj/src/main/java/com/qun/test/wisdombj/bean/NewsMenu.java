package com.qun.test.wisdombj.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/5.
 */

public class NewsMenu implements Parcelable {
    private int retcode;
    private ArrayList<NewsMenuData> data;
    public ArrayList<Integer> extend;
    private int type;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public ArrayList<NewsMenuData> getData() {
        return data;
    }

    public void setData(ArrayList<NewsMenuData> data) {
        this.data = data;
    }

    public ArrayList<Integer> getExtend() {
        return extend;
    }

    public void setExtend(ArrayList<Integer> extend) {
        this.extend = extend;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NewsMenu{" +
                "retcode=" + retcode +
                ", data=" + data +
                ", extend=" + extend +
                ", type=" + type +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.retcode);
        dest.writeTypedList(this.data);
        dest.writeList(this.extend);
        dest.writeInt(this.type);
    }

    public NewsMenu() {
    }

    protected NewsMenu(Parcel in) {
        this.retcode = in.readInt();
        this.data = in.createTypedArrayList(NewsMenuData.CREATOR);
        this.extend = new ArrayList<Integer>();
        in.readList(this.extend, Integer.class.getClassLoader());
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<NewsMenu> CREATOR = new Parcelable.Creator<NewsMenu>() {
        @Override
        public NewsMenu createFromParcel(Parcel source) {
            return new NewsMenu(source);
        }

        @Override
        public NewsMenu[] newArray(int size) {
            return new NewsMenu[size];
        }
    };
}
