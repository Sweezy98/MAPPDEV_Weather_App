package at.fh.mappdev.sweather
import android.content.Context
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import at.fh.mappdev.sweather.SettingsActivity.Companion.UNIT
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TempTests {
    val sharedPreferences = getTargetContext().getSharedPreferences("at.fh.mappdev.sweather", Context.MODE_PRIVATE)
    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }
    // Test if the temperature is displayed in Celsius
    @Test
    fun clickOnRadioButtonCelsius() {
        onView(withId(R.id.settingsBtn)).perform(click())
        onView(withId(R.id.Celsius)).perform(click())
        // Check if the shared preferences Unit is set to Celsius
        val unit = sharedPreferences.getString(UNIT, null)
        assert(unit == "Celsius")
    }
    // Test if the temperature is displayed in Fahrenheit
    @Test
    fun clickOnRadioButtonFahrenheit() {
        onView(withId(R.id.settingsBtn)).perform(click())
        onView(withId(R.id.Fahrenheit)).perform(click())
        val unit = sharedPreferences.getString(UNIT, null)
        assert(unit == "Fahrenheit")
    }
    // Test if the temperature changes on the main activity
    @Test
    fun checkTempChanged() {

        /*val temperatureVal = onView(withId(R.id.current_temperature)).toString().replace("°", "")
        val temperatureValInt = temperatureVal.toInt()
        val tempChangedVal:Int = temperatureValInt * (9/5) + 32

        onView(withId(R.id.settingsBtn)).perform(click())
        onView(withId(R.id.Celsius)).perform(click())
        onView(withId(R.id.Fahrenheit)).perform(click())

        val otherTemperatureVal = onView(withId(R.id.current_temperature)).toString().replace("°", "")

        assert(tempChangedVal == otherTemperatureVal.toInt())*/
    }
}