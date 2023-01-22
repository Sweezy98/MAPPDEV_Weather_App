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
    // Initializes Coroutine Scope
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        const val UNIT = "UNIT" // Key for the unit
    }

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE) // gets the shared preferences
       val name = sharedPreferences.getString(LoginFragment.NAME, null) // gets the name from the shared preferences
       setContentView(R.layout.activity_settings) // loads the Username from the shared preferences into
       findViewById<TextView>(R.id.locations).text = name // sets the name of the user in the settings activity to the name from the shared preferences

        // logs out the user
       findViewById<MaterialCardView>(R.id.logoutBtn_card).setOnClickListener() {
           launch {
               val session = sharedPreferences.getString(LoginFragment.SESSION, null)
               if (session != null) {
                   val result =
                       apolloClient(applicationContext).query(LogoutQuery(session)).execute()
               }

               sharedPreferences.edit().remove(LoginFragment.SESSION).apply() // removes the session from the shared preferences
               sharedPreferences.edit().remove(LoginFragment.ACCESS_TOKEN).apply() // removes the access token from the shared preferences
               sharedPreferences.edit().remove(LoginFragment.REFRESH_TOKEN).apply() // removes the refresh token from the shared preferences
               sharedPreferences.edit().remove(LoginFragment.EMAIL).apply() // removes the email from the shared preferences
               sharedPreferences.edit().remove(LoginFragment.NAME).apply() // removes the name from the shared preferences


               val intent = Intent(this@SettingsActivity, LoginRegisterActivity::class.java)
               startActivity(intent)
               finish()
           }
       }
        // Button to change the User Password incase the User forgot theirs and got one sent to them by email
       findViewById<MaterialCardView>(R.id.ChangePWDBtn).setOnClickListener() {
           val intent = Intent(this@SettingsActivity, ResetPasswordActivity::class.java)
           startActivity(intent)
       }
        // selects the celsius radio button
       findViewById<RadioButton>(R.id.Celsius).setOnClickListener() {
           sharedPreferences.edit().putString(UNIT, "Celsius").apply()
       }
       // Selects the Fahrenheit Radio Button
       findViewById<RadioButton>(R.id.Fahrenheit).setOnClickListener() {
           sharedPreferences.edit().putString(UNIT, "Fahrenheit").apply()
       }
       // Opens the AboutActivity when the about button is clicked
       findViewById<MaterialCardView>(R.id.AboutBtn_card).setOnClickListener() {
           val intent = Intent(this@SettingsActivity, AboutActivity::class.java)
           startActivity(intent)
       }


   }
    // Keeps the selected unit in the settings, so that it doesnt reset when you go to another activity
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
