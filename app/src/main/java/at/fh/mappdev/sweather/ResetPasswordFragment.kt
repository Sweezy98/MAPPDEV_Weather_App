package at.fh.mappdev.sweather

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.apollographql.apollo3.ApolloClient
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.Console
import kotlin.coroutines.CoroutineContext

class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password), CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<MaterialCardView>(R.id.pwreset_btn).setOnClickListener() {
            launch {
                val email = view.findViewById<EditText>(R.id.pwreset_emailInput).text.toString()
                if (email.isEmpty()) {
                    // view.findViewById<EditText>(R.id.pwreset_email).error = "Please enter your email address"
                    return@launch
                }
                Log.d("ResetPasswordFragment", "Email: $email")
                view.findViewById<ProgressBar>(R.id.pwreset_LoadingSpinner).visibility = View.VISIBLE
                val result = apolloClient(requireContext()).query(ForgotPasswordQuery(email = email)).execute()
                Log.d("ResetPasswordFragment", result.data.toString())
                view.findViewById<ProgressBar>(R.id.pwreset_LoadingSpinner).visibility = View.INVISIBLE
                activity?.supportFragmentManager?.commit {
                    setReorderingAllowed(true)
                    replace<LoginFragment>(R.id.login_fragmentContainer)
                }
                Toast.makeText(requireContext(), "Password reset was sent!", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<EditText>(R.id.pwreset_emailInput).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                view.findViewById<MaterialCardView>(R.id.pwreset_email).strokeWidth = 4
                view.findViewById<MaterialCardView>(R.id.pwreset_email).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                view.findViewById<MaterialCardView>(R.id.pwreset_email).strokeWidth = 0
                view.findViewById<MaterialCardView>(R.id.pwreset_email).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }
    }

    private fun getColorWithAlpha(color: Int, alpha: Float): Int {
        val baseColor = ResourcesCompat.getColor(resources, color, null)
        val alphaInt = (alpha * 255).toInt()
        return Color.argb(alphaInt, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor))
    }
}