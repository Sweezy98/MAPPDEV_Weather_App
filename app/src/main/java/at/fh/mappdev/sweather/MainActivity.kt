package at.fh.mappdev.sweather

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import at.fh.mappdev.sweather.type.Weather
import com.apollographql.apollo3.api.Optional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the current weather
        loadCurrentWeatherData()

        //locations button
        findViewById<Button>(R.id.locationBtn).setOnClickListener() {
            //toast message
            Toast.makeText(this, "Locations are not implemented yet", Toast.LENGTH_SHORT).show()
        }

        //settings button
        findViewById<ImageButton>(R.id.settingsBtn).setOnClickListener() {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        //favorise button
        findViewById<ImageButton>(R.id.favoriseBtn).setOnClickListener() {
            //toast message
            Toast.makeText(this, "Favorise is not implemented yet", Toast.LENGTH_SHORT).show()
        }

        //clothes recommendation button
        findViewById<ImageButton>(R.id.weather_avatar).setOnClickListener() {
            //toast message
            Toast.makeText(this, "Clothes recommendation is not implemented yet", Toast.LENGTH_SHORT).show()
        }

        //weather card day 0
        findViewById<CardView>(R.id.Day0).setOnClickListener() {
            //toast message
            Toast.makeText(this, "Weather card is not implemented yet", Toast.LENGTH_SHORT).show()
        }

        //weather card day 1
        findViewById<CardView>(R.id.Day1).setOnClickListener() {
            //toast message
            Toast.makeText(this, "Weather card is not implemented yet", Toast.LENGTH_SHORT).show()
        }

        //weather card day 2
        findViewById<CardView>(R.id.Day2).setOnClickListener() {
            //toast message
            Toast.makeText(this, "Weather card is not implemented yet", Toast.LENGTH_SHORT).show()
        }

        //weather card day 3
        findViewById<CardView>(R.id.Day3).setOnClickListener() {
            //toast message
            Toast.makeText(this, "Weather card is not implemented yet", Toast.LENGTH_SHORT).show()
        }

        //weather card day 4
        findViewById<CardView>(R.id.Day4).setOnClickListener() {
            //toast message
            Toast.makeText(this, "Weather card is not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        //reload the current weather when the activity is resumed
        loadCurrentWeatherData()
    }

    //load the current weather data from the API
    fun loadCurrentWeatherData() {
        launch {
            //get unit from shared preferences
            val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
            val unit: String = sharedPreferences.getString(SettingsActivity.UNIT, "imperial").toString()

            val weatherResult = apolloClient(applicationContext).query(GetWeatherDataQuery(lat = 47.076668, lon = 15.421371, units = unit)).execute()
            val weather = weatherResult.data?.getWeatherData?.weather?.get(0)?.main
            val temp = weatherResult.data?.getWeatherData?.main?.temp
            val tempMin = weatherResult.data?.getWeatherData?.main?.temp_min
            val tempMax = weatherResult.data?.getWeatherData?.main?.temp_max

            //debug
            Log.e("Weather", weather.toString())

            //set current weather icon
            //set weather icon
            val weatherIcon = findViewById<ImageView>(R.id.weatherIcon)
            if (weather != null) {
                when (weather) {
                    "Clear" -> {
                        weatherIcon.setImageResource(R.drawable.sun)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))

                    }
                    "Clouds" -> {
                        weatherIcon.setImageResource(R.drawable.cloud)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))
                    }
                    "Rain" -> {
                        weatherIcon.setImageResource(R.drawable.rain)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))
                    }
                    "Snow" -> {
                        weatherIcon.setImageResource(R.drawable.snow)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))

                    }
                    "Thunderstorm" -> {
                        weatherIcon.setImageResource(R.drawable.storm)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))
                    }
                    "Drizzle" -> {
                        weatherIcon.setImageResource(R.drawable.rain)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))
                    }
                    "Mist" -> {
                        weatherIcon.setImageResource(R.drawable.fog)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))
                    }
                    "Fog" -> {
                        weatherIcon.setImageResource(R.drawable.fog)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))
                    }
                    "Smoke" -> {
                        weatherIcon.setImageResource(R.drawable.fog)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))
                    }
                    "Haze" -> {
                        weatherIcon.setImageResource(R.drawable.fog)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))
                    }
                    "Dust" -> {
                        weatherIcon.setImageResource(R.drawable.fog)
                        weatherIcon.setColorFilter(resources.getColor(R.color.white))
                    }

                }
            } else {
                weatherIcon.setImageResource(R.drawable.warning)
            }
            //change weatherIcon size
            weatherIcon.layoutParams.height = 200
            weatherIcon.layoutParams.width = 200

            //set current temperature
            if (temp != null) {
                val tempText = findViewById<TextView>(R.id.current_temperature)
                //cut decimal places
                val tempString = temp.toString().substringBefore(".")
                if (unit == "metric") {
                    tempText.text = "$tempString°C"
                } else {
                    tempText.text = "$tempString°F"
                }
            }

            //set max temperature
            if (tempMax != null) {
                val maxTempText = findViewById<TextView>(R.id.max_temp)
                //cut decimal places
                val maxTempString = tempMax.toString().substringBefore(".")
                maxTempText.text = maxTempString + "°"
            }

            //set min temperature
            if (tempMin != null) {
                val minTempText = findViewById<TextView>(R.id.min_temp)
                //cut decimal places
                val minTempString = tempMin.toString().substringBefore(".")
                minTempText.text = minTempString + "°"
            }
        }
    }
}