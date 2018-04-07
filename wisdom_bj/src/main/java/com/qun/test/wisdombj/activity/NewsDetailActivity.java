package com.qun.test.wisdombj.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qun.test.wisdombj.R;

public class NewsDetailActivity extends AppCompatActivity {

    private ImageButton mIbBack;
    private TextView mTvTitle;
    private ImageButton mIbTextSize;
    private ImageButton mIbShared;
    private WebView mWebView;
    private static final String EXTRA_URL = "EXTRA_URL";
    private ProgressBar mPb;

    public static void enter(Context context, String url) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initViews();
        loadUrl();
    }

    private void loadUrl() {
        mUrl = getIntent().getExtras().getString(EXTRA_URL);
        mWebView.loadUrl(mUrl);
    }

    private void initViews() {
        mPb = findViewById(R.id.pb);
        mPb.setMax(100);
        mIbBack = findViewById(R.id.ib_back);
        mIbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle = findViewById(R.id.tv_title);
        mIbTextSize = findViewById(R.id.ib_text_size);
        mIbTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseDialog();
            }
        });
        mIbShared = findViewById(R.id.ib_shared);
        mWebView = findViewById(R.id.wv);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mPb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mPb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTvTitle.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mPb.setProgress(newProgress);
            }
        });
    }

    private int mTempWhich;// 记录临时选择的字体大小(点击确定之前)

    private int mCurrentWhich = 2;// 记录当前选中的字体大小(点击确定之后), 默认正常字体

    /**
     * 展示选择字体大小的弹窗
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");

        String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
                "超小号字体" };

        builder.setSingleChoiceItems(items, mCurrentWhich,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTempWhich = which;
                    }
                });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 根据选择的字体来修改网页字体大小

                WebSettings settings = mWebView.getSettings();

                switch (mTempWhich) {
                    case 0:
                        // 超大字体
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        // settings.setTextZoom(22);
                        break;
                    case 1:
                        // 大字体
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        // 正常字体
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        // 小字体
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        // 超小字体
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;

                    default:
                        break;
                }

                mCurrentWhich = mTempWhich;
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }
}
