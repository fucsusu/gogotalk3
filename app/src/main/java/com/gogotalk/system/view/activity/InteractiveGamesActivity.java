package com.gogotalk.system.view.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.gogotalk.system.R;
import com.gogotalk.system.model.util.Constant;

import java.lang.reflect.Method;
import butterknife.BindView;

/**
 * 互动游戏界面
 */
@SuppressLint("SetJavaScriptEnabled")
public class InteractiveGamesActivity extends BaseActivity {

    @BindView(R.id.id_mWebView_InterGame)
    WebView webView;
    @BindView(R.id.goback_game)
    ImageView gobank_game;
    private String gameUrl;
    private Dialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            webView.loadUrl(gameUrl);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_interactivegames;
    }

    @Override
    protected void initInject() {
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        Intent mIntent = getIntent();
        gameUrl = mIntent.getStringExtra(Constant.INTENT_DATA_KEY_GAME_URL);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initView() {

        //加载assets目录下的html
        //加上下面这段代码可以使网页中的链接不以浏览器的方式打开
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        //得到webview设置
        WebSettings webSettings = webView.getSettings();
        //允许使用javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);// 必须保留，否则无法播放优酷视频，其他的OK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(webSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= 19) {//设置是否自动加载图片
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }

        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = webSettings.getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webSettings, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        webView.setWebViewClient(new WebViewClient() {
            /**
             * 当前网页的链接仍在webView中跳转
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            /**
             * 处理ssl请求
             */
            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            /**
             * 页面载入完成回调
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:try{autoplay();}catch(e){}");
            }

        });


        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 显示自定义视图，无此方法视频不能播放
             */
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }
        });
        handler.sendEmptyMessageDelayed(1, 1000);
//        webSettings.setDefaultTextEncodingName("UTF-8");
        gobank_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
