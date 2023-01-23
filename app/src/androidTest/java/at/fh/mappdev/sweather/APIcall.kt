package at.fh.mappdev.sweather

import androidx.test.InstrumentationRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class APIcall {

    suspend fun hello(): String {
        return withContext(Dispatchers.Default) {
            val applicationContext = InstrumentationRegistry.getTargetContext().applicationContext
            val response = apolloClient(applicationContext).query(SayHelloQuery("Tester")).execute()
            return@withContext response.data?.sayHello.toString()
        }
    }
}