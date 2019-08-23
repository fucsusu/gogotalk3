package com.gogotalk.system.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gogotalk.system.R;

import java.util.HashMap;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.android.utils.Logger;

public class AfterClassReporActivity extends AppCompatActivity {

    public WebView mWebView;
    public static final String TAG = "AfterClassReporActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_class_repor);

        mWebView = findViewById(R.id.after_class_report_web);
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void shareWeChat(String path) {
        ShareParams shareParams = new ShareParams();
        shareParams.setTitle("");
        shareParams.setText("");
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setUrl("");
        shareParams.setImagePath("");
        JShareInterface.share("Wechat", shareParams, mPlatActionListener);

    }

    private PlatActionListener mPlatActionListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
            Log.e(TAG, "onComplete:分享完成 ");
        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            Log.e(TAG, "onError: " + "分享失败:" + (error != null ? error.getMessage() : "") + "---" + errorCode);
        }

        @Override
        public void onCancel(Platform platform, int action) {
            Log.e(TAG, "onCancel: 分享取消");
        }
    };

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        //加载assets目录下的html
        //加上下面这段代码可以使网页中的链接不以浏览器的方式打开
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        //得到webview设置
        WebSettings webSettings = mWebView.getSettings();
        //允许使用javascript
        webSettings.setJavaScriptEnabled(true);
        //设置加载网页时暂不加载图片
        webSettings.setBlockNetworkImage(false);
        //设置webview推荐使用的窗口，使html界面自适应屏幕
        webSettings.setUseWideViewPort(true);
        //缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        //设置可以访问文件加载本地html
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setSupportZoom(true);
        //设置图片加载
        webSettings.setLoadsImagesAutomatically(true);
        //设置是否需要手势去播放视频
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);// 必须保留，否则无法播放优酷视频，其他的OK
        //设置不缓存
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置渲染优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        mWebView.addJavascriptInterface(this, "androidApi");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * 当前网页的链接仍在webView中跳转
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            /**
             * 页面载入完成回调
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });


        mWebView.setWebChromeClient(new WebChromeClient() {
            /**
             * 显示自定义视图，无此方法视频不能播放
             */
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }
        });
    }
}
