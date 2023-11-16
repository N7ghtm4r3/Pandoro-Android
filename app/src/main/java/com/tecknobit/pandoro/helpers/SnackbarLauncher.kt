package com.tecknobit.pandoro.helpers

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.tecknobit.pandoro.ui.activities.SplashScreen
import com.tecknobit.pandoro.ui.theme.BackgroundLight
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * **SnackbarLauncher** the interface useful to manage the launch of the snackbars
 */
interface SnackbarLauncher {

    /**
     * Function to create an host to manage the snackbars
     *
     * @param hostState: the host to manage the launch of the snackbars
     */
    @Composable
    fun CreateSnackbarHost(hostState: SnackbarHostState) {
        SnackbarHost(hostState = hostState) {
            Snackbar(
                containerColor = BackgroundLight,
                contentColor = PrimaryLight,
                snackbarData = it
            )
        }
    }

    /**
     * Function to show a message with the [SnackbarHostState]
     *
     * @param message: the message resource identifier to show
     */
    fun showSnack(message: Int)

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
        scope.launch {
            snackbarHostState.showSnackbar(
                message = SplashScreen.context.getString(message),
                duration = SnackbarDuration.Short
            )
        }
    }

}