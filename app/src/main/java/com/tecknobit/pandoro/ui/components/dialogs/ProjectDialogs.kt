package com.tecknobit.pandoro.ui.components.dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.R.string.add
import com.tecknobit.pandoro.R.string.add_a_new_project
import com.tecknobit.pandoro.R.string.add_to_a_group
import com.tecknobit.pandoro.R.string.change_notes
import com.tecknobit.pandoro.R.string.description
import com.tecknobit.pandoro.R.string.edit
import com.tecknobit.pandoro.R.string.name
import com.tecknobit.pandoro.R.string.project_repository
import com.tecknobit.pandoro.R.string.schedule
import com.tecknobit.pandoro.R.string.schedule_update
import com.tecknobit.pandoro.R.string.short_description
import com.tecknobit.pandoro.R.string.target_version
import com.tecknobit.pandoro.R.string.to_a_group
import com.tecknobit.pandoro.R.string.version
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.viewmodels.MainActivityViewModel
import com.tecknobit.pandoro.ui.components.PandoroTextField
import com.tecknobit.pandoro.ui.screens.ProjectsScreen.Companion.showAddProjectDialog
import com.tecknobit.pandoro.ui.screens.ProjectsScreen.Companion.showEditProjectDialog
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isContentNoteValid
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidProjectDescription
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidProjectName
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidProjectShortDescription
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidRepository
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidVersion
import com.tecknobit.pandorocore.records.Project

/**
 * The **ProjectDialogs** class is useful to create the projects dialogs
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroDialog
 */
