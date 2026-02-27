package com.galaxyrio.sudokusolver.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun GameToolbar(
    modifier: Modifier = Modifier,
    isNoteMode: Boolean,
    isHintActive: Boolean,
    onUndoClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNoteModeClick: () -> Unit,
    onAutoCandidatesClick: () -> Unit,
    onHintClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Undo Button
        IconButton(
            onClick = onUndoClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            modifier = Modifier.size(50.dp).clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Undo,
                contentDescription = "Undo",
                modifier = Modifier.size(20.dp)
            )
        }

        // Delete Button
        IconButton(
            onClick = onDeleteClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            modifier = Modifier.size(50.dp).clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "Delete",
                modifier = Modifier.size(20.dp)
            )
        }

        // Note Mode Button
        IconButton(
            onClick = onNoteModeClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isNoteMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isNoteMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.size(50.dp).clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Note Mode",
                modifier = Modifier.size(20.dp)
            )
        }

        // Auto Candidates Button
        IconButton(
            onClick = onAutoCandidatesClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            modifier = Modifier.size(50.dp).clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Auto Candidates",
                modifier = Modifier.size(20.dp)
            )
        }

        // Hint Button
        IconButton(
            onClick = onHintClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isHintActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isHintActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.size(50.dp).clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = "Get Hint",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

