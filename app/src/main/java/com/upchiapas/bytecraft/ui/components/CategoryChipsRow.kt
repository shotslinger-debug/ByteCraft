package com.upchiapas.bytecraft.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.upchiapas.bytecraft.ui.theme.CyanNeon
import com.upchiapas.bytecraft.ui.theme.DarkBg
import com.upchiapas.bytecraft.ui.theme.SurfaceBorder
import com.upchiapas.bytecraft.ui.theme.SurfaceDark
import com.upchiapas.bytecraft.ui.theme.TextMuted

@Composable
fun CategoryChipsRow(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val isAllSelected = selectedCategory == null
        FilterChip(
            selected = isAllSelected,
            onClick = { onCategorySelected(null) },
            label = { Text("Todos") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = CyanNeon,
                selectedLabelColor = DarkBg,
                containerColor = SurfaceDark,
                labelColor = TextMuted
            ),
            border = BorderStroke(1.dp, if (isAllSelected) CyanNeon else SurfaceBorder)
        )

        // Chips por categoría
        categories.forEach { category ->
            val isSelected = selectedCategory == category
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CyanNeon,
                    selectedLabelColor = DarkBg,
                    containerColor = SurfaceDark,
                    labelColor = TextMuted
                ),
                border = BorderStroke(1.dp, if (isSelected) CyanNeon else SurfaceBorder)
            )
        }
    }
}
