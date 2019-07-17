package com.gogotalk.model.util;

import com.gogotalk.model.entity.ResponseModel;
import com.gogotalk.model.exception.ApiException;

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
    public static <T> FlowableTransformer<ResponseModel<T>, T> handleMyResult() {   //compose判断结果
        return new FlowableTransformer<ResponseModel<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<ResponseModel<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<ResponseModel<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(ResponseModel<T> tMyHttpResponse) {
                        if (tMyHttpResponse.getResult()==Constant.SUCCESS_CODE) {
                            return createData(tMyHttpResponse.getData());
                        } else {
                            return Flowable.error(new ApiException(tMyHttpResponse.getMsg(),
                                    Integer.valueOf(tMyHttpResponse.getResult())));
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
