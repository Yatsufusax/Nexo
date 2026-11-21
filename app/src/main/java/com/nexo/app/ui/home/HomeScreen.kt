package com.nexo.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexo.app.engine.avatar.AvatarState
import com.nexo.app.ui.avatar.AvatarViewModel
import com.nexo.app.ui.avatar.ChibiAvatar
import com.nexo.app.ui.theme.NexoInk
import com.nexo.app.ui.theme.NexoViolet

@Composable
fun HomeScreen(onChat: () -> Unit, factory: ViewModelProvider.Factory) {
    val vm: AvatarViewModel = viewModel(factory = factory)
    val avatar by vm.selected.collectAsState()
    var state by remember { mutableStateOf(AvatarState.Idle) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(NexoInk, NexoViolet.copy(alpha = 0.35f), NexoInk)))
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "NEXO",
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 40.sp, letterSpacing = 8.sp),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            "Your AI. Your Companion.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )
        Box(
            Modifier.weight(1f).fillMaxWidth().padding(vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            ChibiAvatar(
                definition = avatar,
                state = state,
                modifier = Modifier.fillMaxHeight().aspectRatio(0.62f, matchHeightConstraintsFirst = true),
            )
        }
        Text(
            "${avatar.displayName} · ${state.name}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AvatarState.entries.forEach { s ->
                StateChip(s.name, s == state) { state = s }
            }
        }
        Text(
            "Walk y Sit aún usan la pose base.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp),
        )
        Spacer(Modifier.height(14.dp))
        Button(
            onClick = onChat,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(26.dp),
        ) {
            Text("CONVERSAR", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun StateChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
    }
}
