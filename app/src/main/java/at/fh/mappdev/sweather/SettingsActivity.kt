package at.fh.mappdev.sweather

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
        const val IMAGE = "IMAGE"
    }

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
       val name = sharedPreferences.getString(LoginFragment.NAME, null)
       setContentView(R.layout.activity_settings)
       findViewById<TextView>(R.id.locations).text = name


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
           sharedPreferences.edit().putString(UNIT, "Celsius").apply()
       }
       findViewById<RadioButton>(R.id.Fahrenheit).setOnClickListener() {
           sharedPreferences.edit().putString(UNIT, "Fahrenheit").apply()
       }
       findViewById<MaterialCardView>(R.id.AboutBtn_card).setOnClickListener() {
           val intent = Intent(this@SettingsActivity, AboutActivity::class.java)
           startActivity(intent)
       }


   }

    //check what unit is selected
    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val unit = sharedPreferences.getString(UNIT, "Celsius")
        //if unit is imperial, set the radio button to imperial
        if (unit == "Fahrenheit") {
            findViewById<RadioButton>(R.id.Fahrenheit).isChecked = true
        }

    }

}
