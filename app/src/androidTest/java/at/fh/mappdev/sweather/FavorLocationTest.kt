package at.fh.mappdev.sweather

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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
        onView(withId(R.id.editLocationName)).perform(typeText("Vienna"))

        onView(withId(R.id.predictionsRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(1000)

        onView(withId(R.id.favoriseBtn)).perform(click())
        onView(withId(R.id.locationBtn)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.favouritesRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(1000)
        //get text value of the location button and check if it is Vienna
        onView(withId(R.id.locationBtn)).check(ViewAssertions.matches(withText("Vienna")))


    }
}