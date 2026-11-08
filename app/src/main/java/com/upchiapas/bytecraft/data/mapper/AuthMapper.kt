package com.upchiapas.bytecraft.data.mapper

import com.upchiapas.bytecraft.data.remote.dto.AuthResponseDto
import com.upchiapas.bytecraft.data.remote.dto.UserDto
import com.upchiapas.bytecraft.domain.model.AuthSession
import com.upchiapas.bytecraft.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email
    )
}

fun AuthResponseDto.toDomain(): AuthSession {
    return AuthSession(
        token = token,
        user = user.toDomain()
    )
}
