package com.upchiapas.bytecraft.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.upchiapas.bytecraft.domain.model.Component
import com.upchiapas.bytecraft.ui.theme.CyanNeon
import com.upchiapas.bytecraft.ui.theme.DarkBg
import com.upchiapas.bytecraft.ui.theme.OrangeNeon
import com.upchiapas.bytecraft.ui.theme.SurfaceBorder
import com.upchiapas.bytecraft.ui.theme.SurfaceDark
import com.upchiapas.bytecraft.ui.theme.SurfaceDarkElevated
import com.upchiapas.bytecraft.ui.theme.TextMuted
import com.upchiapas.bytecraft.ui.theme.TextPrimary
import com.upchiapas.bytecraft.ui.theme.TextSecondary
import java.util.UUID

@Composable
fun ComponentFormDialog(
    initialComponent: Component? = null,
    onDismiss: () -> Unit,
    onSave: (Component) -> Unit
) {
    val isEditing = initialComponent != null

    var nombre by remember { mutableStateOf(initialComponent?.nombre.orEmpty()) }
    var categoria by remember { mutableStateOf(initialComponent?.categoria ?: "GPU") }
    var condicion by remember { mutableStateOf(initialComponent?.condicion ?: "NUEVO") }
    var precioText by remember { mutableStateOf(initialComponent?.precio?.toString().orEmpty()) }
    var stockText by remember { mutableStateOf(initialComponent?.stock?.toString().orEmpty()) }
    var enOferta by remember { mutableStateOf(initialComponent?.enOferta ?: false) }
    var descuentoText by remember { mutableStateOf(initialComponent?.descuentoPorcentaje?.toString().orEmpty()) }

    var showError by remember { mutableStateOf(false) }

    val categories = listOf("GPU", "CPU", "RAM", "SSD", "MOTHERBOARD")
    val conditions = listOf("NUEVO", "REACONDICIONADO", "USADO")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (isEditing) "Editar Componente" else "Nuevo Componente",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ingresa los atributos requeridos por la API",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Campo: Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Componente") },
                    isError = showError && nombre.trim().length < 3,
                    singleLine = true,
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Selector: Categoría
                Text("Categoría:", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        FilterChip(
                            selected = categoria == cat,
                            onClick = { categoria = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = filterChipColors(categoria == cat)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.drop(3).forEach { cat ->
                        FilterChip(
                            selected = categoria == cat,
                            onClick = { categoria = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = filterChipColors(categoria == cat)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Selector: Condición
                Text("Condición:", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    conditions.forEach { cond ->
                        FilterChip(
                            selected = condicion == cond,
                            onClick = { condicion = cond },
                            label = { Text(cond, fontSize = 10.sp) },
                            colors = filterChipColors(condicion == cond)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Campos: Precio y Stock
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = precioText,
                        onValueChange = { precioText = it },
                        label = { Text("Precio ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = textFieldColors(),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it },
                        label = { Text("Stock") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = textFieldColors(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Switch: En Oferta
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceDarkElevated, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Marcar en Oferta?",
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = enOferta,
                        onCheckedChange = { enOferta = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextPrimary,
                            checkedTrackColor = OrangeNeon
                        )
                    )
                }

                // Campo Reactivo Desplegable: Porcentaje de Descuento
                AnimatedVisibility(visible = enOferta) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = descuentoText,
                            onValueChange = { descuentoText = it },
                            label = { Text("Descuento (%)") },
                            placeholder = { Text("Ej. 15") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (showError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Verifica que el nombre tenga al menos 3 letras, y precio y stock sean válidos.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botones Cancelar y Guardar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            val precio = precioText.toDoubleOrNull()
                            val stock = stockText.toIntOrNull()
                            val descuento = if (enOferta) descuentoText.toIntOrNull() else null

                            if (nombre.trim().length >= 3 && precio != null && precio > 0 && stock != null && stock >= 0) {
                                val component = Component(
                                    id = initialComponent?.id ?: "comp_${UUID.randomUUID().toString().take(6)}",
                                    nombre = nombre.trim(),
                                    categoria = categoria,
                                    precio = precio,
                                    stock = stock,
                                    enOferta = enOferta,
                                    condicion = condicion,
                                    descuentoPorcentaje = descuento
                                )
                                onSave(component)
                            } else {
                                showError = true
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyanNeon,
                            contentColor = DarkBg
                        )
                    ) {
                        Text("Guardar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CyanNeon,
    unfocusedBorderColor = SurfaceBorder,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedLabelColor = CyanNeon,
    unfocusedLabelColor = TextMuted,
    focusedContainerColor = SurfaceDarkElevated,
    unfocusedContainerColor = SurfaceDarkElevated
)

@Composable
private fun filterChipColors(selected: Boolean) = FilterChipDefaults.filterChipColors(
    selectedContainerColor = CyanNeon,
    selectedLabelColor = DarkBg,
    containerColor = SurfaceDarkElevated,
    labelColor = TextSecondary
)
