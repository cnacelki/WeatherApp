package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView resultText,resultWind;
    ImageView resultImage;
    LinearLayout layout1,layout2,layout3,layout4,layout5,layout6,layout7;
    DecimalFormat df = new DecimalFormat("#.#");
    LinearLayout[] layouts = new LinearLayout[7];
    String forecastUrl ="";

    private final String apikey = "566f531109d265633437418ae7db5607";
    private final String currenturl = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final String dailyurl = "https://api.openweathermap.org/data/2.5/onecall?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = findViewById(R.id.cityText);
        resultText = findViewById(R.id.resultText);
        resultImage = findViewById(R.id.resultImg);
        resultWind = findViewById(R.id.resultWind);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout5 = findViewById(R.id.layout5);
        layout6 = findViewById(R.id.layout6);
        layout7 = findViewById(R.id.layout7);
        layouts[0]=layout1;
        layouts[1]=layout2;
        layouts[2]=layout3;
        layouts[3]=layout4;
        layouts[4]=layout5;
        layouts[5]=layout6;
        layouts[6]=layout7;


    }
    public void setWeather(String desc, ImageView img){
        switch (desc) {
            case "Clear":
                img.setImageResource(R.drawable.sun);
                break;
            case "Clouds":
                img.setImageResource(R.drawable.cloud);
                break;
            case "Rain":
                img.setImageResource(R.drawable.rain);
                break;
            case "Snow":
                img.setImageResource(R.drawable.snowfall);
                break;
        }
    }
    public void getWeather(View view) {
        String location = cityName.getText().toString().trim();
        String finalUrl = currenturl + location + "&appid=" + apikey;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonRes = new JSONObject(response);
                    //Get data from coord object
                    JSONObject jsonCoord = jsonRes.getJSONObject("coord");
                    Double lon =jsonCoord.getDouble("lon");
                    Double lat =jsonCoord.getDouble("lat");

                    //Get data from wind object
                    JSONObject jsonWind = jsonRes.getJSONObject("wind");
                    String windSpeed =""+jsonWind.getDouble("speed");

                    //Get data from weather object
                    JSONArray jsonArr = jsonRes.getJSONArray("weather");
                    JSONObject jsonWeather = jsonArr.getJSONObject(0);
                    String description = jsonWeather.getString("main");

                    //Get data from main object
                    JSONObject jsonMain = jsonRes.getJSONObject("main");
                    String temp = ""+ df.format(jsonMain.getDouble("temp")- 273.15);

                    //Calling Forecast API with coord
                    forecastUrl = dailyurl + "lat=" +lat +"&lon=" + lon+ "&appid=" + apikey;
                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, forecastUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonRes = new JSONObject(response);
                                JSONArray jsonArr = jsonRes.getJSONArray("daily");
                                //Get daily data
                                for(int i = 1;i<8;i++) {
                                    JSONObject jsonDay = jsonArr.getJSONObject(i);

                                    //Get temp data
                                    JSONObject jsonDailyTemp = jsonDay.getJSONObject("temp");
                                    String temp = "" + df.format(jsonDailyTemp.getDouble("day") - 273.15);
                                    TextView textDaily = (TextView)layouts[i-1].getChildAt(2);
                                    textDaily.setText(temp+" °C");

                                    //Get wind data
                                    double windDaily = jsonDay.getDouble("wind_speed");
                                    TextView windDailyText = (TextView)layouts[i-1].getChildAt(3);
                                    windDailyText.setText(""+windDaily +" m/s");
                                    //Get time
                                    long unixSeconds = jsonDay.getLong("dt");
                                    Date date = new java.util.Date(unixSeconds*1000L);
                                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM");
                                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
                                    String formattedDate = sdf.format(date);
                                    TextView textDay = (TextView)layouts[i-1].getChildAt(0);
                                    textDay.setText(formattedDate);

                                    //Get weather data
                                    JSONArray jsonWeatherArr = jsonDay.getJSONArray("weather");
                                    JSONObject jsonDailyWeather = jsonWeatherArr.getJSONObject(0);
                                    String descriptionDaily = jsonDailyWeather.getString("main");
                                    ImageView imgDay = (ImageView)layouts[i-1].getChildAt(1);
                                    setWeather(descriptionDaily,imgDay);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest1);
                    //Set result data
                    resultText.setText(temp+" °C");
                    resultWind.setText(windSpeed+ " m/s");
                    //Set image
                    setWeather(description,resultImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void secondActivity(View view) {
        Intent intent = new Intent(this,HourlyActivity.class);
        intent.putExtra("Url", forecastUrl);
        startActivity(intent);
    }

    public void thirdActivity(View view) {
        Intent intent = new Intent(this,thirdActivity.class);
        startActivity(intent);
    }
}