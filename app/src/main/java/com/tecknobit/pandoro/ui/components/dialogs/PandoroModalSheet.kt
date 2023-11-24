package com.tecknobit.pandoro.ui.components.dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.helpers.SnackbarLauncher
import com.tecknobit.pandoro.ui.theme.DwarfWhiteColor
import kotlinx.coroutines.CoroutineScope

/**
 * The **PandoroModalSheet** class is useful to create the custom Pandoro's modal bottom sheets
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see SnackbarLauncher
 */
class PandoroModalSheet : SnackbarLauncher {

    /**
     * **scope** the coroutine to launch the snackbars
     */
    private lateinit var scope: CoroutineScope

    /**
     * **snackbarHostState** the host to launch the snackbars
     */
    private lateinit var snackbarHostState: SnackbarHostState

    /**
     * Function to create a Pandoro's custom modal bottom sheet
     *
     * @param modifier: the modifier for the modal bottom sheet
     * @param columnModifier: the modifier for the column inside the modal bottom sheet
     * @param containerColor: the container color of the modal bottom sheet
     * @param show: whether show the modal bottom sheet
     * @param onDismissRequest: the action to execute when the modal bottom sheet is dismissed
     * @param title: the title of the modal bottom sheet
     * @param content: the content to show with the modal bottom sheet
     */
    @SuppressLint("NotConstructor")
    @Composable
    fun PandoroModalSheet(
        modifier: Modifier = Modifier,
        columnModifier: Modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        containerColor: Color = DwarfWhiteColor,
        show: MutableState<Boolean>,
        onDismissRequest: () -> Unit = { show.value = false },
        title: Int,
        content: @Composable ColumnScope.() -> Unit
    ) {
        PandoroModalSheet(
            modifier = modifier,
            columnModifier = columnModifier,
            containerColor = containerColor,
            show = show,
            onDismissRequest = onDismissRequest,
            title = stringResource(title),
            content = content
        )
    }

    /**
     * Function to create a Pandoro's custom modal bottom sheet
     *
     * @param modifier: the modifier for the modal bottom sheet
     * @param columnModifier: the modifier for the column inside the modal bottom sheet
     * @param containerColor: the container color of the modal bottom sheet
     * @param show: whether show the modal bottom sheet
     * @param onDismissRequest: the action to execute when the modal bottom sheet is dismissed
     * @param title: the title of the modal bottom sheet
     * @param content: the content to show with the modal bottom sheet
     */
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NotConstructor")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PandoroModalSheet(
        modifier: Modifier = Modifier,
        columnModifier: Modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        containerColor: Color = DwarfWhiteColor,
        show: MutableState<Boolean>,
        onDismissRequest: () -> Unit = { show.value = false },
        title: String,
        content: @Composable ColumnScope.() -> Unit
    ) {
        scope = rememberCoroutineScope()
        snackbarHostState = remember { SnackbarHostState() }
        if (show.value) {
            ModalBottomSheet(
                modifier = modifier,
                onDismissRequest = onDismissRequest,
                sheetState = rememberModalBottomSheetState(),
                containerColor = containerColor
            ) {
                Scaffold(
                    containerColor = containerColor,
                    snackbarHost = { CreateSnackbarHost(hostState = snackbarHostState) }
                ) {
                    Column(
                        modifier = columnModifier
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 10.dp
                                ),
                            text = title,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                        Divider(thickness = 1.dp)
                        Column(
                            content = content
                        )
                    }
                }
            }
        }
    }

    /**
     * Function to show a message with the [SnackbarHostState]
     *
     * @param message: the message to show
     */
    override fun showSnack(message: String) {
        showSnack(
            scope = scope,
            snackbarHostState = snackbarHostState,
            message = message
        )
    }

}