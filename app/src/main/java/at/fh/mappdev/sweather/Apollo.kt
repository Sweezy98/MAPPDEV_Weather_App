package at.fh.mappdev.sweather

import android.content.Context
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import kotlin.coroutines.CoroutineContext

private class AuthorizationInterceptor(val context: Context): Interceptor, CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    val apolloClientRefresh = ApolloClient.Builder()
        .serverUrl("https://api.mappdev.jslabs.at/api/v1/graphql")
        .build()

    // function to check if the token is expired
    fun isTokenExpired(token: String): Boolean {
        val tokenParts = token.split(".")
        val payload = tokenParts[1]
        val decodedPayload = String(android.util.Base64.decode(payload, android.util.Base64.DEFAULT))
        val expIndex = decodedPayload.indexOf("exp")
        val expString = decodedPayload.substring(expIndex + 5, expIndex + 15)
        val exp = expString.toLong()
        val now = System.currentTimeMillis() / 1000
        Log.d("isTokenExpired", "exp: $exp, now: $now")
        return exp < now
    }

    // function to refresh the token
    fun refreshToken(context: Context): String {
        val refreshToken = context.getSharedPreferences("at.fh.mappdev.sweather", Context.MODE_PRIVATE).getString("REFRESH_TOKEN", null)
        var accessToken = ""
        launch {
            val result = apolloClientRefresh.query(RenewTokenQuery(refreshToken!!)).execute()
            if (result.data?.renewToken != null && result.errors == null) {
                context.getSharedPreferences("at.fh.mappdev.sweather", Context.MODE_PRIVATE).edit().putString("ACCESS_TOKEN", result.data!!.renewToken!!.accessToken).apply()
                context.getSharedPreferences("at.fh.mappdev.sweather", Context.MODE_PRIVATE).edit().putString("REFRESH_TOKEN", result.data!!.renewToken!!.refreshToken).apply()
                accessToken = result.data!!.renewToken!!.accessToken!!
            }
        }
        return accessToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var requestBuilder = chain.request().newBuilder()
        // get access token from shared preferences
        var accessToken = context.getSharedPreferences("at.fh.mappdev.sweather", Context.MODE_PRIVATE).getString("ACCESS_TOKEN", null)
        // if access token is not null, add it to the request
        if (accessToken != null) {
            // check if access token JWT is expired
            // if it is, refresh it
            if (isTokenExpired(accessToken)) {
                accessToken = refreshToken(context)
            }
            requestBuilder = requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

fun apolloClient(context: Context): ApolloClient {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthorizationInterceptor(context))
        .build()

    return ApolloClient.Builder()
        .serverUrl("https://api.mappdev.jslabs.at/api/v1/graphql")
        .okHttpClient(okHttpClient)
        .build()
}