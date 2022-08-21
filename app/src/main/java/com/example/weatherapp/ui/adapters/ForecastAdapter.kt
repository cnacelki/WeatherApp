package com.example.weatherapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.models.Daily
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(
    private val context: Context,
    private val dailyData: List<Daily>
): RecyclerView.Adapter<ForecastAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val forecastDay: TextView = view.findViewById(R.id.forecastDay)
        val forecastTemp: TextView = view.findViewById(R.id.forecastTemp)
        val forecastWind: TextView = view.findViewById(R.id.forecastWind)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dailyData[position]
        val sdf = SimpleDateFormat("dd-MM")
        val netDate = item.dt.toLong() * 1000
        val day = sdf.format(netDate)
        holder.forecastDay.text = day
        holder.forecastTemp.text ="${item.temp.day.toInt().toString()} °C"
        holder.forecastWind.text ="${item.wind_speed.toInt().toString()} km/s"

    }

    override fun getItemCount(): Int {
        return dailyData.size
    }
}