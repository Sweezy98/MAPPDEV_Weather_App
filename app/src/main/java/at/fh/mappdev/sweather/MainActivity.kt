package at.fh.mappdev.sweather

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.card.MaterialCardView
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

        val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)

        //locations button
        findViewById<Button>(R.id.locationBtn).setOnClickListener() {
            //toast message
            Toast.makeText(this, "Locations are not implemented yet", Toast.LENGTH_SHORT).show()
        }

        //settings button
        findViewById<ImageButton>(R.id.settingsBtn).setOnClickListener() {
            //val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            //startActivity(intent)
            //toast message
            Toast.makeText(this, "Settings are not implemented yet", Toast.LENGTH_SHORT).show()
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
        
        findViewById<Button>(R.id.ToSettings).setOnClickListener {
          startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}