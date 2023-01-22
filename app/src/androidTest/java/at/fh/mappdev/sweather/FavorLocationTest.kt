package at.fh.mappdev.sweather

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
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
class FavorLocationTest {
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
    fun favorLocation() {
        onView(withId(R.id.locationBtn)).perform(click())

        onView(withId(R.id.editLocationName)).perform(click())
        onView(withId(R.id.editLocationName)).perform(typeText("Graz"))
        onView(withId(R.id.locationName)).perform(click())

        onView(withId(R.id.favoriseBtn)).perform(click())
        onView(withId(R.id.locationBtn)).perform(click())

        assert(onView(withId(R.id.favouriteName)).toString().contains("Graz"))
    }
}