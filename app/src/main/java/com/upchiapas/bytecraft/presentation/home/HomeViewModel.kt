package com.upchiapas.bytecraft.presentation.home

import androidx.lifecycle.ViewModel
import com.upchiapas.bytecraft.domain.model.Component
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            components = listOf(
                Component(
                    id = "comp_1",
                    nombre = "NVIDIA GeForce RTX 4070 Super 12GB",
                    categoria = "GPU",
                    precio = 12899.0,
                    stock = 8,
                    enOferta = true,
                    condicion = "NUEVO",
                    descuentoPorcentaje = 15
                ),
                Component(
                    id = "comp_2",
                    nombre = "AMD Ryzen 7 7800X3D (8C/16T)",
                    categoria = "CPU",
                    precio = 8450.50,
                    stock = 15,
                    enOferta = false,
                    condicion = "NUEVO",
                    descuentoPorcentaje = null
                ),
                Component(
                    id = "comp_3",
                    nombre = "Corsair Vengeance DDR5 32GB (2x16GB) 6000MHz",
                    categoria = "RAM",
                    precio = 2350.0,
                    stock = 3,
                    enOferta = false,
                    condicion = "NUEVO",
                    descuentoPorcentaje = null
                ),
                Component(
                    id = "comp_4",
                    nombre = "Kingston NV2 NVMe M.2 1TB PCIe 4.0",
                    categoria = "SSD",
                    precio = 1199.0,
                    stock = 0,
                    enOferta = false,
                    condicion = "REACONDICIONADO",
                    descuentoPorcentaje = null
                )
            )
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun selectCategory(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun openCreateDialog() {
        _uiState.update { it.copy(isFormOpen = true, editingComponent = null) }
    }

    fun openEditDialog(component: Component) {
        _uiState.update { it.copy(isFormOpen = true, editingComponent = component) }
    }

    fun closeFormDialog() {
        _uiState.update { it.copy(isFormOpen = false, editingComponent = null) }
    }

    fun saveComponent(component: Component) {
        _uiState.update { state ->
            val list = state.components.toMutableList()
            val index = list.indexOfFirst { it.id == component.id }
            val message: String
            if (index != -1) {
                list[index] = component
                message = "Componente '${component.nombre}' actualizado"
            } else {
                list.add(0, component)
                message = "Componente '${component.nombre}' registrado"
            }
            state.copy(
                components = list,
                isFormOpen = false,
                editingComponent = null,
                userMessage = message
            )
        }
    }

    fun requestDelete(component: Component) {
        _uiState.update { it.copy(componentToDelete = component) }
    }

    fun dismissDelete() {
        _uiState.update { it.copy(componentToDelete = null) }
    }

    fun confirmDelete() {
        _uiState.update { state ->
            val target = state.componentToDelete
            if (target != null) {
                val list = state.components.filter { it.id != target.id }
                state.copy(
                    components = list,
                    componentToDelete = null,
                    userMessage = "'${target.nombre}' eliminado del inventario"
                )
            } else {
                state
            }
        }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
