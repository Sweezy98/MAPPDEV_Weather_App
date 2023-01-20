package at.fh.mappdev.sweather

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityUnitTests {
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

    @Test
    fun clickOnSettingsButton() {
        onView(withId(R.id.settingsBtn)).perform(click())
        intended(hasComponent(SettingsActivity::class.java.name))
    }

    @Test
    fun clickLocationsButton() {
        onView(withId(R.id.locationBtn)).perform(click())
        intended(hasComponent(LocationActivity::class.java.name))
    }

    @Test
    fun clickOnWeatherForecastCard0() {
        onView(withId(R.id.Day0)).perform(click())
        intended(hasComponent(DetailsActivity::class.java.name))
    }

    @Test
    fun clickOnWeatherForecastCard1() {
        onView(withId(R.id.Day1)).perform(click())
        intended(hasComponent(DetailsActivity::class.java.name))
    }

    @Test
    fun clickOnWeatherForecastCard2() {
        onView(withId(R.id.Day2)).perform(click())
        intended(hasComponent(DetailsActivity::class.java.name))
    }

    @Test
    fun clickOnWeatherForecastCard3() {
        onView(withId(R.id.Day3)).perform(click())
        intended(hasComponent(DetailsActivity::class.java.name))
    }

    @Test
    fun clickOnWeatherForecastCard4() {
        onView(withId(R.id.Day4)).perform(click())
        intended(hasComponent(DetailsActivity::class.java.name))
    }

}