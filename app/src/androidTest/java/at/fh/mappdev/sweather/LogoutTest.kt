package at.fh.mappdev.sweather

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogoutTest {
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
    fun checkLogout() {
        onView(withId(R.id.settingsBtn)).perform(click())
        Thread.sleep(1000)
        intended(hasComponent(SettingsActivity::class.java.name))
        onView(withId(R.id.logoutBtn_card)).perform(click())
        Thread.sleep(1000)
        intended(hasComponent(LoginRegisterActivity::class.java.name))
    }
}