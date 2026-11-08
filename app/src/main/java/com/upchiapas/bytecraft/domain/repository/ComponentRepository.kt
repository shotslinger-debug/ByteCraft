package com.upchiapas.bytecraft.domain.repository

import com.upchiapas.bytecraft.domain.model.Component
import com.upchiapas.bytecraft.domain.util.NetworkResult

interface ComponentRepository {
    suspend fun getComponents(categoria: String? = null): NetworkResult<List<Component>>
    suspend fun getComponentById(id: String): NetworkResult<Component>
    suspend fun createComponent(component: Component): NetworkResult<Component>
    suspend fun updateComponent(component: Component): NetworkResult<Component>
    suspend fun deleteComponent(id: String): NetworkResult<Unit>
}
