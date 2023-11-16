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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.R.string.confirm
import com.tecknobit.pandoro.helpers.isContentNoteValid
import com.tecknobit.pandoro.helpers.isEmailValid
import com.tecknobit.pandoro.helpers.isPasswordValid
import com.tecknobit.pandoro.toImportFromLibrary.Group
import com.tecknobit.pandoro.toImportFromLibrary.Project
import com.tecknobit.pandoro.ui.activities.GroupActivity
import com.tecknobit.pandoro.ui.activities.GroupActivity.Companion.GROUP_KEY
import com.tecknobit.pandoro.ui.activities.ProjectActivity
import com.tecknobit.pandoro.ui.activities.ProjectActivity.Companion.PROJECT_KEY
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.theme.DwarfWhiteColor
import layouts.components.PandoroOutlinedTextField
import java.io.Serializable

@Structure
abstract class Screen {

    enum class ScreenType {

        Projects,

        Notes,

        Overview,

        Profile;

        fun getStringResourceTitle(screenType: ScreenType): Int {
            return when (screenType) {
                Projects -> R.string.projects
                Notes -> R.string.notes
                Overview -> R.string.overview
                Profile -> R.string.profile
            }
        }

    }

    protected var sheetInputValue = mutableStateOf("")

    @Composable
    abstract fun ShowScreen()

    @Composable
    protected fun SetScreen(content: @Composable ColumnScope.() -> Unit) {
        SetScreen(
            scrollEnabled = false,
            content = content
        )
    }

    @Composable
    protected fun SetScrollableScreen(content: @Composable ColumnScope.() -> Unit) {
        SetScreen(
            scrollEnabled = true,
            content = content
        )
    }

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

    @Composable
    protected fun CreateInputModalBottom(
        containerColor: Color = DwarfWhiteColor,
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
            containerColor = containerColor,
            show = show,
            onDismissRequest = {
                show.value = false
                sheetInputValue.value = ""
            },
            title = title,
            content = {
                PandoroOutlinedTextField(
                    label = label,
                    value = sheetInputValue,
                    isError = !inputValid(title),
                    requiredTextArea = requiredTextArea
                )
                Button(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(50.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    onClick = requestLogic
                ) {
                    Text(
                        text = stringResource(id = buttonText),
                        fontSize = 20.sp
                    )
                }
            }
        )
    }

    private fun inputValid(title: Int): Boolean {
        val sTitle = context.getString(title)
        return if (sTitle.contains("password"))
            isPasswordValid(sheetInputValue.value)
        else if (sTitle.contains("email"))
            isEmailValid(sheetInputValue.value)
        else
            isContentNoteValid(sheetInputValue.value)
    }

    protected fun navToProject(project: Project) {
        navTo(PROJECT_KEY, project, ProjectActivity::class.java)
    }

    protected fun navToGroup(group: Group) {
        navTo(GROUP_KEY, group, GroupActivity::class.java)
    }

    private fun <T> navTo(key: String, extra: Serializable, clazz: Class<T>) {
        val intent = Intent(context, clazz)
        intent.putExtra(key, extra)
        startActivity(context, intent, null)
    }

}