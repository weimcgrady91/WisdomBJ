package com.qun.test.wisdombj.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.qun.test.wisdombj.R;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Administrator on 2018/4/7.
 */

public class ImageUtil {
    public static ImageOptions sImageOptions;
    static {
        ImageOptions.Builder builder = new ImageOptions.Builder();
        builder.setLoadingDrawableId(R.drawable.news_pic_default);
        sImageOptions = builder.build();
    }
    public static void bindImage(ImageView imageView, String url) {

        x.image().bind(imageView, url, null, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
