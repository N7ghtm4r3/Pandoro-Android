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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.R.string.delete_project
import com.tecknobit.pandoro.R.string.delete_text_dialog
import com.tecknobit.pandoro.toImportFromLibrary.Group
import com.tecknobit.pandoro.toImportFromLibrary.Project
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.projectDialogs
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.components.PandoroAlertDialog
import com.tecknobit.pandoro.ui.components.PandoroCard
import com.tecknobit.pandoro.ui.components.PandoroTextField
import com.tecknobit.pandoro.ui.theme.ErrorLight

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
        lateinit var projectsList: SnapshotStateList<Project>

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
        projectsList = remember { mutableStateListOf() }
        projectsList.addAll(user.projects)
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
                val frequentProjects =
                    filterProjects(filterFrequentQuery, populateFrequentProjects(projectsList))
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
                        items(frequentProjects) { project ->
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
                val currentProjects = filterProjects(filterQuery, projectsList)
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
                        items(currentProjects) { project ->
                            ProjectCard(project = project)
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to populate the frequent projects list
     *
     * @param projectsList: the main projects list from get the frequent projects
     * @return frequent projects list as [SnapshotStateList] of [Project]
     */
    // TODO: PACK IN THE LIBRARY
    private fun populateFrequentProjects(
        projectsList: SnapshotStateList<Project>
    ): SnapshotStateList<Project> {
        val frequentProjects = mutableStateListOf<Project>()
        val updatesNumber = ArrayList<Int>()
        for (project in projectsList)
            updatesNumber.add(project.updatesNumber)
        updatesNumber.sortDescending()
        for (updates in updatesNumber) {
            if (frequentProjects.size < 9) {
                for (project in projectsList) {
                    if (project.updatesNumber == updates && !frequentProjects.contains(project)) {
                        frequentProjects.add(project)
                        break
                    }
                }
            }
        }
        if (frequentProjects.size > 9)
            frequentProjects.removeRange(8, frequentProjects.size - 1)
        return frequentProjects
    }

    /**
     * Function to filter the projects list
     *
     * @param query: the query to filter the projects list
     * @param list: the list of the [Project] to filter
     *
     * @return projects list filtered as [SnapshotStateList] of [Project]
     */
    // TODO: PACK IN THE LIBRARY
    private fun filterProjects(
        query: String,
        list: SnapshotStateList<Project>
    ): SnapshotStateList<Project> {
        return if (query.isEmpty())
            list
        else {
            val checkQuery = query.uppercase()
            val filteredList = mutableStateListOf<Project>()
            for (project in list) {
                if (project.name.uppercase().contains(checkQuery) ||
                    project.shortDescription.uppercase().contains(checkQuery) ||
                    project.description.uppercase().contains(checkQuery) ||
                    project.version.uppercase().contains(checkQuery) ||
                    groupMatch(project.groups, checkQuery)
                ) {
                    filteredList.add(project)
                }
            }
            return filteredList
        }
    }

    /**
     * Function to check whether name match with a group of the list
     *
     * @param groups: the groups of the project
     * @param name: the name to do the check
     *
     * @return whether name match with a group of the list as [Boolean]
     */
    // TODO: PACK IN THE LIBRARY
    private fun groupMatch(
        groups: ArrayList<Group>,
        name: String
    ): Boolean {
        groups.forEach { group: Group ->
            if (group.name.uppercase().contains(name))
                return true
        }
        return false
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
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(string.no_projects_found),
                fontSize = 18.sp
            )
        }
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
                                        /*MAKE REQUEST THEN*/
                                        showDeleteDialog.value = false
                                    }
                                )
                            } else {
                                Text(
                                    text = project.name,
                                    fontSize = 18.sp,
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