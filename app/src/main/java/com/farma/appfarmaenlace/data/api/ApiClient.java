package com.farma.appfarmaenlace.data.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiClient {
    private final Retrofit retrofit;

    public ApiClient(String ip) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .readTimeout(360, TimeUnit.SECONDS)
                .connectTimeout(20,TimeUnit.SECONDS)
                .writeTimeout(360,TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(ip)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create().asLenient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Retrofit retrofitInstance(){
        return retrofit;
    }
}
