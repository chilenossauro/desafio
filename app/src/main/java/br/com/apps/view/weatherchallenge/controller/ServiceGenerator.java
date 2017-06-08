package br.com.apps.view.weatherchallenge.controller;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import br.com.apps.view.weatherchallenge.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ServiceGenerator {

    public static <S> S createService(Class<S> serviceClass) {

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.URL_API)
                .client(new OkHttpClient().newBuilder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(serviceClass);
    }
}
