package com.tecknobit.pandoro.helpers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Divide() {
    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp
    )
}

@Composable
fun SpaceContent(height: Int = 10) {
    Spacer(
        modifier = Modifier.height(height.dp)
    )
    Divide()
}

@Composable
fun ColoredBorder(color: Color) {
    Box(
        modifier = Modifier
            .background(color)
            .fillMaxHeight()
            .width(10.dp),
        content = {
            Text(
                text = ""
            )
        }
    )
}