package com.scmp.android.di

import com.scmp.android.network.StaffLoginService
import com.scmp.android.repository.StaffLoginRepo
import com.scmp.android.repository.StaffLoginRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object StaffLoginModule {
    @ActivityRetainedScoped
    @Provides
    fun provideStaffLoginService(
        retrofit: Retrofit
    ): StaffLoginService {
        return retrofit
            .create(StaffLoginService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideStaffLoginRepo(
        staffLoginService: StaffLoginService,
    ): StaffLoginRepo {
        return StaffLoginRepoImpl(
            staffLoginService
        )
    }
}