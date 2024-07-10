package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.pandoro.ui.activities.session.GroupActivity
import com.tecknobit.pandorocore.records.Group
import com.tecknobit.pandorocore.records.users.GroupMember
import com.tecknobit.pandorocore.records.users.GroupMember.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GroupActivityViewModel(
    val initialGroup: Group,
    override var snackbarHostState: SnackbarHostState?
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    private var isRefreshing: Boolean = false

    private val _group = MutableStateFlow(initialGroup)
    val group: StateFlow<Group> = _group

    override fun restartRefresher() {
        if(isRefreshing)
            super.restartRefresher()
    }

    fun refreshGroup(
        onSuccess: () -> Unit
    ) {
        execRefreshingRoutine(
            currentContext = GroupActivity::class.java,
            routine = {
                isRefreshing = true
                requester.sendRequest(
                    request = {
                        requester.getGroup(
                            groupId = _group.value.id
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
    ) {
        requester.sendRequest(
            request = {
                requester.editProjects(
                    groupId = _group.value.id,
                    projects = projects
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnack(it) }
        )
    }

    fun changeMemberRole(
        member: GroupMember,
        role: Role
    ) {
        requester.sendRequest(
            request = {
                requester.changeMemberRole(
                    groupId = _group.value.id,
                    memberId = member.id,
                    role = role
                )
            },
            onSuccess = {},
            onFailure = { showSnack(it) }
        )
    }

}