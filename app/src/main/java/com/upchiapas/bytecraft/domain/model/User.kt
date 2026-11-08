package com.upchiapas.bytecraft.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String
)

data class AuthSession(
    val token: String,
    val user: User
)
