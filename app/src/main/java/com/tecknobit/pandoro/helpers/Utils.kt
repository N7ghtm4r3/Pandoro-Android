package com.tecknobit.pandoro.helpers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.context
import com.tecknobit.pandorocore.records.Note

/**
 * Method to create a [Divider] on the UI
 *
 * No any params required
 */
@Composable
fun Divide() {
    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp
    )
}

/**
 * Method to space the content of the UI and create a [Divider]
 *
 * @param height: the height of the [Spacer]
 */
@Composable
fun SpaceContent(height: Int = 10) {
    Spacer(
        modifier = Modifier.height(height.dp)
    )
    Divide()
}

/**
 * Method to color the border of a [Box]
 *
 * @param color: the color for the border
 */
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

/**
 * Function to copy the value of a note
 *
 * @param note: the note to copy
 */
fun copyNote(note: Note) {
    copyContent(note.content)
}

/**
 * Function to copy a content value
 *
 * @param content: the content to copy
 */
fun copyContent(content: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Pandoro-Note", content)
    clipboard.setPrimaryClip(clip)
}
