package com.tecknobit.pandoro.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.R.string.delete_project
import com.tecknobit.pandoro.R.string.delete_text_dialog
import com.tecknobit.pandoro.R.string.no_projects_found
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.projectDialogs
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.requester
import com.tecknobit.pandoro.ui.components.PandoroAlertDialog
import com.tecknobit.pandoro.ui.components.PandoroCard
import com.tecknobit.pandoro.ui.components.PandoroTextField
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandorocore.records.Project
import com.tecknobit.pandorocore.ui.filterProjects
import com.tecknobit.pandorocore.ui.populateFrequentProjects

/**
 * The **ProjectsScreen** class is useful to show the projects of the user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 */
class ProjectsScreen: Screen() {

    companion object {

        /**
         * **projectsList** -> the list of the projects
         */
        val projectsList: SnapshotStateList<Project> = mutableStateListOf()

        /**
         * **showAddProjectDialog** -> the flag to show the dialog to create a new project
         */
        lateinit var showAddProjectDialog: MutableState<Boolean>

        /**
         * **showEditProjectDialog** -> the flag to show the dialog to edit an existing project
         */
        lateinit var showEditProjectDialog: MutableState<Boolean>

    }

    /**
     * Function to show the content screen
     *
     * No any params required
     */
    @Composable
    override fun ShowScreen() {
        showAddProjectDialog = rememberSaveable { mutableStateOf(false) }
        showEditProjectDialog = rememberSaveable { mutableStateOf(false) }
        SetScreen {
            projectDialogs.AddNewProject()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(string.frequent_projects),
                    fontSize = 20.sp
                )
                var filterFrequentQuery by remember { mutableStateOf("") }
                val frequentProjects = filterProjects(filterFrequentQuery,
                    populateFrequentProjects(projectsList)).toMutableStateList()
                SearchField(
                    query = filterFrequentQuery,
                    onValueChange = { filterFrequentQuery = it },
                    onClear = { filterFrequentQuery = "" }
                )
                if (frequentProjects.isEmpty())
                    NoProjectsFound()
                else {
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(1),
                        contentPadding = PaddingValues(end = 1.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = frequentProjects,
                            key = { project ->
                                project.id
                            }
                        ) { project ->
                            ProjectCard(project = project)
                        }
                    }
                }
            }
            Column (
                modifier = Modifier
                    .weight(1.6f)
                    .fillMaxSize()
                    .padding(top = 10.dp)
            ) {
                var filterQuery by remember { mutableStateOf("") }
                val currentProjects = filterProjects(filterQuery, projectsList).toMutableStateList()
                Text(
                    text = stringResource(string.current_projects),
                    fontSize = 20.sp
                )
                SearchField(
                    query = filterQuery,
                    onValueChange = { filterQuery = it },
                    onClear = { filterQuery = "" }
                )
                if (currentProjects.isEmpty())
                    NoProjectsFound()
                else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        contentPadding = PaddingValues(bottom = 1.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = currentProjects,
                            key = { project ->
                                project.id
                            }
                        ) { project ->
                            ProjectCard(project = project)
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to create the search field to filter the projects list
     *
     * @param query: the query to filter the projects list
     * @param onValueChange: the action to execute when the query value change
     * @param onClear: the action to execute to clear the query value
     */
    @Composable
    private fun SearchField(
        query: String,
        onValueChange: (String) -> Unit,
        onClear: () -> Unit
    ) {
        PandoroTextField(
            modifier = Modifier
                .padding(
                    top = 20.dp,
                    bottom = 20.dp
                )
                .size(
                    width = 250.dp,
                    height = 55.dp
                ),
            label = stringResource(string.search),
            onValueChange = onValueChange,
            value = query,
            trailingIcon = {
                Icon(
                    modifier = if (query.isNotEmpty()) {
                        Modifier.clickable { onClear.invoke() }
                    } else Modifier,
                    imageVector = if (query.isEmpty()) Default.Search else Default.Clear,
                    contentDescription = null,
                )
            }
        )
    }

    /**
     * Function to show the section when no projects has been found in the query search
     *
     * No any params required
     */
    @Composable
    private fun NoProjectsFound() {
        EmptyList(message = no_projects_found)
    }

    /**
     * Function to create a card for a project
     *
     * @param project: the project to create the card
     */
    @Composable
    private fun ProjectCard(project: Project) {
        var showOptions by remember { mutableStateOf(false) }
        PandoroCard(
            modifier = Modifier.size(
                width = 200.dp,
                height = 120.dp
            ),
            onClick = { navToProject(project = project) },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(3f)
                                .fillMaxWidth()
                        ) {
                            if (showOptions) {
                                val showDeleteDialog = remember { mutableStateOf(false) }
                                projectDialogs.EditProject(project = project)
                                Row {
                                    IconButton(
                                        modifier = Modifier.size(24.dp),
                                        onClick = { showEditProjectDialog.value = true }
                                    ) {
                                        Icon(
                                            imageVector = Default.Edit,
                                            contentDescription = null
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    IconButton(
                                        modifier = Modifier.size(24.dp),
                                        onClick = { showDeleteDialog.value = true }
                                    ) {
                                        Icon(
                                            imageVector = Default.Delete,
                                            contentDescription = null,
                                            tint = ErrorLight
                                        )
                                    }
                                }
                                PandoroAlertDialog(
                                    show = showDeleteDialog,
                                    title = delete_project,
                                    text = delete_text_dialog,
                                    requestLogic = {
                                        showDeleteDialog.value = false
                                        requester!!.execDeleteProject(project.id)
                                        if(!requester!!.successResponse())
                                            showSnack(requester!!.errorMessage())
                                    }
                                )
                            } else {
                                Text(
                                    text = project.name,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            IconButton(
                                modifier = Modifier.size(24.dp),
                                onClick = { showOptions = !showOptions }
                            ) {
                                Icon(
                                    imageVector = Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = project.shortDescription
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(3f)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "v. ${project.version}"
                            )
                        }
                        if (project.hasGroups()) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Default.Group,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        )
    }

}