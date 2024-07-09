package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.pandoro.ui.activities.session.GroupActivity
import com.tecknobit.pandorocore.records.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GroupActivityViewModel(
    val initialGroup: Group,
    override var snackbarHostState: SnackbarHostState?
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    private val _group = MutableStateFlow(initialGroup)
    val group: StateFlow<Group> = _group

    fun refreshGroup(
        onSuccess: () -> Unit
    ) {
        execRefreshingRoutine(
            currentContext = GroupActivity::class.java,
            routine = {
                println("gagag")
                requester.sendRequest(
                    request = {
                        requester.getProject(
                            projectId = _group.value.id
                        )
                    },
                    onSuccess = { response ->
                        _group.value = Group.getInstance(
                            response.getJSONObject(RESPONSE_MESSAGE_KEY)
                        )
                        onSuccess.invoke()
                    },
                    onFailure = { showSnack(it) }
                )
            }
        )
    }

    fun addMembers(
        members: List<String>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        requester.sendRequest(
            request = {
                requester.addMembers(
                    groupId = _group.value.id,
                    members = members
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = {
                onFailure.invoke(it.getString(RESPONSE_MESSAGE_KEY))
            }
        )
    }

    fun editProjects(
        projects: List<String>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        requester.sendRequest(
            request = {
                requester.editProjects(
                    groupId = _group.value.id,
                    projects = projects
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = {
                onFailure.invoke(it.getString(RESPONSE_MESSAGE_KEY))
            }
        )
    }

}