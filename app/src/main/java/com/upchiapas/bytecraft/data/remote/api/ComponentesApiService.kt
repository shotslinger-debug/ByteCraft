package com.upchiapas.bytecraft.data.remote.api

import com.upchiapas.bytecraft.data.remote.dto.ComponentDto
import com.upchiapas.bytecraft.data.remote.dto.CreateComponentDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ComponentesApiService {

    @GET("componentes")
    suspend fun getComponentes(
        @Query("categoria") categoria: String? = null
    ): Response<List<ComponentDto>>

    @GET("componentes/{id}")
    suspend fun getComponenteById(
        @Path("id") id: String
    ): Response<ComponentDto>

    @POST("componentes")
    suspend fun createComponente(
        @Body dto: CreateComponentDto
    ): Response<ComponentDto>

    @PUT("componentes/{id}")
    suspend fun updateComponente(
        @Path("id") id: String,
        @Body dto: CreateComponentDto
    ): Response<ComponentDto>

    @DELETE("componentes/{id}")
    suspend fun deleteComponente(
        @Path("id") id: String
    ): Response<Unit>
}
