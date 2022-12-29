package at.fh.mappdev.sweather

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.fragment.app.commit
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class LoginFragment : Fragment(R.layout.fragment_login), CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val SESSION = "SESSION"
        const val EMAIL = "EMAIL"
        const val NAME = "NAME"
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.login_registerLink).setOnClickListener {
            activity?.supportFragmentManager?.commit {
                setReorderingAllowed(true)
                replace<RegisterFragment>(R.id.login_fragmentContainer)
            }
        }

        view.findViewById<TextView>(R.id.login_forgotPasswordLink).setOnClickListener {
            activity?.supportFragmentManager?.commit {
                setReorderingAllowed(true)
                replace<ResetPasswordFragment>(R.id.login_fragmentContainer)
            }
        }

        view.findViewById<MaterialCardView>(R.id.login_loginBtn).setOnClickListener() {
            launch {
                val sharedPreferences = activity?.getSharedPreferences(activity?.packageName, MODE_PRIVATE)


                val email = view.findViewById<EditText>(R.id.login_emailInput).text.toString()
                val password = view.findViewById<EditText>(R.id.login_passwordInput).text.toString()
                view.findViewById<ProgressBar>(R.id.login_LoadingSpinner).visibility = View.VISIBLE
                val result = apolloClient(requireContext()).query(LoginQuery(email = email, password = password)).execute()
                if (result.data?.login != null && result.errors == null) {
                    sharedPreferences?.edit()?.putString(ACCESS_TOKEN, result.data!!.login!!.tokens!!.accessToken)?.apply()
                    sharedPreferences?.edit()?.putString(REFRESH_TOKEN, result.data!!.login!!.tokens!!.refreshToken)?.apply()
                    sharedPreferences?.edit()?.putString(SESSION, result.data!!.login!!.session)?.apply()
                    sharedPreferences?.edit()?.putString(NAME, result.data!!.login!!.name)?.apply()
                    sharedPreferences?.edit()?.putString(EMAIL, result.data!!.login!!.email)?.apply()

                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)

                    activity?.finish()
                } else {
                    //wrong credentials
                }
                view.findViewById<ProgressBar>(R.id.login_LoadingSpinner).visibility = View.INVISIBLE
            }
        }

        view.findViewById<EditText>(R.id.login_emailInput).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                view.findViewById<MaterialCardView>(R.id.login_email).strokeWidth = 4
                view.findViewById<MaterialCardView>(R.id.login_email).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                view.findViewById<MaterialCardView>(R.id.login_email).strokeWidth = 0
                view.findViewById<MaterialCardView>(R.id.login_email).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }

        view.findViewById<EditText>(R.id.login_passwordInput).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                view.findViewById<MaterialCardView>(R.id.login_password).strokeWidth = 4
                view.findViewById<MaterialCardView>(R.id.login_password).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                view.findViewById<MaterialCardView>(R.id.login_password).strokeWidth = 0
                view.findViewById<MaterialCardView>(R.id.login_password).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }
    }

    private fun getColorWithAlpha(color: Int, alpha: Float): Int {
        val baseColor = ResourcesCompat.getColor(resources, color, null)
        val alphaInt = (alpha * 255).toInt()
        return Color.argb(alphaInt, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor))
    }
}