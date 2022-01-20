package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
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

public class HourlyActivity extends AppCompatActivity {
    LinearLayout mainlayout;
    DecimalFormat df = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly);
        mainlayout = findViewById(R.id.mainlayout);
        String finalUrl = getIntent().getStringExtra("Url");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonRes = new JSONObject(response);
                    JSONArray jsonArr = jsonRes.getJSONArray("hourly");
                    int leng = jsonArr.length();
                    for(int i = 0; i<leng;i++){
                        JSONObject jsonHour = jsonArr.getJSONObject(i);
                        JSONArray jsonWeatherArr = jsonHour.getJSONArray("weather");
                        JSONObject jsonHourlyWeather = jsonWeatherArr.getJSONObject(0);
                        String descriptionHourly = jsonHourlyWeather.getString("main");
                        String temp = "" + df.format(jsonHour.getDouble("temp") - 273.15);


                        long unixSeconds = jsonHour.getLong("dt");
                        Date date = new java.util.Date(unixSeconds*1000L);
                        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm dd-MM");
                        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
                        String formattedDate = sdf.format(date);
                        createLayout(formattedDate,temp,descriptionHourly);

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
        requestQueue.add(stringRequest);
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
    private void createLayout(String unix, String temp, String main) {
        LinearLayout.LayoutParams imgprm = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(100, 100));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lp.gravity=Gravity.CENTER;
        lp.topMargin=10;
        imgprm.rightMargin=30;
        imgprm.gravity= Gravity.CENTER;

        LinearLayout linlay = new LinearLayout(this);
        linlay.setOrientation(LinearLayout.HORIZONTAL);
        linlay.setLayoutParams(lp);
        linlay.setBackgroundResource(R.drawable.daily_bg);

        TextView txt = new TextView(this);
        txt.setText(unix +"\n"+temp + "°C  ");
        txt.setTextSize(30);
        txt.setLayoutParams(lp);
        txt.setGravity(Gravity.CENTER);

        ImageView img = new ImageView(this);
        setWeather(main,img);
        img.setLayoutParams(imgprm);

        linlay.addView(img);
        linlay.addView(txt);
        mainlayout.addView(linlay);
    }
}