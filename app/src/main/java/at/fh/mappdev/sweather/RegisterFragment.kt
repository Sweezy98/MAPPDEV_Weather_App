package at.fh.mappdev.sweather

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
import at.fh.mappdev.sweather.type.UserInput
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RegisterFragment : Fragment(R.layout.fragment_register), CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.register_LoginLink).setOnClickListener() {
            activity?.supportFragmentManager?.commit {
                setReorderingAllowed(true)
                replace<LoginFragment>(R.id.login_fragmentContainer)
            }
        }

        view.findViewById<MaterialCardView>(R.id.register_registerBtn).setOnClickListener() {
            launch {
                val email = view.findViewById<EditText>(R.id.register_emailInput).text.toString()
                val password =
                    view.findViewById<EditText>(R.id.register_passwordInput).text.toString()
                val passwordConfirm =
                    view.findViewById<EditText>(R.id.register_confirmPasswordInput).text.toString()
                val name = view.findViewById<EditText>(R.id.register_nameInput).text.toString()

                if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || name.isEmpty()) {
                    // view.findViewById<TextView>(R.id.register_error).text = "Please fill out all fields"
                    // view.findViewById<TextView>(R.id.register_error).setTextColor(Color.RED)
                    return@launch
                }

                if (password != passwordConfirm) {
                    // view.findViewById<TextView>(R.id.register_error).text = "Passwords do not match"
                    // view.findViewById<TextView>(R.id.register_error).setTextColor(Color.RED)
                    return@launch
                }
                view.findViewById<ProgressBar>(R.id.register_LoadingSpinner).visibility = View.VISIBLE
                val result = apolloClient(requireContext()).mutation(RegisterMutation(UserInput(email = email, password = password, name = name))).execute()
                if (result.hasErrors()) {
                    // view.findViewById<TextView>(R.id.register_error).text = result.errors().toString()
                    // view.findViewById<TextView>(R.id.register_error).setTextColor(Color.RED)
                    view.findViewById<ProgressBar>(R.id.register_LoadingSpinner).visibility = View.INVISIBLE
                    return@launch
                } else {
                    // view.findViewById<TextView>(R.id.register_error).text = "Registration successful"
                    // view.findViewById<TextView>(R.id.register_error).setTextColor(Color.GREEN)
                    view.findViewById<ProgressBar>(R.id.register_LoadingSpinner).visibility = View.INVISIBLE
                    activity?.supportFragmentManager?.commit {
                        setReorderingAllowed(true)
                        replace<LoginFragment>(R.id.login_fragmentContainer)
                    }
                }
            }
        }

        view.findViewById<EditText>(R.id.register_nameInput).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                view.findViewById<MaterialCardView>(R.id.register_name).strokeWidth = 4
                view.findViewById<MaterialCardView>(R.id.register_name).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                view.findViewById<MaterialCardView>(R.id.register_name).strokeWidth = 0
                view.findViewById<MaterialCardView>(R.id.register_name).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }

        view.findViewById<EditText>(R.id.register_emailInput).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                view.findViewById<MaterialCardView>(R.id.register_email).strokeWidth = 4
                view.findViewById<MaterialCardView>(R.id.register_email).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                view.findViewById<MaterialCardView>(R.id.register_email).strokeWidth = 0
                view.findViewById<MaterialCardView>(R.id.register_email).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }

        view.findViewById<EditText>(R.id.register_passwordInput).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                view.findViewById<MaterialCardView>(R.id.register_password).strokeWidth = 4
                view.findViewById<MaterialCardView>(R.id.register_password).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                view.findViewById<MaterialCardView>(R.id.register_password).strokeWidth = 0
                view.findViewById<MaterialCardView>(R.id.register_password).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }

        view.findViewById<EditText>(R.id.register_confirmPasswordInput).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                view.findViewById<MaterialCardView>(R.id.register_password2).strokeWidth = 4
                view.findViewById<MaterialCardView>(R.id.register_password2).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                view.findViewById<MaterialCardView>(R.id.register_password2).strokeWidth = 0
                view.findViewById<MaterialCardView>(R.id.register_password2).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }

    }

    private fun getColorWithAlpha(color: Int, alpha: Float): Int {
        val baseColor = ResourcesCompat.getColor(resources, color, null)
        val alphaInt = (alpha * 255).toInt()
        return Color.argb(alphaInt, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor))
    }
}