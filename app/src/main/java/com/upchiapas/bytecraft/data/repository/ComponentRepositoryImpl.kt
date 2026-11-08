package com.upchiapas.bytecraft.data.repository

import com.upchiapas.bytecraft.data.mapper.toCreateDto
import com.upchiapas.bytecraft.data.mapper.toDomain
import com.upchiapas.bytecraft.data.remote.api.ComponentesApiService
import com.upchiapas.bytecraft.domain.model.Component
import com.upchiapas.bytecraft.domain.repository.ComponentRepository
import com.upchiapas.bytecraft.domain.util.NetworkResult
import java.io.IOException

class ComponentRepositoryImpl(
    private val apiService: ComponentesApiService
) : ComponentRepository {

    override suspend fun getComponents(categoria: String?): NetworkResult<List<Component>> {
        return try {
            val response = apiService.getComponentes(categoria = categoria)
            if (response.isSuccessful) {
                val body = response.body() ?: emptyList()
                NetworkResult.Success(body.toDomain())
            } else {
                val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                    ?: "Error al obtener componentes (${response.code()})"
                NetworkResult.Error(errorMsg, response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Error("Fallo de red: imposible contactar el servidor", null)
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Error desconocido al consultar el inventario", null)
        }
    }

    override suspend fun getComponentById(id: String): NetworkResult<Component> {
        return try {
            val response = apiService.getComponenteById(id = id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body.toDomain())
                } else {
                    NetworkResult.Error("Componente no encontrado en el servidor", response.code())
                }
            } else {
                val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                    ?: "Componente no encontrado (${response.code()})"
                NetworkResult.Error(errorMsg, response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Error("Fallo de red al buscar el componente", null)
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Error desconocido al buscar componente", null)
        }
    }

    override suspend fun createComponent(component: Component): NetworkResult<Component> {
        return try {
            val response = apiService.createComponente(dto = component.toCreateDto())
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body.toDomain())
                } else {
                    NetworkResult.Error("El servidor no retornó el componente creado", response.code())
                }
            } else {
                val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                    ?: "Error al crear el componente (${response.code()})"
                NetworkResult.Error(errorMsg, response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Error("Fallo de red al crear el componente", null)
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Error desconocido al crear componente", null)
        }
    }

    override suspend fun updateComponent(component: Component): NetworkResult<Component> {
        return try {
            val response = apiService.updateComponente(
                id = component.id,
                dto = component.toCreateDto()
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body.toDomain())
                } else {
                    NetworkResult.Error("El servidor no retornó el componente actualizado", response.code())
                }
            } else {
                val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                    ?: "Error al actualizar el componente (${response.code()})"
                NetworkResult.Error(errorMsg, response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Error("Fallo de red al actualizar el componente", null)
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Error desconocido al actualizar componente", null)
        }
    }

    override suspend fun deleteComponent(id: String): NetworkResult<Unit> {
        return try {
            val response = apiService.deleteComponente(id = id)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                    ?: "Error al eliminar el componente (${response.code()})"
                NetworkResult.Error(errorMsg, response.code())
            }
        } catch (e: IOException) {
            NetworkResult.Error("Fallo de red al eliminar el componente", null)
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Error desconocido al eliminar componente", null)
        }
    }
}
