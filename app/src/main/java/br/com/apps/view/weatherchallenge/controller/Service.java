package br.com.apps.view.weatherchallenge.controller;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("find")
    Observable<ResponseBody> getWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("lang") String lang,@Query("units") String units,@Query("cnt") String cnt, @Query("appid") String appid);
}
