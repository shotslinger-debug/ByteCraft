package com.upchiapas.bytecraft.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upchiapas.bytecraft.ui.components.CategoryChipsRow
import com.upchiapas.bytecraft.ui.components.ComponentCard
import com.upchiapas.bytecraft.ui.components.ComponentFormDialog
import com.upchiapas.bytecraft.ui.theme.CyanNeon
import com.upchiapas.bytecraft.ui.theme.DarkBg
import com.upchiapas.bytecraft.ui.theme.ErrorRed
import com.upchiapas.bytecraft.ui.theme.SurfaceDark
import com.upchiapas.bytecraft.ui.theme.TextMuted
import com.upchiapas.bytecraft.ui.theme.TextPrimary
import com.upchiapas.bytecraft.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val categories = listOf("GPU", "CPU", "RAM", "SSD", "MOTHERBOARD")

    // Manejo reactivo de Snackbars informativos
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            homeViewModel.clearUserMessage()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { err ->
            snackbarHostState.showSnackbar(err)
        }
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
                    IconButton(onClick = { homeViewModel.loadComponents() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar inventario",
                            tint = CyanNeon
                        )
                    }
                    IconButton(onClick = onLogout) {
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
                onClick = { homeViewModel.openCreateDialog() },
                containerColor = CyanNeon,
                contentColor = DarkBg
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Componente")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DarkBg,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Chips de filtro horizontal
            CategoryChipsRow(
                categories = categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { homeViewModel.selectCategory(it) }
            )

            // Contenedor principal con soporte exhaustivo de estados: Carga, Error, Vacío y Lista
            when {
                // Estado 1: Carga en progreso
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = CyanNeon)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Sincronizando inventario...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Estado 2: Error de red o servidor con botón de reintento
                uiState.errorMessage != null && uiState.components.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error de conexión",
                                tint = ErrorRed,
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No se pudo conectar al servidor",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = uiState.errorMessage ?: "Verifica que el backend esté en ejecución.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(
                                onClick = {
                                    homeViewModel.clearErrorMessage()
                                    homeViewModel.loadComponents()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyanNeon,
                                    contentColor = DarkBg
                                )
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Reintentar conexión", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Estado 3: Lista vacía (Empty State)
                uiState.filteredComponents.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "📦",
                                fontSize = 52.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (uiState.selectedCategory != null) {
                                    "Sin componentes en categoría ${uiState.selectedCategory}"
                                } else {
                                    "Inventario sin hardware registrado"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Comienza registrando un nuevo componente de hardware",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedButton(
                                onClick = { homeViewModel.openCreateDialog() },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanNeon),
                                border = BorderStroke(1.dp, CyanNeon)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Registrar Componente", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                // Estado 4: Lista poblada con tarjetas de hardware
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.filteredComponents,
                            key = { it.id }
                        ) { component ->
                            ComponentCard(
                                component = component,
                                onEdit = { homeViewModel.openEditDialog(it) },
                                onDelete = { homeViewModel.requestDelete(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Crear / Editar
    if (uiState.isFormOpen) {
        ComponentFormDialog(
            initialComponent = uiState.editingComponent,
            onDismiss = { homeViewModel.closeFormDialog() },
            onSave = { homeViewModel.saveComponent(it) }
        )
    }

    // Diálogo de confirmación para eliminar (Requisito RF-09)
    uiState.componentToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { homeViewModel.dismissDelete() },
            title = { Text("¿Eliminar componente?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("Se dará de baja '${target.nombre}' del inventario.", color = TextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = { homeViewModel.confirmDelete() }
                ) {
                    Text("Eliminar", color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { homeViewModel.dismissDelete() }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = SurfaceDark
        )
    }
}
