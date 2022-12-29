package at.fh.mappdev.sweather.view

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import at.fh.mappdev.sweather.R
import com.google.android.material.card.MaterialCardView

class IconInputView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        init(attrs)
    }


    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.view_icon_input, this)

        findViewById<EditText>(R.id.iconInputView_input).setOnFocusChangeListener() { _, hasFocus ->
            @RequiresApi(Build.VERSION_CODES.P)
            if (hasFocus) {
                findViewById<MaterialCardView>(R.id.iconInputView_card).strokeWidth = 4
                findViewById<MaterialCardView>(R.id.iconInputView_card).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
            } else {
                findViewById<MaterialCardView>(R.id.iconInputView_card).strokeWidth = 0
                findViewById<MaterialCardView>(R.id.iconInputView_card).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
            }
        }

        val ta = context.obtainStyledAttributes(attrs, R.styleable.IconInputView)
        try {
            val hintText = ta.getString(R.styleable.IconInputView_hintText)
            findViewById<EditText>(R.id.iconInputView_input).hint = hintText

            val icon = ta.getResourceId(R.styleable.IconInputView_icon, 0)
            findViewById<ImageView>(R.id.iconInputView_icon).setImageResource(icon)

            val inputType = ta.getInt(R.styleable.IconInputView_inputType, 0)
            findViewById<EditText>(R.id.iconInputView_input).inputType = inputType

            val autoFillHint = ta.getString(R.styleable.IconInputView_autoFillHints)
            @RequiresApi(Build.VERSION_CODES.P)
            if(autoFillHint != null) {
                findViewById<EditText>(R.id.iconInputView_input).setAutofillHints(autoFillHint)
            }
        } finally {
            ta.recycle()
        }
    }

    private fun getColorWithAlpha(color: Int, alpha: Float): Int {
        val baseColor = ResourcesCompat.getColor(resources, color, null)
        val alphaInt = (alpha * 255).toInt()
        return Color.argb(alphaInt, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor))
    }
}