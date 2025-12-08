package com.example.cybersurvivaljava;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WeatherService {
    @GET("v1/forecast?latitude=36.65&longitude=-121.80&current_weather=true&temperature_unit=fahrenheit")
    Call<WeatherResponse> getCurrentWeather();
}
