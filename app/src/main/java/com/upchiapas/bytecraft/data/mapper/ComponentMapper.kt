package com.upchiapas.bytecraft.data.mapper

import com.upchiapas.bytecraft.data.remote.dto.ComponentDto
import com.upchiapas.bytecraft.data.remote.dto.CreateComponentDto
import com.upchiapas.bytecraft.domain.model.Component

fun ComponentDto.toDomain(): Component {
    return Component(
        id = id.orEmpty(),
        nombre = nombre.orEmpty(),
        categoria = categoria ?: "OTRO",
        precio = precio ?: 0.0,
        stock = stock ?: 0,
        enOferta = enOferta ?: false,
        condicion = condicion ?: "NUEVO",
        descuentoPorcentaje = descuentoPorcentaje
    )
}

fun List<ComponentDto>.toDomain(): List<Component> {
    return map { it.toDomain() }
}

fun Component.toCreateDto(): CreateComponentDto {
    return CreateComponentDto(
        nombre = nombre,
        categoria = categoria,
        precio = precio,
        stock = stock,
        enOferta = enOferta,
        condicion = condicion,
        descuentoPorcentaje = descuentoPorcentaje
    )
}
