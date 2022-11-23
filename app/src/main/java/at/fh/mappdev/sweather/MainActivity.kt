package at.fh.mappdev.sweather

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launch {
            val response = apolloClient.query(HelloQuery("User")).execute()
            findViewById<TextView>(R.id.HelloMsg).text = response.data?.sayHello
        }
    }
}