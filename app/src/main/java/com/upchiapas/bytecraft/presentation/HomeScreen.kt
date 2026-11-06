package com.upchiapas.bytecraft.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upchiapas.bytecraft.domain.model.Component
import com.upchiapas.bytecraft.ui.components.CategoryChipsRow
import com.upchiapas.bytecraft.ui.components.ComponentCard
import com.upchiapas.bytecraft.ui.components.ComponentFormDialog
import com.upchiapas.bytecraft.ui.theme.CyanNeon
import com.upchiapas.bytecraft.ui.theme.DarkBg
import com.upchiapas.bytecraft.ui.theme.ErrorRed
import com.upchiapas.bytecraft.ui.theme.SurfaceDark
import com.upchiapas.bytecraft.ui.theme.TextPrimary
import com.upchiapas.bytecraft.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    // Datos semilla para el Commit 1 (Maquetado de UI interactivo en memoria)
    val componentsList = remember {
        mutableStateListOf(
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
    }

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var editingComponent by remember { mutableStateOf<Component?>(null) }
    var componentToDelete by remember { mutableStateOf<Component?>(null) }

    val categories = listOf("GPU", "CPU", "RAM", "SSD", "MOTHERBOARD")

    val filteredComponents = if (selectedCategory == null) {
        componentsList
    } else {
        componentsList.filter { it.categoria == selectedCategory }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ByteCraft",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                actions = {
                    IconButton(onClick = { /* Próximo commit: Logout */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = ErrorRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceDark,
                    titleContentColor = TextPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingComponent = null
                    showDialog = true
                },
                containerColor = CyanNeon,
                contentColor = DarkBg
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Componente")
            }
        },
        containerColor = DarkBg,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Fila de chips de categorías
            CategoryChipsRow(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            // Lista de Componentes
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredComponents,
                    key = { it.id }
                ) { component ->
                    ComponentCard(
                        component = component,
                        onEdit = {
                            editingComponent = it
                            showDialog = true
                        },
                        onDelete = {
                            componentToDelete = it
                        }
                    )
                }
            }
        }
    }

    // Modal Crear / Editar
    if (showDialog) {
        ComponentFormDialog(
            initialComponent = editingComponent,
            onDismiss = { showDialog = false },
            onSave = { saved ->
                val index = componentsList.indexOfFirst { it.id == saved.id }
                if (index != -1) {
                    componentsList[index] = saved
                } else {
                    componentsList.add(0, saved)
                }
                showDialog = false
            }
        )
    }

    // Diálogo de confirmación para eliminar
    componentToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { componentToDelete = null },
            title = { Text("¿Eliminar componente?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("Se dará de baja '${target.nombre}' del inventario.", color = TextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        componentsList.remove(target)
                        componentToDelete = null
                    }
                ) {
                    Text("Eliminar", color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { componentToDelete = null }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = SurfaceDark
        )
    }
}
