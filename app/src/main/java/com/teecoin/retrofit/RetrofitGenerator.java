package com.teecoin.retrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teecoin.utils.TCUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitGenerator {


    public static <S> S createService(@NonNull Class<S> serviceClass, String url) {
        try {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Content-Language", TCUtils.getLanguageCode())
                                    .header("Content-Type", "application/json")
                                    .header("Authorization", TCUtils.getUserToken())
                                    .header("User-Agent", TCUtils.getUserAgent())
                                    .method(original.method(), original.body());

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    });
            if (TCUtils.isShowLog()) {
                httpClientBuilder.addInterceptor(logging);
            }
            OkHttpClient okHttpClient = httpClientBuilder.build();

            Gson gson =new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(serviceClass);
        } catch (Exception e) {
            Log.e("Retrofit error: ", e.toString());
        }
        return null;
    }

}
