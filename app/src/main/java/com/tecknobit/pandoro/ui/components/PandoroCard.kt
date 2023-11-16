package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PandoroCard(
    modifier: Modifier,
    elevation: CardElevation = CardDefaults.elevatedCardElevation(),
    shape: Shape = CardDefaults.elevatedShape,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        ElevatedCard(
            modifier = modifier,
            elevation = elevation,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            onClick = onClick,
            content = content,
        )
    } else {
        ElevatedCard(
            modifier = modifier,
            elevation = elevation,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            content = content
        )
    }
}