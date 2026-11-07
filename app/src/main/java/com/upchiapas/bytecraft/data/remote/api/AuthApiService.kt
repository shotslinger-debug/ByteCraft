package com.upchiapas.bytecraft.data.remote.api

import com.upchiapas.bytecraft.data.remote.dto.AuthResponseDto
import com.upchiapas.bytecraft.data.remote.dto.LoginRequestDto
import com.upchiapas.bytecraft.data.remote.dto.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): Response<AuthResponseDto>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<AuthResponseDto>
}
