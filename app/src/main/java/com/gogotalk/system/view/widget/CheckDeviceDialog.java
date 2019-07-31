package com.gogotalk.system.view.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogotalk.system.R;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.AudioRecoderUtils;
import com.gogotalk.system.util.CameraUtils;
import com.gogotalk.system.util.RecordPlayer;
import com.gogotalk.system.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CheckDeviceDialog extends Dialog {

    public CheckDeviceDialog(Context context) {
        super(context);
    }
    public CheckDeviceDialog(Context context, int theme) {
        super(context, theme);
    }
    @Override
    public void show() {
        AppUtils.focusNotAle(this.getWindow());
        super.show();
        AppUtils.fullScreenImmersive(this.getWindow());
        AppUtils.clearFocusNotAle(this.getWindow());
    }
    public static class Builder {
        private String positiveButtonText;
        private String negativeButtonText;
        private View.OnClickListener positiveButtonClickListener;
        private View.OnClickListener negativeButtonClickListener;
        private View.OnClickListener singleButtonClickListener;
        private View contentView;
        private View layout;
        private String singleButtonText;
        private CheckDeviceDialog dialog;
        private ImageView iv_camera,iv_speaker,iv_micro,iv_camera_flag,iv_speaker_flag,iv_micro_flag;
        private int currentStep=0;
        private Activity mContext;
        private List<Boolean> states=new ArrayList<>();
        private boolean isLongClickModule = false;
        private Timer timer = null;
        private Handler handler;
        private Runnable r;
        public Builder(Activity context) {
            handler= new Handler();
            mContext = context;
            final AudioRecoderUtils recoderUtils = new AudioRecoderUtils();
            dialog = new CheckDeviceDialog(context, R.style.CustemDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_check_device, null);
            iv_camera = layout.findViewById(R.id.iv_camera);
            iv_speaker = layout.findViewById(R.id.iv_speaker);
            iv_micro = layout.findViewById(R.id.iv_micro);
            iv_camera_flag = layout.findViewById(R.id.iv_camera_flag);
            iv_speaker_flag = layout.findViewById(R.id.iv_speaker_flag);
            iv_micro_flag = layout.findViewById(R.id.iv_micro_flag);
            CameraUtils.calculateCameraPreviewOrientation(mContext);
            CameraUtils.startPreview();
            final ImageView btn_speaker = layout.findViewById(R.id.btn_speaker);
            btn_speaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(mContext).asGif().load(R.mipmap.ic_dialog_check_device_btn_speaker_gif)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(btn_speaker);
                    final MediaPlayer player = MediaPlayer.create(mContext, R.raw.welcome_gogotalk);
                    player.start();
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            player.pause();
                            player.seekTo(0);
                            btn_speaker.setImageResource(R.mipmap.ic_dialog_check_device_speaker);
                        }
                    });
                }
            });
            final ImageView btn_micro = layout.findViewById(R.id.btn_micro);
            btn_micro.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    isLongClickModule = true;
                                    recoderUtils.startRecord();
                                    mContext.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Glide.with(mContext).asGif().load(R.mipmap.ic_dialog_check_device_btn_microphone_gif)
                                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(btn_micro);
                                        }
                                    });
                                }
                            }, 500);
                            break;
                        case MotionEvent.ACTION_UP:
                            if (isLongClickModule == true) {
                                isLongClickModule = false;
                                if (timer != null) {
                                    timer.cancel();
                                    timer = null;
                                }
                                btn_micro.setImageResource(R.mipmap.ic_dialog_chcek_device_item_microphone);
                                recoderUtils.stopRecord();
                                RecordPlayer player = new RecordPlayer(mContext);
                                File recordFile = new File(Environment.getExternalStorageDirectory(), "/recoder.amr");
                                player.playRecordFile(recordFile);
                            } else {
                                btn_micro.setImageResource(R.mipmap.ic_dialog_chcek_device_item_microphone);
                                ToastUtils.showLongToast(mContext,"至少长按1秒,并说话");
                                if (timer != null) {
                                    timer.cancel();
                                    timer = null;
                                }
                                File recordFile = new File(Environment.getExternalStorageDirectory(), "/recoder.amr");
                                // 判断，若当前文件已存在，则删除
                                if (recordFile.exists()) {
                                    recordFile.delete();
                                }
                            }
                            break;
                    }
                    return true;
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        public void setState(boolean flag){
            if(currentStep==0){
                if(flag){
                    iv_camera.setImageResource(R.mipmap.ic_dialog_check_device_camera_selected);
                    iv_camera_flag.setImageResource(R.mipmap.ic_dialog_check_device_right);
                }else{
                    iv_camera.setImageResource(R.mipmap.ic_dialog_check_device_camera);
                    iv_camera_flag.setImageResource(R.mipmap.ic_dialog_check_device_wrong);
                }
                iv_camera_flag.setVisibility(View.VISIBLE);
                states.add(flag);
                layout.findViewById(R.id.layout_camera).setVisibility(View.GONE);
                layout.findViewById(R.id.layout_speaker).setVisibility(View.VISIBLE);
                layout.findViewById(R.id.layout_microphone).setVisibility(View.GONE);
                layout.findViewById(R.id.layout_error).setVisibility(View.GONE);
                ((TextView) layout.findViewById(R.id.tv_label_no)).setText("听不到");
                ((TextView) layout.findViewById(R.id.tv_label_yes)).setText("可以听到");
            }else if(currentStep==1){
                if(flag){
                    iv_speaker.setImageResource(R.mipmap.ic_dialog_check_device_speaker_selected);
                    iv_speaker_flag.setImageResource(R.mipmap.ic_dialog_check_device_right);
                }else{
                    iv_speaker.setImageResource(R.mipmap.ic_dialog_check_device_speaker);
                    iv_speaker_flag.setImageResource(R.mipmap.ic_dialog_check_device_wrong);
                }
                states.add(flag);
                iv_speaker_flag.setVisibility(View.VISIBLE);
                layout.findViewById(R.id.layout_camera).setVisibility(View.GONE);
                layout.findViewById(R.id.layout_speaker).setVisibility(View.GONE);
                layout.findViewById(R.id.layout_microphone).setVisibility(View.VISIBLE);
                layout.findViewById(R.id.layout_error).setVisibility(View.GONE);
                ((TextView) layout.findViewById(R.id.tv_label_no)).setText("听不到");
                ((TextView) layout.findViewById(R.id.tv_label_yes)).setText("可以听到");
            }else if(currentStep==2){
                if(flag){
                    iv_micro.setImageResource(R.mipmap.ic_dialog_check_device_microphone_selected);
                    iv_micro_flag.setImageResource(R.mipmap.ic_dialog_check_device_right);
                }else{
                    iv_micro.setImageResource(R.mipmap.ic_dialog_check_device_microphone);
                    iv_micro_flag.setImageResource(R.mipmap.ic_dialog_check_device_wrong);
                }
                if(states.size()>2){
                    states.remove(states.size()-1);
                }
                states.add(flag);
                iv_micro_flag.setVisibility(View.VISIBLE);
                if(states.size()==3){
                    if(!states.contains(false)){
                        ToastUtils.showLongToast(mContext,"恭喜，您已检测完毕！");
                        layout.findViewById(R.id.layout_camera).setVisibility(View.GONE);
                        layout.findViewById(R.id.layout_speaker).setVisibility(View.GONE);
                        layout.findViewById(R.id.layout_microphone).setVisibility(View.VISIBLE);
                        layout.findViewById(R.id.layout_error).setVisibility(View.GONE);
                        r = new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                handler.removeCallbacks(r);
                            }
                        };
                        handler.postDelayed(r,3000);
                    }else{
                        handler.removeCallbacks(r);
                        layout.findViewById(R.id.layout_camera).setVisibility(View.GONE);
                        layout.findViewById(R.id.layout_speaker).setVisibility(View.GONE);
                        layout.findViewById(R.id.layout_microphone).setVisibility(View.GONE);
                        layout.findViewById(R.id.layout_error).setVisibility(View.VISIBLE);
                        createSingleButtonDialog();
                    }
                }
            }
        }
        public void reset(){
            currentStep = 0;
            iv_camera_flag.setVisibility(View.INVISIBLE);
            iv_speaker_flag.setVisibility(View.INVISIBLE);
            iv_micro_flag.setVisibility(View.INVISIBLE);
            states.clear();
            layout.findViewById(R.id.layout_camera).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.layout_speaker).setVisibility(View.GONE);
            layout.findViewById(R.id.layout_microphone).setVisibility(View.GONE);
            layout.findViewById(R.id.layout_error).setVisibility(View.GONE);
            createTwoButtonDialog();
            ((TextView) layout.findViewById(R.id.tv_label_no)).setText("看不到");
            ((TextView) layout.findViewById(R.id.tv_label_yes)).setText("可以看到");
        }
        public List<Boolean> getStates(){
            return states;
        }
        public void setCurrentStep(int step){
            currentStep = step;
        }
        public int getCurrentStep(){
            return currentStep;
        }
        public Builder setSpeakerImage(int resId){
            iv_speaker.setImageResource(resId);
            return this;
        }
        public Builder setMicroImage(int resId){
            iv_micro.setImageResource(resId);
            return this;
        }
        public Builder setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }
        public Builder setSingleButton(String singleButtonText, View.OnClickListener listener) {
            this.singleButtonText = singleButtonText;
            this.singleButtonClickListener = listener;
            return this;
        }
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }
        public CheckDeviceDialog createSingleButtonDialog() {
            showSingleButton();
            layout.findViewById(R.id.btn_sigle).setOnClickListener(singleButtonClickListener);
            //如果传入的按钮文字为空，则使用默认的“返回”
            if (singleButtonText != null&&!"".equals(singleButtonText)) {
                ((TextView) layout.findViewById(R.id.tv_label_sigle)).setText(singleButtonText);
            }
            create();
            return dialog;
        }
        public CheckDeviceDialog createTwoButtonDialog() {
            showTwoButton();
            layout.findViewById(R.id.btn_no).setOnClickListener(positiveButtonClickListener);
            layout.findViewById(R.id.btn_yes).setOnClickListener(negativeButtonClickListener);
            if (positiveButtonText != null&&!"".equals(positiveButtonText)) {
                ((TextView) layout.findViewById(R.id.tv_label_no)).setText(positiveButtonText);
            }
            if (negativeButtonText != null&&!"".equals(negativeButtonText)) {
                ((TextView) layout.findViewById(R.id.tv_label_yes)).setText(negativeButtonText);
            }
            create();
            return dialog;
        }



        /**
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        private void create() {
            if (contentView != null) {       //如果使用Builder的setContentview()方法传入了布局，则使用传入的布局
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            dialog.setContentView(layout);
            dialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
            layout.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        /**
         * 显示双按钮布局，隐藏单按钮
         */
        private void showTwoButton() {
            layout.findViewById(R.id.layout_sigle).setVisibility(View.GONE);
            layout.findViewById(R.id.layout_double).setVisibility(View.VISIBLE);
        }

        /**
         * 显示单按钮布局，隐藏双按钮
         */
        private void showSingleButton() {
            layout.findViewById(R.id.layout_sigle).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.layout_double).setVisibility(View.GONE);
        }
    }

}

