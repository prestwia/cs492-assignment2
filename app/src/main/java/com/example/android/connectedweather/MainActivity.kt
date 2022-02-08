package com.example.android.connectedweather

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.android.connectedweather.data.ForecastPeriod
import com.example.android.connectedweather.data.WeatherResults
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    private val openWeatherAdapter = ForecastAdapter(this@MainActivity,::onWeatherResultClick)

    private val apiBaseUrl = "https://api.openweathermap.org/data/2.5/forecast?"

    private lateinit var forecastListRV: RecyclerView
    private lateinit var fetchErrorTV: TextView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        forecastListRV = findViewById<RecyclerView>(R.id.rv_forecast_list)
        fetchErrorTV = findViewById<TextView>(R.id.tv_fetch_error)
        loadingIndicator = findViewById<ProgressBar>(R.id.indeterminateBar)
        forecastListRV.layoutManager = LinearLayoutManager(this)
        forecastListRV.setHasFixedSize(true)
        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator_layout)

        forecastListRV.adapter = openWeatherAdapter

        queryOpenWeather()
    }

    // start with hardcoded location as Corvallis (can try to implement geocoding api as bonus)
    private fun queryOpenWeather(){
        val url = "${apiBaseUrl}q=Corvallis,Oregon&units=imperial&appid=05daa70b4de95831930f7b0b507e9564"

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<OpenWeatherResults> =
            moshi.adapter(OpenWeatherResults::class.java)

        val req = StringRequest(
            Request.Method.GET,
            url,
            {
                val results = jsonAdapter.fromJson(it)
                openWeatherAdapter.updateWeatherList(results?.list)
                loadingIndicator.visibility = View.INVISIBLE
                forecastListRV.visibility = View.VISIBLE
            },
            {
                loadingIndicator.visibility = View.INVISIBLE
                fetchErrorTV.visibility = View.VISIBLE
            })

        loadingIndicator.visibility = View.VISIBLE
        forecastListRV.visibility = View.INVISIBLE
        fetchErrorTV.visibility = View.INVISIBLE
        requestQueue.add(req)
    }

    private data class OpenWeatherResults(
        val list: List<WeatherResults>
    )

    private fun onWeatherResultClick(result: WeatherResults) {
        val intent = Intent(this, DetailedWeatherView::class.java).apply {
            putExtra(EXTRA_WEATHER_RESULT, result)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.open_map, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_open_map -> {
                viewLocationOnWeb()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun viewLocationOnWeb() {
        val uri = Uri.parse("geo:44.5646,123.2620?q=" + Uri.encode("Corvallis,Oregon"))
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(coordinatorLayout,
                "Unable to Access Maps",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}