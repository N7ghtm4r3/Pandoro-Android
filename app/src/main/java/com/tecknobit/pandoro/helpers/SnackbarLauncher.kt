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

interface SnackbarLauncher {

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
     * @param message: the message to show
     */
    fun showSnack(message: Int)

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