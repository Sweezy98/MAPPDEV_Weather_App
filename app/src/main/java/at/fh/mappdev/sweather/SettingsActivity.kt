package at.fh.mappdev.sweather

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Radio
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SettingsActivity : AppCompatActivity(), CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        const val UNIT = "UNIT"
    }

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
       val name = sharedPreferences.getString(LoginFragment.NAME, null)
       setContentView(R.layout.activity_settings)
       findViewById<TextView>(R.id.Username).text = name


       findViewById<MaterialCardView>(R.id.logoutBtn_card).setOnClickListener() {
           launch {
               val session = sharedPreferences.getString(LoginFragment.SESSION, null)
               if (session != null) {
                   val result =
                       apolloClient(applicationContext).query(LogoutQuery(session)).execute()
               }

               sharedPreferences.edit().remove(LoginFragment.SESSION).apply()
               sharedPreferences.edit().remove(LoginFragment.ACCESS_TOKEN).apply()
               sharedPreferences.edit().remove(LoginFragment.REFRESH_TOKEN).apply()
               sharedPreferences.edit().remove(LoginFragment.EMAIL).apply()
               sharedPreferences.edit().remove(LoginFragment.NAME).apply()

               val intent = Intent(this@SettingsActivity, LoginRegisterActivity::class.java)
               startActivity(intent)
               finish()
           }
       }

       findViewById<MaterialCardView>(R.id.ChangePWDBtn).setOnClickListener() {
           val intent = Intent(this@SettingsActivity, ResetPasswordActivity::class.java)
           startActivity(intent)
       }

       findViewById<RadioButton>(R.id.Celsius).setOnClickListener() {
           sharedPreferences.edit().putString("Unit", "Celsius").apply()
       }
       findViewById<RadioButton>(R.id.Fahrenheit).setOnClickListener() {
           sharedPreferences.edit().putString("Unit", "Fahrenheit").apply()
       }
       findViewById<MaterialCardView>(R.id.AboutBtn_card).setOnClickListener() {
           val intent = Intent(this@SettingsActivity, AboutActivity::class.java)
           startActivity(intent)
       }
   }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val unit = sharedPreferences.getString(UNIT, "metric")
        //if unit is imperial, set the radio button to imperial
        if (unit == "imperial") {
            findViewById<RadioButton>(R.id.Fahrenheit).isChecked = true
        }
    }
}
       /*findViewById<ImageView>(R.id.UserIcon).setOnClickListener() {
           val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
           //intent.setDataAndType(Uri.parse("file:///sdcard/"), "image/*");
           startActivityForResult(intent, 1);
       }
       fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
           super.onActivityResult(requestCode, resultCode, data)
           if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
               val selectedImage = data.data
               val imageView = findViewById<ImageView>(R.id.UserIcon)
               imageView.setImageURI(selectedImage)
           }
       }*/

*/