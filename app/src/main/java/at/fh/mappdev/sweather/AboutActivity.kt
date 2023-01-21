package at.fh.mappdev.sweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.google.android.material.card.MaterialCardView

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // when clicking on the card Share_App_Button, the android sharing intent is opened
        findViewById<MaterialCardView>(R.id.Share_App_Button).setOnClickListener() {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey, check out this cool weather app: https://openweathermap.org/"
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        // when clicking on the card Rate_App_Button, the project submission page is opened
        findViewById<MaterialCardView>(R.id.Rate_App_Button).setOnClickListener() {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = "https://moodle-22-23.fh-joanneum.at/mod/assign/view.php?id=15204".toUri()
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }
}