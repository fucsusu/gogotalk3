package com.gogotalk.system.model.util;

import android.content.Intent;

import com.gogotalk.system.app.AppManager;
import com.gogotalk.system.model.entity.ResponseModel;
import com.gogotalk.system.model.exception.ApiException;
import com.gogotalk.system.presenter.BaseContract;
import com.gogotalk.system.util.ToastUtils;
import com.gogotalk.system.view.activity.LoginActivity;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {
    /**
     * 统一线程处理
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    /**
     * 统一返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<ResponseModel<T>, T> handleMyResult(BaseContract.View view,boolean isShowSuccessMsg) {   //compose判断结果
        return new FlowableTransformer<ResponseModel<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<ResponseModel<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<ResponseModel<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(ResponseModel<T> responseModel) {
                        if (responseModel.getResult()==Constant.HTTP_SUCCESS_CODE) {
                            if(isShowSuccessMsg){
                                ToastUtils.showShortToast(view.getActivity(),responseModel.getMsg());
                            }
                            return createData(responseModel.getData()==null?((T)new Object()):responseModel.getData());
                        }else if(responseModel.getResult()==Constant.HTTP_TOKEN_EXPIRE_CODE){
                            if(view!=null){
                                AppManager.getAppManager().finishAllActivity();
                                view.getActivity().startActivity(new Intent(view.getActivity(), LoginActivity.class));
                            }
                            return Flowable.error(new ApiException(responseModel.getMsg(),
                                    Integer.valueOf(responseModel.getResult())));
                        }else if(responseModel.getResult()==Constant.HTTP_FAIL_CODE){
                            return Flowable.error(new ApiException(responseModel.getMsg(),
                                    Integer.valueOf(responseModel.getResult())));
                        }else {
                            return Flowable.error(new ApiException(responseModel.getMsg(),
                                    Integer.valueOf(responseModel.getResult())));
                        }
                    }
                });
            }
        };
    }
    /**
     * 生成Flowable
     *
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
