package com.upchiapas.bytecraft.domain.model

data class Component(
    val id: String,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val stock: Int,
    val enOferta: Boolean,
    val condicion: String = "NUEVO",
    val descuentoPorcentaje: Int? = null
) {
    val precioFinal: Double
        get() {
            val descuento = descuentoPorcentaje
            return if (enOferta && descuento != null && descuento in 1..90) {
                precio * (1.0 - descuento / 100.0)
            } else {
                precio
            }
        }
}
