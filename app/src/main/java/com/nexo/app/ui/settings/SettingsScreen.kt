package com.nexo.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexo.app.ui.components.shortLabel

@Composable
fun SettingsScreen(
    onOpenProvider: () -> Unit,
    onOpenAbout: () -> Unit,
    factory: ViewModelProvider.Factory,
) {
    val vm: SettingsViewModel = viewModel(factory = factory)
    val status by vm.status.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("Ajustes", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 6.dp))
        SettingsRow("Proveedor de IA", "${vm.providerName} · ${status.shortLabel()}", true, onOpenProvider)
        SettingsRow("Memoria", "Próximamente", false)
        SettingsRow("Comportamiento autónomo", "Próximamente", false)
        SettingsRow("Volumen de voz", "Próximamente", false)
        SettingsRow("No molestar", "Próximamente", false)
        SettingsRow("Permisos", "Próximamente", false)
        SettingsRow("Acerca de NEXO", "Versión y licencia", true, onOpenAbout)
    }
}

@Composable
private fun SettingsRow(title: String, subtitle: String, enabled: Boolean, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp)
            .alpha(if (enabled) 1f else 0.5f),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (enabled) {
            Text("›", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.secondary)
        }
    }
}
