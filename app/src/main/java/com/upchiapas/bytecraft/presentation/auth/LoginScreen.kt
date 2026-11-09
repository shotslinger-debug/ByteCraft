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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.upchiapas.bytecraft.ui.theme.SurfaceBorder
import com.upchiapas.bytecraft.ui.theme.SurfaceDark
import com.upchiapas.bytecraft.ui.theme.SurfaceDarkElevated
import com.upchiapas.bytecraft.ui.theme.TextMuted
import com.upchiapas.bytecraft.ui.theme.TextPrimary
import com.upchiapas.bytecraft.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            authViewModel.resetSuccessFlags()
            onLoginSuccess()
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
            // Logotipo
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(CyanNeon, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BC",
                    color = DarkBg,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ByteCraft",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Control de Inventario & Hardware",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tarjeta de Formulario
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ingresa tus credenciales para continuar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Input Correo
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

                    // Input Contraseña
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { authViewModel.onPasswordChange(it) },
                        label = { Text("Contraseña") },
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

                    // Botón Iniciar Sesión
                    Button(
                        onClick = { authViewModel.login() },
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
                                text = "INICIAR SESIÓN",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Enlace a Registro
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "¿No tienes una cuenta?",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text = "Regístrate aquí",
                        color = CyanNeon,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Badge de conexión de red
            Row(
                modifier = Modifier
                    .background(SurfaceDarkElevated, CircleShape)
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.size(7.dp).background(CyanNeon, CircleShape))
                Text(
                    text = "API REST · AWS EC2 (52.87.39.224)",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun authTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CyanNeon,
    unfocusedBorderColor = SurfaceBorder,
    errorBorderColor = ErrorRed,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedLabelColor = CyanNeon,
    unfocusedLabelColor = TextMuted,
    focusedContainerColor = SurfaceDarkElevated,
    unfocusedContainerColor = SurfaceDarkElevated,
    errorContainerColor = SurfaceDarkElevated
)
