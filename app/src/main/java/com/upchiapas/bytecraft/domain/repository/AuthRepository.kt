package com.upchiapas.bytecraft.domain.repository

import com.upchiapas.bytecraft.domain.model.AuthSession
import com.upchiapas.bytecraft.domain.util.NetworkResult

interface AuthRepository {
    suspend fun login(email: String, password: String): NetworkResult<AuthSession>
    suspend fun register(name: String, email: String, password: String): NetworkResult<AuthSession>
}
