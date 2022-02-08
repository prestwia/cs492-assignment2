package com.example.android.connectedweather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.connectedweather.data.WeatherResults
import kotlin.math.roundToInt


class ForecastAdapter(private val context: Context, private val onWeatherClick: (WeatherResults) -> Unit) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    var weatherList = listOf<WeatherResults>()

    fun updateWeatherList(newWeatherList: List<WeatherResults>?) {
        weatherList = newWeatherList ?: listOf()
        notifyDataSetChanged()
    }

    override fun getItemCount() = this.weatherList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_list_item, parent, false)
        return ViewHolder(view, onWeatherClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    class ViewHolder(view: View, val onClick: (WeatherResults) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val monthTV: TextView = view.findViewById(R.id.tv_month)
        private val dayTV: TextView = view.findViewById(R.id.tv_day)
        private val timeTV: TextView = view.findViewById(R.id.tv_time)
        private val highTempTV: TextView = view.findViewById(R.id.tv_max_temp)
        private val lowTempTV: TextView = view.findViewById(R.id.tv_min_temp)
        private val popTV: TextView = view.findViewById(R.id.tv_pop)


        private var currentWeatherResult: WeatherResults? = null

        init {
            view.setOnClickListener {
                currentWeatherResult?.let(onClick)
            }
        }

        private fun parseMonth(date: String): String {
            val list = date.split("-")
            when (list[1]) {
                "01" -> return "Jan."
                "02" -> return "Feb."
                "03" -> return "Mar."
                "04" -> return "Apr."
                "05" -> return "May"
                "06" -> return "Jun."
                "07" -> return "Jul."
                "08" -> return "Aug."
                "09" -> return "Sep."
                "10" -> return "Oct."
                "11" -> return "Nov."
                "12" -> return "Dec."
                else -> {
                    return "Unknown"
                }
            }
        }

        private fun parseDay(date: String): String {
            val list = date.split("-", " ")
            return list[2].trimStart() { it == '0' }
        }

        private fun parseTime(date: String): String {
            val list = date.split("-", " ")
            val time = list[3].split(":")
            var hourInt = time[0].toInt()
            var tod = ""
            if (hourInt > 12) {
                hourInt -= 12
                tod = "pm"
            } else if (hourInt == 0) {
                hourInt = 12
                tod = "am"
            } else {
                tod = "am"
            }
            return hourInt.toString() + ":" + time[1] + " " + tod
        }

        fun bind(weatherResult: WeatherResults) {
            currentWeatherResult = weatherResult

            monthTV.text = parseMonth(weatherResult.dt_txt)
            dayTV.text = parseDay(weatherResult.dt_txt)
            timeTV.text = parseTime(weatherResult.dt_txt)
            highTempTV.text = weatherResult.main.temp_max.roundToInt().toString() + "°F"
            lowTempTV.text = weatherResult.main.temp_min.roundToInt().toString() + "°F"
            popTV.text = weatherResult.pop.roundToInt().toString() + "%"
        }
    }
}