package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.ui.theme.CustomGrayColor
import com.tecknobit.pandoro.ui.theme.fontFamily

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
    value: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,
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
            label = { Text(label, fontSize = 14.sp) },
            onValueChange = onValueChange,
            value = value
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