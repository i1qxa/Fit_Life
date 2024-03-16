package com.fitlife.atfsd.data.local.remote.yoga

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://yoga-api-nzy4.onrender.com/v1/categories/"
interface YogaService {

    @GET("?")
    suspend fun getYogaResponse()
    : Response<List<PoseCategory>>

    companion object {

        private fun createOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(createLoggingInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
        }

        private fun createLoggingInterceptor(): Interceptor {
            return HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }


        var yogaService: YogaService? = null
        fun getInstance(): YogaService {
            if (yogaService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(createOkHttpClient())
                    .addConverterFactory(Json{
                        ignoreUnknownKeys = true
                        allowSpecialFloatingPointValues = true
                    }.asConverterFactory("application/json".toMediaType()))
                    .build()
                yogaService = retrofit.create(YogaService::class.java)
            }
            return yogaService!!
        }
    }
}
