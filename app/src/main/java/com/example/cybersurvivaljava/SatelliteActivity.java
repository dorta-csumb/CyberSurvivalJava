package com.example.cybersurvivaljava;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersurvivaljava.databinding.ActivitySatelliteBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SatelliteActivity extends AppCompatActivity {

    private ActivitySatelliteBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySatelliteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvSatelliteStatus.setText("Uplink Ready... Waiting for Scan");

        binding.btnScan.setOnClickListener(v -> fetchWeatherData());
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void fetchWeatherData() {
        binding.tvSatelliteStatus.setText("Acquiring Signal...");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeather();

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    String weatherData = "TEMP: " + weatherResponse.current_weather.temperature + "°F\n" +
                                           "WIND: " + weatherResponse.current_weather.windspeed + " km/h";
                    binding.tvWeatherData.setText(weatherData);
                    binding.tvSatelliteStatus.setText("Uplink Established (Connected)");
                } else {
                    binding.tvSatelliteStatus.setText("Uplink Failed: No Data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                binding.tvSatelliteStatus.setText("Connection Lost: Atmospheric Interference");
                Log.e("SatelliteActivity", "API Call Failed", t);
            }
        });
    }
}
