package com.sample.android.cafebazaar.network

import com.sample.android.cafebazaar.BuildConfig
import com.sample.android.cafebazaar.util.toSimpleString
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A retrofit service to fetch items.
 */
interface FoursquareService {

    @GET("v2/venues/explore")
    fun getCategories(@Query("ll") latLon: String): Observable<ResponseWrapper>

    @GET("v2/venues/{VENUE_ID}")
    fun getVenue(@Path("VENUE_ID") id : String) : Observable<ResponseNetworkWrapper>
}

@Singleton
class RequestInterceptor @Inject constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("client_id", BuildConfig.FOURSQUARE_CLIENT_ID)
            .addQueryParameter("client_secret", BuildConfig.FOURSQUARE_CLIENT_SECRET)
            .addQueryParameter("v", toSimpleString(Calendar.getInstance().time))
            .build()

        val request = original.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**
 * Main entry point for network access.
 */
@Module
class Network {

    @Provides
    @Singleton
    fun provideOkHttpClient(requestInterceptor: RequestInterceptor): OkHttpClient {

        val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.d(it)
        })
        logger.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(logger)
            .build()
    }

    // Configure retrofit to parse JSON and use rxJava
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Singleton
    @Provides
    fun itemApi(retrofit: Retrofit): FoursquareService =
        retrofit.create(FoursquareService::class.java)
}