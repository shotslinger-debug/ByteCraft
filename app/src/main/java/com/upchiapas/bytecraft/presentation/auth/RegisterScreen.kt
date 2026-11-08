package com.upchiapas.bytecraft.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upchiapas.bytecraft.ui.theme.CyanNeon
import com.upchiapas.bytecraft.ui.theme.DarkBg
import com.upchiapas.bytecraft.ui.theme.ErrorRed
import com.upchiapas.bytecraft.ui.theme.SurfaceDark
import com.upchiapas.bytecraft.ui.theme.TextMuted
import com.upchiapas.bytecraft.ui.theme.TextPrimary
import com.upchiapas.bytecraft.ui.theme.TextSecondary

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isRegisterSuccess) {
        if (uiState.isRegisterSuccess) {
            authViewModel.resetSuccessFlags()
            onRegisterSuccess()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Regístrate para administrar el inventario ByteCraft",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Nombre
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = { authViewModel.onNameChange(it) },
                        label = { Text("Nombre Completo") },
                        isError = uiState.nameError != null,
                        supportingText = {
                            uiState.nameError?.let {
                                Text(it, color = ErrorRed, style = MaterialTheme.typography.labelSmall)
                            }
                        },
                        singleLine = true,
                        colors = authTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Correo
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { authViewModel.onEmailChange(it) },
                        label = { Text("Correo Electrónico") },
                        placeholder = { Text("angel@upchiapas.edu.mx") },
                        isError = uiState.emailError != null,
                        supportingText = {
                            uiState.emailError?.let {
                                Text(it, color = ErrorRed, style = MaterialTheme.typography.labelSmall)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        colors = authTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Contraseña
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { authViewModel.onPasswordChange(it) },
                        label = { Text("Contraseña (mínimo 8 caracteres)") },
                        isError = uiState.passwordError != null,
                        supportingText = {
                            uiState.passwordError?.let {
                                Text(it, color = ErrorRed, style = MaterialTheme.typography.labelSmall)
                            }
                        },
                        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { authViewModel.togglePasswordVisibility() }) {
                                Icon(
                                    imageVector = if (uiState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Alternar visibilidad contraseña",
                                    tint = TextSecondary
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = authTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Confirmar Contraseña
                    OutlinedTextField(
                        value = uiState.confirmPassword,
                        onValueChange = { authViewModel.onConfirmPasswordChange(it) },
                        label = { Text("Confirmar Contraseña") },
                        isError = uiState.confirmPasswordError != null,
                        supportingText = {
                            uiState.confirmPasswordError?.let {
                                Text(it, color = ErrorRed, style = MaterialTheme.typography.labelSmall)
                            }
                        },
                        visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { authViewModel.toggleConfirmPasswordVisibility() }) {
                                Icon(
                                    imageVector = if (uiState.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Alternar visibilidad confirmación",
                                    tint = TextSecondary
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = authTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Banner de Error General / Conexión
                    if (uiState.generalError != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ErrorRed.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                .border(1.dp, ErrorRed, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error de conexión",
                                tint = ErrorRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = uiState.generalError ?: "",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón Crear Cuenta
                    Button(
                        onClick = { authViewModel.register() },
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyanNeon,
                            contentColor = DarkBg
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = DarkBg,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text = "CREAR CUENTA",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Enlace a Login
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "¿Ya tienes cuenta?",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "Inicia sesión",
                        color = CyanNeon,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
