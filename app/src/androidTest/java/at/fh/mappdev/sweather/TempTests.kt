package at.fh.mappdev.sweather
import android.content.Context
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import at.fh.mappdev.sweather.SettingsActivity.Companion.UNIT
import org.hamcrest.CoreMatchers.containsString
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
        //read the current temperature
        onView(withId(R.id.current_temperature)).check(matches(withText(containsString("°"))))
        val currentTemp = onView(withId(R.id.current_temperature)).toString()

        onView(withId(R.id.settingsBtn)).perform(click())
        onView(withId(R.id.Fahrenheit)).perform(click())
        //read the new temperature
        onView(withId(R.id.current_temperature)).check(matches(withText(containsString("°"))))
        val newTemp = onView(withId(R.id.current_temperature)).toString()
        //check if the temperature changed
        assert(currentTemp != newTemp)
    }
}