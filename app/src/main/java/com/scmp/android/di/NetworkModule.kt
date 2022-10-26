package com.scmp.android.di

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val HTTPS_URL_APP_PREFIX = "https://reqres.in/api/"

    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideRetrofit(
        context: Context
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(HTTPS_URL_APP_PREFIX)
            .client(OkHttpClient.Builder().addInterceptor{
                it.proceed(it.request()).apply {
                    Log.d("Eddie","test request:${code()}")
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
}