// TODO: TO COMMENT
class ProjectDialogs(
    val viewModel: MainActivityViewModel
) : PandoroDialog() {

    /**
     * Function to create a Pandoro's custom dialog to add a new [Project]
     *
     * **No-any params required
     */
    @Composable
    fun AddNewProject() {
        if(showAddProjectDialog.value) {
            CreateProjectDialog(
                show = showAddProjectDialog,
                title = stringResource(add_a_new_project),
                confirmText = stringResource(add)
            )
        }
    }

    /**
     * Function to create a Pandoro's custom dialog to edit an existing [Project]
     *
     * @param project: the project to edit
     */
    @Composable
    fun EditProject(
        project: Project
    ) {
        if(showEditProjectDialog.value) {
            CreateProjectDialog(
                project = project,
                show = showEditProjectDialog,
                title = stringResource(edit) + " " + project.name + " " + stringResource(string.project),
                confirmText = stringResource(edit)
            )
        }
    }

    /**
     * Function to create a Pandoro's custom dialog to manage a [Project]
     *
     * @param project: the project if is a dialog to edit an existing project
     * @param show: whether show the dialog
     * @param title: the title of the dialog
     * @param confirmText: the text to confirm an action
     */
    @SuppressLint("UnrememberedMutableState")
    @Composable
    private fun CreateProjectDialog(
        project: Project? = null,
        show: MutableState<Boolean>,
        title: String,
        confirmText: String
    ) {
        snackbarHostState = remember { SnackbarHostState() }
        viewModel.snackbarHostState = snackbarHostState
        viewModel.name = remember {
            mutableStateOf(if (project == null) "" else project.name)
        }
        viewModel.description = remember {
            mutableStateOf(if (project == null) "" else project.description)
        }
        viewModel.shortDescription = remember {
            mutableStateOf(if (project == null) "" else project.shortDescription)
        }
        viewModel.version = remember {
            mutableStateOf(if (project == null) "" else project.version)
        }
        viewModel.projectRepository = remember {
            mutableStateOf(if (project == null) "" else project.projectRepo)
        }
        viewModel.projectGroups = remember { mutableListOf() }
        if (project != null)
            viewModel.projectGroups.addAll(project.groups)
        CreatePandoroDialog(
            show = show,
            title = title,
            confirmText = confirmText,
            requestLogic = {
                viewModel.workWithProject(
                    project = project,
                    onSuccess = { show.value = false }
                )
            },
            content = {
                PandoroTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    textFieldModifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(name),
                    isError = !isValidProjectName(viewModel.name.value),
                    value = viewModel.name
                )
                PandoroTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(height = 60.dp),
                    textFieldModifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(description),
                    isError = !isValidProjectDescription(viewModel.description.value),
                    value = viewModel.description,
                )
                PandoroTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(height = 60.dp),
                    textFieldModifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(short_description),
                    isError = !isValidProjectShortDescription(viewModel.shortDescription.value),
                    value = viewModel.shortDescription
                )
                PandoroTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(height = 60.dp),
                    textFieldModifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(version),
                    isError = !isValidVersion(viewModel.version.value),
                    value = viewModel.version
                )
                PandoroTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(height = 60.dp),
                    textFieldModifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(project_repository),
                    isError = !isValidRepository(viewModel.projectRepository.value),
                    value = viewModel.projectRepository
                )
                if(user.adminGroups.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                start = 15.dp,
                                bottom = 10.dp,
                                top = 5.dp
                            ),
                            text =
                            if (project == null)
                                stringResource(add_to_a_group)
                            else {
                                stringResource(add) + " " + project.name + " " +
                                        stringResource(to_a_group)
                            },
                            fontSize = 18.sp
                        )
                        HorizontalDivider(thickness = 1.dp)
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3)
                        ) {
                            items(
                                items = user.adminGroups,
                                key = { group ->
                                    group.id
                                }
                            ) { group ->
                                var inserted by remember {
                                    mutableStateOf(viewModel.projectGroups.contains(group))
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = inserted,
                                        onCheckedChange = {
                                            inserted = it
                                            if (it)
                                                viewModel.projectGroups.add(group)
                                            else
                                                viewModel.projectGroups.remove(group)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = group.name,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    /**
     * Function to create a Pandoro's custom dialog to schedule a new update
     *
     * @param project: the project where schedule the update
     * @param show: whether show the dialog
     */
    @OptIn(ExperimentalFoundationApi::class)
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun ScheduleUpdate(
        project: Project,
        show: MutableState<Boolean>
    ) {
        val notes = mutableStateListOf("")
        viewModel.targetVersion = remember { mutableStateOf("") }
        CreatePandoroDialog(
            show = show,
            title = stringResource(schedule_update),
            customWeight = 2f,
            confirmText = stringResource(schedule),
            requestLogic = {
                viewModel.scheduleUpdate(
                    project = project,
                    notes = notes,
                    onSuccess = { show.value = false }
                )
            }
        ) {
            PandoroTextField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(height = 60.dp),
                textFieldModifier = Modifier.fillMaxWidth(),
                label = stringResource(target_version),
                value = viewModel.targetVersion,
                isError = !isValidVersion(viewModel.targetVersion.value),
            )
            Text(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 10.dp
                ),
                text = stringResource(id = change_notes),
                fontSize = 22.sp
            )
            LazyColumn {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 10.dp,
                                top = 5.dp,
                                bottom = 5.dp
                            )
                    ) {
                        FloatingActionButton(
                            modifier = Modifier
                                .size(40.dp),
                            onClick = { notes.add("") },
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
                items(notes.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 10.dp
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val content = mutableStateOf(notes[index])
                        PandoroTextField(
                            modifier = Modifier
                                .padding(
                                    bottom = 5.dp,
                                    start = 15.dp
                                )
                                .weight(8f),
                            label = stringResource(string.content),
                            isError = !isContentNoteValid(content.value),
                            onValueChange = {
                                if (it.isNotEmpty()) {
                                    content.value = it
                                    notes.removeAt(index)
                                    notes.add(index, it)
                                }
                            },
                            value = content
                        )
                        IconButton(
                            modifier = Modifier.padding(start = 10.dp),
                            onClick = { notes.removeAt(index) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = ErrorLight
                            )
                        }
                    }
                }
            }
        }
    }

}