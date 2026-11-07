package com.upchiapas.bytecraft.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ComponentDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("categoria")
    val categoria: String? = null,
    @SerializedName("precio")
    val precio: Double? = null,
    @SerializedName("stock")
    val stock: Int? = null,
    @SerializedName("enOferta")
    val enOferta: Boolean? = null,
    @SerializedName("condicion")
    val condicion: String? = null,
    @SerializedName("descuentoPorcentaje")
    val descuentoPorcentaje: Int? = null
)

data class CreateComponentDto(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("categoria")
    val categoria: String,
    @SerializedName("precio")
    val precio: Double,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("enOferta")
    val enOferta: Boolean,
    @SerializedName("condicion")
    val condicion: String,
    @SerializedName("descuentoPorcentaje")
    val descuentoPorcentaje: Int? = null
)
