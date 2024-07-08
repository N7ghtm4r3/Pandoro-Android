package com.tecknobit.pandoro.helpers

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.theme.BackgroundLight
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * **SnackbarLauncher** the interface useful to manage the launch of the snackbars
 * @author N7ghtm4r3 - Tecknobit
 */
interface SnackbarLauncher {



    /**
     * Function to show a message with the [SnackbarHostState]
     *
     * @param message: the message to show
     */
    fun showSnack(message: String)

    /**
     * Function to show a message with the [SnackbarHostState]
     *
     * @param message: the message resource identifier to show
     */
    fun showSnack(message: Int) {
        showSnack(context.getString(message))
    }

    /**
     * Function to show a message with the [SnackbarHostState]
     *
     * @param scope: the coroutine to launch the snackbars
     * @param snackbarHostState: the host to launch the snackbars
     * @param message: the message resource identifier to show
     */
    fun showSnack(
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState,
        message: Int
    ) {
        showSnack(scope, snackbarHostState, context.getString(message))
    }

    /**
     * Function to show a message with the [SnackbarHostState]
     *
     * @param scope: the coroutine to launch the snackbars
     * @param snackbarHostState: the host to launch the snackbars
     * @param message: the message to show
     */
    fun showSnack(
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState,
        message: String
    ) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

}