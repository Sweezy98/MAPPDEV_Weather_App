package at.fh.mappdev.sweather

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// declare Image Maps
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

private val snowImageMap = mapOf(
    R.drawable.snowman  to "snowman",
    R.drawable.sledging to "sledging",
    R.drawable.skiing to "skiing",
    R.drawable.movienight to "movienight",
    R.drawable.iceskating to "iceskating",
    R.drawable.hotchoc to "hotchocolate",
    R.drawable.cookies to "cookies",
    R.drawable.christmasmarket to "christmasmarket"
)

private val coldImageMap = mapOf(
    R.drawable.bouldering2 to "bouldering2",
    R.drawable.cleaning2 to "cleaning2",
    R.drawable.netflix2 to "netflix2",
    R.drawable.ordertakeout to "ordertakeout",
    R.drawable.plantrip to "plantrip",
    R.drawable.walkpet to "walkpet",
    R.drawable.reading2 to "reading2",
    R.drawable.wellness2 to "wellness2"
)

private val darkGoodImageMap = mapOf(
    R.drawable.runningdark to "runningdark",
    R.drawable.walkdark to "walkdark",
    R.drawable.cafedark to "cafedark",
    R.drawable.parkdark to "parkdark",
    R.drawable.minigolfdark to "minigolfdark",
    R.drawable.swimdark to "swimmingdark",
    R.drawable.relaxdark to "relaxdark",
    R.drawable.hikedark to "hikingdark",
)

private val darkBadImageMap = mapOf(
    R.drawable.bakedark to "bakingdark",
    R.drawable.netflixdark to "netflixdark",
    R.drawable.readdark to "readingdark",
    R.drawable.cleandark to "cleaningdark",
    R.drawable.boulderingdark to "boulderingdark",
    R.drawable.wellnessdark to "wellnessdark",
    R.drawable.cinemadark to "cinemadark",
    R.drawable.gamesdark to "gamesdark"
)

private val darkSnowImageMap = mapOf(
    R.drawable.snowmandark to "snowmandark",
    R.drawable.sledgingdark to "sledgingdark",
    R.drawable.skiingdark to "skiingdark",
    R.drawable.moviedark to "movienightdark",
    R.drawable.iceskatingdark to "iceskatingdark",
    R.drawable.hotchocdark to "hotchocolatedark",
    R.drawable.cookiesdark to "cookiesdark",
    R.drawable.christmasmarketdark to "christmasmarketdark"
)

private val darkColdImageMap = mapOf(
    R.drawable.bouldering2dark to "bouldering2dark",
    R.drawable.clean2dark to "cleaning2dark",
    R.drawable.netflix2dark to "netflix2dark",
    R.drawable.takeoutdark to "ordertakeoutdark",
    R.drawable.tripdark to "plantripdark",
    R.drawable.walkpetdark to "walkpetdark",
    R.drawable.reading2dark to "reading2dark",
    R.drawable.wellness2dark to "wellness2dark"
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
            val unit: String = sharedPreferences.getString(SettingsActivity.UNIT, "Celsius").toString()

            // get lat and lon from shared preferences
            val lat: Float = sharedPreferences.getFloat(LocationActivity.LAT, 47.0667F)
            val lon: Float = sharedPreferences.getFloat(LocationActivity.LON, 15.4333F)

            // read value from intent
            val value = intent.getIntExtra("day", 0)

            // get the current weather data
            val weatherResult = apolloClient(applicationContext).query(GetWeatherDataQuery(lat = lat.toDouble(), lon = lon.toDouble())).execute()
            val weatherIcon = weatherResult.data?.getWeatherData?.weather?.get(value)?.icon

            // Set textViews
            val location = findViewById<TextView>(R.id.details_location)
            val weekday = findViewById<TextView>(R.id.details_weekday)
            val temperature = findViewById<TextView>(R.id.details_temperature)
            val humidity = findViewById<TextView>(R.id.details_humidity_num)
            val precipitation = findViewById<TextView>(R.id.details_precipitation_num)
            val visibility = findViewById<TextView>(R.id.details_visibility_num)
            val wind = findViewById<TextView>(R.id.details_wind_num)
            val pressure = findViewById<TextView>(R.id.details_pressure_num)
            val visTemp = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.visibility?.toInt()?.div(1000)
            val windArrow = findViewById<ImageView>(R.id.details_windarrow)
            windArrow.rotation = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.wind?.deg?.toFloat()!!

            location.text = weatherResult.data?.getWeatherData?.name
            if (unit == "Fahrenheit") { temperature.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.temps?.min?.f.toString() + "°" + " - " + weatherResult.data?.getWeatherData?.weather?.get(value)?.temps?.max?.f.toString() + "°"}
                else { temperature.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.temps?.min?.c.toString() + "°" + " - " + weatherResult.data?.getWeatherData?.weather?.get(value)?.temps?.max?.c.toString() + "°"
            weekday.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.weekday?.long
            humidity.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.humidity?.toInt().toString() + "%"
            precipitation.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.precipitation?.toInt().toString() + " mm"
            visibility.text = visTemp.toString() + " km"
            wind.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.wind?.speed.toString() + " km/h"
            pressure.text = weatherResult.data?.getWeatherData?.weather?.get(value)?.details?.pressure?.toInt().toString() + " hPa"


            // declare prerequisites for the image algorithm
            val image1 = findViewById<ImageView>(R.id.details_image1)
            val image2 = findViewById<ImageView>(R.id.details_image2)
            val image3 = findViewById<ImageView>(R.id.details_image3)
            val image4 = findViewById<ImageView>(R.id.details_image4)
            val selectedImages = mutableListOf<Int>()
            val selectedImagesTest = mutableSetOf<Int>()
            val tempC = weatherResult.data?.getWeatherData?.weather?.get(value)?.temps?.max?.c
            val random = Random(System.currentTimeMillis())
            val darkModeTest = if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) true else false

            val goodImages = if (!darkModeTest) goodImageMap.keys else darkGoodImageMap.keys
            val badImages = if (!darkModeTest) badImageMap.keys else darkBadImageMap.keys
            val coldImages = if (!darkModeTest) coldImageMap.keys else darkColdImageMap.keys
            val snowImages = if (!darkModeTest) snowImageMap.keys else darkSnowImageMap.keys

            val allImages = when (weatherIcon) {
                "rain", "storm" -> badImages
                "snow" -> snowImages
                "sun", "cloud", "cloud_sun" -> if (tempC!! < 20) coldImages else goodImages
                else -> goodImages
            }

            // select 4 random images and check for duplicates
            for (i in 0..3) {
                var image = allImages.elementAt(random.nextInt(allImages.size))
                while (selectedImagesTest.contains(image)) {
                    image = allImages.elementAt(random.nextInt(allImages.size))
                }
                selectedImagesTest.add(image)
                selectedImages.add(image)

                // set images
                when (i) {
                    0 -> image1.setImageResource(image)
                    1 -> image2.setImageResource(image)
                    2 -> image3.setImageResource(image)
                    3 -> image4.setImageResource(image)
                }

                // set onclicklisteners on netflix cardview
                if (image == R.drawable.netflix2 || image == R.drawable.netflix || image == R.drawable.netflixdark || image == R.drawable.netflix2dark) {
                    when (i) {
                        0 -> image1.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.netflix.com/browse"))
                            startActivity(intent)
                        }
                        1 -> image2.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.netflix.com/browse"))
                            startActivity(intent)
                        }
                        2 -> image3.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.netflix.com/browse"))
                            startActivity(intent)
                        }
                        3 -> image4.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.netflix.com/browse"))
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}
}

