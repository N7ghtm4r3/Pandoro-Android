package com.tecknobit.pandoro.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.R.string.confirm
import com.tecknobit.pandoro.records.Group
import com.tecknobit.pandoro.records.Project
import com.tecknobit.pandoro.ui.activities.GroupActivity
import com.tecknobit.pandoro.ui.activities.GroupActivity.Companion.GROUP_KEY
import com.tecknobit.pandoro.ui.activities.ProjectActivity
import com.tecknobit.pandoro.ui.activities.ProjectActivity.Companion.PROJECT_KEY
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.components.PandoroOutlinedTextField
import java.io.Serializable

/**
 * The **Screen** class is useful to give the base behaviour of the other screens of the
 * Pandoro's application
 * @author N7ghtm4r3 - Tecknobit
 */
@Structure
abstract class Screen {

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
    @Composable
    private fun SetScreen(
        content: @Composable ColumnScope.() -> Unit,
        scrollEnabled: Boolean
    ) {
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
        Column(
            modifier = modifier,
            content = content
        )
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
            PandoroOutlinedTextField(
                label = label,
                value = sheetInputValue,
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
     * Function to navigate to a project's screen
     *
     * @param project: the project to show
     */
    protected fun navToProject(project: Project) {
        navTo(PROJECT_KEY, project, ProjectActivity::class.java)
    }

    /**
     * Function to navigate to a group's screen
     *
     * @param group: the group to show
     */
    fun navToGroup(group: Group) {
        navTo(GROUP_KEY, group, GroupActivity::class.java)
    }

    /**
     * Function to navigate to a screen
     *
     * @param key: key to fetch the extra value
     * @param extra: the payload to share with the other activity
     * @param clazz: the class of the extra
     */
    private fun <T> navTo(key: String, extra: Serializable, clazz: Class<T>) {
        val intent = Intent(context, clazz)
        intent.putExtra(key, extra)
        startActivity(context, intent, null)
    }

}