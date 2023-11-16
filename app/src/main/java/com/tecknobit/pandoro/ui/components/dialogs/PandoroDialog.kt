package com.tecknobit.pandoro.ui.components.dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tecknobit.pandoro.helpers.SnackbarLauncher
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import kotlinx.coroutines.CoroutineScope

open class PandoroDialog : SnackbarLauncher {

    protected lateinit var scope: CoroutineScope

    protected lateinit var snackbarHostState: SnackbarHostState

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun CreatePandoroDialog(
        show: MutableState<Boolean>,
        title: String,
        customWeight: Float = 3f,
        confirmText: String,
        requestLogic: () -> Unit,
        content: @Composable ColumnScope.() -> Unit
    ) {
        if (show.value) {
            PandoroTheme {
                scope = rememberCoroutineScope()
                snackbarHostState = remember { SnackbarHostState() }
                Dialog(
                    onDismissRequest = { show.value = false },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false
                    )
                ) {
                    Scaffold(
                        snackbarHost = { CreateSnackbarHost(hostState = snackbarHostState) }
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(85.dp)
                                        .background(PrimaryLight),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .weight(1f),
                                        onClick = { show.value = false }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(customWeight)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = title,
                                            color = Color.White,
                                            fontSize = 18.sp
                                        )
                                    }
                                    TextButton(
                                        modifier = Modifier.weight(1f),
                                        onClick = requestLogic
                                    ) {
                                        Text(
                                            text = confirmText,
                                            color = ErrorLight
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(20.dp),
                                    content = content
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun showSnack(message: Int) {
        showSnack(
            scope = scope,
            snackbarHostState = snackbarHostState,
            message = message
        )
    }

}