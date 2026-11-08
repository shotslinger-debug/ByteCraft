package com.upchiapas.bytecraft.data.repository

import com.upchiapas.bytecraft.data.mapper.toDomain
import com.upchiapas.bytecraft.data.remote.api.AuthApiService
import com.upchiapas.bytecraft.data.remote.dto.LoginRequestDto
import com.upchiapas.bytecraft.data.remote.dto.RegisterRequestDto
import com.upchiapas.bytecraft.domain.model.AuthSession
import com.upchiapas.bytecraft.domain.repository.AuthRepository
import com.upchiapas.bytecraft.domain.util.NetworkResult
import java.io.IOException

class AuthRepositoryImpl(
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun login(email: String, password: String): NetworkResult<AuthSession> {
        return try {
            val response = authApiService.login(
                LoginRequestDto(email = email, password = password)
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body.toDomain())
                } else {
                    NetworkResult.Error("Respuesta vacía del servidor", response.code())
                }
            } else {
                val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                    ?: "Error de autenticación (${response.code()})"
                NetworkResult.Error(errorMsg, response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Error("No se pudo conectar al servidor. Revisa tu conexión a internet o la IP del backend.", null)
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Error inesperado durante el inicio de sesión", null)
        }
    }

    override suspend fun register(name: String, email: String, password: String): NetworkResult<AuthSession> {
        return try {
            val response = authApiService.register(
                RegisterRequestDto(name = name, email = email, password = password)
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body.toDomain())
                } else {
                    NetworkResult.Error("Respuesta vacía del servidor", response.code())
                }
            } else {
                val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                    ?: "Error al registrar usuario (${response.code()})"
                NetworkResult.Error(errorMsg, response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Error("No se pudo conectar al servidor. Revisa tu conexión a internet o la IP del backend.", null)
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Error inesperado durante el registro", null)
        }
    }
}
