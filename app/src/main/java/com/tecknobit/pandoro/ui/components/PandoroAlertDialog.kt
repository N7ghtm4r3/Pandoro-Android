package com.tecknobit.pandoro.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.tecknobit.pandoro.R

/**
 * Function to create a Pandoro's custom alert dialog
 *
 * @param show: whether show the alert dialog
 * @param title: the title of the alert dialog
 * @param extraTitle: the extra content for the title of the alert dialog
 * @param text: the text of the alert dialog
 * @param requestLogic: the request to execute when the confirm text has been pressed
 */
@Composable
fun PandoroAlertDialog(
    show: MutableState<Boolean>,
    title: Int,
    extraTitle: String? = null,
    text: Int,
    requestLogic: () -> Unit
) {
    PandoroAlertDialog(
        show = show,
        title = title,
        extraTitle = extraTitle,
        text = { Text(text = stringResource(text)) },
        requestLogic = requestLogic
    )
}

/**
 * Function to create a Pandoro's custom alert dialog
 *
 * @param show: whether show the alert dialog
 * @param title: the title of the alert dialog
 * @param extraTitle: the extra content for the title of the alert dialog
 * @param text: the text of the alert dialog
 * @param requestLogic: the request to execute when the confirm text has been pressed
 */
@Composable
fun PandoroAlertDialog(
    show: MutableState<Boolean>,
    title: Int,
    extraTitle: String? = null,
    text: @Composable () -> Unit,
    requestLogic: () -> Unit
) {
    if(show.value) {
        AlertDialog(
            onDismissRequest = { show.value = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            },
            title = {
                var titleText = stringResource(title)
                if (extraTitle != null)
                    titleText += " $extraTitle"
                Text(
                    text = titleText
                )
            },
            text = text,
            dismissButton = {
                TextButton(
                    onClick = { show.value = false },
                    content = {
                        Text(
                            text = stringResource(R.string.dismiss)
                        )
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = requestLogic,
                    content = {
                        Text(
                            text = stringResource(R.string.confirm),
                        )
                    }
                )
            }
        )
    }
}