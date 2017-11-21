package com.ljt.latestnews.main.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ${JT.L} on 2017/11/20.
 */

public class OkHttpUtils {
    public static String TAG= OkHttpUtils.class.getSimpleName();

    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpUtils(){
        mOkHttpClient=new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10,TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30,TimeUnit.SECONDS);

        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
    }
    private synchronized static OkHttpUtils getmInstance(){
        if(mInstance==null){
            mInstance=new OkHttpUtils();
        }
        return mInstance;
    }
    private void getRequest(String url,final ResultCallback callback){
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(callback,request);
    }
    private void postRequest(String url, final ResultCallback callback, List<Param> params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    private Request buildPostRequest(String url, List<Param> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    private void deliveryResult(final ResultCallback callback, Request request) {

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailCallback(callback,e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String str = response.body().string();
                    if(callback.mType==String.class){
                       sendSuccessCallBack(callback,str);
                    }else{
                        Object obj = JsonUtils.deserialize(str, callback.mType);
                        sendSuccessCallBack(callback,obj);
                    }
                }catch (Exception e){
                    sendFailCallback(callback,e);
                }
            }
        });

    }
    private void sendFailCallback(final ResultCallback callback, final Exception e){
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if(callback!=null){
                    callback.onFailure(e);
                }
            }
        });
    }
    private void sendSuccessCallBack(final ResultCallback callback, final Object obj){
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if(callback!=null){
                    callback.onSuccess(obj);
                }
            }
        });
    }

    //对外接口
    public static void get(String url,ResultCallback callback){
        getmInstance().getRequest(url,callback);
    }


    //
    public static abstract class ResultCallback<T>{
       Type mType;
       public ResultCallback(){
           mType=getSuperclassTypeParameter(getClass());
       }
       static Type getSuperclassTypeParameter(Class<?> subclass){
           Type superclass = subclass.getGenericSuperclass();
           if(superclass instanceof  Class){
               throw new RuntimeException("Missing type parameter.");
           }
           ParameterizedType parameterized = (ParameterizedType) superclass;
           return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
       }

       public abstract void onSuccess(T response);

       public abstract void onFailure(Exception e);



   }

    /**
     * post请求参数类
     */
    public static class Param {

        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }

}
