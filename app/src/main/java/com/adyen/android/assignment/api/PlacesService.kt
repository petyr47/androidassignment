package com.adyen.android.assignment.api

import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.api.model.details.PlaceDetailsResponse
import com.adyen.android.assignment.api.model.search.PlacesSearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap


interface PlacesService {
    /**
     * Get venue recommendations.
     *
     * See [the docs](https://developer.foursquare.com/docs/api/venues/explore)
     */
    @GET("places/search")
    suspend fun getPlaces(@QueryMap query: Map<String, String>): PlacesSearchResponse

    @GET("places/{fsq_id}")
    suspend fun getPlaceDetails(@Path("fsq_id", encoded = true) id : String, @QueryMap query: Map<String, String>) : PlaceDetailsResponse

    companion object  {

        private fun makeClient(): OkHttpClient {
            val clientBuilder = OkHttpClient.Builder()
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            clientBuilder.addInterceptor(loggingInterceptor)
            clientBuilder.addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", BuildConfig.API_KEY)
                    .build()
                chain.proceed(newRequest)
            }
            return clientBuilder.build()
        }

        private val retrofit by lazy {
            Retrofit.Builder()
                .client(makeClient())
                .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        val instance: PlacesService by lazy { retrofit.create(PlacesService::class.java) }
    }
}
