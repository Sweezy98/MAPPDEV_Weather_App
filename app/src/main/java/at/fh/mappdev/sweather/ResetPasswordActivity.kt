package at.fh.mappdev.sweather

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ResetPasswordActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        findViewById<MaterialCardView>(R.id.ChangePWDBtn_card).setOnClickListener {
            var oldPw = findViewById<TextView>(R.id.old_passwordInput).text.toString()
            var newPw = findViewById<TextView>(R.id.changePw_passwordInput).text.toString()
            var newPw2 = findViewById<TextView>(R.id.changePw_confirmPasswordInput).text.toString()

            if (oldPw != "" && newPw != "" && newPw2 != "") {
                // if newPw and newPw2 are not equal
                if (newPw != newPw2) {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                } else {
                    launch {
                    var result = apolloClient(applicationContext).mutation(ChangePasswordMutation(oldPw, newPw)).execute()
                        if (result.hasErrors()) {
                            Toast.makeText(this@ResetPasswordActivity, "Error: ${result.errors?.get(0)?.message.toString()}", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(this@ResetPasswordActivity, "Password changed!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }
        }

        findViewById<EditText>(R.id.old_passwordInput).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                findViewById<MaterialCardView>(R.id.old_password).strokeWidth = 4
                findViewById<MaterialCardView>(R.id.old_password).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                findViewById<MaterialCardView>(R.id.old_password).strokeWidth = 0
                findViewById<MaterialCardView>(R.id.old_password).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }

        findViewById<EditText>(R.id.changePw_passwordInput).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                findViewById<MaterialCardView>(R.id.changePw_password).strokeWidth = 4
                findViewById<MaterialCardView>(R.id.changePw_password).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                findViewById<MaterialCardView>(R.id.changePw_password).strokeWidth = 0
                findViewById<MaterialCardView>(R.id.changePw_password).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }

        findViewById<EditText>(R.id.changePw_confirmPasswordInput).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                findViewById<MaterialCardView>(R.id.changePw_password2).strokeWidth = 4
                findViewById<MaterialCardView>(R.id.changePw_password2).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                findViewById<MaterialCardView>(R.id.changePw_password2).strokeWidth = 0
                findViewById<MaterialCardView>(R.id.changePw_password2).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }
    }

    private fun getColorWithAlpha(color: Int, alpha: Float): Int {
        val baseColor = ResourcesCompat.getColor(resources, color, null)
        val alphaInt = (alpha * 255).toInt()
        return Color.argb(alphaInt, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor))
    }
}