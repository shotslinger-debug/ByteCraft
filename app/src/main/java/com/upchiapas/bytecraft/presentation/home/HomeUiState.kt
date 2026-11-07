package com.upchiapas.bytecraft.presentation.home

import com.upchiapas.bytecraft.domain.model.Component

data class HomeUiState(
    val isLoading: Boolean = false,
    val components: List<Component> = emptyList(),
    val selectedCategory: String? = null,
    val isFormOpen: Boolean = false,
    val editingComponent: Component? = null,
    val componentToDelete: Component? = null,
    val userMessage: String? = null,
    val errorMessage: String? = null
) {
    val filteredComponents: List<Component>
        get() = if (selectedCategory == null) {
            components
        } else {
            components.filter { it.categoria == selectedCategory }
        }
}
