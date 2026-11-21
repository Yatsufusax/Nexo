package com.nexo.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nexo.app.engine.ai.ConnectionStatus
import com.nexo.app.ui.theme.NexoCyan
import com.nexo.app.ui.theme.NexoMuted

private val Amber = Color(0xFFFFC857)
private val Red = Color(0xFFFF6B6B)

@Composable
fun ConnectionBadge(status: ConnectionStatus, modifier: Modifier = Modifier) {
    val (label, detail, color) = when (status) {
        ConnectionStatus.NotConnected -> Triple("Sin conectar", null, NexoMuted)
        ConnectionStatus.AuthPending -> Triple("Autenticación pendiente", "Verificando con el proveedor…", Amber)
        is ConnectionStatus.AccountConnected -> Triple("Cuenta conectada", status.accountLabel, NexoCyan)
        is ConnectionStatus.ApiConnected -> Triple("API conectada", status.detail, NexoCyan)
        is ConnectionStatus.Error -> Triple("Error de conexión", status.message, Red)
        is ConnectionStatus.Unavailable -> Triple("Proveedor no disponible", status.reason, NexoMuted)
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(Modifier.size(10.dp).clip(CircleShape).background(color))
        Column {
            Text(label, style = MaterialTheme.typography.labelLarge, color = color)
            if (!detail.isNullOrBlank()) {
                Text(
                    detail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
