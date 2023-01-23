package at.fh.mappdev.sweather
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class APITest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    // Test if API is working
    @Test
    fun useRunBlocking() = runBlocking<Unit> {
        val hello = APIcall().hello()

        assert(hello == "Hello, Tester! This is London calling!")
    }
}