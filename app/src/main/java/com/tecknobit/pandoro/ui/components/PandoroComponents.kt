package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.ui.theme.CustomGrayColor
import com.tecknobit.pandoro.ui.theme.fontFamily

/**
 * Function to create a Pandoro's custom card
 *
 * @param modifier: the modifier for the card
 * @param elevation: the elevation of the card
 * @param shape: the shape of the card
 * @param onClick: the on click action to execute when the card has been pressed
 * @param content: the content to show with the card
 */
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

/**
 * Function to create a custom [TextField]
 *
 * @param modifier: modifier to be applied to the layout corresponding to the surface
 * @param label: the label for the [TextField]
 * @param value: the value of the [TextField],
 * @param visualTransformation: transforms the visual representation of the input [value], default [VisualTransformation.None]
 * @param onValueChange: the callback that is triggered when the input service updates the text. An updated text comes as
 * a parameter of the callback
 * @param leadingIcon: the optional leading icon to be displayed at the beginning of the text field container
 * @param trailingIcon: the optional trailing icon to be displayed at the end of the text field container
 * @param isError: indicates if the text field's current value is in error. If set to true, the label, bottom indicator
 * and trailing icon by default will be displayed in error color
 * @param textFieldModifier: a [Modifier] for this text field
 */
@Composable
fun PandoroTextField(
    modifier: Modifier = Modifier
        .padding(20.dp)
        .size(width = 250.dp, height = 55.dp),
    label: String,
    value: MutableState<String>,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit = { value.value = it },
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    textFieldModifier: Modifier = Modifier.width(width = 250.dp)
) {
    Surface(
        modifier = modifier,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        TextField(
            modifier = textFieldModifier,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = fontFamily
            ),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = CustomGrayColor,
                unfocusedContainerColor = CustomGrayColor
            ),
            isError = isError,
            shape = RoundedCornerShape(10.dp),
            label = {
                Text(
                    text = label,
                    fontSize = 14.sp
                )
            },
            onValueChange = onValueChange,
            value = value.value
        )
    }
}

/**
 * Function to create a custom [OutlinedTextField]
 *
 * @param modifier: modifier to be applied to the layout corresponding to the surface
 * @param label: the label for the [OutlinedTextField]
 * @param value: the value of the [OutlinedTextField],
 * @param requiredTextArea: whether is required a text area or a simple text field
 */
@Composable
fun PandoroOutlinedTextField(
    modifier: Modifier = Modifier,
    label: Int,
    isError: Boolean = false,
    value: MutableState<String>,
    requiredTextArea: Boolean
) {
    var textFieldModifier = modifier
        .padding(10.dp)
        .fillMaxWidth()
    if (requiredTextArea)
        textFieldModifier = textFieldModifier.height(100.dp)
    OutlinedTextField(
        modifier = textFieldModifier,
        value = value.value,
        label = { Text(text = stringResource(label), fontSize = 14.sp) },
        isError = isError,
        onValueChange = {
            value.value = it
        },
        singleLine = !requiredTextArea
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