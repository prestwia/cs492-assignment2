package com.example.android.connectedweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.android.connectedweather.data.WeatherResults
import kotlin.math.roundToInt

const val EXTRA_WEATHER_RESULT = "WEATHER_RESULT"
class DetailedWeatherView : AppCompatActivity() {
    private var result: WeatherResults ?= null

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
        }
        else if (hourInt == 0) {
            hourInt = 12
            tod = "am"
        }
        else {
            tod = "am"
        }
        return hourInt.toString() + ":" + time[1] + " " + tod
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_weather_view)

        if (intent != null && intent.hasExtra((EXTRA_WEATHER_RESULT))) {
            result = intent.getSerializableExtra(EXTRA_WEATHER_RESULT) as WeatherResults

            findViewById<TextView>(R.id.tv_city_name).text = "Corvallis"
            findViewById<TextView>(R.id.tv_detail_date).text = parseMonth(result!!.dt_txt) +
                    " " + parseDay(result!!.dt_txt) + ", " + parseTime(result!!.dt_txt)
            findViewById<TextView>(R.id.tv_detail_min_temp).text = result!!.main.temp_min
                .roundToInt().toString() + "°F"
            findViewById<TextView>(R.id.tv_detail_max_temp).text = result!!.main.temp_max
                .roundToInt().toString() + "°F"
            findViewById<TextView>(R.id.tv_detail_pop).text = result!!.pop.roundToInt()
                .toString() + "%"
            findViewById<TextView>(R.id.tv_detail_clouds).text = result!!.clouds!!.all
                .toString() + "%"
            findViewById<TextView>(R.id.tv_detail_wind).text = result!!.wind!!.speed
                .roundToInt().toString() + " MPH"
            findViewById<TextView>(R.id.tv_detail_desc).text = result!!.weather[0].description

            val img = findViewById<ImageView>(R.id.detailed_weather_icon)
            val url = "http://openweathermap.org/img/wn/${result!!.weather[0].icon}.png"
            //val url = "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg"
            Log.d("Image", url)
            if (url !== null) {
                Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .into(img)
            } else {
                img.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_share -> {
                shareWeather()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareWeather() {
        if (result != null) {
            val text = getString(
                R.string.share_weather,
                result!!.weather[0].description,
                "Corvallis, Oregon"
            )
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, null))
        }
    }
}