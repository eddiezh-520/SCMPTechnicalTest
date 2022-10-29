package com.scmp.android.di

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.scmp.android.util.Constant.HTTPS_URL_APP_PREFIX
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



    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(HTTPS_URL_APP_PREFIX)
            .client(OkHttpClient.Builder().addInterceptor{
                it.proceed(it.request()).apply {
                    Log.d("Eddie","test request:${code()}")
                    Log.d("Eddie","test isSuccessful:$isSuccessful")
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
}