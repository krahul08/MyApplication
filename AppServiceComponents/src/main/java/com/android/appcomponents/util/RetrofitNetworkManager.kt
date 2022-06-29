package com.android.appcomponents.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkUtility(private val ctx: Context) {

    private val onlineInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
        response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private var offlineInterceptor = Interceptor { chain ->
        var request: Request = chain.request()
        if (!Utility(ctx).isNetworkConnected()) {
            // Offline cache available for 30 days
            val maxStale = 60 * 60 * 24 * 30
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma")
                .build()
        }
        chain.proceed(request)
    }

    var cacheSize = 10 * 1024 * 1024 // 10 MB

    var cache: Cache = Cache(ctx.cacheDir, cacheSize.toLong())

    @RequiresApi(Build.VERSION_CODES.M)
    var okHttpClient: OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) //wait for 1 minute for response
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
            .cache(cache)
            .build()

    fun getRetrofitInstance(base_url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}

