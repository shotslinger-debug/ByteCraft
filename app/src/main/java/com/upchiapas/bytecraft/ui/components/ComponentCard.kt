package com.upchiapas.bytecraft.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upchiapas.bytecraft.domain.model.Component
import com.upchiapas.bytecraft.ui.theme.CyanNeon
import com.upchiapas.bytecraft.ui.theme.ErrorContainerRed
import com.upchiapas.bytecraft.ui.theme.ErrorRed
import com.upchiapas.bytecraft.ui.theme.OrangeNeon
import com.upchiapas.bytecraft.ui.theme.PurpleNeon
import com.upchiapas.bytecraft.ui.theme.SuccessGreen
import com.upchiapas.bytecraft.ui.theme.SurfaceBorder
import com.upchiapas.bytecraft.ui.theme.SurfaceDark
import com.upchiapas.bytecraft.ui.theme.SurfaceDarkElevated
import com.upchiapas.bytecraft.ui.theme.TextMuted
import com.upchiapas.bytecraft.ui.theme.TextPrimary
import com.upchiapas.bytecraft.ui.theme.TextSecondary
import com.upchiapas.bytecraft.ui.theme.WarningAmber
import java.util.Locale

@Composable
fun ComponentCard(
    component: Component,
    onEdit: (Component) -> Unit,
    onDelete: (Component) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Fila 1: Badges (Categoría, Oferta, Condición) y Stock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge Categoría
                    BadgePill(
                        text = component.categoria,
                        backgroundColor = SurfaceDarkElevated,
                        textColor = CyanNeon
                    )

                    // Badge Condición
                    BadgePill(
                        text = component.condicion,
                        backgroundColor = SurfaceDarkElevated,
                        textColor = PurpleNeon
                    )

                    // Badge Descuento si está en oferta
                    if (component.enOferta) {
                        val pct = component.descuentoPorcentaje ?: 10
                        BadgePill(
                            text = "🔥 -$pct%",
                            backgroundColor = OrangeNeon,
                            textColor = Color.White
                        )
                    }
                }

                // Indicador de Stock
                val stockColor = when {
                    component.stock > 5 -> SuccessGreen
                    component.stock > 0 -> WarningAmber
                    else -> ErrorRed
                }
                val stockText = if (component.stock > 0) "Stock: ${component.stock}" else "Agotado"

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(stockColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stockText,
                        color = stockColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Nombre del componente
            Text(
                text = component.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = SurfaceBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Fila inferior: Precios y Botones de Acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Precios
                Column {
                    if (component.enOferta && component.descuentoPorcentaje != null) {
                        Text(
                            text = String.format(Locale.US, "$%.2f", component.precio),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = TextMuted
                        )
                    }
                    Text(
                        text = String.format(Locale.US, "$%.2f", component.precioFinal),
                        style = MaterialTheme.typography.titleLarge,
                        color = CyanNeon,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Botones Editar y Eliminar
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { onEdit(component) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = SurfaceDarkElevated,
                            contentColor = TextSecondary
                        ),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar componente",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { onDelete(component) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = ErrorContainerRed,
                            contentColor = ErrorRed
                        ),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar componente",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgePill(
    text: String,
    backgroundColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
