package com.gogotalk.system.model.util;

import java.util.Map;
import okhttp3.RequestBody;

public class HttpUtils {

    public static RequestBody getRequestBody(Map<String,String> map){
        if(map==null||(map!=null&&map.size()==0)){
            throw new RuntimeException("参数不能为空！");
        }
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), GsonUtils.gson.toJson(map));
    }

}
