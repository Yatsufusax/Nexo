package com.nexo.app.ui.provider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexo.app.engine.ai.ConnectionStatus
import com.nexo.app.ui.components.ConnectionBadge
import com.nexo.app.ui.components.NexoScaffold

@Composable
fun ProviderScreen(onBack: () -> Unit, factory: ViewModelProvider.Factory) {
    val vm: ProviderViewModel = viewModel(factory = factory)
    val status by vm.status.collectAsState()
    val uriHandler = LocalUriHandler.current

    NexoScaffold(title = "Proveedor de IA", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Google Gemini", style = MaterialTheme.typography.titleLarge)
            ConnectionBadge(status)
            Text(
                "NEXO usa tu propia clave de API de Google AI Studio. La clave se guarda " +
                    "cifrada en este teléfono y nunca se envía a NEXO.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = vm.keyInput,
                onValueChange = { vm.keyInput = it },
                label = {
                    Text(
                        if (vm.hasStoredKey) "Clave guardada (escribe para reemplazar)"
                        else "Clave de API de Gemini",
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = vm.modelInput,
                onValueChange = { vm.modelInput = it },
                label = { Text("Modelo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = { vm.saveAndVerify() },
                enabled = status !is ConnectionStatus.AuthPending &&
                    (vm.keyInput.isNotBlank() || vm.hasStoredKey),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("GUARDAR Y VERIFICAR", style = MaterialTheme.typography.labelLarge)
            }
            TextButton(onClick = { uriHandler.openUri("https://aistudio.google.com/app/apikey") }) {
                Text("Conseguir una clave en Google AI Studio")
            }
            if (vm.hasStoredKey) {
                TextButton(onClick = { vm.disconnect() }) {
                    Text("Desconectar y borrar clave")
                }
            }
            Text(
                "Aviso: en el nivel gratuito de la API, Google puede usar lo que escribas " +
                    "para mejorar sus productos. No envíes datos sensibles.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
