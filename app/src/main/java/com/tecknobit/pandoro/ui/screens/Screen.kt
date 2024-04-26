package com.tecknobit.pandoro.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.R.string.confirm
import com.tecknobit.pandoro.helpers.SnackbarLauncher
import com.tecknobit.pandorocore.records.Group
import com.tecknobit.pandorocore.records.Project
import com.tecknobit.pandoro.ui.activities.GroupActivity
import com.tecknobit.pandoro.ui.activities.ProjectActivity
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.components.PandoroOutlinedTextField
import com.tecknobit.pandorocore.helpers.*
import kotlinx.coroutines.CoroutineScope

/**
 * The **Screen** class is useful to give the base behaviour of the other screens of the
 * Pandoro's application
 * @author N7ghtm4r3 - Tecknobit
 * @see SnackbarLauncher
 */
@Structure
abstract class Screen: SnackbarLauncher {

    /**
     * **ScreenType** the list of available screen types
     */
    enum class ScreenType {

        /**
         * **Projects** screen type
         */
        Projects,

        /**
         * **Notes** screen type
         */
        Notes,

        /**
         * **Overview** screen type
         */
        Overview,

        /**
         * **Profile** screen type
         */
        Profile;

        /**
         * Function to get the resource title to show in the UI
         *
         * @param screenType: screen type to select the resource title
         */
        fun getStringResourceTitle(screenType: ScreenType): Int {
            return when (screenType) {
                Projects -> R.string.projects
                Notes -> R.string.notes
                Overview -> R.string.overview
                Profile -> R.string.profile
            }
        }

    }

    companion object {

        /**
         * **scope** the coroutine to launch the snackbars
         */
        lateinit var scope: CoroutineScope

        /**
         * **snackbarHostState** the host to launch the snackbars
         */
        lateinit var snackbarHostState: SnackbarHostState

        /**
         * **currentProject** the current project currently displayed
         */
        var currentProject = mutableStateOf<Project?>(null)

        /**
         * **currentGroup** the current group currently displayed
         */
        var currentGroup = mutableStateOf<Group?>(null)

    }

    /**
     * **sheetInputValue** the value fetched from the bottom modal sheet
     */
    protected var sheetInputValue = mutableStateOf("")

    /**
     * Function to show the content screen
     *
     * No any params required
     */
    @Composable
    abstract fun ShowScreen()

    /**
     * Function to set the content for the screen
     * @param content: content to set
     */
    @Composable
    protected fun SetScreen(content: @Composable ColumnScope.() -> Unit) {
        SetScreen(
            scrollEnabled = false,
            content = content
        )
    }

    /**
     * Function to set the content for the scrollable screen
     * @param content: content to set
     */
    @Composable
    protected fun SetScrollableScreen(content: @Composable ColumnScope.() -> Unit) {
        SetScreen(
            scrollEnabled = true,
            content = content
        )
    }

    /**
     * Function to set the content for the scrollable o simple screen
     * @param content: content to set
     * @param scrollEnabled: whether enable the scrollable option
     */
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    private fun SetScreen(
        content: @Composable ColumnScope.() -> Unit,
        scrollEnabled: Boolean
    ) {
        scope = rememberCoroutineScope()
        snackbarHostState = remember { SnackbarHostState() }
        var modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 80.dp,
                bottom = 96.dp,
                end = 16.dp,
                start = 16.dp
            )
        if (scrollEnabled)
            modifier = modifier.verticalScroll(rememberScrollState())
        Scaffold(
            snackbarHost = { CreateSnackbarHost(hostState = snackbarHostState) }
        ) {
            Column(
                modifier = modifier,
                content = content
            )
        }
    }

    /**
     * Function to create a Pandoro's custom modal bottom sheet to get an input
     *
     * @param show: whether show the modal bottom sheet
     * @param title: the title of the modal bottom sheet
     * @param label: the label for the textarea
     * @param requestLogic: the request to execute when the button has been pressed
     * @param buttonText: the text for the button
     * @param requiredTextArea: whether is required a textarea or a simple text field
     */
    @Composable
    protected fun CreateInputModalBottom(
        show: MutableState<Boolean>,
        title: Int,
        label: Int,
        requestLogic: () -> Unit,
        buttonText: Int = confirm,
        requiredTextArea: Boolean = false
    ) {
        sheetInputValue.value = ""
        pandoroModalSheet.PandoroModalSheet(
            modifier = Modifier
                .fillMaxHeight()
                .imePadding(),
            show = show,
            onDismissRequest = {
                show.value = false
                sheetInputValue.value = ""
            },
            title = title
        ) {
            val sTitle = stringResource(id = title)
            PandoroOutlinedTextField(
                label = label,
                value = sheetInputValue,
                isError = if(sTitle.contains("password"))
                    !isPasswordValid(sheetInputValue.value)
                else if(sTitle.contains("email"))
                    !isEmailValid(sheetInputValue.value)
                else
                    !isContentNoteValid(sheetInputValue.value),
                requiredTextArea = requiredTextArea
            )
            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .height(65.dp)
                    .fillMaxWidth(),
                onClick = requestLogic,
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    text = stringResource(id = buttonText),
                    fontSize = 20.sp
                )
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

    /**
     * Function to show the empty section when a list is empty
     *
     * No any params required
     */
    @Composable
    protected fun EmptyList(message: Int) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(message),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    /**
     * Function to navigate to a project's screen
     *
     * @param project: the project to show
     */
    fun navToProject(project: Project) {
        currentProject.value = project
        navTo(ProjectActivity::class.java)
    }

    /**
     * Function to navigate to a group's screen
     *
     * @param group: the group to show
     */
    fun navToGroup(group: Group) {
        currentGroup.value = group
        navTo(GroupActivity::class.java)
    }

    /**
     * Function to navigate to a screen
     *
     * @param clazz: the class to display
     */
    private fun <T> navTo(clazz: Class<T>) {
        startActivity(context, Intent(context, clazz), null)
    }

}