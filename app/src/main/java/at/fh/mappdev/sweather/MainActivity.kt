package at.fh.mappdev.sweather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
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

        //toast loading message
        Toast.makeText(this@MainActivity, "Loading weather data...", Toast.LENGTH_SHORT).show()
        // Get the current weather
        loadCurrentWeatherData()

        //locations button
        findViewById<Button>(R.id.locationBtn).setOnClickListener() {
            val intent = Intent(this@MainActivity, LocationActivity::class.java)
            startActivity(intent)
        }

        //settings button
        findViewById<ImageButton>(R.id.settingsBtn).setOnClickListener() {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        //favorite button
        findViewById<ImageButton>(R.id.favoriseBtn).setOnClickListener() {
            //toast message
            Toast.makeText(this, "Favorise is not implemented yet", Toast.LENGTH_SHORT).show()
        }

        //clothes recommendation button
        findViewById<ImageButton>(R.id.weather_avatar).setOnClickListener() {
            //which image is displayed
            val image = findViewById<ImageView>(R.id.weather_avatar).tag.toString()
            //toast message
            when (image) {
                "jacketgood" -> Toast.makeText(this, "It´s cold outside! You should wear something thick and warm.", Toast.LENGTH_SHORT).show()
                "jacketbad" -> Toast.makeText(this, "It´s cold outside and weather doesn´t look good! You should wear something thick and warm. Don´t forget your umbrella!", Toast.LENGTH_SHORT).show()
                "longgood" -> Toast.makeText(this, "It´s a bit chilly today. You should wear something with long sleeves.", Toast.LENGTH_SHORT).show()
                "longbad" -> Toast.makeText(this, "It´s a bit chilly and weather doesn´t look good! You should wear something with long sleeves. Don´t forget your umbrella!", Toast.LENGTH_SHORT).show()
                "shortlonggood" -> Toast.makeText(this, "It looks quite nice outside, but don´t go outside dressed too short.", Toast.LENGTH_SHORT).show()
                "shortlongbad" -> Toast.makeText(this, "Weather doesn´t look that nice today, don´t go outside dressed too short and don´t forget your umbrella.", Toast.LENGTH_SHORT).show()
                "shortgood" -> Toast.makeText(this, "It´s hot outside! Make sure you wear something short.", Toast.LENGTH_SHORT).show()
                "shortbad" -> Toast.makeText(this, "It´s hot outside, but weather doesn´t seem nice! Make sure you wear something short and don´t forget your umbrella.", Toast.LENGTH_SHORT).show()
            }
        }

        //weather card day 0
        findViewById<CardView>(R.id.Day0).setOnClickListener() {
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
            intent.putExtra("day", 0)
            startActivity(intent)
        }

        //weather card day 1
        findViewById<CardView>(R.id.Day1).setOnClickListener() {
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
            intent.putExtra("day", 1)
            startActivity(intent)
        }

        //weather card day 2
        findViewById<CardView>(R.id.Day2).setOnClickListener() {
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
            intent.putExtra("day", 2)
            startActivity(intent)
        }

        //weather card day 3
        findViewById<CardView>(R.id.Day3).setOnClickListener() {
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
            intent.putExtra("day", 3)
            startActivity(intent)
        }

        //weather card day 4
        findViewById<CardView>(R.id.Day4).setOnClickListener() {
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
            intent.putExtra("day", 4)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        //reload the current weather when the activity is resumed
        loadCurrentWeatherData()
    }

    //load the current weather data from the API
    private fun loadCurrentWeatherData() {
        launch {

            //get unit from shared preferences
            val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
            val unit: String = sharedPreferences.getString(SettingsActivity.UNIT, "Fahrenheit").toString() //maybe mixed up

            //get lat and lon from shared preferences
            val lat: Float = sharedPreferences.getFloat(LocationActivity.LAT, 47.0667F)
            val lon: Float = sharedPreferences.getFloat(LocationActivity.LON, 15.4333F)

            //get the current weather data
            val weatherResult = apolloClient(applicationContext).query(GetWeatherDataQuery(lat = lat.toDouble(), lon = lon.toDouble())).execute()
            val weather = weatherResult.data?.getWeatherData?.weather?.get(0)?.description

            //set location name
            val location = weatherResult.data?.getWeatherData?.name
            findViewById<TextView>(R.id.locationBtn).text = location

            //check if api returns error
            val error = weatherResult.errors?.get(0)?.message
            if (error != null) {
                Log.e("MainActivity", error)
                //set warning icon
                findViewById<ImageView>(R.id.weatherIcon).setImageResource(R.drawable.warning)
                //toast message
                Toast.makeText(this@MainActivity, "Error loading Weather Data.", Toast.LENGTH_SHORT).show()
            }

            //set weather according to shared preferences
            val temp: Int?
            val tempMax: Int?
            val tempMin: Int?
            if (unit == "Celsius"){
                temp = weatherResult.data?.getWeatherData?.weather?.get(0)?.temps?.cur?.c
                tempMin = weatherResult.data?.getWeatherData?.weather?.get(0)?.temps?.min?.c
                tempMax = weatherResult.data?.getWeatherData?.weather?.get(0)?.temps?.max?.c
            } else {
                temp = weatherResult.data?.getWeatherData?.weather?.get(0)?.temps?.cur?.f
                tempMin = weatherResult.data?.getWeatherData?.weather?.get(0)?.temps?.min?.f
                tempMax = weatherResult.data?.getWeatherData?.weather?.get(0)?.temps?.max?.f
            }

            //get and set current weather icon
            val weatherIcon = weatherResult.data?.getWeatherData?.weather?.get(0)?.icon
            when (weatherIcon) {
                "cloud" -> findViewById<ImageView>(R.id.weatherIcon).setImageResource(R.drawable.cloud)
                "cloud_sun" -> findViewById<ImageView>(R.id.weatherIcon).setImageResource(R.drawable.cloud_sun)
                "sun" -> findViewById<ImageView>(R.id.weatherIcon).setImageResource(R.drawable.sun)
                "rain" -> findViewById<ImageView>(R.id.weatherIcon).setImageResource(R.drawable.rain)
                "snow" -> findViewById<ImageView>(R.id.weatherIcon).setImageResource(R.drawable.snow)
                "storm" -> findViewById<ImageView>(R.id.weatherIcon).setImageResource(R.drawable.storm)
            }
            //set color theme of icon
            findViewById<ImageView>(R.id.weatherIcon).setColorFilter(resources.getColor(R.color.white))
            //set current temperature
            findViewById<TextView>(R.id.current_temperature).text = temp.toString() + "°"
            //set max temperature
            findViewById<TextView>(R.id.max_temp).text = tempMax.toString() + "°"
            //set min temperature
            findViewById<TextView>(R.id.min_temp).text = tempMin.toString() + "°"


            //set clothing recommendation icons
            var clothesImage = findViewById<ImageButton>(R.id.weather_avatar)
            if (weatherIcon == "rain" || weatherIcon == "storm") {
                when (weatherResult.data?.getWeatherData?.weather?.get(0)?.temps?.cur?.c) {
                    in -50..10 -> {
                        clothesImage.setImageResource(R.drawable.jacketbad)
                        clothesImage.tag = "jacketbad"
                    }
                    in 11..15 -> {
                        clothesImage.setImageResource(R.drawable.longbad)
                        clothesImage.tag = "longbad"
                    }
                    in 16..20 -> {
                        clothesImage.setImageResource(R.drawable.shortlongbad)
                        clothesImage.tag = "shortlongbad"
                    }
                    in 21..50 -> {
                        clothesImage.setImageResource(R.drawable.shortbad)
                        clothesImage.tag = "shortbad"
                    }
                }
            } else {
                when (weatherResult.data?.getWeatherData?.weather?.get(0)?.temps?.cur?.c) {
                    in -50..10 -> {
                        clothesImage.setImageResource(R.drawable.jacketgood)
                        clothesImage.tag = "jacketgood"
                    }
                    in 11..15 -> {
                        clothesImage.setImageResource(R.drawable.longgood)
                        clothesImage.tag = "longgood"
                    }
                    in 16..20 -> {
                        clothesImage.setImageResource(R.drawable.shortlonggood)
                        clothesImage.tag = "shortlonggood"
                    }
                    in 21..50 -> {
                        clothesImage.setImageResource(R.drawable.shortgood)
                        clothesImage.tag = "shortgood"
                    }
                }
            }


            //forecast weather loop
            for (i in 0..4) {
                //set weather icon
                val weatherIcon = weatherResult.data?.getWeatherData?.weather?.get(i)?.icon
                val dayIcon: String = "day" + i.toString() + "_weather"
                val dayIconResID = resources.getIdentifier(dayIcon, "id", packageName)
                val dayName: String = "day" + i.toString() + "_name"
                val dayNameResID = resources.getIdentifier(dayName, "id", packageName)
                val dayMinTemp: String = "day" + i.toString() + "_min"
                val dayMinTempResID = resources.getIdentifier(dayMinTemp, "id", packageName)
                val dayMaxTemp: String = "day" + i.toString() + "_max"
                val dayMaxTempResID = resources.getIdentifier(dayMaxTemp, "id", packageName)

                //set weather icon
                when (weatherIcon) {
                    "cloud" -> findViewById<ImageView>(dayIconResID).setImageResource(R.drawable.cloud)
                    "cloud_sun" -> findViewById<ImageView>(dayIconResID).setImageResource(R.drawable.cloud_sun)
                    "sun" -> findViewById<ImageView>(dayIconResID).setImageResource(R.drawable.sun)
                    "rain" -> findViewById<ImageView>(dayIconResID).setImageResource(R.drawable.rain)
                    "snow" -> findViewById<ImageView>(dayIconResID).setImageResource(R.drawable.snow)
                    "storm" -> findViewById<ImageView>(dayIconResID).setImageResource(R.drawable.storm)
                }

                //set day name
                if (i == 0){
                    findViewById<TextView>(dayNameResID).text = "Today"
                } else {
                    findViewById<TextView>(dayNameResID).text = weatherResult.data?.getWeatherData?.weather?.get(i)?.weekday?.long
                }

                //set minimum temperature
                if (unit == "Celsius"){
                    findViewById<TextView>(dayMinTempResID).text = weatherResult.data?.getWeatherData?.weather?.get(i)?.temps?.min?.c.toString() + "°"
                } else {
                    findViewById<TextView>(dayMinTempResID).text = weatherResult.data?.getWeatherData?.weather?.get(i)?.temps?.min?.f.toString() + "°"
                }

                //set maximum temperature
                if (unit == "Celsius"){
                    findViewById<TextView>(dayMaxTempResID).text = weatherResult.data?.getWeatherData?.weather?.get(i)?.temps?.max?.c.toString() + "°"
                } else {
                    findViewById<TextView>(dayMaxTempResID).text = weatherResult.data?.getWeatherData?.weather?.get(i)?.temps?.max?.f.toString() + "°"
                }

            }


        }
    }
}