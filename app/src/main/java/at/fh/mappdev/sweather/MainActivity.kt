package at.fh.mappdev.sweather

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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

        findViewById<Button>(R.id.logoutBtn).setOnClickListener() {
            launch {
                val session = sharedPreferences.getString(LoginFragment.SESSION, null)
                if (session != null) {
                    val result = apolloClient(applicationContext).query(LogoutQuery(session)).execute()
                }
                sharedPreferences.edit().remove(LoginFragment.SESSION).apply()
                sharedPreferences.edit().remove(LoginFragment.ACCESS_TOKEN).apply()
                sharedPreferences.edit().remove(LoginFragment.REFRESH_TOKEN).apply()
                sharedPreferences.edit().remove(LoginFragment.EMAIL).apply()
                sharedPreferences.edit().remove(LoginFragment.NAME).apply()

                val intent = Intent(this@MainActivity, LoginRegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        findViewById<Button>(R.id.ToSettings).setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
    }
}