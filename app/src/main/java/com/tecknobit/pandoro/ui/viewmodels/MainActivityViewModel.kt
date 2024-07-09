package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.pandoro.R.string.insert_a_correct_description
import com.tecknobit.pandoro.R.string.insert_a_correct_name
import com.tecknobit.pandoro.R.string.insert_a_correct_repository_url
import com.tecknobit.pandoro.R.string.insert_a_correct_short_description
import com.tecknobit.pandoro.R.string.insert_a_correct_version
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.activeScreen
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.activities.session.MainActivity
import com.tecknobit.pandoro.ui.screens.NotesScreen.Companion.notes
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.changelogs
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.groups
import com.tecknobit.pandoro.ui.screens.Screen.Companion.currentGroup
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Notes
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Overview
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Profile
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Projects
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidProjectDescription
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidProjectName
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidProjectShortDescription
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidRepository
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidVersion
import com.tecknobit.pandorocore.records.Changelog
import com.tecknobit.pandorocore.records.Group
import com.tecknobit.pandorocore.records.Note
import com.tecknobit.pandorocore.records.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivityViewModel(
    override var snackbarHostState: SnackbarHostState? = null
) : PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    private val _projects = MutableStateFlow<MutableList<Project>>(mutableListOf())
    val projects: StateFlow<MutableList<Project>> = _projects

    private val _groups = MutableStateFlow<MutableList<Group>>(mutableListOf())

    private val _changelogs = MutableStateFlow<MutableList<Changelog>>(mutableListOf())

    private val _notes = MutableStateFlow<MutableList<Note>>(mutableListOf())

    init {
        groups = _groups
        changelogs = _changelogs
        notes = _notes
    }

    /**
     * **unreadChangelogsNumber** -> the number of the changelogs yet to read
     */
    lateinit var unreadChangelogsNumber: MutableIntState

    lateinit var name: MutableState<String>

    lateinit var description: MutableState<String>

    lateinit var shortDescription: MutableState<String>

    lateinit var version: MutableState<String>

    lateinit var projectRepository: MutableState<String>

    lateinit var projectGroups: MutableList<Group>

    fun refreshValues() {
        execRefreshingRoutine(
            currentContext = MainActivity::class.java,
            routine = {
                if(activeScreen.value == Projects || activeScreen.value == Overview
                    || currentGroup.value != null) {
                    requester.sendRequest(
                        request = { requester.getProjectsList() },
                        onSuccess = { response ->
                            _projects.value = Project.getInstances(
                                response.getJSONArray(RESPONSE_MESSAGE_KEY)
                            )
                            user.setProjects(_projects.value)
                        },
                        onFailure = { showSnack(it) }
                    )
                } else if(activeScreen.value == Profile || activeScreen.value == Projects) {
                    requester.sendRequest(
                        request = { requester.getGroupsList() },
                        onSuccess = { response ->
                            _groups.value = Group.getInstances(
                                response.getJSONArray(RESPONSE_MESSAGE_KEY)
                            )
                            user.setGroups(_groups.value)
                        },
                        onFailure = { showSnack(it) }
                    )
                } else if(activeScreen.value == Notes) {
                    requester.sendRequest(
                        request = { requester.getNotesList() },
                        onSuccess = { response ->
                            _notes.value = Note.getInstances(
                                response.getJSONArray(RESPONSE_MESSAGE_KEY)
                            )
                        },
                        onFailure = { showSnack(it) }
                    )
                }
                requester.sendRequest(
                    request = { requester.getChangelogsList() },
                    onSuccess = { response ->
                        _changelogs.value = Changelog.getInstances(
                            response.getJSONArray(RESPONSE_MESSAGE_KEY)
                        )
                        unreadChangelogsNumber.intValue = 0
                        _changelogs.value.forEach { changelog ->
                            if(!changelog.isRed)
                                unreadChangelogsNumber.intValue++
                        }
                    },
                    onFailure = { showSnack(it) },
                    onConnectionError = {
                        // TODO: MANAGE HERE WRONG CREDENTIALS OR SERVER OFFLINE
                        showSnack(it)
                    }
                )
            }
        )
    }
    
    fun workWithProject(
        project: Project?,
        onSuccess: () -> Unit
    ) {
        if (isValidProjectName(name.value)) {
            if (isValidProjectDescription(description.value)) {
                if (isValidProjectShortDescription(shortDescription.value)) {
                    if (isValidVersion(version.value)) {
                        if (isValidRepository(projectRepository.value)) {
                            val groupIds = mutableListOf<String>()
                            projectGroups.forEach { group ->
                                groupIds.add(group.id)
                            }
                            if(project == null) {
                                addProject(
                                    groupIds = groupIds,
                                    onSuccess = onSuccess
                                )
                            } else {
                                editProject(
                                    project = project,
                                    groupIds = groupIds,
                                    onSuccess = onSuccess
                                )
                            }
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
    }

    private fun addProject(
        groupIds: List<String>,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.addProject(
                    name = name.value,
                    projectDescription = description.value,
                    projectShortDescription = shortDescription.value,
                    projectVersion = version.value,
                    groups = groupIds,
                    projectRepository = projectRepository.value
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnack(it) }
        )
    }

    private fun editProject(
        project: Project,
        groupIds: List<String>,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.editProject(
                    projectId = project.id,
                    name = name.value,
                    projectDescription = description.value,
                    projectShortDescription = shortDescription.value,
                    projectVersion = version.value,
                    groups = groupIds,
                    projectRepository = projectRepository.value
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnack(it) }
        )
    }

    fun deleteProject(
        project: Project,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.deleteProject(
                    projectId = project.id
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnack(it) }
        )
    }

}