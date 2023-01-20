package at.fh.mappdev.sweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.datatransport.runtime.util.PriorityMapping.toInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


private val goodImageMap = mapOf(
    R.drawable.running to "running",
    R.drawable.walk to "walk",
    R.drawable.cafe to "cafe",
    R.drawable.park to "park",
    R.drawable.minigolf to "minigolf",
    R.drawable.swimming to "swimming",
    R.drawable.relax to "relax",
    R.drawable.hiking to "hiking",
)

private val badImageMap = mapOf(
    R.drawable.baking to "baking",
    R.drawable.netflix to "netflix",
    R.drawable.reading to "reading",
    R.drawable.cleaning to "cleaning",
    R.drawable.bouldering to "bouldering",
    R.drawable.wellness to "wellness",
    R.drawable.cinema to "cinema",
    R.drawable.games to "games"
)

class DetailsActivity : AppCompatActivity(), CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        launch {
            val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
            val unit: String = sharedPreferences.getString(SettingsActivity.UNIT, "Fahrenheit").toString() //maybe mixed up


            val lat: Float = sharedPreferences.getFloat(LocationActivity.LAT, 47.0667F)
            val lon: Float = sharedPreferences.getFloat(LocationActivity.LON, 15.4333F)

            // read value from intent
            val value = intent.getIntExtra("day", 0)

            //get the current weather data
            val weatherResult = apolloClient(applicationContext).query(GetWeatherDataQuery(lat = lat.toDouble(), lon = lon.toDouble())).execute()
            val weatherIcon = weatherResult.data?.getWeatherData?.weather?.get(0)?.icon

            // Set textViews
            val location = findViewById<TextView>(R.id.details_location)
            val weekday = findViewById<TextView>(R.id.details_weekday)
            val temperature = findViewById<TextView>(R.id.details_temperature)
            val humidity = findViewById<TextView>(R.id.details_humidity_num)
            val precipitation = findViewById<TextView>(R.id.details_precipitation_num)
            val visibility = findViewById<TextView>(R.id.details_visibility_num)
            val wind = findViewById<TextView>(R.id.details_wind_num)
            val pressure = findViewById<TextView>(R.id.details_pressure_num)

            location.text = weatherResult.data?.getWeatherData?.name
            if (unit == "Fahrenheit") {
                temperature.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.temps?.cur?.f.toString() + "°"
            } else {
                temperature.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.temps?.cur?.c.toString() + "°"
            }
            weekday.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.weekday?.long
            humidity.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.humidity.toString() + "%"
            precipitation.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.percipitation.toString() + " mm"
            visibility.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.visibility?.toInt().toString() + " km"
            wind.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.wind?.speed.toString() + " km/h"
            pressure.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.pressure.toString() + " hPa"

            val selectedImages = mutableListOf<Int>()
            // if weather is good / bad set cardview images
            if (weatherIcon == "rain" || weatherIcon == "storm" || weatherIcon == "snow" || weatherIcon == "cloud") {
                val allImages = badImageMap.keys.toMutableList()
                val random = Random(System.currentTimeMillis())
                for (i in 0..3) {
                    val index = random.nextInt(allImages.size)
                    selectedImages.add(allImages[index])
                    allImages.removeAt(index)
                }
            } else {
                val allImages = goodImageMap.keys.toMutableList()
                val random = Random(System.currentTimeMillis())
                for (i in 0..3) {
                    val index = random.nextInt(allImages.size)
                    selectedImages.add(allImages[index])
                    allImages.removeAt(index)
                }
            }

            // Set images
            val image1 = findViewById<ImageView>(R.id.details_image1)
            val image2 = findViewById<ImageView>(R.id.details_image2)
            val image3 = findViewById<ImageView>(R.id.details_image3)
            val image4 = findViewById<ImageView>(R.id.details_image4)

            image1.setImageResource(selectedImages[0])
            image2.setImageResource(selectedImages[1])
            image3.setImageResource(selectedImages[2])
            image4.setImageResource(selectedImages[3])

        }

    }

}