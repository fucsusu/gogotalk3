package com.gogotalk.model.util;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

public class HttpUtils {

    public static RequestBody getRequestBody(Map<String,String> map){
        if(map==null||(map!=null&&map.size()==0)){
            throw new RuntimeException("参数不能为空！");
        }
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), EntityUtils.gson.toJson(map));
    }
    public static class BaseResponseObserver<T> implements Observer<T> {


        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(T t) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
