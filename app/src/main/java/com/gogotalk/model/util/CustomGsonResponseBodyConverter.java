package com.gogotalk.model.util;

import android.util.Log;

import com.gogotalk.model.entity.ResponseModel;
import com.gogotalk.model.exception.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 * Created by xkai on 2018/7/26.
 */

public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            Logger.json(value.string());
            ResponseModel responseModel = gson.fromJson(value.string(), ResponseModel.class);
            if(responseModel.getData()==null){
                responseModel.setData(new Object());
            }
            return adapter.fromJson(gson.toJson(responseModel));
        }finally {
            value.close();
        }
    }
}