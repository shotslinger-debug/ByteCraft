package com.upchiapas.bytecraft.di

import android.content.Context
import com.upchiapas.bytecraft.data.local.SessionManager
import com.upchiapas.bytecraft.data.remote.api.AuthApiService
import com.upchiapas.bytecraft.data.remote.api.ComponentesApiService
import com.upchiapas.bytecraft.data.remote.interceptor.AuthInterceptor
import com.upchiapas.bytecraft.data.repository.AuthRepositoryImpl
import com.upchiapas.bytecraft.data.repository.ComponentRepositoryImpl
import com.upchiapas.bytecraft.domain.repository.AuthRepository
import com.upchiapas.bytecraft.domain.repository.ComponentRepository
import com.upchiapas.bytecraft.presentation.auth.AuthViewModelFactory
import com.upchiapas.bytecraft.presentation.home.HomeViewModelFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val sessionManager: SessionManager
    val authRepository: AuthRepository
    val componentRepository: ComponentRepository
    val authViewModelFactory: AuthViewModelFactory
    val homeViewModelFactory: HomeViewModelFactory
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    // URL base de producción en AWS EC2 (Elastic IP)
    var baseUrl: String = "http://52.87.39.224:3000/api/"

    override val sessionManager: SessionManager by lazy {
        SessionManager(context.applicationContext)
    }

    private val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(sessionManager)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
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

    override val authViewModelFactory: AuthViewModelFactory by lazy {
        AuthViewModelFactory(authRepository, sessionManager)
    }

    override val homeViewModelFactory: HomeViewModelFactory by lazy {
        HomeViewModelFactory(componentRepository, sessionManager)
    }
}
