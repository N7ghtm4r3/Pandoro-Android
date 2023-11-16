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
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.tecknobit.pandoro.R.string.*
import com.tecknobit.pandoro.helpers.areNotesValid
import com.tecknobit.pandoro.helpers.isContentNoteValid
import com.tecknobit.pandoro.helpers.isValidProjectDescription
import com.tecknobit.pandoro.helpers.isValidProjectName
import com.tecknobit.pandoro.helpers.isValidProjectShortDescription
import com.tecknobit.pandoro.helpers.isValidRepository
import com.tecknobit.pandoro.helpers.isValidVersion
import com.tecknobit.pandoro.toImportFromLibrary.Group
import com.tecknobit.pandoro.toImportFromLibrary.Project
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.screens.ProjectsScreen.Companion.showAddProjectDialog
import com.tecknobit.pandoro.ui.screens.ProjectsScreen.Companion.showEditProjectDialog
import com.tecknobit.pandoro.ui.theme.ErrorLight
import layouts.components.PandoroTextField

class ProjectDialogs : PandoroDialog() {

    @Composable
    fun AddNewProject() {
        CreateProjectDialog(
            show = showAddProjectDialog,
            title = stringResource(add_a_new_project),
            confirmText = stringResource(add)
        )
    }

    @Composable
    fun EditProject(project: Project) {
        CreateProjectDialog(
            project = project,
            show = showEditProjectDialog,
            title = stringResource(edit_caps) + " " + project.name + " " + stringResource(string.project),
            confirmText = stringResource(edit)
        )
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    private fun CreateProjectDialog(
        project: Project? = null,
        show: MutableState<Boolean>,
        title: String,
        confirmText: String
    ) {
        var name by remember {
            mutableStateOf(if (project == null) "" else project.name)
        }
        var description by remember {
            mutableStateOf(if (project == null) "" else project.description)
        }
        var shortDescription by remember {
            mutableStateOf(if (project == null) "" else project.shortDescription)
        }
        var version by remember {
            mutableStateOf(if (project == null) "" else project.version)
        }
        var projectRepository by remember {
            mutableStateOf(if (project == null) "" else project.projectRepo)
        }
        val groups = mutableStateListOf<Group>()
        if (project != null)
            groups.addAll(project.groups)
        CreatePandoroDialog(
            show = show,
            title = title,
            confirmText = confirmText,
            requestLogic = {
                if (isValidProjectName(name)) {
                    if (isValidProjectDescription(description)) {
                        if (isValidProjectShortDescription(shortDescription)) {
                            if (isValidVersion(version)) {
                                if (isValidRepository(projectRepository)) {
                                    show.value = false
                                } else
                                    showSnack(insert_a_correct_repository_url)
                            } else
                                showSnack(insert_a_correct_version)
                        } else
                            showSnack(insert_a_correct_short_description)
                    } else
                        showSnack(insert_a_correct_description)
                } else
                    showSnack(insert_a_correct_name)
            },
            content = {
                PandoroTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(height = 55.dp),
                    textFieldModifier = Modifier.fillMaxWidth(),
                    label = stringResource(string.name),
                    isError = !isValidProjectName(name),
                    value = name,
                    onValueChange = {
                        name = it
                    }
                )
                PandoroTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(height = 55.dp),
                    textFieldModifier = Modifier.fillMaxWidth(),
                    label = stringResource(string.description),
                    isError = !isValidProjectDescription(description),
                    value = description,
                    onValueChange = {
                        description = it
                    }
                )
                PandoroTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(height = 55.dp),
                    textFieldModifier = Modifier.fillMaxWidth(),
                    label = stringResource(short_description),
                    isError = !isValidProjectShortDescription(shortDescription),
                    value = shortDescription,
                    onValueChange = {
                        shortDescription = it
                    }
                )
                PandoroTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(height = 55.dp),
                    textFieldModifier = Modifier.fillMaxWidth(),
                    label = stringResource(string.version),
                    isError = !isValidVersion(version),
                    value = version,
                    onValueChange = {
                        version = it
                    }
                )
                PandoroTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(height = 55.dp),
                    textFieldModifier = Modifier.fillMaxWidth(),
                    label = stringResource(project_repository),
                    isError = !isValidRepository(projectRepository),
                    value = projectRepository,
                    onValueChange = {
                        projectRepository = it
                    }
                )
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 15.dp, bottom = 10.dp, top = 5.dp),
                        text =
                        if (project == null)
                            stringResource(add_to_a_group)
                        else {
                            stringResource(add) + " " + project.name + " " +
                                    stringResource(to_a_group)
                        },
                        fontSize = 18.sp
                    )
                    Divider(thickness = 1.dp)
                    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                        items(user.adminGroups) { group ->
                            var inserted by remember { mutableStateOf(groups.contains(group)) }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = inserted,
                                    onCheckedChange = {
                                        inserted = it
                                        if (it)
                                            groups.add(group)
                                        else
                                            groups.remove(group)
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
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun ScheduleUpdate(
        project: Project,
        show: MutableState<Boolean>
    ) {
        val notes = mutableStateListOf("")
        var targetVersion by remember { mutableStateOf("") }
        CreatePandoroDialog(
            show = show,
            title = stringResource(schedule_update),
            customWeight = 2.5f,
            confirmText = stringResource(schedule),
            requestLogic = {
                if (isValidVersion(targetVersion)) {
                    if (notes.isNotEmpty()) {
                        if (areNotesValid(notes)) {
                            // TODO: MAKE REQUEST THEN
                            show.value = false
                        } else
                            showSnack(you_must_insert_correct_notes)
                    } else
                        showSnack(you_must_insert_one_note_at_least)
                } else
                    showSnack(insert_a_correct_target_version)
            }
        ) {
            PandoroTextField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(height = 55.dp),
                textFieldModifier = Modifier.fillMaxWidth(),
                label = stringResource(target_version),
                value = targetVersion,
                isError = !isValidVersion(targetVersion),
                onValueChange = {
                    targetVersion = it
                }
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
                            modifier = Modifier.size(40.dp),
                            onClick = { notes.add("") },
                            content = { Icon(Icons.Filled.Add, null) }
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
                            value = content.value
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