package com.upchiapas.bytecraft.di

import android.content.Context
import com.upchiapas.bytecraft.data.remote.api.AuthApiService
import com.upchiapas.bytecraft.data.remote.api.ComponentesApiService
import com.upchiapas.bytecraft.data.repository.AuthRepositoryImpl
import com.upchiapas.bytecraft.data.repository.ComponentRepositoryImpl
import com.upchiapas.bytecraft.domain.repository.AuthRepository
import com.upchiapas.bytecraft.domain.repository.ComponentRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val authRepository: AuthRepository
    val componentRepository: ComponentRepository
}

class DefaultAppContainer(
    @Suppress("unused") private val context: Context
) : AppContainer {

    private val baseUrl = "http://10.0.2.2:3000/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    private val componentesApiService: ComponentesApiService by lazy {
        retrofit.create(ComponentesApiService::class.java)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authApiService)
    }

    override val componentRepository: ComponentRepository by lazy {
        ComponentRepositoryImpl(componentesApiService)
    }
}
