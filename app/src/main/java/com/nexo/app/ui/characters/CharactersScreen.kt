package com.nexo.app.ui.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexo.app.engine.avatar.AvatarCatalog
import com.nexo.app.engine.avatar.AvatarDefinition
import com.nexo.app.engine.avatar.AvatarState
import com.nexo.app.ui.avatar.AvatarViewModel
import com.nexo.app.ui.avatar.ChibiAvatar

@Composable
fun CharactersScreen(factory: ViewModelProvider.Factory) {
    val vm: AvatarViewModel = viewModel(factory = factory)
    val selected by vm.selected.collectAsState()
    val preview = AvatarCatalog.byId(vm.pendingId)
    val changed = vm.pendingId != selected.id

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        Text("Personajes", style = MaterialTheme.typography.titleLarge)
        Text(
            "Por ahora son variantes de color de un modelo provisional.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box(
            Modifier.weight(1f).fillMaxWidth().padding(vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            ChibiAvatar(
                definition = preview,
                state = AvatarState.Idle,
                modifier = Modifier.fillMaxHeight().aspectRatio(0.62f, matchHeightConstraintsFirst = true),
            )
        }
        Text(
            preview.displayName,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth().height(176.dp).padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(AvatarCatalog.all) { a ->
                Thumb(a, a.id == vm.pendingId) { vm.pendingId = a.id }
            }
        }
        Button(
            onClick = { vm.apply() },
            enabled = changed,
            modifier = Modifier.fillMaxWidth().height(52.dp).padding(top = 0.dp),
            shape = RoundedCornerShape(26.dp),
        ) {
            Text(if (changed) "APLICAR SELECCIÓN" else "EN USO", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun Thumb(avatar: AvatarDefinition, isSelected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        Modifier
            .aspectRatio(1f)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface, shape)
            .clickable(onClick = onClick)
            .padding(6.dp),
        contentAlignment = Alignment.Center,
    ) {
        ChibiAvatar(
            definition = avatar,
            state = AvatarState.Idle,
            modifier = Modifier.fillMaxSize(),
            animate = false,
        )
    }
}
