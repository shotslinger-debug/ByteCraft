package com.upchiapas.bytecraft.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upchiapas.bytecraft.data.local.SessionManager
import com.upchiapas.bytecraft.domain.model.Component
import com.upchiapas.bytecraft.domain.repository.ComponentRepository
import com.upchiapas.bytecraft.domain.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val componentRepository: ComponentRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadComponents(null)
    }

    fun loadComponents(categoria: String? = _uiState.value.selectedCategory) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = componentRepository.getComponents(categoria)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            components = result.data,
                            selectedCategory = categoria,
                            errorMessage = null
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun selectCategory(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadComponents(category)
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
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isFormOpen = false, editingComponent = null) }
            val isEditing = component.id.isNotBlank() && _uiState.value.components.any { it.id == component.id }
            val result = if (isEditing) {
                componentRepository.updateComponent(component)
            } else {
                componentRepository.createComponent(component)
            }

            when (result) {
                is NetworkResult.Success -> {
                    val msg = if (isEditing) {
                        "Componente '${component.nombre}' actualizado con éxito"
                    } else {
                        "Componente '${component.nombre}' registrado con éxito"
                    }
                    _uiState.update { it.copy(userMessage = msg) }
                    loadComponents(_uiState.value.selectedCategory)
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al guardar: ${result.message}"
                        )
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun requestDelete(component: Component) {
        _uiState.update { it.copy(componentToDelete = component) }
    }

    fun dismissDelete() {
        _uiState.update { it.copy(componentToDelete = null) }
    }

    fun confirmDelete() {
        val target = _uiState.value.componentToDelete ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, componentToDelete = null) }
            when (val result = componentRepository.deleteComponent(target.id)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(userMessage = "'${target.nombre}' eliminado del inventario")
                    }
                    loadComponents(_uiState.value.selectedCategory)
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al eliminar: ${result.message}"
                        )
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
