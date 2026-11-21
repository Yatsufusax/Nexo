package com.nexo.app.ui.components

import com.nexo.app.engine.ai.ConnectionStatus

fun ConnectionStatus.shortLabel(): String = when (this) {
    ConnectionStatus.NotConnected -> "Sin conectar"
    ConnectionStatus.AuthPending -> "Autenticación pendiente"
    is ConnectionStatus.AccountConnected -> "Cuenta conectada"
    is ConnectionStatus.ApiConnected -> "API conectada"
    is ConnectionStatus.Error -> "Error de conexión"
    is ConnectionStatus.Unavailable -> "Proveedor no disponible"
}
