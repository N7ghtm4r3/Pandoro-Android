package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.pandoro.ui.activities.session.GroupActivity
import com.tecknobit.pandorocore.records.Group
import com.tecknobit.pandorocore.records.users.GroupMember
import com.tecknobit.pandorocore.records.users.GroupMember.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **GroupActivityViewModel** class is the support class used by the [GroupActivity]
 * to refresh the group displayed and to manage it
 *
 * @param initialGroup: the initial value of the project displayed
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
class GroupActivityViewModel(
    val initialGroup: Group,
    override var snackbarHostState: SnackbarHostState?
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * **isRefreshing** -> whether the [refreshGroup] has been already invoked
     */
    private var isRefreshing: Boolean = false

    /**
     * **_group** -> the group currently displayed
     */
    private val _group = MutableStateFlow(initialGroup)
    val group: StateFlow<Group> = _group

    /**
     * Function to restart the current [refreshRoutine] after other requests has been executed,
     * the [isRefreshing] instance will be set as **true** to deny the restart of the routine after executing
     * the other requests
     *
     * No-any params required
     */
    override fun restartRefresher() {
        if(isRefreshing)
            super.restartRefresher()
    }

    /**
     * Function to execute the request to refresh the [_group] item
     *
     * @param onSuccess: the action to execute whether the request has been successful
     */
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

    /**
     * Function to execute the request to add new members to the current [_group]
     *
     * @param members: the list of the new members to add
     * @param onSuccess: the action to execute whether the request has been successful
     * @param onFailure: the action to execute whether the request has been failed
     */
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

    /**
     * Function to execute the request to edit the projects shared with the group
     *
     * @param projects: the projects shared with the group
     * @param onSuccess: the action to execute whether the request has been successful
     */
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

    /**
     * Function to execute the request to change the role of a member of the group
     *
     * @param member: the member to change its role
     * @param role: the new role of the user
     */
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